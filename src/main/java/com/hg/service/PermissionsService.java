package com.hg.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hg.dao.PermissionDao;
import com.hg.dao.RoleDao;
import com.hg.model.Permission;





@Service
public class PermissionsService {
	
	@Inject
	private PermissionDao permissionDao;
	@Inject
	private RoleDao roleDao;
	
	
	public void getPermissionTrees(Long pid,Permission menu,Long id){
		List<Permission> list = permissionDao.findByRoleIdAndPid(id,pid);
		if(null != list&&list.size()>0){
			for(Permission d : list){
				getPermissionTrees(d.getId(),d,id);
				d.setText(d.getName());
			}
			menu.setChildren(list);
		}
	}
	
	
//	public void getRolePermissionTrees(Long pid,Permission menu,Long roleId){
//		List<Permission> list = permissionDao.findByParentId(pid);
//		Role role = roleDao.findOne(roleId);
//		if(null != list&&list.size()>0){
//			for(Permission d : list){
//				getPermissionTrees(d.getId(),d);
//				for(Permission p : role.getPermissions()){
//					if(p.getId().intValue()==d.getId().intValue()){
//						d.setChecked(true);
//					}
//				}
//			}
//			menu.setChildren(list);
//		}
//	}
	
	public void getRolePermissionTrees(Long pid,Permission menu,String roleids,StringBuffer tree){
		//List<Permission> list = baseDao.find("select new com.hr.model.shiro.Permission(a.id,a.name,a.url,a.rel,a.type) from Permission a,SysRolePermission b where a.id=b.permissionId and a.parentId="+pid+" and b.roleId in ("+roleids+") group by a.id HAVING(COUNT(a.id)>=1)");
		List<Permission> list = permissionDao.findByParentIds(pid, roleids);
		if(null != list&&list.size()>0&&pid.intValue()!=1){
			tree.append("<ul>");
		}
		if(null != list&&list.size()>0){
			for(Permission d : list){
				tree.append("<li>");
				if(d.getType().equals("资源")){
					tree.append("<a href=\""+d.getUrl()+"permissionsSeq="+d.getId()+"\" rel=\""+d.getRel()+"\" target=\"navTab\">"+d.getName()+"</a>");	
				}else{
					tree.append("<a href=\"javascript::\">"+d.getName()+"</a>");
				}
				getRolePermissionTrees(d.getId(),d,roleids,tree);
				tree.append("</li>");
			}
		}
		if(null != list&&list.size()>0&&pid.intValue()!=1){
			tree.append("</ul>");
		}
	}
	

}
