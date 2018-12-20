package com.hg.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.hg.util.StringUtil;



@Controller
@RequestMapping("fileManage")
public class FileManageController {
	
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)  
    public void upload(MultipartFile myfiles,Model model,HttpServletRequest request,HttpServletResponse response) throws IOException{  
            if(!myfiles.isEmpty()){
            	String fileName = StringUtil.getUUID()+myfiles.getOriginalFilename();
            	fileName = fileName.replace("/", "_");
            	fileName = fileName.replace(",", "_");
            	fileName = fileName.replace(" ", "_");
            	fileName = fileName.replace("#", "-");
                String realPath = request.getSession().getServletContext().getRealPath("/files");  
                FileUtils.copyInputStreamToFile(myfiles.getInputStream(), new File(realPath, fileName)); 
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(fileName);
        }
    } 

	
	@RequestMapping(value="/uploadb", method=RequestMethod.POST)  
    public void uploadb(MultipartFile myfiles,Model model,HttpServletRequest request,HttpServletResponse response,String location) throws IOException{  
            if(!myfiles.isEmpty()){
            	String fileName =myfiles.getOriginalFilename();
            	fileName = fileName.replace("/", "-");
            	SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            	String now = simple.format(new Date());
                String realPath = request.getSession().getServletContext().getRealPath("/files/"+location+"/"+now);  

                FileUtils.copyInputStreamToFile(myfiles.getInputStream(), new File(realPath, fileName)); 
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("/"+location+"/"+now+"/"+fileName);
        }
    } 
}
