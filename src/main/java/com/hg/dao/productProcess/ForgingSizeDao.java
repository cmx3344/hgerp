package com.hg.dao.productProcess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.productProcess.ForgingSize;


public interface ForgingSizeDao extends PagingAndSortingRepository<ForgingSize, Long>, JpaSpecificationExecutor<ForgingSize>{

	
	public List<ForgingSize> findByForgingNum(String forgingNum);
}
