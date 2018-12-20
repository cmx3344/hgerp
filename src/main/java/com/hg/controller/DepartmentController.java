package com.hg.controller;


import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hg.dao.DepartmentDao;
import com.hg.model.Department;

@Controller
@RequestMapping("department")
public class DepartmentController {
	private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);  
	
	@Inject
	private DepartmentDao departmentDao;
	
	
	@RequestMapping("/list")
	public String list(){
		
		return "main/department/list";
	}
	
	@RequestMapping("/getList")
	@ResponseBody
	public Object getList(){
		return departmentDao.findAll();
	}
	
	
	@RequestMapping("/edit")
	public String edit(Long id,Model model){
		if(null != id){
			Department dep = departmentDao.findOne(id);
			model.addAttribute("bean", dep);
		}
		return "main/department/edit";
	}

	
	@RequestMapping("/doEdit")
	@ResponseBody
	public String doEdit(Department dep){
		departmentDao.save(dep);
		return "success";
	}
}
