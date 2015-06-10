package com.palmyou.fw.web.util;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.palmyou.fw.spring.ext.SpringUtils;
import com.palmyou.fw.web.session.SystemSession;

@SuppressWarnings("unchecked")
public class RedisTemplateUtil {

	private RedisTemplateUtil(){}
	
	/**
	 * bean 池中取得redis valueOperations 对象
	 * @return
	 */
	@SuppressWarnings("hiding")
	public static <T> ValueOperations<String,T> getRedisValueOperations(){
		RedisTemplate<String,T> redisTemplate = (RedisTemplate<String,T>)SpringUtils.getBean("redisTemplate");
		return redisTemplate.opsForValue();
	}
	
	/**
	 * bean 池中取得redis 对象，模拟session使用
	 * 
	 */
	public static RedisTemplate<String,SystemSession> getRedisTemplate(){

		RedisTemplate<String,SystemSession> redisTemplate = (RedisTemplate<String,SystemSession>)SpringUtils.getBean("redisTemplate");
		return redisTemplate;
	}

}
