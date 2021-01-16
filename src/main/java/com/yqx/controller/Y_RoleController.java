package com.yqx.controller;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.yqx.entity.Y_Role;
import com.yqx.model.JqgridBean;
import com.yqx.service.Y_MenuService;
import com.yqx.service.Y_RoleService;
import com.yqx.util.JsonUtil;

@Controller
@RequestMapping("/role")
public class Y_RoleController {
	
	
	@Autowired
	private Y_RoleService roleService;
	
	@Autowired 
	private Y_MenuService menuService;
	
	
	@RequestMapping("/torolemanage")
    @RequiresPermissions(value = {"角色管理"})
    public String tousermanage() {
        return "/role/role";
    }
	
    /**
     * 分页查询角色信息
     */
    @ResponseBody
    @RequestMapping(value = "/list")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> list(JqgridBean jqgridbean) throws Exception {
       
        Example troleExample = new Example(Y_Role.class);
        Example.Criteria criteria = troleExample.or();
        JsonUtil.searPage(jqgridbean, criteria, troleExample, "eq", "name");
        List<Y_Role> roleList = roleService.selectByExample(troleExample);
        return JsonUtil.getPageRusult(roleList);
    }

    @ResponseBody
    @RequestMapping(value = "/addupdaterole")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> addupdaterole(Y_Role trole) {
        LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>();
        try {
            if (trole.getId() == null) {//新建
                Example troleExample = new Example(Y_Role.class); //首先判断用户名是否可用
                troleExample.or().andEqualTo("name",trole.getName());
                List<Y_Role> rolelist = roleService.selectByExample(troleExample);
                if (rolelist != null && rolelist.size() > 0) {
                    resultmap.put("state", "fail");
                    resultmap.put("mesg", "当前角色名已存在");
                    return resultmap;
                }
                roleService.saveNotNull(trole);
            } else {//编辑
            	Y_Role oldObject = roleService.selectByKey(trole.getId());
                if (oldObject == null) {
                    resultmap.put("state", "fail");
                    resultmap.put("mesg", "当前角色名不存在");
                    return resultmap;
                } else {
                    roleService.updateNotNull(trole);
                }
            }
            resultmap.put("state", "success");
            resultmap.put("mesg", "操作成功");
            return resultmap;
        } catch (Exception e) {
            e.printStackTrace();
            resultmap.put("state", "fail");
            resultmap.put("mesg", "操作失败，系统异常");
            return resultmap;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/deleterole")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> deleteuser(Y_Role trole) {
        try {
        	if(null == trole.getId() || trole.getId().equals(0)) {
        		
        		return JsonUtil.operationErrorMap(2);
        	}
        	Y_Role role = roleService.selectByKey(trole.getId());
            if (null == role) {
            	
            	return JsonUtil.operationErrorMap(2);
            } 
            roleService.batchDelRole(trole);
            return JsonUtil.operationSuccessMap();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.operationErrorMap(1);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/selectRoleById")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> selectRoleById(Y_Role trole) {
        Map<String, Object> resultmap = null;
        try {
        	
        	if(null == trole.getId() || 0 == trole.getId()) {
        		return JsonUtil.operationErrorMap(2);
        	}
        	
        	trole = roleService.selectByKey(trole.getId());
        	if(null == trole) {
        		return JsonUtil.operationErrorMap(2);
        	}
        	resultmap = JsonUtil.operationSuccessMap();
            resultmap.put("trole", trole);
            return resultmap;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.operationErrorMap(1);
        }
    }
    
    @ResponseBody
    @RequestMapping("/saveMenuSet")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> saveMenuSet(String menuIds, Integer roleId) throws Exception {
       
    	try {
    		if(StringUtils.isEmpty(menuIds)) {
        		
        		return JsonUtil.operationErrorMap(2);
        	}
        	roleService.saveMenuSet(menuIds, roleId);
            return JsonUtil.operationSuccessMap();
		} catch (Exception e) {
			return JsonUtil.operationErrorMap(1);
		}
    }
    
    /**
     * 根据父节点获取所有复选框权限菜单树
     *
     * @param parentId
     * @param roleId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/loadCheckMenuInfo")
    @RequiresPermissions(value = {"角色管理"})
    public String loadCheckMenuInfo(Integer parentId, Integer roleId) throws Exception {
        List<Y_Menu> menuList = menuService.getMenuByRole(roleId);// 根据角色查询所有权限菜单信息
        //移除所有没有pid的menuid
        Iterator<Y_Menu> it = menuList.iterator();
        while (it.hasNext()) {
        	Y_Menu tmenu = it.next();
            if (tmenu.getP_id() == null) {
                it.remove();
            }
        }
        List<Integer> menuIdList = new LinkedList<Integer>();
        for (Y_Menu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        String json = getAllCheckedMenuByParentId(parentId, menuIdList).toString();
        return json;
    }
    
    
    /**
     * 根据父节点ID和权限菜单ID集合获取复选框菜单节点
     * @param parentId
     * @param menuIdList
     * @return
     */
    private JsonArray getAllCheckedMenuByParentId(Integer parentId, List<Integer> menuIdList) {
        JsonArray jsonArray = this.getCheckedMenuByParentId(parentId, menuIdList);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            Example example=new Example(Y_Menu.class);//判断该节点下时候还有子节点
            example.or().andEqualTo("p_id",jsonObject.get("id").getAsString());
            if (menuService.selectCountByExample(example) > 0) {

                jsonObject.add("children", getAllCheckedMenuByParentId(jsonObject.get("id").getAsInt(), menuIdList));
            }
        }
        return jsonArray;
    }
    
    /**
     * 根据父节点ID和权限菜单ID集合获取复选框菜单节点
     *
     * @param parentId
     * @param menuIdList
     * @return
     */
    private JsonArray getCheckedMenuByParentId(Integer parentId, List<Integer> menuIdList) {
        Example tmenuExample = new Example(Y_Menu.class);
        tmenuExample.or().andEqualTo("p_id",parentId);
        List<Y_Menu> menuList = menuService.selectByExample(tmenuExample);
        JsonArray jsonArray = new JsonArray();
        for (Y_Menu menu : menuList) {
            JsonObject jsonObject = new JsonObject();
            Integer menuId = menu.getId();
            jsonObject.addProperty("id", menuId); // 节点id
            jsonObject.addProperty("name", menu.getName()); // 节点名称
            //判断该节点下时候还有子节点
            Example example=new Example(Y_Menu.class);
            example.or().andEqualTo("p_id",jsonObject.get("id").getAsString());
            int menuCount = menuService.selectCountByExample(example);
            jsonObject.addProperty("open", menuCount == 0 ? "true" : "false"); // 无子节点
            
            if (menuIdList.contains(menuId)) {
                jsonObject.addProperty("checked", true);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
