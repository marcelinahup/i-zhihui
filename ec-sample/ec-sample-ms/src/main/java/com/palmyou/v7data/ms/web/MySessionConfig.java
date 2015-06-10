package com.palmyou.v7data.ms.web;

import com.palmyou.fw.web.session.SessionConfig;

public class MySessionConfig implements SessionConfig {

	public static final String OS_SESSION_KEY = "OS_SESSION_KEY";
	public static final int OS_SESSION_TIMEOUT = 30 * 60;
	
	@Override
	public String getSessionKey() {
		
		return "v7data_ms_session";
	}

	@Override
	public long getSessionTimeout() {
		return 30 * 60;
	}

}
