package com.hg.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hg.constant.AttendanceDay;
import com.hg.dao.AttendanceDao;
import com.hg.dao.EmployeeDao;
import com.hg.dao.GroupDao;
import com.hg.model.Attendance;
import com.hg.model.Employee;
import com.hg.model.Group;
import com.hg.model.GroupVoEmpl;
import com.hg.util.DateUtil;


@Controller
@RequestMapping("hr")
public class HrController {
	
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private AttendanceDao attendanceDao;
	
	@RequestMapping("groupList")
	public String groupList(){
		return "main/hr/groupList";
	}

	@RequestMapping("getGroupList")
	@ResponseBody
	public Object getGroupList(){
		
		return groupDao.findAll();
	}
	
	@RequestMapping("groupEdit")
	public String groupEdit(Long id,Model model){
		if(id!=null){
			Group group = groupDao.findOne(id);
			model.addAttribute("bean", group);
		}
		return "main/hr/groupEdit";
	}
	
	@RequestMapping("doGroupEdit")
	@ResponseBody
	public Object doGroupEdit(Group group){
		groupDao.save(group);
		return "success";
	}
	
	
	@RequestMapping("employeeList")
	public String employeeList(){
		return "main/hr/employeeList";
	}

	@RequestMapping("getEmployeeList")
	@ResponseBody
	public Object getEmployeeList(String name){
		if(name==null){
			name="";
		}
		List<GroupVoEmpl> list = employeeDao.findempls(name);
		return list;
	}
	
	@RequestMapping("employeeEdit")
	public String employeeEdit(Long id,Model model){
		if(id!=null){
			Employee empl = employeeDao.findOne(id);
			model.addAttribute("bean", empl);
		}
		return "main/hr/employeeEdit";
	}
	
	@RequestMapping("doEmployeeEdit")
	@ResponseBody
	public Object doEmployeeEdit(Employee empl) throws ParseException{
		empl.setInDate(empl.getInDate().replaceAll("/", "-"));
		String birthday = AttendanceDay.getBirthdayFromIdentificationNumber(empl.getIdentificationNumber());
		String month = AttendanceDay.getMonthFromIdentificationNumber(empl.getIdentificationNumber());
		empl.setBirthday(birthday);
		empl.setMonth(month);
		
		String end = AttendanceDay.getPrevMonthLastDay();
		int months = AttendanceDay.monthBetween(empl.getInDate(), end);
		int years = AttendanceDay.yearsBetween(birthday, DateUtil.getCurrentymd());
		empl.setAgeOfWork(months+"");
		empl.setAge(years+"");
		int x = 0;
		if(("男").equals(empl.getGender())){
			x = 50-Integer.parseInt(empl.getAge());
		}else{
			x = 60-Integer.parseInt(empl.getAge());
		}
		String lastYear = AttendanceDay.getYear(DateUtil.getCurrentymd(), x);
		empl.setRetireDate(lastYear);
		employeeDao.save(empl);
		return "success";
	}
	
	@RequestMapping("attendanceList")
	public String attendanceList(){
		return "main/hr/attendanceList";
	}
	
	@RequestMapping("removeEmpl")
	@ResponseBody
	public Object removeEmpl(Long id){
		employeeDao.delete(id);
		List<Attendance> list = attendanceDao.findByEmplId(id);
		for(Attendance att : list){
			attendanceDao.delete(att);
		}
		return "success";
	}
	
	@RequestMapping("getAttendanceList")
	@ResponseBody
	public Object getAttendanceList(){
		return attendanceDao.findAttendances();
	}
	
	@RequestMapping("attendanceEdit")
	public String attendanceEdit(Long id,Model model){
		if(id!=null){
			Attendance att = attendanceDao.findOne(id);
			model.addAttribute("bean",att);
		}
		return "main/hr/attendanceEdit";
	}
	
	@RequestMapping("removeAtten")
	@ResponseBody
	public Object removeAtten(@RequestParam(value = "ids", required = false) List<Long> ids){
		List<Attendance> list = (List<Attendance>) attendanceDao.findAll(ids);
		for(Attendance att : list){
			attendanceDao.delete(att);
		}
		return "success";
	}
	
	@RequestMapping("doAttendanceEdit")
	@ResponseBody
	public Object doAttendanceEdit(Attendance att) throws ParseException{
		Employee empl = employeeDao.findOne(att.getEmplId());
		Group group = groupDao.findOne(empl.getGroupId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int months = AttendanceDay.monthBetween(empl.getInDate(),AttendanceDay.getPrevMonthLastDay());
		att.setSeniorityPay(AttendanceDay.getSeniorityPay(months));
		Double attendancePay = AttendanceDay.DUE_ATTENDANCE_DAY>att.getAttendanceDay()?
				(att.getSeniorityPay()+empl.getSalary())/AttendanceDay.DUE_ATTENDANCE_DAY*att.getAttendanceDay():att.getSeniorityPay()+empl.getSalary();
		att.setAttendancePay(attendancePay);
		att.setPiecePay(group.getUnitPrice()*att.getWeight());
		Double restDay = AttendanceDay.DUE_ATTENDANCE_DAY>att.getAttendanceDay()?0D:att.getAttendanceDay()-AttendanceDay.DUE_ATTENDANCE_DAY;
		double df = new BigDecimal(restDay).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		att.setRestDay(df);
		double restPay = Math.round(att.getLengthenHour()*15+att.getRestDay()*empl.getSalary()/AttendanceDay.DUE_ATTENDANCE_DAY*2);
		att.setRestPay(restPay);
		att.setOtherPay(att.getOtherHour()*15);
		Double safetyPrize = AttendanceDay.DUE_ATTENDANCE_DAY>att.getAttendanceDay()?
				220/AttendanceDay.DUE_ATTENDANCE_DAY*att.getAttendanceDay():220;
		Integer x = Integer.parseInt(new java.text.DecimalFormat("0").format(safetyPrize));
		att.setSafetyPrize(x.doubleValue());
		Double attendanceBonus = att.getAttendanceDay()>=AttendanceDay.DUE_ATTENDANCE_DAY?100D:0D;
		att.setAttendanceBonus(attendanceBonus);
		Double examineMoney = att.getTool()+att.getQualityPrize()+att.getSafetyPrize()+att.getAttendanceBonus();
		att.setExamineMoney(examineMoney);
		double total = Math.round(att.getAttendancePay()+att.getPiecePay()+att.getRestPay()+att.getOtherPay()+examineMoney);
		att.setTotal(total);
		att.setDueDay(AttendanceDay.DUE_ATTENDANCE_DAY);
		attendanceDao.save(att);
		return "success";
	}
}
