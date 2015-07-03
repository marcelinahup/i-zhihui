package com.hk.commons.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;


/*
 * 对于TESFS文件服务器的方法的封装
 */
public class TesFs {
	
	private static final Logger logger=Logger.getLogger(TesFs.class);

	public static void tesfsUpload(InputStream ins,String fileid,String filename,String saveFilename) throws Exception{
		//调用文件服务器 上传文件  tesfs_upload
				logger.info("start tesfs_upload.........");
				//String tesfileURL=PropertyUtils.getString("tesfs_upload");
				
//				String TESFS_SERVER=PropertyUtils.getString("TESFS_SERVER");
				String TESFS_SERVER= "TESFS_SERVER";
				
				//TESFS_SERVER_UPLOAD=file/upload.html
//				String TESFS_SERVER_UPLOAD=PropertyUtils.getString("TESFS_SERVER_UPLOAD");
				String TESFS_SERVER_UPLOAD="TESFS_SERVER_UPLOAD";
				
				String tesfileURL=TESFS_SERVER+TESFS_SERVER_UPLOAD;
				
				HttpClient httpclient = new DefaultHttpClient();  
				
				
		        HttpPost httppost = new HttpPost(tesfileURL); 
		        //uploadFile
		        InputStreamBody bin=null;
		        bin=new InputStreamBody(ins,filename);
		        
		        MultipartEntity reqEntity = new MultipartEntity();  
		        StringBody id=new StringBody(fileid);
		        reqEntity.addPart("id",id);
		        
		        //StringBody respath=new StringBody(saveFilename);
		        StringBody respath=new StringBody(saveFilename, "text/plain", Charset.forName( "UTF-8" ));
		        reqEntity.addPart("respath",respath);
		        
		        reqEntity.addPart("uploadFile", bin);  
		        
		       
	            
		      //将POST参数以UTF-8编码并包装成表单实体对象  
	          // httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));  
		       httppost.setEntity(reqEntity); 
		       
		        System.out.println("执行: " + httppost.getRequestLine());  
		          
		        HttpResponse response = httpclient.execute(httppost);  
		        logger.info("statusCode is " + response.getStatusLine().getStatusCode());  
		        HttpEntity resEntity = response.getEntity();  
		        //logger.info("----------------------------------------");  
		        //logger.info(response.getStatusLine());  
		        if (resEntity != null) {  
		        	logger.info("返回长度: " + resEntity.getContentLength());  
		        	logger.info("返回类型: " + resEntity.getContentType());  
		          InputStream in = resEntity.getContent();  
		          logger.info("in is " + in);  
		         
		        }  
	}
	
	/*
     * 由attachId 获取该文件的访问路径
     */
    public static String getTESFSURL(String attachid){
    	
    	if(attachid!=null && !attachid.equals("null")){
			//加入wavpath
			//http://192.168.101.253:8080/tesfs/file/download.html?id=CAFE678B804F38E937EB4D0580B9DD44
			//TESFS_SERVER   +DOWNLOADURL + attachid
			String TESFS_SERVER="TESFS_SERVER";
			String TESFS_SERVER_DOWNLOAD="TESFS_SERVER_DOWNLOAD";
			String url=TESFS_SERVER+TESFS_SERVER_DOWNLOAD+"?id="+attachid;
			//logger.info("---wavpath:"+wavpath);
			return url;
		}
    	
    	return null;
    	
    }
    
    /**http下载*/
	public static boolean httpDownload(String httpUrl,String saveFile){
        int byteread = 0;

        URL url = null;
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e1) {
			
			e1.printStackTrace();
			return false;
		}

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                //System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            
            if(fs != null){
            	fs.close();
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
