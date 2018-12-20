package com.hg.util;

import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.hg.dao.UserDao;
import com.hg.model.Role;
import com.hg.model.User;



public class MyShiroRealm extends AuthorizingRealm {
	
	@Autowired
	private UserDao userRepository;
	
	
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        //获取用户的输入的账号.
    	UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;
    	Object result = GetShiroMd5.getShiroMd5(token.getUsername(), String.valueOf(token.getPassword()));
    	User user = userRepository.findByUsernameAndPassword(token.getUsername(),result.toString());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        if(user==null){
        	return null;
        }
        Set<Role> set = user.getRoles();
        Long roleId = 0L;
        for (Role obj: set) {  
        	roleId = obj.getId();
        }
        SecurityUtils.getSubject().getSession().setAttribute("userChnName",user.getUserChnName());
        SecurityUtils.getSubject().getSession().setAttribute("roleId",roleId);
        SecurityUtils.getSubject().getSession().setAttribute("userId",user.getId());
        SecurityUtils.getSubject().getSession().setAttribute("username",user.getUsername());
        return new SimpleAuthenticationInfo(token.getUsername(), String.valueOf(token.getPassword()), getName());
    }

}