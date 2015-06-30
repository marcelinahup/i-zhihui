package com.palmyou.v7data.ms.controller.user;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hk.ec.fw.result.JsonResult;
import com.hk.ec.fw.spring.ext.RequestBean;
import com.palmyou.v7data.api.domain.user.UserInfo;
import com.palmyou.v7data.api.service.user.UserServiceApi;
import com.palmyou.v7data.ms.controller.BaseController;

/**
 * 
 * @author James
 * 
 */
@Controller
@RequestMapping("userinfo")
public class UserInfoController extends BaseController {
	
	@Resource
	private UserServiceApi userServiceApi;
	
	private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	
	@RequestMapping("get")
	@ResponseBody
	public JsonResult insertIntoContactGroupByScreening(JsonResult result, 
			@RequestBean("user")UserInfo query) {
		
		UserInfo user = userServiceApi.getUserInfoById(query.getUserId());
		
		result.setOk(true);
		result.setData(user);
		return result;
	}
	
	@RequestMapping("page")
	@ResponseBody
	public JsonResult page(JsonResult result) {
		
		List<UserInfo> users = userServiceApi.getUserListPage(1, 10);
		
		result.setOk(true);
		result.setData(users);
		return result;
	}
}
