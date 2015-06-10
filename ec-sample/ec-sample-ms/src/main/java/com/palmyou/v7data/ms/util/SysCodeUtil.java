package com.palmyou.v7data.ms.util;

import java.util.Set;

import org.apache.commons.collections.SetUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

import com.palmyou.fw.ui.KeyValEntry;
import com.palmyou.fw.web.util.RedisTemplateUtil;

public class SysCodeUtil {
	
	/**
	 * 取得项目路径
	 * @param res
	 * @return
	 */
	public static String getValue(String res){
		
		return "/ec-v7data-ms" + res;
	}
	
	/**
	 * 取得k-v列表
	 * @param type
	 * @return
	 */
	public static Set<KeyValEntry> getKeyValEntry(String type){
		
		ValueOperations<String, Set<KeyValEntry>> valueOperations = RedisTemplateUtil.getRedisValueOperations();
		
		Set<KeyValEntry> keyValEntrieSet = valueOperations.get(type);
		
		SetUtils.orderedSet(keyValEntrieSet);
		
		return keyValEntrieSet;
	}
	
	/**
	 * 根据类型和key取得实际的值
	 * @param type
	 * @param key
	 * @return
	 */
	public static String getValue(String type, String key){
		
		Set<KeyValEntry> keyValEntrySet = getKeyValEntry(type);
		
		for (KeyValEntry entry : keyValEntrySet) {
			
			if (entry.getKey().equals(key)) {
				return entry.getVal();
			}
		}
		
		return null;
	}
	
	/**
	 * 根据类型和值取得key
	 * @param type
	 * @param value
	 * @return
	 */
	public static String getKey(String type, String value){
		
		ValueOperations<String, Set<KeyValEntry>> valueOperations = RedisTemplateUtil.getRedisValueOperations();
		
		Set<KeyValEntry> keyValEntrySet = valueOperations.get(type);
		for (KeyValEntry entry : keyValEntrySet) {
			
			if (entry.getVal().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
