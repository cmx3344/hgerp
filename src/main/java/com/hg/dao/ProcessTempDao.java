package com.hg.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.ProcessTemp;

public interface ProcessTempDao extends PagingAndSortingRepository<ProcessTemp, Long>, JpaSpecificationExecutor<ProcessTemp>{

}
