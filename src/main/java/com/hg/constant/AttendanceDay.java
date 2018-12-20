package com.hg.constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttendanceDay {
	
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static Calendar startDate = Calendar.getInstance();
	private static Calendar endDate = Calendar.getInstance();
	
	/*
	 * 出勤天数
	 * 
	 */
	public static final Double DUE_ATTENDANCE_DAY = 26D;
	
	
	public static String getPrevMonthLastDay(){
		Calendar cale = Calendar.getInstance();   
		cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
		String  lastDay = sdf.format(cale.getTime());
		return lastDay;
	}
	
	/**
	 * 
	* @Title: getSeniorityPay   
	* @Description: 获取工龄工资
	* @param @return 
	* @return Double
	* @throws
	 */
	public static Double getSeniorityPay(int months){
		
		if(months==4){
			return 200D;
		}else if(months>4){
			Double a = Double.parseDouble(""+months);
			Double x = Math.ceil(a/new Double(12));
			
			return x*50;
		}else{
			return 0D;
		}
	}

	/**
	 * 
	* @Title: monthBetween   
	* @Description: 计算两个月份之间的差值
	* @param @param start
	* @param @param end
	* @param @return
	* @param @throws ParseException 
	* @return int
	* @throws
	 */
	public static int monthBetween(String start, String end) throws ParseException{
		startDate.setTime(sdf.parse(start));
		endDate.setTime(sdf.parse(end));
		int result = yearsBetween(start, end) * 12 + endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
		return result+1;
	}
	
	/**
	 * 
	* @Title: yearsBetween   
	* @Description: 计算两个年份之间的差值
	* @param @param start
	* @param @param end
	* @param @return
	* @param @throws ParseException 
	* @return int
	* @throws
	 */
	public static int yearsBetween(String start, String end) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		startDate.setTime(sdf.parse(start));
		endDate.setTime(sdf.parse(end));
		return (endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR));
	}
	/**
	 * 
	* @Title: getBirthdayFromIdentificationNumber   
	* @Description: 从是身份证中获取生日
	* @param @param IdentificationNumber
	* @param @return 
	* @return String
	* @throws
	 */
	public static String getBirthdayFromIdentificationNumber(String IdentificationNumber){
		if(null == IdentificationNumber&&("").equals(IdentificationNumber)){
			return "";
		}
		return IdentificationNumber.substring(6,10)+"-"+IdentificationNumber.substring(10,12)+"-"+IdentificationNumber.substring(12,14);
	}
	/**
	 * 
	* @Title: getBirthdayFromIdentificationNumber   
	* @Description: 获取出生月份
	* @param @param IdentificationNumber
	* @param @return 
	* @return String
	* @throws
	 */
	public static String getMonthFromIdentificationNumber(String IdentificationNumber){
		if(null == IdentificationNumber&&("").equals(IdentificationNumber)){
			return "";
		}
		return IdentificationNumber.substring(10,12);
	}
	
	public static String getYear(String startYear,int years) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(startYear));  
		cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)+years);
		Date date=cal.getTime();
		return sdf.format(date);
	}
	
	public static List<String> getDaysByMonth(String month) throws ParseException{
		
		  SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		  
		  Calendar calstar= Calendar.getInstance();
		  
		  String[] date = month.split("-");
		  
		  calstar.set(Calendar.YEAR, Integer.parseInt(date[0]));

		  calstar.set(Calendar.MONTH, Integer.parseInt(date[1]));

		  calstar.set(Calendar.DAY_OF_MONTH, 0);//最后一天
		
		  
		  Date firstDate=sm.parse(month+"-01");
		  
		  Long time = 24 * 60 * 60 * 1000L;
		  
		  int  t = 0;
		  
		  Date current = new Date(firstDate.getTime() + t * time);
		  
		  Date last=sdf.parse(sm.format(calstar.getTime()));
		  
		  List<String> listDate = new ArrayList<String>();
		  
		  while(current.compareTo(last)<=0){
			  listDate.add(sm.format(current));
			  t++;
	 	       current = new Date(firstDate.getTime() + t * time);
		  }
		  
		  return listDate;
	}

}
