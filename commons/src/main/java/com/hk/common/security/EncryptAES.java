package com.hk.common.security;
import java.security.SecureRandom;  
import javax.crypto.Cipher;  
import javax.crypto.KeyGenerator;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;  
public class EncryptAES {  
	private EncryptAES(){
		
	}

	private static String strDefaultKey = "AES";
	
	/** 
     * AES加密 
     */  
    public static String encrypt(String strIn ) {
    	
    	String key = strDefaultKey;
        return encrypt(strIn, key);
    }  
  
    /** 
     * AES解密 
     */  
    public static String decrypt(String encrypted) {
    	String key = strDefaultKey;
        return decrypt(encrypted, key);
    }
    
    /** 
     * AES加密 
     */  
    public static String encrypt(String strIn, String key ){  
        byte[] result = null;
		try {
			byte[] rawKey = getRawKey(key.getBytes());
			result = encrypt(rawKey, strIn.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return toHex(result);  
    }  
  
    /** 
     * AES解密 
     */  
    public static String decrypt(String encrypted, String key){  
        if (key == null || "".equals(key) || encrypted == null || "".equals(encrypted)){  
            return null;  
        }  
        byte[] result = null;
		try {
			byte[] rawKey = getRawKey(key.getBytes());
			byte[] enc = toByte(encrypted);
			result = decrypt(rawKey, enc);
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return new String(result);  
    }
    
    public static String toHex(String txt) {  
        return toHex(txt.getBytes());  
    }  
  
    public static String fromHex(String hex) {  
        return new String(toByte(hex));  
    }
  
    private static byte[] getRawKey(byte[] seed) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");  
        sr.setSeed(seed);  
        kgen.init(128, sr); // 192 and 256 bits may not be available  
        SecretKey skey = kgen.generateKey();  
        byte[] raw = skey.getEncoded();  
        return raw;  
    }  
  
    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {  
        if (raw == null || clear == null){  
            return null;  
        }  
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);  
        byte[] encrypted = cipher.doFinal(clear);  
        return encrypted;  
    }  
  
    private static byte[] decrypt(byte[] raw, byte[] encrypted)  
            throws Exception {  
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);  
        byte[] decrypted = cipher.doFinal(encrypted);  
        return decrypted;  
    }  
  
    private static byte[] toByte(String hexString) {  
        if (hexString == null || "".equals(hexString)){  
            return null;  
        }  
        int len = hexString.length() / 2;  
        byte[] result = new byte[len];  
        for (int i = 0; i < len; i++) {  
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();  
        }  
        return result;  
    }  
  
    private static String toHex(byte[] buf) {  
        if (buf == null)  
            return "";  
        StringBuffer result = new StringBuffer(2 * buf.length);  
        for (int i = 0; i < buf.length; i++) {  
            appendHex(result, buf[i]);  
        }  
        return result.toString();  
    }  
  
    private final static String HEX = "0123456789ABCDEF";  
  
    private static void appendHex(StringBuffer sb, byte b) {  
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));  
    }  

}  