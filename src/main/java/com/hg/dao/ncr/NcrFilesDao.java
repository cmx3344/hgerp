package com.hg.dao.ncr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.NcrFiles;


public interface NcrFilesDao extends PagingAndSortingRepository<NcrFiles, Long>, JpaSpecificationExecutor<NcrFiles>{

	
	public List<NcrFiles> findByParentId(Long parentId);
}
