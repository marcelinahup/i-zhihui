package com.palmyou.v7data.ms.web;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.hk.ec.fw.spring.ext.SpringUtils;

/**
 * 系统启动加载类。所有需要初始化的数据请放在这里。
 * @author James
 *
 */
public class SystemContextListener extends ContextLoaderListener {

	@Override
	public void contextInitialized ( ServletContextEvent sce )
	{
		super.contextInitialized(sce);
		SpringUtils.setApplicationContext((WebApplicationContext) sce.getServletContext().
				getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}
