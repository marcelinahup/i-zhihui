package com.hk.ec.fw.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.runtime.RuntimeConstants;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import com.hk.ec.fw.spring.ext.MyStringTrimmerEditor;
import com.hk.ec.fw.spring.ext.StringArgumentUtil;

/**
 * Crontroller类的基础类，可以 除掉参数中的空格
 */
public abstract class AbstractController {
	
//	@Resource
//	private VelocityConfigurer velocityConfigurer;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(StringArgumentUtil.DATE_TIME_FORMAT);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
		
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(
				Integer.class, true));
		
		binder.registerCustomEditor(String.class, new MyStringTrimmerEditor(true));
	}

	/**
	 * 校验绑定对象
	 */
	protected BindException validateRequestBean(Validator validator, Object entity)
			throws Exception {
		Class<? extends Object> target = entity.getClass();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(entity,StringArgumentUtil.firstCharToLow(target.getSimpleName()));
		BindException errors = new BindException(binder.getBindingResult());
		validator.validate(entity, errors);
		return errors;
	}
	
	/**
	 * 初始化输出
	 * @param fileName
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected PrintWriter initPrintWriter(String fileName,HttpServletResponse response) throws Exception{
		   response.setContentType("application/octet-stream;charset=GBK");
		   response.setHeader("Content-Disposition","attachment;  filename="+fileName);
		   PrintWriter out = response.getWriter();//放在第一句是会出现乱码 
		   return out;
	}
	
	/**
	 * 输出数据
	 * @param writer
	 * @param data
	 * @param isEnd
	 * @throws Exception
	 */
	protected void writeData(PrintWriter writer ,String data , boolean isEnd) throws Exception{
		writer.write(data);
		writer.flush();
		if(isEnd){
			writer.close();
		}
	}
	
	/**
	 * 根据request取得IP
	 * @param request
	 * @return
	 */
	protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
	
	/**
	 * 利用数据和模板生成html字符串【可以使用自定义宏，但系统宏不能使用】
	 * @param vmFile 从view开始
	 * @param vmodel
	 * @return
	 */
	/*protected String merge2HTML(String vmFile,Map<String,Object> dataMap){

		//dataMap 中加入velocityToolBox生成的处理对象，方便vm里面的宏调用
		
		SAXBuilder builder = new SAXBuilder();  
        Document doc = null;
		try {
			// D:\eclipse-luna\servers\tomcat\tomcat7-ms\webapps\edu-v7cwts-ms\WEB-INF\view
			String viewFilePath = velocityConfigurer.getVelocityEngine().getProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH).toString();
			String toolBoxPath = viewFilePath.replace("view", "") + "velocity_toolbox.xml";

			doc = builder.build(new File(toolBoxPath));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        
		// 取得root
        Element rootEle = doc.getRootElement();
        
        @SuppressWarnings("unchecked")
		List<Element> tools = rootEle.getChildren("tool");
        for (Element tool : tools) {
        	
        	Element keyEle = tool.getChild("key");
        	Element clazzEle = tool.getChild("class");
        	
        	String key = keyEle.getValue();
			String clsLongName = clazzEle.getValue();
			try {
				Class<?> clazz = Class.forName(clsLongName);
				dataMap.put(key, clazz.newInstance());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
    	 
    	 * velocityConfigurer.resourceLoaderPath
		 * templateLocation the location of template, relative to Velocity's resource loader path
    	 
		return VelocityEngineUtils.mergeTemplateIntoString(velocityConfigurer.getVelocityEngine(), vmFile, "utf-8", dataMap);
	}*/
	
}
