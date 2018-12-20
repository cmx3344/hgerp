package com.hg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hg.model.Permission;




public interface PermissionDao extends CrudRepository<Permission, Long> {
	
	public List<Permission> findByParentId(Long parentId);
	
	@Query("select new com.hg.model.Permission(a.id,a.name,a.url,a.rel,a.type,a.parentId,a.uniqueValue) from com.hg.model.Permission a,com.hg.model.SysRolePermission b where a.id=b.permissionId and a.parentId= ?1 and b.roleId in (?2) group by a.id HAVING(COUNT(a.id)>=1)")
	public List<Permission> findByParentIds(Long parentId,String roleIds);
	
	@Query("select new com.hg.model.Permission(a.id,a.name,a.url,a.rel,a.type,a.parentId,a.uniqueValue) from com.hg.model.Permission a,com.hg.model.SysRolePermission b where a.id=b.permissionId and b.roleId = :id")
	public List<Permission> findByroleId(@Param("id") Long id);
	
	@Query("select new com.hg.model.Permission(a.id,a.name,a.url,a.rel,a.type,a.parentId,a.uniqueValue) from com.hg.model.Permission a,com.hg.model.SysRolePermission b where a.id=b.permissionId and b.roleId = :id and a.parentId =:pid")
	public List<Permission> findByRoleIdAndPid(@Param("id") Long id,@Param("pid") Long pid);
	
}
