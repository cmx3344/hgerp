package com.hg;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
    public void handleGlobalException(HttpServletRequest req, Exception e) {
        //打印异常信息：
        e.printStackTrace();
    }

}
