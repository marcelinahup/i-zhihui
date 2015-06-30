package com.hk.ec.fw.task;

import javax.annotation.Resource;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.utils.NetUtils;

/**
 * 基于zookeeper(dubbo的注册中心)的任务调度管理器
 */
public class TaskManager {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

	private static final String TIMER_NODE_NAME = "timer";
	private static final String MASTER_NODE_NAME = "master";

	@Resource
	private ZookeeperClientFactory zookeeperClientFactory;
	private boolean masterNode = false;
	
	/**
	 * 是否为主结点
	 * @return
	 */
	public boolean isMasterNode(){
		if(masterNode){
			logger.info("************************************************");
			logger.info("*******  Is master,Is running！  ****************");
			logger.info("************************************************");
		}else{
			logger.info("************************************************");
			logger.info("******* Not master,Not running！****************");
			logger.info("************************************************");
		}
		return masterNode;
	}
	
	/**
	 * bean 初始化使用
	 * check并创建主节点
	 */
	public void init(){
		
		logger.debug("taskManager.init......");
		
		String timerNodePath = zookeeperClientFactory.getAppPath() + "/" + TIMER_NODE_NAME ;
		ZkClient zkClient = zookeeperClientFactory.getZkClient();
		
		if(!zkClient.exists(timerNodePath)){
			try{
				zkClient.createPersistent(timerNodePath);
			}catch(Exception e){
				logger.error("create Persistent node error ,path:"+timerNodePath, e);
			}
		}
		
		// node初始化的时候，创建masterNode
		createMasterNode();
	}
	
	/**
	 * 创建，并监控主节点
	 * @return
	 */
	private boolean createMasterNode(){
		
		StringBuilder masterNodePathSB = new StringBuilder();
		masterNodePathSB.append(zookeeperClientFactory.getAppPath())
			.append("/").append(TIMER_NODE_NAME)
			.append("/").append(MASTER_NODE_NAME);
		String masterNodePath = masterNodePathSB.toString();
		
		String ip = NetUtils.getLocalHost();
		ZkClient zkClient = zookeeperClientFactory.getZkClient();
		
		// 存在主节点
		if(zkClient.exists(masterNodePath)){
			Object data = zkClient.readData(masterNodePath, true);
			
			// 当前为主节点
			if(data != null && data.equals(ip)){
				masterNode = true;
			}else{
				
				// 添加watch 监控master节点
				subscribeDataChanges(zkClient, masterNodePath);
			}
		}else{
			try{
				zkClient.createEphemeral(masterNodePath, ip);
				masterNode = true;
			}catch(Exception e){
				
				logger.error("create Ephemeral node error ,path:"+masterNodePath, e);
				
				if(zkClient.exists(masterNodePath)){
					
					// 添加watch 监控master节点
					subscribeDataChanges(zkClient, masterNodePath);
				}
			}
		}
		return masterNode;
	}
	
	/**
	 * 添加watch 监控master节点的状态，当主节点宕机，重新选择主节点
	 * @param zkClient
	 * @param masterNodePath
	 */
	private void subscribeDataChanges(ZkClient zkClient, String masterNodePath){
		
		zkClient.subscribeDataChanges(masterNodePath, new IZkDataListener(){
			
			@Override
			public void handleDataChange(String dataPath, Object data)
					throws Exception {
				createMasterNode();
			}
			
			@Override
			public void handleDataDeleted(String dataPath)
					throws Exception {
				createMasterNode();
			}
			
		});
	}
}
