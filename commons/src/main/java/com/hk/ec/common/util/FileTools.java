package com.hk.ec.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;

/*
 * 提供文件 上传下载的工具
 */
public class FileTools{
	
	//download
	public static void download( HttpServletResponse response
			,String downLoadPath,String realName) throws Exception {  
        response.setContentType("text/html;charset=UTF-8");  
      
        BufferedInputStream bis = null;  
        BufferedOutputStream bos = null;  
        long fileLength = new File(downLoadPath).length();  
  
        //response.setContentType("");  
        response.setHeader("Content-disposition", "attachment; filename="  
                + new String(realName.getBytes("utf-8"), "ISO8859-1"));  
        response.setHeader("Content-Length", String.valueOf(fileLength));  
  
        bis = new BufferedInputStream(new FileInputStream(downLoadPath));  
        bos = new BufferedOutputStream(response.getOutputStream());  
        byte[] buff = new byte[2048];  
        int bytesRead;  
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
            bos.write(buff, 0, bytesRead);  
        }  
        bis.close();  
        bos.close();  
    }  
	
	//upload
	public void upload(){
		//use springmvc function to do
	}
}