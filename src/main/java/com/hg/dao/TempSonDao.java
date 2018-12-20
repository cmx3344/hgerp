package com.hg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.ProcessTempSon;


public interface TempSonDao extends PagingAndSortingRepository<ProcessTempSon, Long>, JpaSpecificationExecutor<ProcessTempSon>{

	@Query("from ProcessTempSon where parentId = ?1 order by orderNum")
	public List<ProcessTempSon> findByParentId(Long parentId);
	
	public ProcessTempSon findByParentIdAndOrderNum(Long parentId,Integer orderNum);
	
	public ProcessTempSon findByParentIdAndProcessId(Long parentId,Long processId);
}
