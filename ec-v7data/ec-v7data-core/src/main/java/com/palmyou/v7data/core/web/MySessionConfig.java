package com.palmyou.v7data.core.web;

import com.palmyou.fw.web.session.SessionConfig;


public class MySessionConfig implements SessionConfig {

	@Override
	public String getSessionKey() {
		
		return "v7data_ms_session";
	}

	@Override
	public long getSessionTimeout() {
		return 30 * 60;
	}

}
