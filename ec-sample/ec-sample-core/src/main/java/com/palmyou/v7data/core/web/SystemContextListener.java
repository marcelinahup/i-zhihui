package com.palmyou.v7data.core.web;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.palmyou.fw.spring.ext.PropertiesUtils;
import com.palmyou.fw.spring.ext.SpringUtils;
import com.palmyou.fw.ui.KeyValEntry;

/**
 * 系统启动加载类。所有需要初始化的数据请放在这里。
 * @author James
 *
 */
public class SystemContextListener extends ContextLoaderListener {

	/**
	 * 系统上下文统初始化
	 *
	 */
	@Override
	public void contextInitialized ( ServletContextEvent sce )
	{
		super.contextInitialized(sce);
		SpringUtils.setApplicationContext((WebApplicationContext) sce.getServletContext().
				getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));
		
//		ValueOperations<String, Set<KeyValEntry>> sysCodeValueOperations = RedisTemplateUtil.getRedisValueOperations();
		
		// 初始化系统编码-值数据
//		initSysCode(sysCodeValueOperations);
		
	}
	
	/**
	 * 系统上下文销毁
	 *
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
	
	
	/**
	 * 把sysCode.properties里面的数据缓存到redis中，方便其它项目使用
	 */
	private void initSysCode(ValueOperations<String, Set<KeyValEntry>> valueOperations){
		
		updateSysCodeRedisByPrefix("sys.gender", valueOperations);
		updateSysCodeRedisByPrefix("sys.grade", valueOperations);
	}
	
	/**
	 * 根据sysCode.properties里面的key取得数据，缓存到redis中，方便其它项目使用
	 * @param prefix
	 */
	private void updateSysCodeRedisByPrefix(String prefix, ValueOperations<String, Set<KeyValEntry>> valueOperations ){
		
		Set<String> keysSet = PropertiesUtils.getkeys("(" + prefix + ")\\d");
		
		Set<KeyValEntry> keyValEntrySet = new HashSet<>();
		for (String pKey : keysSet) {
			
			String val = PropertiesUtils.getString(pKey);
			
			KeyValEntry entry = JSONObject.parseObject(val,KeyValEntry.class);
			keyValEntrySet.add(entry);
			
		}
		valueOperations.set(prefix, keyValEntrySet);
	}
}
