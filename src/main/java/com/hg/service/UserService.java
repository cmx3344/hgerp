package com.hg.service;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.hg.dao.UserDao;
import com.hg.model.Role;
import com.hg.model.User;



@Service
public class UserService {

    @Inject
    private UserDao userDao;

    
    public User save(User user) {
        // TODO Auto-generated method stub
        return userDao.save(user);
    }

    
    public Set<String> getStringRoles(String username) {
        // TODO Auto-generated method stub

        User user = userDao.findByUsername(username);

        Set<Role> roles = user.getRoles();

        Set<String> stringRoles = new HashSet<>();
        for (Role r : roles) {
            stringRoles.add(r.getRole());
        }

        return stringRoles;
    }

    
    public Set<String> getStringPermissions(String username) {
        // TODO Auto-generated method stub

        User user = userDao.findByUsername(username);

        Set<String> stringPermissions = new HashSet<>();

        return stringPermissions;
    }

}
