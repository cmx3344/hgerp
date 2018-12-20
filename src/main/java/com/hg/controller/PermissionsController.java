package com.hg.controller;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hg.dao.PermissionDao;
import com.hg.dao.RoleDao;
import com.hg.model.Permission;
import com.hg.model.Role;
import com.hg.service.PermissionsService;




@Controller
@RequestMapping("/api/permission")
public class PermissionsController {

	@Inject
    private PermissionDao permissionDao;
	@Inject 
	private RoleDao roleDao;
	@Inject
	private PermissionsService permissionService;
	
	@RequestMapping(value = "/edit")
    public String edit(Long id,Model model,int type) {
    	if(type==2){
    		Permission p2 = permissionDao.findOne(id);
    		model.addAttribute("bean", p2);
    	}else{
    		Permission p = new Permission();
    		p.setParentId(id);
    		model.addAttribute("bean", p);
    	}
        return "role/editPermission";
    }
	
	@RequestMapping(value = "/getPermissionTree")
	@ResponseBody
    public Object getPermissionTree() {
		Long roleId = (Long) SecurityUtils.getSubject().getSession().getAttribute("roleId");
		Permission permission = permissionDao.findOne(1L);
		permission.setText(permission.getName());
		permissionService.getPermissionTrees(1L, permission,roleId);
		JSONObject json = (JSONObject) JSONObject.toJSON(permission);
        return "["+json+"]";
    }
	
	@RequestMapping(value = "/getRolePermissionTree")
	@ResponseBody
    public Object getRolePermissionTree(Long id) {
		Role role = roleDao.findOne(id);
		List<Permission> list = (List<Permission>) permissionDao.findAll(); 
		if(role.getPermissions()!=null&&role.getPermissions().size()>0){
			for(Permission p : list){
				p.setUrl(null);
				a:for(Permission r : role.getPermissions()){
					if(p.getId().intValue()==r.getId().intValue()){
						p.setChecked(true);
						break a;
					}
				}
			}
		}
        return list;
    }
	
	
//    @RequiresPermissions("operation:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@RequestParam Long id) {

    	permissionDao.delete(id);

        return "OK";
    }
    
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(Permission permission) {
    	permissionDao.save(permission);
        return "success";
    }
    
    @RequestMapping(value = "/rolePermission", method = RequestMethod.GET)
    public String rolePermission(Long id,Model model) {
    	model.addAttribute("id", id);
        return "main/role/setRolePermissons";
    }
    
    /**
     * 
    * @Title: authorize   
    * @Description: 授权
    * @param @param permission
    * @param @return 
    * @return String
    * @throws
     */
    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    @ResponseBody
    public String authorize(Long id,@RequestParam(value = "permissions", required = false) List<Long> ids) {
    	Role role = roleDao.findOne(id);
    	List<Permission> permissions = (List<Permission>) permissionDao.findAll(ids);
    	role.setPermissions(new HashSet<>(permissions));
    	roleDao.save(role);
        return "success";
    }
}
