package com.yqx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yqx.entity.Y_Menu;
import com.yqx.entity.Y_Role;
import com.yqx.entity.Y_RoleMenu;
import com.yqx.entity.Y_User;
import com.yqx.model.JqgridBean;
import com.yqx.service.Y_MenuService;
import com.yqx.service.Y_RoleMenuService;
import com.yqx.service.Y_RoleService;
import com.yqx.service.Y_UserService;
import com.yqx.util.JsonUtil;
import com.yqx.util.Util;

@Controller
@RequestMapping("/user")
public class Y_UserController {
    
	@Autowired
	private Y_RoleService roleService;
	
	@Autowired
	private Y_MenuService menuService;
	
	@Autowired
	private Y_RoleMenuService roleMenuService;
	
	@Autowired
	private Y_UserService  userService;
	
    
    /***用户管理页面*/
    @RequestMapping("/tousermanage")
    @RequiresPermissions(value = {"用户管理"})
    public String tousermanage() {
    	
    	return "/user/toUserManage";
    }
    
    //跳转到修改密码页面
    @RequestMapping("/toUpdatePassword")
    @RequiresPermissions(value = {"修改密码"})
    public String toUpdatePassword() {
    	
        return "/user/updatePassword";
    }
    
    
    /**退出*/
    @GetMapping("/logout")
	public String logout() throws Exception {
    	
		SecurityUtils.getSubject().logout();
		SecurityUtils.getSubject().getSession().removeAttribute("user");
		SecurityUtils.getSubject().getSession().removeAttribute("currentRole");
		SecurityUtils.getSubject().getSession().removeAttribute("tmenuOneClassList");
		return "redirect:/tologin";
	}
 
    /***登陆*/
    @RequestMapping("/loginUser")
    @ResponseBody
    public Map<String, Object> loginUser(String userName, String password,String imageCode,HttpSession session,HttpServletRequest request) {
    	Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isEmpty(imageCode)) {
			map.put("success", false);
			map.put("errorInfo", "请输入验证码！");
			return map;
		}
		String code = (String) session.getAttribute("code");
		imageCode = imageCode.toLowerCase();
		code = code.toLowerCase();
		if(!code.equals(imageCode)) {
			map.put("success", false);
			map.put("errorInfo", "验证码输入错误！");
			return map;
		}
    	Subject subject = SecurityUtils.getSubject();//1获取subject
    	password = Util.MD5(password).toString().toLowerCase();//密码加密处理
    	UsernamePasswordToken token = new UsernamePasswordToken(userName, password);//封装用户数据
    	try {//执行登录方法
			subject.login(token);//成功
			Y_User user = (Y_User) subject.getPrincipals().getPrimaryPrincipal();//获取当前登录的用户信息
			subject.getSession().setAttribute("user", user);//保存在session中
			List<Y_Role> roleList = roleService.getRoleByUserId(user.getId());//获取该用户拥有多少角色信息
			if (!roleList.isEmpty()) {
				map.put("roleList", roleList);
				map.put("roleSize", roleList.size());
				map.put("success", true);
			}
		} catch (UnknownAccountException e) {
			map.put("success", false);
			map.put("errorInfo", "用户名不存在！");
			return map;
		}catch (IncorrectCredentialsException a) {
			map.put("success", false);
			map.put("errorInfo", "密码不存在！");
			return map;
		}
    	return map;
    }
    //获取当前登陆用户
    @ResponseBody
	@PostMapping("/saveRole")
	public Map<String,Object> saveRole(Integer roleId,HttpSession session)throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		Y_Role currentRole = roleService.selectByKey(roleId);
		session.setAttribute("currentRole", currentRole); // 保存当前角色信息
		List<Integer> list = getRoles(roleId);
		putTmenuOneClassListIntoSession(session,list);
		map.put("success", true);
		return map;
	}
    
    @ResponseBody
	@GetMapping("/loadMenuInfo")
	public String loadMenuInfo(HttpSession session, Integer parentId)throws Exception {
    	Y_Role currentRole = (Y_Role) session.getAttribute("currentRole");
		String json = getAllMenuByParentId(parentId, currentRole.getId()).toString();
		return json;
	}
    /**
     * 分页查询用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/list")
    @RequiresPermissions(value = {"用户管理"})
    public Map<String, Object> list(JqgridBean jqgridbean) throws Exception {
       
        Example tuserExample = new Example(Y_User.class);
        tuserExample.setOrderByClause("id desc");
        Example.Criteria criteria = tuserExample.or();
        criteria.andNotEqualTo("user_name","admin");
        JsonUtil.searPage(jqgridbean, criteria, tuserExample, "eq", "user_name");
        List<Y_User> userList = userService.selectByExample(tuserExample);
        if(null != userList) {
        	for (Y_User u : userList) {
                List<Y_Role> roleList = roleService.getRoleByUserId(u.getId());
                StringBuffer sb = new StringBuffer();
                for (Y_Role r : roleList) {
                    sb.append("," + r.getName());
                }
                u.setRoles(sb.toString().replaceFirst(",", ""));
            }
        }
        return JsonUtil.getPageRusult(userList);
    }

    @ResponseBody
    @RequestMapping(value = "/addupdateuser")
    @RequiresPermissions(value = {"用户管理"})
    public Map<String, Object> addupdateuser(Y_User tuser) {
        LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>();
        try {
            if (tuser.getId() == null) {//新建
                //首先判断用户名是否可用
                Example tuserExample = new Example(Y_User.class);
                tuserExample.or().andEqualTo("user_name",tuser.getUser_name());
                List<Y_User> userlist = userService.selectByExample(tuserExample);
                if (userlist != null && userlist.size() > 0) {
                	return JsonUtil.operationErrorMap(3);
                }
                tuser.setPassword(Util.MD5Code(tuser.getPassword()).toString().toLowerCase());
                userService.saveNotNull(tuser);
            } else {//编辑
                Y_User oldObject = userService.selectByKey(tuser.getId());
                if (oldObject==null) {
                   return JsonUtil.operationErrorMap(2);
                } 
                tuser.setPassword(Util.MD5Code(tuser.getPassword()).toString().toLowerCase());
                userService.updateNotNull(tuser);
            }
            return JsonUtil.operationSuccessMap();
        } catch (Exception e) {
            e.printStackTrace();
            resultmap.put("state", "fail");
            resultmap.put("mesg", "操作失败，系统异常");
            return resultmap;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/deleteuser")
    @RequiresPermissions(value = {"用户管理"})
    public Map<String, Object> deleteuser(Y_User tuser) {
        try {
            if (tuser.getId() != null && !tuser.getId().equals(0)) {
            	Y_User user = userService.selectByKey(tuser.getId());
                if (user == null) {
                	return JsonUtil.operationErrorMap(2);
                }
                userService.batchDelUser(tuser);
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
    @RequestMapping(value = "/selectUserById")
    @RequiresPermissions(value = {"用户管理"})
    public Map<String, Object> selectUserById(Y_User tuser,int flag) {

        LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>();
        try {
        	
        	if (null == tuser.getId() || tuser.getId().equals(0)) {
        		
        		return JsonUtil.operationErrorMap(2);
        	}
        	
        	tuser = userService.selectByKey(tuser.getId());
        	if (null == tuser) {
        		
        		return JsonUtil.operationErrorMap(2);
        	}
        	 resultmap = JsonUtil.operationSuccessMap();
            if(flag == 1) {
            	
            	List<Y_Role> roleList = roleService.getRoleByUserId(tuser.getId());
            	List<Y_Role> notinrolelist = roleService.getNoneRoleById(tuser.getId());
            	resultmap.put("roleList",roleList);//用户拥有的所有角色
                resultmap.put("notinrolelist",notinrolelist);//用户不拥有的角色
            }
           
            resultmap.put("tuser",tuser);
            return resultmap;
        } catch (Exception e) {
            e.printStackTrace();
           return JsonUtil.operationErrorMap(1);
        }
    }

    //设置用户角色
    @ResponseBody
    @RequestMapping(value = "/saveRoleSet")
    @RequiresPermissions(value = {"用户管理"})
    public Map<String, Object> saveRoleSet(Integer[] role,Integer id) {
        try {
        	userService.addUser(role, id);
        	return JsonUtil.operationSuccessMap();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.operationErrorMap(1);
        }
    }
    //修改密码
    @ResponseBody
    @PostMapping("/updatePassword")
    @RequiresPermissions(value = {"修改密码"})
    public Map<String, Object> updatePassword(Y_User tuser) throws Exception {
        LinkedHashMap<String, Object> resultmap = new LinkedHashMap<String, Object>();
        try {
            if(tuser==null){
                resultmap.put("state", "fail");
                resultmap.put("mesg", "设置失败，缺乏字段信息");
                return resultmap;
            }else{
                if (tuser.getId()!=null &&tuser.getId().intValue()!=0 && StringUtils.isNotEmpty(tuser.getUser_name())
                            && StringUtils.isNotEmpty(tuser.getOldPassword()) && StringUtils.isNotEmpty(tuser.getPassword())){
                    Example userExample=new Example(Y_User.class);
                    Example.Criteria criteria=userExample.or();
                    criteria.andEqualTo("id",tuser.getId())
                            .andEqualTo("user_name",tuser.getUser_name())  
                            .andEqualTo("password",tuser.getOldPassword());
                    List<Y_User> tuserList = userService.selectByExample(userExample);
                    if(null == tuserList || 0 == tuserList.size()){
                        resultmap.put("state", "fail");
                        resultmap.put("mesg", "用户名或密码错误");
                        return resultmap;
                    }else{
                    	Y_User newEntity = tuserList.get(0);
                        newEntity.setPassword(tuser.getPassword());
                        userService.updateNotNull(newEntity);
                    }
                }else{
                    resultmap.put("state", "fail");
                    resultmap.put("mesg", "设置失败，缺乏字段信息");
                    return resultmap;
                }
            }

            resultmap.put("state", "success");
            resultmap.put("mesg", "密码修改成功");
            return resultmap;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.operationErrorMap(1);
        }
    }

    /**
	 * 获取根频道所有菜单信息
	 * @param parentId
	 * @param roleId
	 */
	private JsonObject getAllMenuByParentId(Integer parentId, Integer roleId) {
		JsonObject resultObject = new JsonObject();
		JsonArray jsonArray = this.getMenuByParentId(parentId,roleId);// 得到所有一级菜单
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonObject = (JsonObject) jsonArray.get(i);// 判断该节点下时候还有子节点
			Example example = new Example(Y_Menu.class);
			example.or().andEqualTo("p_id", jsonObject.get("id").getAsString());
			int menuCount = menuService.selectCountByExample(example);
			if (menuCount > 0) {// 由于后台模板的规定，一级菜单以title最为json的key
				resultObject.add(jsonObject.get("title").getAsString(),getAllMenuJsonArrayByParentId(jsonObject.get("id").getAsInt(), roleId));
			}
		}
		return resultObject;
	}
	
	// 获取根频道下子频道菜单列表集合
	private JsonArray getAllMenuJsonArrayByParentId(Integer parentId,Integer roleId) {
		JsonArray jsonArray = this.getMenuByParentId(parentId,roleId);
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonObject = (JsonObject) jsonArray.get(i);
			Example example = new Example(Y_Menu.class);// 判断该节点下是否还有子节点
			example.or().andEqualTo("p_id", jsonObject.get("id").getAsString());
			int menuCount = menuService.selectCountByExample(example);
			if (menuCount > 0) {
				jsonObject.add("children",getAllMenuJsonArrayByParentId(jsonObject.get("id").getAsInt(), roleId));
			}
		}
		return jsonArray;
	}	
	/**
	 * 根据父节点和用户角色id查询菜单
	 * @param parentId
	 * @param roleId
	 */
	private JsonArray getMenuByParentId(Integer parentId, Integer roleId) {
		List<Y_Menu> menuList = menuService.selectByParentIdAndRoleId(parentId, roleId);
		JsonArray jsonArray = new JsonArray();
		for(Y_Menu menu : menuList) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", menu.getId()); // 节点id
			jsonObject.addProperty("title", menu.getName()); // 节点名称
			jsonObject.addProperty("spread", true); // 不展开
			jsonObject.addProperty("icon", menu.getIcon());
			if(StringUtils.isNotEmpty(menu.getUrl())) {
				jsonObject.addProperty("href", menu.getUrl()); // 菜单请求地址
			}
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
		
    private void putTmenuOneClassListIntoSession(HttpSession session,List<Integer> menuIds){
    	Example example = new Example(Y_Menu.class);
    	example.and().andIn("id", menuIds);
    	example.and().andEqualTo("p_id",1);
    	List<Y_Menu> menuList = menuService.selectByExample(example);
    	session.setAttribute("tmenuOneClassList", menuList);
    }
    
    //根据角色获取用户菜单
    private List<Integer> getRoles(Integer roleId) {
    	List<Integer> list = new ArrayList<>();
    	Example example = new Example(Y_RoleMenu.class);
    	example.and().andEqualTo("role_id", roleId);
    	List<Y_RoleMenu> menuList = roleMenuService.selectByExample(example);
    	if(!menuList.isEmpty()) {
    		for(Y_RoleMenu menu : menuList) {
    			list.add(menu.getMenu_id());
        	}
    	}
    	return list;
    }
}