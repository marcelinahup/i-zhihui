package com.palmyou.fw.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class LogAspect {

	private static Logger logger = Logger.getLogger(LogAspect.class);

	public void doBefore(JoinPoint jp) {
		logger.debug("log Begining method: "
				+ jp.getTarget().getClass().getName() + "."
				+ jp.getSignature().getName());
	}

	public void doAfter(JoinPoint jp) {
		logger.debug("log Ending method: " + jp.getTarget().getClass().getName()
				+ "." + jp.getSignature().getName());
	}

	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long time = System.currentTimeMillis();
		Object result = pjp.proceed();
		time = System.currentTimeMillis() - time;

		return result;
	}

	public void doThrowing(JoinPoint jp, Throwable ex) {
		logger.debug("method " + jp.getTarget().getClass().getName() + "."
				+ jp.getSignature().getName() + " throw exception");
		logger.debug(ex.getMessage());
	}
}
