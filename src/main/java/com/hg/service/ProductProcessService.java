package com.hg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hg.dao.productProcess.ForgingSizeDao;
import com.hg.model.productProcess.ForgingSize;
import com.hg.util.PageInfo;
import com.hg.util.PageResponse;



@Service
public class ProductProcessService {
	
	@Inject
	private ForgingSizeDao forgingSizeDao;
	
	public PageResponse queryList(PageInfo pageinfo,final ForgingSize bean) {
	       
		// 获得分页对象pageable，并且在pageable中页码是从0开始，设定按照sortType升序排列
		 Pageable pageable = new PageRequest(pageinfo.getPage()-1, pageinfo.getRows());
         Page<ForgingSize> objPage = forgingSizeDao.findAll(new Specification<ForgingSize>() {

            public Predicate toPredicate(Root<ForgingSize> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> lstPredicates = new ArrayList<Predicate>();

                if (bean!=null) {
                	if(bean.getForgingNum()!=null&&!("").equals(bean.getForgingNum())){
                		lstPredicates.add(cb.like(root.get("forgingNum").as(String.class), "%" + bean.getForgingNum() + "%"));
                	}
                	if(bean.getSizes()!=null&&!("").equals(bean.getSizes())){
                		lstPredicates.add(cb.like(root.get("forgingSize").as(String.class), "%" + bean.getSizes() + "%"));
                	}
                	
                }

                Predicate[] arrayPredicates = new Predicate[lstPredicates.size()];
                return cb.and(lstPredicates.toArray(arrayPredicates));
            }
        }, pageable);
         PageResponse page = new PageResponse();
         page.setRows(objPage.getContent());
         page.setTotal(objPage.getTotalElements());
		return page;
	}

}
