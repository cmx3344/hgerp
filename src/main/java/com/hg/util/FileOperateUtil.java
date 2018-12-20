package com.hg.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

public class FileOperateUtil {

	    private static final String REALNAME = "realName";  
	    private static final String STORENAME = "storeName";  
	    private static final String SIZE = "size";  
	    private static final String SUFFIX = "suffix";  
	    private static final String CONTENTTYPE = "contentType";  
	    private static final String CREATETIME = "createTime";  
	    private static final String UPLOADDIR = "uploadDir/";  
	  
	    /** 
	     * 将上传的文件进行重命名 
	     *  
	     * @author geloin 
	     * @date 2012-3-29 下午3:39:53 
	     * @param name 
	     * @return 
	     */  
	    private static String rename(String name) {  
	  
	        Long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")  
	                .format(new Date()));  
	        Long random = (long) (Math.random() * now);  
	        String fileName = now + "" + random;  
	  
	        if (name.indexOf(".") != -1) {  
	            fileName += name.substring(name.lastIndexOf("."));  
	        }  
	  
	        return fileName;  
	    }  
	  
	    /** 
	     * 压缩后的文件名 
	     *  
	     * @author geloin 
	     * @date 2012-3-29 下午6:21:32 
	     * @param name 
	     * @return 
	     */  
	    private static String zipName(String name) {  
	        String prefix = "";  
	        if (name.indexOf(".") != -1) {  
	            prefix = name.substring(0, name.lastIndexOf("."));  
	        } else {  
	            prefix = name;  
	        }  
	        return prefix + ".zip";  
	    }  

	  
	    /** 
	     * 下载 
	     *  
	     * @author geloin 
	     * @date 2012-5-5 下午12:25:39 
	     * @param request 
	     * @param response 
	     * @param storeName 
	     * @param contentType 
	     * @param realName 
	     * @throws Exception 
	     */  
	    public static void download(HttpServletRequest request,  
	            HttpServletResponse response, String file, String realName) throws Exception {  
	        response.setContentType("text/html;charset=UTF-8");  
	        request.setCharacterEncoding("UTF-8");  
	        BufferedInputStream bis = null;  
	        BufferedOutputStream bos = null;  
	        String contentType = "application/octet-stream";
	        long fileLength = new File(file).length();  
	  
	        response.setContentType(contentType);  
	        response.setHeader("Content-disposition", "attachment; filename="  
	                + new String(realName.getBytes("utf-8"), "ISO8859-1"));  
	        response.setHeader("Content-Length", String.valueOf(fileLength));  
	  
	        bis = new BufferedInputStream(new FileInputStream(file));  
	        bos = new BufferedOutputStream(response.getOutputStream());  
	        byte[] buff = new byte[2048];  
	        int bytesRead;  
	        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
	            bos.write(buff, 0, bytesRead);  
	        }  
	        bis.close();  
	        bos.close();  
	    }
	    
	    public static void copyFile(HttpServletRequest request,String oldName,String newName,String filePath) throws IOException{  
	    	String realPath = request.getSession().getServletContext().getRealPath("/");  
	    	File file = new File(realPath+"files/"+"/"+oldName);
	    	if(!file.exists()){
	    		FileUtils.copyFile(new File(realPath+"files/"+"catalina/"+oldName), new File(realPath+"files/"+filePath+"/", newName));
            }
    }  
	    
	  public static void deleteFiles(HttpServletRequest request,String filePath){
	    if(null != filePath && !("").equals(filePath)){
	    	String realPath = request.getSession().getServletContext().getRealPath("/files/"+filePath);
	    	File file = new File(realPath);
	    	if(file.exists()){
	    		file.delete();
	    	}
	    }
	  }
	  
}
