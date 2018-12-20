package com.hg.util;
import java.io.BufferedReader;  
import java.io.File;
import java.io.FileOutputStream;  
import java.io.InputStream;
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
  
/** 
 * MySQL数据库备份 
 *  
 * @author GaoHuanjie 
 */  
public class MySQLDatabaseBackup {  
  
	 public static void backup() {
         try {
             Runtime rt = Runtime.getRuntime();
             Process child = rt.exec("C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump hgerp -P3306 -uroot -proot");


             // 设置导出编码为utf-8。这里必须是utf-8
             // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
             InputStream in = child.getInputStream();// 控制台的输出信息作为输入流
             InputStreamReader xx = new InputStreamReader(in, "utf-8");
             // 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码
             String inStr;
             StringBuffer sb = new StringBuffer("");
             String outStr;
             // 组合控制台输出信息字符串
             BufferedReader br = new BufferedReader(xx);
             while ((inStr = br.readLine()) != null) {
                 sb.append(inStr + "\r\n");
             }
             outStr = sb.toString();
             // 要用来做导入用的sql目标文件：
             File file = new File("F:\\backup\\"+DateUtil.getCurrentymd()+"\\");
             if(!file.exists()){
            	 file.mkdirs();
             }
             FileOutputStream fout = new FileOutputStream("F:\\backup\\"+DateUtil.getCurrentymd()+"\\"+DateUtil.getCurrentTime2()+".sql");
             OutputStreamWriter writer = new OutputStreamWriter(fout, "utf-8");
             writer.write(outStr);
             writer.flush();
             in.close();
             xx.close();
             br.close();
             writer.close();
             fout.close();
             child.destroy();
         } catch (Exception e) {
             e.printStackTrace();
         }
  
     }
}  