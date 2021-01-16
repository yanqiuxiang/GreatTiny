package com.yqx.controller;

import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yqx.entity.Y_Menu;
import com.yqx.service.Y_MenuService;
import com.yqx.util.JsonUtil;


@Controller
@RequestMapping("/menu")
public class Y_MenuController {
	
	@Autowired
	private Y_MenuService menuService;
	
	
	@RequestMapping("/tomunemanage")
    @RequiresPermissions(value = {"菜单管理"})
    public String tousermanage() {
        return "/menu/menu";
    }
	
	 
    @PostMapping("/loadCheckMenuInfo")
    @RequiresPermissions(value = {"菜单管理"})
    @ResponseBody
    public String loadCheckMenuInfo(Integer parentId) throws Exception {
        String json = getAllMenuByParentId(parentId).toString();
        return json;
    }
    
    @ResponseBody
    @RequestMapping(value = "/deletemenu")
    @RequiresPermissions(value = {"菜单管理"})
    public Map<String, Object> deletemenu(HttpSession session,Y_Menu tmenu) {
        try {
            if(tmenu.getId() != null && !tmenu.getId().equals(0)){
            	Y_Menu menu = menuService.selectByKey(tmenu.getId());
                if(menu==null){
                	return JsonUtil.operationErrorMap(2);
                }else{
                	menuService.batchDelMenu(menu);
                }
            }else{
            	return JsonUtil.operationErrorMap(0);
            }
            return JsonUtil.operationSuccessMap();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.operationErrorMap(1);
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/selectMenuById")
    @RequiresPermissions(value = {"菜单管理"})
    public Map<String, Object> selectMenuById(Integer id) {
        Map<String, Object> resultmap = new LinkedHashMap<String, Object>();
        try {
        	
            if (id==null || id==0) {
            	resultmap = JsonUtil.operationErrorMap(3);
            } else {
            	
                Y_Menu tmenu = menuService.selectByKey(id);
                if (tmenu == null) {
                	
                	resultmap = JsonUtil.operationErrorMap(3);
                } else {
                	resultmap = JsonUtil.operationSuccessMap();
                    resultmap.put("tmenu", tmenu);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultmap = JsonUtil.operationErrorMap(1);
        }
        return resultmap;
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/addupdatemenu")
    @RequiresPermissions(value = {"菜单管理"})
    public Map<String, Object> addupdatemenu(HttpSession session, Y_Menu tmenu) {
        LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>();
        try {
            if (tmenu.getId() == null) {//新建
                //首先校验本次新增操作提交的菜单对象中的name属性的值是否存在
                Example tmenuExample = new Example(Y_Menu.class);
                tmenuExample.or().andEqualTo("name",tmenu.getName());
                int menuCount = menuService.selectCountByExample(tmenuExample);
                if (menuCount > 0) {
                	
                	return JsonUtil.operationErrorMap(3);
                }

                //校验是否提交了pId
                if(tmenu.getP_id()==null||tmenu.getP_id()==0){
                    resultmap.put("state", "fail");
                    resultmap.put("mesg", "无法获取父级id");
                    return resultmap;
                }else{

                	Y_Menu pmenu = menuService.selectByKey(tmenu.getP_id());//父节点对象
                    if(pmenu.getState()==3){
                        resultmap.put("state", "fail");
                        resultmap.put("mesg", "3级菜单不可再添加子菜单");
                        return resultmap;
                    }
                    if("-1".equalsIgnoreCase(String.valueOf(pmenu.getP_id()))
                            &&"1".equalsIgnoreCase(String.valueOf(pmenu.getState()))){//如果父节点是最顶级那一个，则本次新增为一级菜单

                        //一级菜单的名字不可为纯数字
                        if(isNumeric(tmenu.getName())){
                            resultmap.put("state", "fail");
                            resultmap.put("mesg", "1级菜单的名字不可为纯数字");
                            return resultmap;
                        }

                        tmenu.setState(1);
                    }else if("1".equalsIgnoreCase(String.valueOf(pmenu.getP_id()))
                            &&"1".equalsIgnoreCase(String.valueOf(pmenu.getState()))){//如果父节点是一级菜单，本次新增为2级菜单
                        tmenu.setState(2);
                    }else if(!"1".equalsIgnoreCase(String.valueOf(pmenu.getP_id()))
                            &&"2".equalsIgnoreCase(String.valueOf(pmenu.getState()))){//如果父节点是二级菜单，本次新增为3级菜单
                        tmenu.setState(3);
                    }

                    //指定pid的值，根据id倒序查询同级菜单集合
                    Example example = new Example(Y_Menu.class);
                    example.or().andEqualTo("p_id",String.valueOf(tmenu.getP_id()));
                    example.setOrderByClause("ID DESC");
                    
                    List<Y_Menu> list = menuService.selectByExample(example);

                    if(null != list && list.size() > 0) {//如果本次新增的菜单实体的同一级菜单集合不为空
                        tmenu.setId(list.get(0).getId()+1);//获取已经存在的同级菜单的id的最大值+1
                    }else{//如果本次新增的菜单实体还没有同一级的菜单的话，则根据父节点生成子节点id
                        if("1".equalsIgnoreCase(String.valueOf(tmenu.getP_id()))){
                            tmenu.setId(tmenu.getP_id()*10);//第一个一级菜单id为1*10
                        }else{
                            tmenu.setId(tmenu.getP_id()*100);//二级三级菜单id生成策略为根据父菜单id*100
                        }
                    }
                }

                menuService.saveNotNull(tmenu);
            } else {//编辑(对于节点的编辑只允许编辑icon、name、url)
                //首先校验本次编辑操作提交的菜单对象中的name属性的值是否存在
                Example tmenuExample = new Example(Y_Menu.class);
                tmenuExample.or().andEqualTo("name",tmenu.getName());
                List<Y_Menu> menulist = menuService.selectByExample(tmenuExample);
                if (menulist.size() > 0 && Integer.compare(menulist.get(0).getId(),tmenu.getId())!=0) {//如果本次提交的名字在本次修改的节点之外已经存在
                	return JsonUtil.operationErrorMap(3);
                }else{
                	Y_Menu tmenuNew = new Y_Menu();
                    tmenuNew.setId(tmenu.getId());
                    if(StringUtils.isNotEmpty(tmenu.getIcon())) {
                        tmenuNew.setIcon(tmenu.getIcon());
                    }
                    if(StringUtils.isNotEmpty(tmenu.getName())) {
                        tmenuNew.setName(tmenu.getName());
                    }
                    if(StringUtils.isNotEmpty(tmenu.getUrl())) {
                        tmenuNew.setUrl(tmenu.getUrl());
                    }
                    menuService.updateNotNull(tmenuNew);
                }
            }
            resultmap.put("state", "success");
            resultmap.put("mesg", "操作成功");
            resultmap.put("id", String.valueOf(tmenu.getId()));
            return resultmap;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.operationErrorMap(1);
        }
    }
    
    private JsonArray getAllMenuByParentId(Integer parentId) {
    	
        JsonArray jsonArray = this.getMenuByParentId(parentId);
        for (int i = 0,j=jsonArray.size(); i < j; i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            //判断该节点下时候还有子节点
            Example example=new Example(Y_Menu.class);
            example.or().andEqualTo("p_id",jsonObject.get("id").getAsString());
            int menuCount = menuService.selectCountByExample(example);
            if (menuCount >  0) {
               jsonObject.add("children", getAllMenuByParentId(jsonObject.get("id").getAsInt()));
            }
        }
        return jsonArray;
    }


    private JsonArray getMenuByParentId(Integer parentId) {
        Example tmenuExample = new Example(Y_Menu.class);
        tmenuExample.or().andEqualTo("p_id",parentId);
        List<Y_Menu> menuList = menuService.selectByExample(tmenuExample);
        JsonArray jsonArray = new JsonArray();
        for (Y_Menu menu : menuList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", menu.getId()); // 节点id
            jsonObject.addProperty("name", menu.getName()); // 节点名称
            //判断该节点下是否还有子节点
            Example example=new Example(Y_Menu.class);
            example.or().andEqualTo("p_id",jsonObject.get("id").getAsString());
            int countExample = menuService.selectCountByExample(example);
            jsonObject.addProperty("open", countExample == 0 ? "false" : "true");
            jsonObject.addProperty("state", String.valueOf(menu.getState()));
            jsonObject.addProperty("iconValue", menu.getIcon());
            jsonObject.addProperty("pId", String.valueOf(menu.getId()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
