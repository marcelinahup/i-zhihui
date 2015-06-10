package com.palmyou.fw.web.session;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;

import com.palmyou.fw.web.util.CookieUtil;
import com.palmyou.fw.web.util.RedisTemplateUtil;

/**
 * 自定义Session的工具类
 * @author James
 *
 */
public class SessionManager {
	
	public static SessionConfig config;
	
	public static SessionConfig getConfig() {
		return config;
	}

	public static void setConfig(SessionConfig config) {
		SessionManager.config = config;
	}

	/**
	 * 获取一个session
	 * @param request
	 * @param response
	 * @return
	 */
	public static SystemSession getSession(HttpServletRequest request,HttpServletResponse response){
		SystemSession session = null;
		Cookie cookie = CookieUtil.getCookieByName(request, config.getSessionKey());
		if(cookie == null){
			session = createSession(request,response);
		}else{
			RedisTemplate<String,SystemSession> redisTemplate = RedisTemplateUtil.getRedisTemplate();
			session = (SystemSession)redisTemplate.opsForValue().get(cookie.getValue());
			if(session == null){
				session = createSession(request,response);
			}else{
				/**
				 * 设置session 有效时间
				 */
				redisTemplate.expire(session.getSessionId(), config.getSessionTimeout(), TimeUnit.SECONDS);
			}
		}
		return session;
	}
	
	/**
	 * 新建一个session
	 * @param request
	 * @param response
	 * @return
	 */
	private static SystemSession createSession(HttpServletRequest request,HttpServletResponse response){
		SystemSession session = new SystemSession();;
		String sessionKey = generateSessionKey();
		RedisTemplate<String,SystemSession> redisTemplate = RedisTemplateUtil.getRedisTemplate();
		session.setSessionId(sessionKey);
		redisTemplate.opsForValue().set(sessionKey, session);
		/**
		 * 设置session 有效时间
		 */
		redisTemplate.expire(sessionKey, config.getSessionTimeout(), TimeUnit.SECONDS);
		response.setHeader("Set-Cookie",  config.getSessionKey() + "="+sessionKey+";Path=/;HTTPOnly");
		return session;
	}
	/**
	 * 通过uuid生成sessionID
	 * @return
	 */
	private static String generateSessionKey(){
		String sessionKey = UUID.randomUUID().toString(); 
		return sessionKey.replace("-","");
	}
	/**
	 * 更新session数据内容
	 * @param sessionKey
	 * @param session
	 */
	public static void updateSession(String sessionKey,SystemSession session){
		RedisTemplate<String,SystemSession> redisTemplate = RedisTemplateUtil.getRedisTemplate();
		redisTemplate.opsForValue().set(sessionKey, session);
		/**
		 * 设置session 有效时间
		 */
		redisTemplate.expire(sessionKey, config.getSessionTimeout(), TimeUnit.SECONDS);
	}
	/**
	 * 删除session,使其失效
	 * @param sessionKey
	 */
	public static void deleteSession(String sessionKey){
		RedisTemplate<String,SystemSession> redisTemplate = RedisTemplateUtil.getRedisTemplate();
		redisTemplate.delete(sessionKey);
	}
}
