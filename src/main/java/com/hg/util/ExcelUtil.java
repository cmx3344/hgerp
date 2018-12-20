package com.hg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	public static Workbook writeExcel(String excelExtName,String path){
		
		Workbook wb = null;
	    try {
	        if ("xls".equals(excelExtName)) {
	            wb = new HSSFWorkbook(new FileInputStream(new File(path)));
	        } else if ("xlsx".equals(excelExtName)) {
	            wb = new XSSFWorkbook(new FileInputStream(new File(path)));
	            
//	            Workbook wb = StreamingReader.builder()
//	                    .rowCacheSize(10000)  //缓存到内存中的行数，默认是10
//	                    .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
//	                    .open(in);  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
	        } 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return wb;
	}

}
