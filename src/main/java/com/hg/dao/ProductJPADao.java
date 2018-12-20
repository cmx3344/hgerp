package com.hg.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.Product;


public interface ProductJPADao extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product>{

	
	public List<Product> findBySerialNumAndOrderNum(String serialNum,String orderNum);
	
	@Query("from Product where id in (?1) and (forgingWorker = ?2 or forgingWorkerA = ?2)")
	public List<Product> select(List<Long> ids,String forgingWorker);
	
	@Query("from Product where id in (?1) order by code,orderA,orderB,serialNum")
	public List<Product> findAllOrder(List<Long> ids);
	
	@Query("from Product where ordera is null or orderb is null")
	public List<Product> findAllIsNull();
	
	@Query("from  Product where state in (6,9)")
	public List<Product> findByState();
	
	@Query("from  Product where serialNum = ?1 and orderNum = ?2 and id != ?3")
	public List<Product> findBySerialNumAndOrderNumAndId(String serialNum,String orderNum,Long id);
	
	public List<Product> findBySerialNum(String serialNum);
	
	public Product findById(Long id);
	
}
