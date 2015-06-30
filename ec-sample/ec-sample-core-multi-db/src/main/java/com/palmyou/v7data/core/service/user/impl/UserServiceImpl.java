package com.palmyou.v7data.core.service.user.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.hk.ec.common.util.StringUtils;
import com.hk.ec.fw.spring.ext.SpringUtils;
import com.palmyou.v7data.api.domain.user.UserInfo;
import com.palmyou.v7data.api.service.user.UserServiceApi;
import com.palmyou.v7data.core.datasource.DatabaseContextHolder;
import com.palmyou.v7data.core.datasource.MyDruidDataSource;
import com.palmyou.v7data.core.persistence.writer.UserInfoWriterMapper;

public class UserServiceImpl implements UserServiceApi {
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Resource
	private UserInfoWriterMapper userInfoWriterMapper;
	
	@Resource
	RedisTemplate<String, String> redisTemplate;
	
	/**
	 * 个人信息查询
	 * @param userId
	 * @return
	 */
	@Override
	public UserInfo getUserInfoById(String userId) {
		
		Map<String, MyDruidDataSource> dataSourceMap = SpringUtils.getApplicationContext().getBeansOfType(MyDruidDataSource.class);
		
		UserInfo user = null;
		
		ValueOperations<String, String> userNameCache = redisTemplate.opsForValue();
		System.out.println("===========================================");
		for (String dataSourceKey : dataSourceMap.keySet()) {
			
			// 取得数据源
			DatabaseContextHolder.setCustomerType(dataSourceKey);
			user = userInfoWriterMapper.selectByPrimaryKey(userId);
			System.out.println(user.getUserName());
			
			String cacheValue = userNameCache.get("userName");
			if (StringUtils.isStrNull(cacheValue)) {
				cacheValue = "";
			}
			userNameCache.set("userName", user.getUserName() + "----" + cacheValue);
			
		}
		System.out.println("===========================================");
		System.out.println(userNameCache.get("userName"));
		return user;
	}
	@Override
	public List<UserInfo> getUserListPage(int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@Override
//	public void addUser(UserInfo userInfo) {
//		userInfoWriterMapper.insert(userInfo);
//	}

}
