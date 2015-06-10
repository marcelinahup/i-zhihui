package com.palmyou.v7data.core.persistence.writer;

import com.palmyou.v7data.api.domain.user.UserInfo;

public interface UserInfoWriterMapper {
    int deleteByPrimaryKey(String userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}