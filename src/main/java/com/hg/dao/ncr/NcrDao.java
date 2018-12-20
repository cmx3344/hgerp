package com.hg.dao.ncr;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.Ncr;


public interface NcrDao extends PagingAndSortingRepository<Ncr, Long>, JpaSpecificationExecutor<Ncr>{

}
