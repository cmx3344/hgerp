package com.hg.util;
import java.text.DateFormat;
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.ArrayList;
import java.util.Calendar;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.List;
import java.util.Map;  
/**  
 * 所有时间按当前2014-12-02计算  
 * @author alone  
 *  
 */  
public class DateUtil {  
    private static String ymdhms = "yyyy-MM-dd HH:mm:ss";    
    private static String ymd = "yyyy-MM-dd";    
    public static SimpleDateFormat ymdSDF = new SimpleDateFormat(ymd);    
    private static String year = "yyyy";    
    private static String month = "MM";    
    private static String day = "dd";    
    public static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat(ymdhms);    
    public static SimpleDateFormat yearSDF = new SimpleDateFormat(year);    
    public static SimpleDateFormat monthSDF = new SimpleDateFormat(month);    
    public static SimpleDateFormat daySDF = new SimpleDateFormat(day);    
    
    public static SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat(    
            "yyyy-MM-dd HH:mm");    
    public static SimpleDateFormat yyyyMMddHHmm2 = new SimpleDateFormat(    
            "yyyy-MM-dd HH:mm:ss"); 
    public static SimpleDateFormat yyyyMMddHHmmdd = new SimpleDateFormat(    
            "yyyy-MM-dd-HH-mm-ss"); 
    public static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");    
    
    public static SimpleDateFormat yyyyMMddHH_NOT_ = new SimpleDateFormat(    
            "yyyyMMdd");    
    
    public static SimpleDateFormat yyMMddHH_NOT_ = new SimpleDateFormat(    
            "yyMMdd"); 
    
    public static SimpleDateFormat mm_dd = new SimpleDateFormat(    
            "MM-dd");
    
    public static long DATEMM = 86400L;    
    /**  
     * 获得当前时间  
     * 格式：2014-12-02 10:38  
     * @return String  
     */  
    public static String getCurrentTime() {    
        return yyyyMMddHHmm.format(new Date());    
    }    
    
    /**  
     * 获得当前时间  
     * 格式：2014-12-02 10:38  
     * @return String  
     */  
    public static String getCurrentTime2() {    
        return yyyyMMddHHmmdd.format(new Date());    
    }    
    /**  
     * 获得当前时间  
     * 格式：12-02
     * @return String  
     */  
    public static String getCurrentTimeMm_DD() {    
        return mm_dd.format(new Date());    
    } 
    
    /**  
     * 可以获取昨天的日期  
     * 格式：2014-12-01  
     * @return String  
     */  
    public static String getYesterdayYYYYMMDD() {    
        Date date = new Date(System.currentTimeMillis() - DATEMM * 1000L);    
        String str = yyyyMMdd.format(date);    
        try {    
            date = yyyyMMddHHmmss.parse(str + " 00:00:00");    
            return yyyyMMdd.format(date);    
        } catch (ParseException e) {    
            e.printStackTrace();    
        }    
        return "";    
    }    
    /**  
     * 可以获取后退N天的日期  
     * 格式：传入2 得到2014-11-30  
     * @param backDay  
     * @return String  
     */  
    public String getStrDate(String backDay) {  
        Calendar calendar = Calendar.getInstance() ;  
        calendar.add(Calendar.DATE, Integer.parseInt("-" + backDay));  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;  
        String back = sdf.format(calendar.getTime()) ;  
        return back ;  
    }  
    /**  
     *获取当前的年、月、日  
     * @return String  
     */  
    public static String getCurrentYear() {    
        return yearSDF.format(new Date());    
    }   
    public static String getCurrentMonth() {    
        return monthSDF.format(new Date());    
    }   
    public static String getCurrentDay() {    
        return daySDF.format(new Date());    
    }    
    /**  
     * 获取年月日 也就是当前时间  
     * 格式：2014-12-02  
     * @return String  
     */  
    public static String getCurrentymd() {    
        return ymdSDF.format(new Date());    
    }    
    /**  
     * 获取今天0点开始的秒数  
     * @return long  
     */  
    public static long getTimeNumberToday() {    
        Date date = new Date();    
        String str = yyyyMMdd.format(date);    
        try {    
            date = yyyyMMdd.parse(str);    
            return date.getTime() / 1000L;    
        } catch (ParseException e) {    
            e.printStackTrace();    
        }    
        return 0L;    
    }    
    /**  
     * 获取今天的日期  
     * 格式：20161202  
     * @return String  
     */  
    public static String getTodateString() {    
        String str = yyyyMMddHH_NOT_.format(new Date());    
        return str;    
    }   
    
    /**  
     * 获取今天的日期  
     * 格式：161202  
     * @return String  
     */  
    public static String getTodateYyyyMMdd() {    
        String str = yyMMddHH_NOT_.format(new Date());    
        return str;    
    }   
    /**  
     * 获取昨天的日期  
     * 格式：20141201  
     * @return String  
     */  
    public static String getYesterdayString() {    
        Date date = new Date(System.currentTimeMillis() - DATEMM * 1000L);    
        String str = yyyyMMddHH_NOT_.format(date);    
        return str;    
    }    
    /**    
     * 获得昨天零点    
     *     
     * @return Date  
     */    
    public static Date getYesterDayZeroHour() {    
        Calendar cal = Calendar.getInstance();    
        cal.add(Calendar.DATE, -1);    
        cal.set(Calendar.SECOND, 0);    
        cal.set(Calendar.MINUTE, 0);    
        cal.set(Calendar.HOUR, 0);    
        return cal.getTime();    
    }    
    /**    
     * 把long型日期转String ；---OK    
     *     
     * @param date    
     *            long型日期；    
     * @param format    
     *            日期格式；    
     * @return    
     */    
    public static String longToString(long date, String format) {    
        SimpleDateFormat sdf = new SimpleDateFormat(format);    
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型    
        java.util.Date dt2 = new Date(date * 1000L);    
        String sDateTime = sdf.format(dt2); // 得到精确到秒的表示：08/31/2006 21:08:00    
        return sDateTime;    
    }    
    
    /**    
     * 获得今天零点    
     *     
     * @return Date  
     */    
    public static Date getTodayZeroHour() {    
        Calendar cal = Calendar.getInstance();    
        cal.set(Calendar.SECOND, 0);    
        cal.set(Calendar.MINUTE, 0);    
        cal.set(Calendar.HOUR, 0);    
        return cal.getTime();    
    }   
    /**    
     * 获得昨天23时59分59秒    
     *     
     * @return    
     */    
    public static Date getYesterDay24Hour() {    
        Calendar cal = Calendar.getInstance();    
        cal.add(Calendar.DATE, -1);    
        cal.set(Calendar.SECOND, 59);    
        cal.set(Calendar.MINUTE, 59);    
        cal.set(Calendar.HOUR, 23);    
        return cal.getTime();    
    }    
    /**    
     * String To Date ---OK    
     *     
     * @param date    
     *            待转换的字符串型日期；    
     * @param format    
     *            转化的日期格式    
     * @return 返回该字符串的日期型数据；    
     */    
    public static Date stringToDate(String date, String format) {    
        SimpleDateFormat sdf = new SimpleDateFormat(format);    
        try {    
            return sdf.parse(date);    
        } catch (ParseException e) {    
            return null;    
        }    
    }    
    
    /**    
     * 获得指定日期所在的自然周的第一天，即周日    
     *     
     * @param date    
     *            日期    
     * @return 自然周的第一天    
     */    
    public static Date getStartDayOfWeek(Date date) {    
        Calendar c = Calendar.getInstance();    
        c.setTime(date);    
        c.set(Calendar.DAY_OF_WEEK, 1);    
        date = c.getTime();    
        return date;    
    }    
    
    /**    
     * 获得指定日期所在的自然周的最后一天，即周六    
     *     
     * @param date    
     * @return    
     */    
    public static Date getLastDayOfWeek(Date date) {    
        Calendar c = Calendar.getInstance();    
        c.setTime(date);    
        c.set(Calendar.DAY_OF_WEEK, 7);    
        date = c.getTime();    
        return date;    
    }    
    
    /**    
     * 获得指定日期所在当月第一天    
     *     
     * @param date    
     * @return    
     */    
    public static Date getStartDayOfMonth(Date date) {    
        Calendar c = Calendar.getInstance();    
        c.setTime(date);    
        c.set(Calendar.DAY_OF_MONTH, 1);    
        date = c.getTime();    
        return date;    
    }    
    
    /**    
     * 获得指定日期所在当月最后一天    
     *     
     * @param date    
     * @return    
     */    
    public static Date getLastDayOfMonth(Date date) {    
        Calendar c = Calendar.getInstance();    
        c.setTime(date);    
        c.set(Calendar.DATE, 1);    
        c.add(Calendar.MONTH, 1);    
        c.add(Calendar.DATE, -1);    
        date = c.getTime();    
        return date;    
    }    
    
    /**    
     * 获得指定日期的下一个月的第一天    
     *     
     * @param date    
     * @return    
     */    
    public static Date getStartDayOfNextMonth(Date date) {    
        Calendar c = Calendar.getInstance();    
        c.setTime(date);    
        c.add(Calendar.MONTH, 1);    
        c.set(Calendar.DAY_OF_MONTH, 1);    
        date = c.getTime();    
        return date;    
    }    
    
    /**    
     * 获得指定日期的下一个月的最后一天    
     *     
     * @param date    
     * @return    
     */    
    public static Date getLastDayOfNextMonth(Date date) {    
        Calendar c = Calendar.getInstance();    
        c.setTime(date);    
        c.set(Calendar.DATE, 1);    
        c.add(Calendar.MONTH, 2);    
        c.add(Calendar.DATE, -1);    
        date = c.getTime();    
        return date;    
    }    
    
    /**    
     *     
     * 求某一个时间向前多少秒的时间(currentTimeToBefer)---OK    
     *     
     * @param givedTime    
     *            给定的时间    
     * @param interval    
     *            间隔时间的毫秒数；计算方式 ：n(天)*24(小时)*60(分钟)*60(秒)(类型)    
     * @param format_Date_Sign    
     *            输出日期的格式；如yyyy-MM-dd、yyyyMMdd等；    
     */    
    public static String givedTimeToBefer(String givedTime, long interval,    
            String format_Date_Sign) {    
        String tomorrow = null;    
        try {    
            SimpleDateFormat sdf = new SimpleDateFormat(format_Date_Sign);    
            Date gDate = sdf.parse(givedTime);    
            long current = gDate.getTime(); // 将Calendar表示的时间转换成毫秒    
            long beforeOrAfter = current - interval * 1000L; // 将Calendar表示的时间转换成毫秒    
            Date date = new Date(beforeOrAfter); // 用timeTwo作参数构造date2    
            tomorrow = new SimpleDateFormat(format_Date_Sign).format(date);    
        } catch (ParseException e) {    
            e.printStackTrace();    
        }    
        return tomorrow;    
    }    
    /**    
     * 把String 日期转换成long型日期；---OK    
     *     
     * @param date    
     *            String 型日期；    
     * @param format    
     *            日期格式；    
     * @return    
     */    
    public static long stringToLong(String date, String format) {    
        SimpleDateFormat sdf = new SimpleDateFormat(format);    
        Date dt2 = null;    
        long lTime = 0;    
        try {    
            dt2 = sdf.parse(date);    
            // 继续转换得到秒数的long型    
            lTime = dt2.getTime() / 1000;    
        } catch (ParseException e) {    
            e.printStackTrace();    
        }    
    
        return lTime;    
    }    
    
    /**    
     * 得到二个日期间的间隔日期；    
     *     
     * @param endTime    
     *            结束时间    
     * @param beginTime    
     *            开始时间    
     * @param isEndTime    
     *            是否包含结束日期；    
     * @return    
     */    
    public static Map<String, String> getTwoDay(String endTime,    
            String beginTime, boolean isEndTime) {    
        Map<String, String> result = new HashMap<String, String>();    
        if ((endTime == null || endTime.equals("") || (beginTime == null || beginTime    
                .equals(""))))    
            return null;    
        try {    
            java.util.Date date = ymdSDF.parse(endTime);    
            endTime = ymdSDF.format(date);    
            java.util.Date mydate = ymdSDF.parse(beginTime);    
            long day = (date.getTime() - mydate.getTime())    
                    / (24 * 60 * 60 * 1000);    
            result = getDate(endTime, Integer.parseInt(day + ""), isEndTime);    
        } catch (Exception e) {    
        }    
        return result;    
    }    
    
    /**    
     * 得到二个日期间的间隔日期；    
     *     
     * @param endTime    
     *            结束时间    
     * @param beginTime    
     *            开始时间    
     * @param isEndTime    
     *            是否包含结束日期；    
     * @return    
     */    
    public static Integer getTwoDayInterval(String endTime, String beginTime,    
            boolean isEndTime) {    
        if ((endTime == null || endTime.equals("") || (beginTime == null || beginTime    
                .equals(""))))    
            return 0;    
        long day = 0l;    
        try {    
            java.util.Date date = ymdSDF.parse(endTime);    
            endTime = ymdSDF.format(date);    
            java.util.Date mydate = ymdSDF.parse(beginTime);    
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);    
        } catch (Exception e) {    
            return 0 ;    
        }    
        return Integer.parseInt(day + "");    
    }    
    
    /**    
     * 根据结束时间以及间隔差值，求符合要求的日期集合；    
     *     
     * @param endTime    
     * @param interval    
     * @param isEndTime    
     * @return    
     */    
    public static Map<String, String> getDate(String endTime, Integer interval,    
            boolean isEndTime) {    
        Map<String, String> result = new HashMap<String, String>();    
        if (interval == 0 || isEndTime) {    
            if (isEndTime)    
                result.put(endTime, endTime);    
        }    
        if (interval > 0) {    
            int begin = 0;    
            for (int i = begin; i < interval; i++) {    
                endTime = givedTimeToBefer(endTime, DATEMM, ymd);    
                result.put(endTime, endTime);    
            }    
        }    
        return result;    
    }    
    
    
     public static boolean isValidDate(String str) {
    	      boolean convertSuccess=true;
    	         SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    	         try {
    	           format.setLenient(false);
    	            format.parse(str);
    	         } catch (ParseException e) {
    	           // e.printStackTrace();
    	            convertSuccess=false;
    	        } 
    	        return convertSuccess;
    	 }
     /**
      * 指定日期加上天数后的日期
      * @param num 为增加的天数
      * @param newDate 创建时间
      * @return
      * @throws ParseException 
      */
     public static String plusDay(int num,String newDate) throws ParseException{
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
         Date  currdate = format.parse(newDate);
         System.out.println("现在的日期是：" + currdate);
         Calendar ca = Calendar.getInstance();
         ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
         currdate = ca.getTime();
         String enddate = format.format(currdate);
         System.out.println("增加天数以后的日期：" + enddate);
         return enddate;
     }
     
     
     public static int compare_date(String DATE1, String DATE2) {
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         try {
             Date dt1 = df.parse(DATE1);
             Date dt2 = df.parse(DATE2);
             if (dt1.getTime() > dt2.getTime()) {
                 System.out.println("dt1 在dt2前");
                 return 1;
             } else if (dt1.getTime() < dt2.getTime()) {
                 System.out.println("dt1在dt2后");
                 return -1;
             } else {
                 return 0;
             }
         } catch (Exception exception) {
             exception.printStackTrace();
         }
         return 0;
     }
     
     public static List<String> getDates(String startDate,String endDate) throws ParseException{
    	 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");  
    	   
         List<String> dates = new ArrayList<String>();
         Date start = f.parse(startDate);  
         Date end = f.parse(endDate);  
         Date tomorrow = new Date();
         tomorrow = start;
         while(tomorrow.getTime()<=end.getTime()){
        	 dates.add(f.format(tomorrow));
        	 Calendar c = Calendar.getInstance();  
             c.setTime(tomorrow);  
             c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天  
             tomorrow = c.getTime();  
         }
         return dates;
     }
     
     /** 
      * 月份加一 
      * @param date 
      * @return 
      */  
     public static String monthAddDay(String date,int day){  
           
    	 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM");  
         try {  
             Calendar ct=Calendar.getInstance();  
             ct.setTime(df.parse(date));  
             ct.add(Calendar.MONTH, day);  
             return df.format(ct.getTime());  
         } catch (ParseException e) {  
             e.printStackTrace();  
         }  
   
         return "";  
     }  
     
     
     /**
      * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
      * 
      * @param nowTime 当前时间
      * @param startTime 开始时间
      * @param endTime 结束时间
      * @return
      * @author jqlin
      */
     public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
         if (nowTime.getTime() == startTime.getTime()
                 || nowTime.getTime() == endTime.getTime()) {
             return true;
         }

         Calendar date = Calendar.getInstance();
         date.setTime(nowTime);

         Calendar begin = Calendar.getInstance();
         begin.setTime(startTime);

         Calendar end = Calendar.getInstance();
         end.setTime(endTime);

         if (date.after(begin) && date.before(end)) {
             return true;
         } else {
             return false;
         }
     }
} 