package com.hg.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class OtherUtil {
	
	/**
	 * 
	* @Title: getRequest   
	* @Description: 获取request内置对象
	* @param @return 
	* @return HttpServletRequest
	* @throws
	 */
	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 
	* @Title: getRequest   
	* @Description: 获取系统路径
	* @param @return 
	* @return HttpServletRequest
	* @throws
	 */
	public static String getAbsolutePath(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getSession().getServletContext().getRealPath("/");
	}
	/**
	 * 
	* @Title: mkdir   
	* @Description: 生成文件夹
	* @param @param filePath 
	* @return void
	* @throws
	 */
	public static void fileMkdir(String filePath){
		File file = new File(filePath);
	     if(!file.exists()){
	    	 file.mkdirs();
		 }
	}
	/**
	 * 
	* @Title: getCurrentTime   
	* @Description: 获取当前系统时间到秒
	* @param @return 
	* @return String
	* @throws
	 */
	public static String getCurrentTime(){
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		return simple.format(new Date());
	}
	
	public static boolean isExistFile(String filePath){
		File file = new File(filePath);
		return file.exists();
	}
	
	/**
	 * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个list分别存obj1,obj2此属性名的值
	 * @param obj1 进行属性比较的对象1
	 * @param obj2 进行属性比较的对象2
	 * @param ignoreArr 选择忽略比较的属性数组
	 * @return 属性差异比较结果map
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> compareFields(Object obj1, Object obj2, String[] ignoreArr) {
		try{
			Map<String, String> map = new HashMap<String, String>();
			List<String> ignoreList = null;
			if(ignoreArr != null && ignoreArr.length > 0){
				// array转化为list
				ignoreList = Arrays.asList(ignoreArr);
			}
			if (obj1.getClass() == obj2.getClass()) {// 只有两个对象都是同一类型的才有可比性
				Class clazz = obj1.getClass();
				// 获取object的属性描述
				PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
						Object.class).getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了
					String name = pd.getName();// 属性名
					if(ignoreList != null && ignoreList.contains(name)){// 如果当前属性选择忽略比较，跳到下一次循环
						continue;
					}
					Method readMethod = pd.getReadMethod();// get方法
					// 在obj1上调用get方法等同于获得obj1的属性值
					Object o1 = readMethod.invoke(obj1);
					// 在obj2上调用get方法等同于获得obj2的属性值
					Object o2 = readMethod.invoke(obj2);
					if(o1 instanceof Timestamp){
						o1 = new Date(((Timestamp) o1).getTime());
					}
					if(o2 instanceof Timestamp){
						o2 = new Date(((Timestamp) o2).getTime());
					}
					if(o1 == null && o2 == null){
						continue;
					}
					if(o1 == null && o2 != null&&("").equals(o2)){
						continue;
					}
					if(o2 == null&&o1 != null&&("").equals(o1)){
						continue;
					}
					if(("").equals(o1)&&("").equals(o2)){
						continue;
					}
					if(!(o1).equals(o2)){
						map.put(name, o1+" 改为 "+o2);
					}
				}
			}
			return map;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
