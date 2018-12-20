package com.hg.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.Group;


public interface GroupDao extends PagingAndSortingRepository<Group, Long>, JpaSpecificationExecutor<Group>{

}
