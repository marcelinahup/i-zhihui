package com.hk.ec.fw.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * 静态资源文件压缩
 * @author James
 *
 */
public class StaticResourceCompressor {
	private static final Logger logger = LoggerFactory.getLogger(StaticResourceCompressor.class);
	private static final String jsPath = "resource/js/";
	private static final String cssPath = "resource/css/";
	private static final String tempPath = "resource/temp/";
	private static final String encoding = "UTF-8";
	
	public static void javascriptCompress(List<String> fileNames , HttpServletResponse response){
		String tempFileName = generateTempFileName("js");
		File tempFile = getStaticResorceFile(tempPath,tempFileName);
		StringBuilder mergeContent = new StringBuilder();
		Reader reader = null;
		Writer writer = null;
		for(String fileName : fileNames){
			File file = getStaticResorceFile(jsPath, fileName);
			if(file.exists()){
				try{
					List<String> fileContentList = FileUtils.readLines(file, encoding);
					for(String content : fileContentList){
						mergeContent.append(content).append("\n");
					}
				}catch(Exception e){
					logger.error(e.getMessage(), e);
				}
			}
		}
		try {
			FileUtils.writeStringToFile(tempFile, mergeContent.toString(), encoding , false);
			reader = new InputStreamReader(new FileInputStream(tempFile),encoding);
			JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new ErrorReporter() {

	             public void warning(String message, String sourceName,
	                     int line, String lineSource, int lineOffset) {
	                 if (line < 0) {
	                	 logger.warn(message);
	                 } else {
	                	 logger.warn("  " + line + ':' + lineOffset + ':' + message);
	                 }
	             }

	             public void error(String message, String sourceName,
	                     int line, String lineSource, int lineOffset) {
	            	  if (line < 0) {
	                 	 logger.error(message);
	                  } else {
	                 	 logger.error("  " + line + ':' + lineOffset + ':' + message);
	                  }
	             }

	             public EvaluatorException runtimeError(String message, String sourceName,
	                     int line, String lineSource, int lineOffset) {
	                 error(message, sourceName, line, lineSource, lineOffset);
	                 return new EvaluatorException(message);
	             }
	         });
			reader.close();
			reader = null;
			writer = new OutputStreamWriter(response.getOutputStream(),encoding);
			compressor.compress(writer, -1, true, false,false, false);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if(tempFile != null && tempFile.exists()){
				tempFile.delete();
			}
		}
	}
	
	public static void cssCompress(List<String> fileNames , HttpServletResponse response){
		String tempFileName = generateTempFileName("css");
		File tempFile = getStaticResorceFile(tempPath,tempFileName);
		StringBuilder mergeContent = new StringBuilder();
		Reader reader = null;
		Writer writer = null;
		for(String fileName : fileNames){
			File file = getStaticResorceFile(cssPath, fileName);
			if(file.exists()){
				try{
					List<String> fileContentList = FileUtils.readLines(file, encoding);
					for(String content : fileContentList){
						mergeContent.append(content).append("\n");
					}
				}catch(Exception e){
					logger.error(e.getMessage(), e);
				}
			}
		}
		try {
			FileUtils.writeStringToFile(tempFile, mergeContent.toString(), encoding , false);
			reader = new InputStreamReader(new FileInputStream(tempFile),encoding);
			writer = new OutputStreamWriter(response.getOutputStream(),encoding);
			CssCompressor compressor = new CssCompressor(reader);
			compressor.compress(writer, -1);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if(tempFile != null && tempFile.exists()){
				tempFile.delete();
			}
		}
	}
	
	private static File getStaticResorceFile(String resourcePath,String fileName){
		URL url = StaticResourceCompressor.class.getClassLoader().getResource("../../");
		StringBuilder sb = new StringBuilder();
		sb.append(url.getPath()).append(resourcePath).append(fileName);
		File file = new File(sb.toString());
		File parentFile = file.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		return file;
	}
	
	private static String generateTempFileName(String postfix){
		StringBuilder fileName = new StringBuilder(); 
	    fileName.append(UUID.randomUUID().toString()).append(".").append(postfix);
		return fileName.toString();
	}
}
