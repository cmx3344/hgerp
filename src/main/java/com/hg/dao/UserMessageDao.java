package com.hg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.UserMessage;

public interface UserMessageDao extends PagingAndSortingRepository<UserMessage, Long>, JpaSpecificationExecutor<UserMessage>{

	@Query("from UserMessage where userId = ?1 and state=?2 order by id desc")
	public List<UserMessage> findByUserIdAndState(Long userId,Integer state);
	
	@Query("from UserMessage where userId = ?1 order by id desc")
	public List<UserMessage> findByUserId(Long userId);
	
	@Query("from UserMessage where userId = ?1 and subject like %?2% order by id desc")
	public List<UserMessage> findByUserIdAndSubject(Long userId,String subject);
}
