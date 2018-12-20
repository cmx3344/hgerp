package com.hg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.OperateRecord;


public interface OperateRecordDao extends PagingAndSortingRepository<OperateRecord, Long>, JpaSpecificationExecutor<OperateRecord>{

	
	public List<OperateRecord> findByProductId(Long productId);
}
