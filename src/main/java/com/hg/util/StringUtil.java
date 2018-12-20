package com.hg.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;



public class StringUtil extends StringUtils {
	
	
	/**
	 * 验证某字符串是否符合邮箱格式
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str){
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";  
	    Pattern   p   =   Pattern.compile(regex);  
	    Matcher   m   =   p.matcher(str);  
	    return m.matches();  
	}
	
	/**
	 * 验证某字符串是否符合手机格式
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str){
		String regular = "1[3,4,5,8]{1}\\d{9}";
		Pattern pattern = Pattern.compile(regular);
		boolean flag = false;
		if (str != null) {
			Matcher matcher = pattern.matcher(str);
			flag = matcher.matches();
		}
		return flag;
	}
	
	
	/**
	 * 根据提供的参数，生成md5值</br>
	 * 会对传过来的值用UTF-8方式编码
	 * @param source
	 * @return	正常的字符串，出错会返回null
	 */

	public static String getMD5(String ss) {
		byte[] source;
		try {
			source = ss.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return null;
		}
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, 
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
		}
		return s;
	}

	
	/**
	 * 提供字符串是否可转换成数值型的判断</br>
	 * 如果可转成数值，则返回false</br>
	 * 如果不可转成数值，则返回true</br>
	 * isnan == is not a number</br>
	 * @param s	需要测试的字符串
	 * @return	true or false
	 */
	public static boolean isNAN(String s){
		if(null == s || s.length() == 0){
			return true;
		}
		Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+$");
		Matcher isNum = pattern.matcher(s);
		if(isNum.matches()){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean isInteger(String s){
		if(null == s || s.length() == 0){
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(s).matches();
	}
	
	
	/**
	 * 取随机的32位uuid
	 * @return
	 */
	public static String getUUID () {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	
	/**
	 * 判断一个字符串是不是空或者为""
	 * @param s
	 * @return
	 */
	public static boolean isNullOrSpace(String s){
		if(null == s || s.length() == 0){
			return true;
		}else{
			return false;
		}
	}
	

	/**
	 * 判断某个类里是否有某个方法
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static boolean isHaveSuchMethod(Class<?> clazz, String methodName){
		Method[] methodArray = clazz.getMethods();
		boolean result = false;
		if(null != methodArray){
			for(int i=0; i<methodArray.length; i++){
				if(methodArray[i].getName().equals(methodName)){
					result = true;
					break;
				}
			}
		}
		return result;
	}
	
	
	
	public static void beanCopy(Object source, Object target) {

		if (null == source || null == target) {
			return ;
		}


		Class<?> sourceClazz = source.getClass();
		Class<?> targetClazz = target.getClass();
		Field[] fields = targetClazz.getDeclaredFields(); // 取到所有类下的属性，也就是变量名
		Field field;

		for (int i = 0; i < fields.length; i++) {
			field = fields[i];
			String fieldName = field.getName();
			// 把属性的第一个字母处理成大写
			String stringLetter = fieldName.substring(0, 1).toUpperCase();
			// 取得setter方法名，比如setBbzt
			String setName = "set" + stringLetter + fieldName.substring(1);
			// 取得getter方法名
			String getName = "get" + stringLetter + fieldName.substring(1);
			// 真正取得get方法。
			Method setMethod = null;
			// 真正取得set方法
			Method sourceGetMethod = null;

			Class<?> fieldClass = field.getType();
			try {
				if (isHaveSuchMethod(targetClazz, setName)) {
					setMethod = targetClazz.getMethod(setName, fieldClass);
					if (isHaveSuchMethod(sourceClazz, getName)) {
						sourceGetMethod = sourceClazz.getMethod(getName);
					}
					Object sourceValue = sourceGetMethod.invoke(source);
					if (null != sourceValue) {
						setMethod.invoke(target, sourceValue);// 为其赋值
					}
				}
			} catch (Exception e) {
				
			}

		}
		return ;
	}
	
	
	/**
	 * 将来源对象的值 ，赋给目标对象</br>
	 * @param source	来源对象
	 * @param target	目标对象
	 * @param isCopyNull	如果source中的值为null时，是否将其赋给target对象
	 */
	public static void beanCopy(Object source, Object target, boolean isCopyNull) {

		if (null == source || null == target) {
			return ;
		}


		Class<?> sourceClazz = source.getClass();
		Class<?> targetClazz = target.getClass();
		Field[] fields = targetClazz.getDeclaredFields(); // 取到所有类下的属性，也就是变量名
		Field field;

		for (int i = 0; i < fields.length; i++) {
			field = fields[i];
			String fieldName = field.getName();
			// 把属性的第一个字母处理成大写
			String stringLetter = fieldName.substring(0, 1).toUpperCase();
			// 取得setter方法名，比如setBbzt
			String setName = "set" + stringLetter + fieldName.substring(1);
			// 取得getter方法名
			String getName = "get" + stringLetter + fieldName.substring(1);
			// 真正取得get方法。
			Method setMethod = null;
			// 真正取得set方法
			Method sourceGetMethod = null;

			Class<?> fieldClass = field.getType();
			try {
				if (isHaveSuchMethod(targetClazz, setName)) {
					setMethod = targetClazz.getMethod(setName, fieldClass);
					if (isHaveSuchMethod(sourceClazz, getName)) {
						sourceGetMethod = sourceClazz.getMethod(getName);
					}
					Object sourceValue = sourceGetMethod.invoke(source);
					if (null != sourceValue) {
						setMethod.invoke(target, sourceValue);// 为其赋值
					}else{
						if(isCopyNull){
							setMethod.invoke(target, sourceValue);
						}
					}
				}
			} catch (Exception e) {
			
			}
		}
		return ;
	}

	
	/**
	 * 生成六位随机数
	 * @return
	 */
	public static Integer getRandom(){
		Random ran=new Random();
		int r=0;
		m1:while(true){    
		    int n=ran.nextInt(1000000);  
		    r=n;   
		    int[] bs=new int[6];
		    for(int i=0;i<bs.length;i++){
				bs[i]=n%10;       
				n/=10;    
		    }    
		Arrays.sort(bs);    
		for(int i=1;i<bs.length;i++){
			if(bs[i-1]==bs[i]){ 
			continue m1;  
			}  
		} 
			break;
	}
		return r;
	}
	
	/**
	 * 数据库字段转java字段
	 * @return
	 */
	public static String fieldTransfer(String str){
		String[] s = str.replaceAll(" ", "").split("_");
		StringBuffer sb = new StringBuffer();
		for(int x = 0;x<s.length;x++){
			if(x==0){
				sb.append(s[x].toLowerCase());
			}else{
				char c = (s[x].charAt(0));
				if (Character.isUpperCase(c)){
					sb.append(c);
				}else{
					sb.append(s[x].substring(0, 1).toUpperCase());
				}
				sb.append(s[x].substring(1, s[x].length()).toLowerCase());
			}
		}
			return sb.toString();
	}
	
	/**
	 * 数据库表名转java表名
	 * @return
	 */
	public static String classTransfer(String str){
		String[] s = str.replaceAll(" ", "").split("_");
		StringBuffer sb = new StringBuffer();
		for(int x = 0;x<s.length;x++){
			char c = (s[x].charAt(0));
			if (Character.isUpperCase(c)){
				sb.append(c);
			}else{
				sb.append(s[x].substring(0, 1).toUpperCase());
			}
			sb.append(s[x].substring(1, s[x].length()).toLowerCase());
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * 判断对象是否为空
	 * 
	 * **/
	public static boolean beanNotNull(Object obj){
		boolean boo = false;
		if(null != obj){
			boo = true;
		}
		return boo;
	}
	
	
	/**
	 * 
	 * 
	 * 判断集合是否为空或size大于0
	 * 
	 * 
	 * */
	public static boolean listIsNotNull(List list){
		boolean boo = false;
		if(null != list&&list.size()>0){
			boo = true;
		}
		return boo;
	}
	
	
		public static String captureNameLow(String name) {
		   name = name.substring(0, 1).toLowerCase() + name.substring(1);
		   return  name;
		}
	   
		
		/** 
	    * 根据属性名获取属性值 
	    * */  
	   public static Object getFieldValueByName(String fieldName, Object o) {  
	          try {    
	              String firstLetter = fieldName.substring(0, 1).toUpperCase();    
	              String getter = "get" + firstLetter + fieldName.substring(1);    
	              Method method = o.getClass().getMethod(getter, new Class[] {});    
	              Object value = method.invoke(o, new Object[] {});    
	              return value;    
	          } catch (Exception e) {    
	              return null;    
	          }    
	    }
	   
	   
	   public static boolean isNumber(String str){
		   if(null == str||("").equals(str)){
			   return false;
		   }
		    Boolean strResult = str.matches("^[0-9]+(.[0-9]+)?$");
		    if(strResult == true) {
		    	return true;
		    } else {
		    	return false;
		    }
		    
		  }
	   
	   //删除list中重复的内容
	   public   static   void  removeDuplicate(List list)  {
		   for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
		    for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
		      if  (list.get(j).equals(list.get(i)))  {
		        list.remove(j);
		      } 
		    } 
		  } 
		}
	   
	   /**
	     * 对字符串进行sha1加密
	     * @param str
	     * @return
	     */
	    public static String sha1Hex(String str){
	        return DigestUtils.sha1Hex(str);
	    }
	    /**
	     * 对字符串进行base64加密
	     * @param str
	     * @return
	     */
	    public static String encodeBase64(String str){
	        return Base64.encodeBase64String(str.getBytes());
	    }
	     
	    /**
	     * 对字符串进行base64解密
	     * @param str
	     * @return
	     */
	    public static String decodeBase64(String str){
	        return new String(Base64.decodeBase64(str));
	    }
	    
	    /**
	     * 对字符串进行MD5加密
	     * @param str
	     * @return
	     */
	    public static  String md5Hex(String str){
	        return DigestUtils.md5Hex(str);
	    }
	    
	    /**
	     * 替换固定位置字符
	     * @param str
	     * @return
	     */
	    public static String replaceIndex(int index,String res,String str){
	    	return res.substring(0, index)+str+res.substring(index+1);
	    }
	    
	   
	   public static Object getShiroMD5PassWord(String username,String password){
		   String hashAlgorithmName = "MD5";//加密方式  
           ByteSource salt = ByteSource.Util.bytes(username);//以账号作为盐值  
           int hashIterations = 1024;//加密1024次  
           Object result = new SimpleHash(hashAlgorithmName,password,salt,hashIterations);
           return result;
	   }
}
