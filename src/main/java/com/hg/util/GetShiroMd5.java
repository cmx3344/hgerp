package com.hg.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class GetShiroMd5 {
	
	public static Object getShiroMd5(String username,String password){
		String hashAlgorithmName = "MD5";//加密方式  
        ByteSource salt = ByteSource.Util.bytes(username);//以账号作为盐值  
        int hashIterations = 1024;//加密1024次  
        Object result = new SimpleHash(hashAlgorithmName,password,salt,hashIterations);
	    return result;
	}

}
