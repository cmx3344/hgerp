package com.hg.dao.ncr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.NcrSon;


public interface NcrSonDao extends PagingAndSortingRepository<NcrSon, Long>, JpaSpecificationExecutor<NcrSon>{

	
	@Query("select new com.hg.model.NcrSon(a.id,a.parentId,a.productId,b.serialNum,b.orderNum,b.sizeA,b.sizeB,b.sizeC,b.sizeD,b.sizeE,b.sizeF,b.sizeG,b.sizeH,b.qty,b.blankWeight,b.material,b.heatNum,a.handleType,a.state) from NcrSon a,Product b where a.productId=b.id and a.parentId=?1")
	public List<NcrSon> findByParentId(Long parentId);
	
	public List<NcrSon> findByParentIdAndState(Long parentId,Integer state);
}
