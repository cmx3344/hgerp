package com.hg.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("other")
public class OtherController {
	
	
	@RequestMapping("/chartA")
	public String chartA(){
		
		return "/main/chart/chart1";
	}
	
	@RequestMapping("/chartB")
	public String chartB(){
		
		return "/main/chart/chart2";
	}
	@RequestMapping("/chartC")
	public String chartC(){
		
		return "/main/chart/chart3";
	}
	
	@RequestMapping("/chartD")
	public String chartD(Model model){
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM");
 		String now = simple.format(new Date());
 		model.addAttribute("now", now);
		return "/main/chart/chart4";
	}
	
	@RequestMapping("/chartE")
	public String chartE(Model model){
		SimpleDateFormat simple = new SimpleDateFormat("yyyy");
 		String now = simple.format(new Date());
 		model.addAttribute("now", now);
		return "/main/chart/chart5";
	}
}
