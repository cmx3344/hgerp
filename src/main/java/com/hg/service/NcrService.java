package com.hg.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.metamodel.source.binder.Sortable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;





import com.hg.dao.ncr.NcrDao;
import com.hg.model.Ncr;
import com.hg.util.PageInfo;
import com.hg.util.PageResponse;

@Service
public class NcrService {
	
	@Inject
	private NcrDao ncrDao;
	
	public PageResponse queryList(PageInfo pageinfo,final Ncr bean) {
		 // 获得分页对象pageable，并且在pageable中页码是从0开始，设定按照sortType升序排列
		 Sort sort = new Sort(Direction.ASC,"state");
		 Pageable pageable = new PageRequest(pageinfo.getPage()-1, pageinfo.getRows(),sort);
         Page<Ncr> objPage = ncrDao.findAll(new Specification<Ncr>() {

            public Predicate toPredicate(Root<Ncr> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> lstPredicates = new ArrayList<Predicate>();

                if (bean!=null) {
                	if(bean.getNcrNum()!=null&&!("").equals(bean.getNcrNum())){
                		lstPredicates.add(cb.like(root.get("ncrNum").as(String.class), "%" + bean.getNcrNum() + "%"));
                	}
                	if(bean.getSerialNum()!=null&&!("").equals(bean.getSerialNum())){
                		lstPredicates.add(cb.like(root.get("serialNum").as(String.class), "%" + bean.getSerialNum() + "%"));
                	}
                	if(bean.getState()!=null){
                		lstPredicates.add(cb.equal(root.get("state").as(Integer.class), bean.getState()));
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
