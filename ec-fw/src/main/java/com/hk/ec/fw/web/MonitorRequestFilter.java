package com.hk.ec.fw.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监控请求执行时间
 * 
 * @author James
 * 
 */
public class MonitorRequestFilter implements Filter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MonitorRequestFilter.class);

	/**
	 * 初始化参数名称
	 */
	private final static String MONITOR = "monitor";
	private final static String OPEN = "true";

	/**
	 * 是否开启监控 true:开启
	 */
	private boolean isMonitor = false;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		StringBuilder buffer = new StringBuilder();
		if (isMonitor) {
			long beginTime = System.currentTimeMillis(); // 开始时间

			buffer.append(
					((HttpServletRequest) request).getRequestURL());

			// 执行filter
			doWebFilter(request, response, filterChain, buffer);

			buffer.append(" , clientip:")
				  .append(request.getRemoteAddr())
			      .append(", elapsed time:")
			      .append(System.currentTimeMillis() - beginTime);

			// 记录执行耗时等信息
			if (logger.isInfoEnabled()) {
				logger.info(buffer.toString());
			}
		} else {
			doWebFilter(request, response, filterChain, buffer);
		}
	}

	/**
	 * 执行filter
	 * 
	 * @param request
	 * @param response
	 * @param filterChain
	 * @param buffer
	 * @throws IOException
	 * @throws ServletException
	 */
	private void doWebFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain, StringBuilder buffer) throws IOException,
			ServletException {
		try {

			filterChain.doFilter(request, response);

		} catch (IOException e) {
			logger.error(buffer.toString(), e);
			throw e;
		} catch (ServletException e) {
			logger.error(buffer.toString(), e);
			throw e;
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		isMonitor = OPEN.equalsIgnoreCase(config.getInitParameter(MONITOR));
	}

	@Override
	public void destroy() {

	}

}
