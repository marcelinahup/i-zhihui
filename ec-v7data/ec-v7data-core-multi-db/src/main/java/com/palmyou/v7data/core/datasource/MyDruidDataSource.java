package com.palmyou.v7data.core.datasource;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcConstants;

public class MyDruidDataSource extends DruidDataSource {

	private static final long serialVersionUID = 9101060587349349429L;
	
	@Override
	public void init() throws SQLException {
		
		this.maxActive = 1;
		this.initialSize = 1;
		this.minIdle = 1;
		this.maxWait = 60000;
		this.timeBetweenEvictionRunsMillis = 60000;
		this.minEvictableIdleTimeMillis = 300000;
		this.setTestOnBorrow(true);
		
		/* 不同类型的数据库验证sql不同 */
		this.validationQuery = "select 1";
		// TODO driver null point
//		String realDriverClassName = driver.getClass().getName();
//        if (JdbcConstants.SQL_SERVER.equals(realDriverClassName)) {
//        	
//        	this.validationQuery = "select 1";
//        } else if (JdbcConstants.ORACLE_DRIVER.equals(realDriverClassName)) {
//        	
//        	this.validationQuery = "select 1 from dual";
//        } else if (JdbcConstants.MYSQL_DRIVER.equals(realDriverClassName)) {
//        	
//        	this.validationQuery = "select 1";
//        }
        
		this.removeAbandoned = true;
		this.setRemoveAbandonedTimeout(600);
		this.logAbandoned = true;
		this.poolPreparedStatements = false;
		
		super.init();
		
	}
	
	

}
