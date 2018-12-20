package com.hg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hg.model.Role;




public interface RoleDao extends JpaRepository<Role, Long> {

}
