package com.hg.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.Department;

public interface DepartmentDao extends PagingAndSortingRepository<Department, Long>, JpaSpecificationExecutor<Department>{

}
