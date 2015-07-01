package com.hk.ec.fw.task;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * zookeeperclient 工厂类
 * @author James
 *
 */
public class ZookeeperClientFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperClientFactory.class);
	private static final String APP_NAME_MARK = "\\$\\{appName\\}";
	
	// -----------------bean 可配置属性--------------------
	private String appName;
	private String appPath ="/apps_node/${appName}";
	private String zkServers;
	private int connectionTimeout = Integer.MAX_VALUE;
	private int sessionTimeOut = 15000 ;
	// ----------------------------------------------------
	
	private ZkClient zkClient;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAppPath() {
		return appPath;
	}
	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}
	
	public void setZkServers(String zkServers) {
		this.zkServers = zkServers;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setSessionTimeOut(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}
	
	public ZkClient getZkClient(){
		return zkClient;
	}
	
	/**
	 * bean 初始化使用
	 * 创建连接和客户端
	 */
	public void init(){
		
		ZkConnection zkConnection = new ZkConnection( zkServers,  sessionTimeOut);
		zkClient = new ZkClient( zkConnection,  connectionTimeout);
		appPath = appPath.replaceAll(APP_NAME_MARK, appName);
		
		// 先创建appPath节点
		if(!zkClient.exists(appPath)){
			try{
				zkClient.createPersistent(appPath, true);
			}catch(Exception e){
				logger.error("create persistent path : " + appPath + "error!", e);
			}
		}
	}
}
