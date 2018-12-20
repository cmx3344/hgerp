package com.hg.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager; 
import javax.transaction.Transactional;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;


@Transactional  
@Repository  
public class ProductDao {

	@PersistenceContext  
    EntityManager entityManager;  
	
	
	public List<Map<String, Object>> getSqlQuery(String sql){
		Session session = entityManager.unwrap(org.hibernate.Session.class);
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);  
		List<Map<String, Object>> result =  query.list();
		return result;
	}
	
	public Object getCount(String sql){
		Session session = entityManager.unwrap(org.hibernate.Session.class);
		SQLQuery query = session.createSQLQuery(sql);
		Object obj = query.uniqueResult();
		if(null==obj){
			obj = 0;
		}
		return obj;
	}
}
