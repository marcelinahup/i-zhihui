package com.palmyou.common.security;

import java.security.MessageDigest;

/**
 * 不可逆加密
 * 依赖于HexConvertUtil进制转换类
 * @author admin
 *
 */
public class IrreversibleEncrypt {
	
	//--------------------------不可逆加密--------------------------------------
	public static final String ENCRYPT_MTH_MD5 = "MD5";
	public static final String ENCRYPT_MTH_SHA_1 = "SHA-1";
	public static final String ENCRYPT_MTH_SHA_256 = "SHA-256";
	public static final String ENCRYPT_MTH_SHA_512 = "SHA-512";
	
	/**
	 * md5小写加密 32
	 * @param src
	 * @return
	 */
	public static String md5(String src){
		return encrypt(ENCRYPT_MTH_MD5, src);
	}
	
	/**
	 * sha1小写加密 40
	 * @param src
	 * @return
	 */
	public static String sha1(String src){
		return encrypt(ENCRYPT_MTH_SHA_1, src);
	}
	
	/**
	 * sha256小写加密 64
	 * @param src
	 * @return
	 */
	public static String sha256(String src){
		return encrypt(ENCRYPT_MTH_SHA_256, src);
	}
	
	/**
	 * sha512小写加密 128
	 * @param src
	 * @return
	 */
	public static String sha512(String src){
		return encrypt(ENCRYPT_MTH_SHA_512, src);
	}
	
	/**
	 * 加密定义
	 * @return
	 */
	protected static String encrypt(String encryptMth, String src){
		String des = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(encryptMth);
			md.update(src.getBytes("UTF-8"));
			des = HexConvertUtil.bytes2Hex(md.digest());
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return des;
	}
	
	//--------------------------可逆加密--------------------------------------
	
	
}
