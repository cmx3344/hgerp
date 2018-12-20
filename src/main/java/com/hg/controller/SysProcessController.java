package com.hg.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hg.dao.ProcessTempDao;
import com.hg.dao.SysProcessDao;
import com.hg.dao.TempSonDao;
import com.hg.model.ProcessTemp;
import com.hg.model.ProcessTempSon;
import com.hg.model.SysProcess;


@Controller
@RequestMapping("process")
public class SysProcessController {
	
	@Inject
	private SysProcessDao sysProcessDao;
	@Inject
	private ProcessTempDao processTempDao;
	@Inject
	private TempSonDao tempSonDao;
	
	
	@RequestMapping("/list")
	public String list(){
		
		return "main/sysProcess/list";
	}

	@RequestMapping("/getList")
	@ResponseBody
	public Object getList(){
		return sysProcessDao.findAll();
	}
	
	@RequestMapping("/edit")
	public String edit(Long id,Model model){
		if(null != id){
			SysProcess p = sysProcessDao.findOne(id);
			model.addAttribute("bean", p);
		}
		return "main/sysProcess/edit";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public String save(SysProcess p){
		sysProcessDao.save(p);
		return "success";
	}
	
	@RequestMapping("/tempList")
	public String tempList(){
		
		return "main/sysProcess/tempList";
	}

	@RequestMapping("/getTempList")
	@ResponseBody
	public Object getTempList(){
		return processTempDao.findAll();
	}
	
	@RequestMapping("/editTemp")
	public String editTemp(Long id,Model model){
		if(null != id){
			ProcessTemp p = processTempDao.findOne(id);
			model.addAttribute("bean", p);
		}
		return "main/sysProcess/editTemp";
	}
	
	@RequestMapping("/saveTemp")
	@ResponseBody
	public String saveTemp(ProcessTemp temp,ProcessTemp t){
		processTempDao.save(temp);
		if(null != t.getListSon()&&t.getListSon().size()>0){
			int x = 1;
			for(ProcessTempSon son : t.getListSon()){
				son.setParentId(temp.getId());
				son.setOrderNum(x);
				tempSonDao.save(son);
				x++;
			}
		}
		return "success";
	}
	
	@RequestMapping("/deletTempSon")
	@ResponseBody
	public String deletTempSon(Long id){
		tempSonDao.delete(id);
		return "success";
	}
	
	@RequestMapping("/getTempSon")
	@ResponseBody
	public Object getTempSon(Long parentId){
		List<ProcessTempSon> list = tempSonDao.findByParentId(parentId);
		for(ProcessTempSon son : list){
			SysProcess proc = sysProcessDao.findOne(son.getProcessId());
			son.setProcName(proc.getChnName());
		}
		return list;
	}
}
