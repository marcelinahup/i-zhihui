package com.hk.ec.fw.aop;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.hk.ec.fw.exception.ServiceResultException;
import com.hk.ec.fw.result.ServiceResult;

public class RollBackAspect {
	
	private static Logger logger = Logger.getLogger(RollBackAspect.class);
	
	public void doAfter(JoinPoint jp) {  
		logger.info("log Ending method: "  
                + jp.getTarget().getClass().getName() + "."  
                + jp.getSignature().getName());  
    }  
	
	@Resource
	private DataSourceTransactionManager transactionManager;
    
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {  
        long time = System.currentTimeMillis();  
        Object retVal = null;

        //获取事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try{
        	 retVal = pjp.proceed(); 
        	 
        	 //事务执行
        	 transactionManager.commit(status);
        } catch(ServiceResultException e){
            if(retVal == null){
            	ServiceResult<Object> result = new ServiceResult<Object>();
            	Map<String, Object> map = new HashMap<String, Object>();
            	map.put("ErrorMessage", e.getErrorMessage());
            	result.setMsgCode(e.getErrorCode());
            	result.setDataMap(map);
            	retVal = result;
            }       	
        	//事务回滚
        	transactionManager.rollback(status);
        	logger.error("ServiceResultException----------------------",e);
        }
        catch(RuntimeException e){
            if(retVal == null){
            	ServiceResult<Object> result = new ServiceResult<Object>();
            	result.setMsgCode(e.getMessage());
            	retVal = result;
            }
        	//事务回滚
        	transactionManager.rollback(status);
        	logger.error("RuntimeException----------------------",e);
        }catch(Exception e){
        	//事务回滚
        	transactionManager.rollback(status);
        	logger.error("update----------------------",e);
        }
         
        time = System.currentTimeMillis() - time;  
        
        if(retVal == null){
        	retVal = new ServiceResult<Object>();
        }
        return retVal;  
    }  
  
    public void doBefore(JoinPoint jp) {  
        logger.info("log Begining method: "  
                + jp.getTarget().getClass().getName() + "."  
                + jp.getSignature().getName());  
    }  
  
    public void doThrowing(JoinPoint jp, Throwable ex) {  
    	 logger.info("method " + jp.getTarget().getClass().getName()  
                + "." + jp.getSignature().getName() + " throw exception");  
    	 logger.info(ex.getMessage());  
    }  
}
