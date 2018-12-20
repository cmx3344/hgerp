package com.hg.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.SysProcess;


public interface SysProcessDao extends PagingAndSortingRepository<SysProcess, Long>, JpaSpecificationExecutor<SysProcess>{

}
