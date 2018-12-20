package com.hg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.MessageFile;


public interface MessageFileDao extends PagingAndSortingRepository<MessageFile, Long>, JpaSpecificationExecutor<MessageFile>{

	public List<MessageFile> findByParentId(Long parentId);
}
