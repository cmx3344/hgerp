package com.hg.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hg.model.User;


public interface UserDao extends JpaRepository<User, Long>{

	User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    @Query("from User u where u.username=:username")
    User findUser(@Param("username") String username);

}
