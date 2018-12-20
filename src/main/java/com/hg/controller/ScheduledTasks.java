package com.hg.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hg.util.DateUtil;
import com.hg.util.MySQLDatabaseBackup;
@Lazy(false) 
@Component
@EnableScheduling  
public class ScheduledTasks {
	
	 @Scheduled(fixedRate = 1000*60*30)//fixedRate = 50000表示当前方法开始执行50000ms后，Spring scheduling会再次调用该方法
	 public void testFixedRate() throws InterruptedException, ParseException {
		 SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
		 Date startTime = df.parse("07:00");
	     Date endTime =   df.parse("20:00");
	     Date now = df.parse(df.format(new Date()));
	     if(DateUtil.isEffectiveDate(now, startTime, endTime)){
		   MySQLDatabaseBackup.backup();
		 }
	 }

}
