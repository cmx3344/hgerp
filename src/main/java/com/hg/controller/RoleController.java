package com.hg.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hg.dao.PermissionDao;
import com.hg.dao.RoleDao;
import com.hg.model.Permission;
import com.hg.model.Role;



@Controller
@RequestMapping("/api/role")
public class RoleController {

    @Inject
    private RoleDao roleDao;
    
    @Inject
    private PermissionDao permissionDao;
    
    @RequestMapping(value = "/roles")
    public String roles() {
        return "main/role/role";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String view(Long id,Model model) {
    	if(null != id){
    		Role role = roleDao.findOne(id);
    		model.addAttribute("bean", role);
    	}
        return "main/role/editRole";
    }

    @RequestMapping(value = { "/all" }, method = RequestMethod.GET)
    @ResponseBody
    public List<Role> all() {
        List<Role> roles = roleDao.findAll();
        return roles;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@RequestParam Long id) {

        roleDao.delete(id);
        return "OK";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam String name,@RequestParam Long id, @RequestParam String description, @RequestParam String role) {
        Role r = new Role();
        r.setName(name);
        r.setId(id);
        r.setDescription(description);
        r.setRole(role);
        List<Permission> list = permissionDao.findByroleId(id);
        r.setPermissions(new HashSet(list));
        roleDao.save(r);

        return "success";
    }

}
