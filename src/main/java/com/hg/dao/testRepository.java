//package com.hg.dao;
//
//import java.util.List;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.mongodb.repository.cdi.MongoRepositoryBean;
//
//import com.hg.model.MongoModel;
//
//public interface testRepository extends MongoRepository<MongoModel, Long>{
//	
//	/**
//     * Like（模糊查询）
//     * {"MongoModelname" : name} ( name as regex)
//     * */
//    List<MongoModel> findByUsernameLike(String username);
//
//    /**
//     * Like（模糊查询）
//     * {"MongoModelname" : name}
//     * */
//    List<MongoModel> findByUsername(String username);
//
//    /**
//     * GreaterThan(大于)
//     * {"age" : {"$gt" : age}}
//     * */
//    List<MongoModel> findByAgeGreaterThan(int age);
//    /**
//     * LessThan（小于）
//     * {"age" : {"$lt" : age}}
//     * */
//    List<MongoModel> findByAgeLessThan(int age);
//    /**
//     * Between（在...之间）
//     * {{"age" : {"$gt" : from, "$lt" : to}}
//     * */
//    List<MongoModel> findByAgeBetween(int from, int to);
//
//    /**
//     * IsNotNull, NotNull（是否非空）
//     *  {"MongoModelname" : {"$ne" : null}}
//     * */
//    List<MongoModel> findByUsernameNotNull();
//
//    /**
//     * IsNull, Null（是否为空）
//     *   {"MongoModelname" : null}
//     * */
//    List<MongoModel> findByUsernameNull();
//
//
//    /**
//     * Not（不包含）
//     *    {"MongoModelname" : {"$ne" : name}}
//     * */
//    List<MongoModel> findByUsernameNot(String name);
//
//}
