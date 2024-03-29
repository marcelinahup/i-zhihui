package com.palmyou.v7data.core.service.user.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageHelper;
import com.palmyou.v7data.api.domain.user.UserInfo;
import com.palmyou.v7data.api.service.user.UserServiceApi;
import com.palmyou.v7data.core.persistence.writer.UserInfoWriterMapper;

public class UserServiceImpl implements UserServiceApi {
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

//	@Resource
//	private Mapper<UserInfo> mapper;
	
	@Resource
	private UserInfoWriterMapper userInfoWriterMapper;
	/**
	 * 个人信息查询
	 * @param userId
	 * @return
	 */
	@Override
	public UserInfo getUserInfoById(String userId) {
		
		logger.debug("getUserById");
		return userInfoWriterMapper.selectByPrimaryKey(userId);
	}
	
	@Override
	public List<UserInfo> getUserListPage(int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		UserInfo record = new UserInfo();
		record.setUserId("1");
		return userInfoWriterMapper.select(record);
	}

}
