package com.hg.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hg.dao.DepartmentDao;
import com.hg.dao.MessageFileDao;
import com.hg.dao.RoleDao;
import com.hg.dao.UserDao;
import com.hg.dao.UserMessageDao;
import com.hg.model.Department;
import com.hg.model.MessageFile;
import com.hg.model.Role;
import com.hg.model.User;
import com.hg.model.UserMessage;
import com.hg.service.UserService;
import com.hg.util.DateUtil;
import com.hg.util.FileOperateUtil;
import com.hg.util.GetShiroMd5;




@Controller
@RequestMapping("/api/user")
public class UserController {
    @Inject
    private UserService userService;

    @Inject
    private DepartmentDao departmentDao;
    @Inject
    private RoleDao roleDao;
    @Inject
    private UserDao userDao;
    @Inject
    private UserMessageDao userMessageDao;
    @Inject
    private MessageFileDao messageFileDao;


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@PathVariable Long id) {
    	User user = userDao.findOne(id);
    	user.setLocked("锁定");
    	userDao.save(user);
        return "OK";
    }
    
    @RequestMapping(value = "/users")
    public String users() {
        return "main/user/user";
    }
    
    @RequestMapping(value = "/edit")
    public String edit(Long id,Model model) {
    	List<Department> list = (List<Department>) departmentDao.findAll();
    	if(null != id){
    		User user = userDao.findOne(id);
        	model.addAttribute("bean", user);
    	}
    	model.addAttribute("list", list);
        return "main/user/editUser";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String saveUser(@RequestParam String username,@RequestParam Long id, @RequestParam String password, @RequestParam String email,@RequestParam Long depId,
    		@RequestParam String userChnName, @RequestParam String locked,@RequestParam(value = "roles", required = false) List<Long> roleIds) {

    	Department dep = departmentDao.findOne(depId);
    	User u= userDao.findByUsername(username);
    	if(id==null&&u != null){
    		return "fail";
    	}
        User user = new User();
        user.setDepId(depId);
        user.setDepName(dep.getName());
        user.setUsername(username);
        Object result = GetShiroMd5.getShiroMd5(username, password);
        user.setPassword(result.toString());
        user.setEmail(email);
        user.setLocked(locked);
        user.setUserChnName(userChnName);
        user.setId(id);

        List<Role> roles = roleDao.findAll(roleIds);

        user.setRoles(new HashSet<>(roles));
        userService.save(user);
        return "success";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@RequestParam(value = "userId", required = true) Long userId,
            @RequestParam(value = "roles", required = true) List<Long> roleIds) {
        User user = userDao.findOne(userId);
        List<Role> roles = roleDao.findAll(roleIds);

        Set<Role> preRoles = user.getRoles();

        for (Role r : roles) {
            if (preRoles.contains(r))
                continue;
            user.getRoles().add(r);
        }

        try {
            userDao.save(user);
        } catch (Exception e) {
            return "update fail!";
        }

        return "OK";
    }
    
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {

        List<User> list = userDao.findAll();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/showMessage")
    public String showMessage(HttpSession session,Model model) {
    	Long userId = (Long) session.getAttribute("userId");
        return "main/user/userMessage";
    }

    @RequestMapping(value = "/getUserMessage")
    @ResponseBody
    public Object getUserMessage(HttpSession session,Model model,UserMessage u) {
    	Long userId = (Long) session.getAttribute("userId");
    	if(null != u&&null != u.getSubject()&&!("").equals(u.getSubject())){
    		List<UserMessage> list = userMessageDao.findByUserIdAndSubject(userId, u.getSubject());
    		return list;
    	}else{
    		List<UserMessage> list = userMessageDao.findByUserId(userId);
    		return list;
    	}
    }

    @RequestMapping(value = "/updateUserMessageState")
    @ResponseBody
    public String updateUserMessageState(Long id) {
    	UserMessage u = userMessageDao.findOne(id);
    	u.setState(2);
    	u.setReadTime(DateUtil.getCurrentTime());
    	userMessageDao.save(u);
        return "success";
    }
    
    @RequestMapping(value = "/getUserMessageTotal")
    @ResponseBody
    public Object getUserMessageTotal(HttpSession session,Model model) {
    	Long userId = (Long) session.getAttribute("userId");
    	List<UserMessage> list = userMessageDao.findByUserIdAndState(userId, 1);
    	int x = 0;
    	for(UserMessage message : list){
    		if(message.getType().intValue()==2&&message.getState().intValue()==1){
    			
    		}else{
    			x++;
    		}
    	}
        return x;
    }
    
    @RequestMapping(value = "/toMessage")
    public String toMessage(HttpSession session,Model model) {
    	Long userId = (Long) session.getAttribute("userId");
    	List<User> list = userDao.findAll();
    	List<User> list2 = new ArrayList<User>();
    	for(User u : list){
    		if(userId.intValue() != u.getId().intValue()){
    			list2.add(u);
    		}
    	}
    	model.addAttribute("list", list2);
        return "main/user/toMessage";
    }
    
    @RequestMapping(value = "/doMessage")
    @ResponseBody
    public String doMessage(UserMessage message,String ids,HttpSession session,String files) {
    	String userChnName = (String) session.getAttribute("userChnName");
    	Long myId = (Long) session.getAttribute("userId");
    	if(null == ids||("").equals(ids)){
    		return "fail";
    	}
    	String[] id = ids.split(",");
    	for(int x =0;x<id.length;x++){
    		UserMessage userMessage = new UserMessage();
    		userMessage.setMessage(message.getMessage());
    		userMessage.setMessageTime(DateUtil.getCurrentTime());
    		userMessage.setOperator(userChnName);
    		userMessage.setState(1);
    		userMessage.setSubject(message.getSubject());
    		userMessage.setUserId(Long.parseLong(id[x]));
    		userMessage.setMyId(myId);
    		userMessage.setType(1);
    		userMessageDao.save(userMessage);
    		if(null != files&&!("").equals(files)){
    			String[] file = files.split(",");
    			for(int y = 0;y<file.length;y++){
    				MessageFile f = new MessageFile();
    				f.setParentId(userMessage.getId());
    				f.setFilePath(file[y]);
    				f.setFileName(file[y].substring(32));
    				messageFileDao.save(f);
    			}
    		}
    	}
    	UserMessage userMessage = new UserMessage();
		userMessage.setMessage(message.getMessage());
		userMessage.setMessageTime(DateUtil.getCurrentTime());
		userMessage.setOperator(userChnName);
		userMessage.setState(1);
		userMessage.setSubject(message.getSubject());
		userMessage.setUserId(myId);
		userMessage.setMyId(myId);
		userMessage.setType(2);
		userMessageDao.save(userMessage);
		if(null != files&&!("").equals(files)){
			String[] file = files.split(",");
			for(int y = 0;y<file.length;y++){
				MessageFile f = new MessageFile();
				f.setParentId(userMessage.getId());
				f.setFilePath(file[y]);
				f.setFileName(file[y].substring(32));
				messageFileDao.save(f);
			}
		}
		
        return "success";
    }
    
    @RequestMapping(value = "/readMessage")
    public String readMessage(Long id,Model model) {
    	UserMessage message = userMessageDao.findOne(id);
    	message.setReadTime(DateUtil.getCurrentTime());
    	message.setState(2);
    	userMessageDao.save(message);
    	List<MessageFile> list = messageFileDao.findByParentId(id);
    	model.addAttribute("list", list);
    	model.addAttribute("bean", message);
        return "main/user/readMessage";
    }
    
	@RequestMapping(value = "download")
	public ModelAndView download(HttpServletRequest request,HttpServletResponse response,Long id) throws Exception {  
	  MessageFile son = messageFileDao.findOne(id);
	  String realPath = request.getSession().getServletContext().getRealPath("/files/"); 
	  FileOperateUtil.download(request, response, realPath+son.getFilePath(),  
	  		son.getFileName());  
	  return null;  
	} 
	
	@RequestMapping(value = "/getFiles")
	@ResponseBody
	public String getFiles(Long id){
		List<MessageFile> list = messageFileDao.findByParentId(id);
		StringBuffer table = new StringBuffer();
		table.append("<table>");
		table.append("<tr><th style=\"color: red;font-size: 16px;\">附件</th></tr>");
		  for(MessageFile file : list){
			  table.append("<tr>");
			  table.append("<td>").append("<a href=\"/hgerp/api/user/download?id="+file.getId()+"\" target=\"_blank\">"+file.getFileName()+"</a>").append("</td>");
			  table.append("</tr>");
		  }
		table.append("</table>");
		return table.toString();
	}
}
