package com.palmyou.v7data.ms;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings({ "hiding", "unchecked" })
public class UnitTestBase {
	
	// spring xml配置文件
	private String springXmlPath;
	
	// spring容器
	private ClassPathXmlApplicationContext context;
	
	public UnitTestBase(String springXmlPath){
		this.springXmlPath = springXmlPath;
	}
	
	protected <T extends Object> T getBean(String beanId){
		return (T)context.getBean(beanId);
	}
	
	protected <T extends Object> T getBean(Class<T> clazz){
		return (T)context.getBean(clazz);
	}
	
	@Before
	public void before(){
		if (StringUtils.isEmpty(springXmlPath)) {
			springXmlPath = "classpath*:spring-*.xml";
		}
		try {
			context = new ClassPathXmlApplicationContext(springXmlPath.split("*[,\\s]+*"));
			context.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@After
	public void after(){
		context.destroy();
	}

}
