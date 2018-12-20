package com.hg;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.web.servlet.ServletComponentScan;



@SpringBootApplication
@ServletComponentScan
public class App extends SpringBootServletInitializer{
	    public static void main(String[] args) {
	        SpringApplication.run(App.class, args);
	    }
}
