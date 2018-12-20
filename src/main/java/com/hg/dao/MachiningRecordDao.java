package com.hg.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.MachiningRecord;


public interface MachiningRecordDao extends PagingAndSortingRepository<MachiningRecord, Long>, JpaSpecificationExecutor<MachiningRecord>{

}
