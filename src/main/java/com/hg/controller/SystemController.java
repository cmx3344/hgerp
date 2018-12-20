package com.hg.controller;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hg.dao.UserDao;
import com.hg.dao.UserMessageDao;
import com.hg.model.User;
import com.hg.model.UserMessage;
import com.hg.util.GetShiroMd5;
@Controller
public class SystemController {
	
	@Autowired
	private UserDao userDao;
	@Inject
    private UserMessageDao userMessageDao;
	
    @RequestMapping(value="/index",method=RequestMethod.GET)
    public String index(Model model,HttpServletRequest request) {
    	String userChnName = (String) SecurityUtils.getSubject().getSession().getAttribute("userChnName");
    	String username = (String) SecurityUtils.getSubject().getSession().getAttribute("username");
    	Long userId = (Long) SecurityUtils.getSubject().getSession().getAttribute("userId");
    	List<UserMessage> list = userMessageDao.findByUserIdAndState(userId,1);
    	model.addAttribute("list", list);
    	model.addAttribute("message",userChnName);
    	model.addAttribute("username",username);
        return "main/index";
    }
    
    @RequestMapping(value="/login",method=RequestMethod.GET)
    public String login() {
        return "main/login";
    }
    
    @RequestMapping(value="/getMenu")
    public Object getMenu() {
        return "main/menu";
    }
    
    /** 
     * 登录方法 
     * @param userInfo 
     * @return 
     */  
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(User user) {  
        try {  
        	SecurityUtils.getSubject().login(new UsernamePasswordToken(user.getUsername(), user.getPassword()));  
        	User users = userDao.findUser(SecurityUtils.getSubject().getPrincipal().toString());
        	if(users!=null &&("启用").equals(users.getLocked())){
        		return "success";  
        	}else{
        		return "error1";  
        	}
        }  catch (AuthenticationException e) {  
            return "error2"; 
        }  
    }  
    
    
    @RequestMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/login";
    }
    
    
    //修改密码
    @RequestMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(String oldPassword,String newPassword) {
    	Object username = SecurityUtils.getSubject().getPrincipal();
    	Object result = GetShiroMd5.getShiroMd5(username.toString(), oldPassword);
    	User user = userDao.findByUsernameAndPassword(username.toString(), result.toString());
    	if(oldPassword.equals(newPassword)){
    		return "e1";
    	}
    	if(user!=null){
    		Object result2 = GetShiroMd5.getShiroMd5(username.toString(), newPassword);
    		user.setPassword(result2.toString());
    		userDao.save(user);
    		return "success";
    	}else{
    		return "fail";
    	}
    }
    
    //判断登录状态
    @RequestMapping("/judgeLogin")
    @ResponseBody
    public String judgeLogin(HttpSession session) {
        String u = (String) session.getAttribute("userChnName");
        if(u ==null){
        	return "1";
        }else{
        	return "2";
        }
    }
    
    //websocket
    @RequestMapping("/websocket")
    public String websocket(HttpSession session) {
    	return "main/websocket";
    }
}
