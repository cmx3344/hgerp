//package com.hg.test;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.hg.dao.testRepository;
//import com.hg.model.MongoModel;
//
//
//@Controller
//public class TestController {
//	@Autowired
//	private testRepository testRepository;
//	@RequestMapping("mongotestCon")
//	public String tests(){
//		// 创建10个MongoModel，并验证MongoModel总数
////        testRepository.save(new MongoModel(1L, "didi", 30));
////        testRepository.save(new MongoModel(2L, "mama", 40));
////        testRepository.save(new MongoModel(3L, "kaka", 50));
////        testRepository.save(new MongoModel(4L, "didi2", 30));
////        testRepository.save(new MongoModel(5L, "mama", 40));
////        testRepository.save(new MongoModel(6L, "kaka2", 50));
////        testRepository.save(new MongoModel(7L, "kaka", 50));
////        testRepository.save(new MongoModel(8L, "kao", 50));
////        testRepository.save(new MongoModel(9L, "ekakae", 50));
////        testRepository.save(new MongoModel(10L, "kaka5", 50));
////        testRepository.save(new MongoModel(11L, "", 50));
////        testRepository.save(new MongoModel(12L, null, 50));
//		MongoModel u = testRepository.findByUsername("mama").get(0);
//		System.out.println(u.getUsername());
//		List<MongoModel> u5 = testRepository.findByUsernameNotNull();
//		return "";
//	}
//}
