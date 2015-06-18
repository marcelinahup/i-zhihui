package com.palmyou.v7data.api.service.user;

import java.util.List;

import com.palmyou.v7data.api.domain.user.UserInfo;

/**
 * 用户信息接口
 * @author tongc
 *
 */
public interface UserServiceApi {

	/**
	 * 个人信息查询
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfoById(String userId);

	public List<UserInfo> getUserListPage(int pageNum,int pageSize);

}
