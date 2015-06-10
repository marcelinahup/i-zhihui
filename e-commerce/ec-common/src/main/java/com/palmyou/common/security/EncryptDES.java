package com.palmyou.common.security;

import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * DES加密和解密工具[16->48]
 */
public class EncryptDES {

    private static String strDefaultKey = "national";

    private EncryptDES() {
    }
    
    /**
     * 使用默认key加密字符串
     * @param strIn
     * @return
     */
    public static String encrypt(String strIn) {
        return byteArr2HexStr(encrypt(strIn.getBytes(),strDefaultKey));
    }
    
    /**
     * 使用默认key解密字符串
     * 
     * @param strIn 待加密字符串
     * @return 解密后的字符串
     */
    public static String decrypt(String encrypted) {
        return new String(decrypt(hexStr2ByteArr(encrypted), strDefaultKey));
    }
    
    /**
     * 使用指定key加密字符串
     * 
     * @param strIn 明文
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encrypt(String strIn,String strKey) {
        return byteArr2HexStr(encrypt(strIn.getBytes(),strKey));
    }
    
    /**
     * 使用指定key解密字符串
     * @param strIn
     * @param strKey
     * @return
     */
    public static String decrypt(String encrypted,String strKey) {
        return new String(decrypt(hexStr2ByteArr(encrypted), strKey));
    }
    
    /**
     * 加密字节数组
     * 
     * @param arrB 要加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    private static byte[] encrypt(byte[] arrB ,String strKey) {
        byte[] bytes = null;
            
            Cipher encryptCipher = createCipher(Cipher.ENCRYPT_MODE, strKey);
            
            try {
                bytes = encryptCipher.doFinal(arrB);
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
        
        return bytes;
    }
    
    /**
     * 解密字节数组
     * 
     * @param arrB 要解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    private static byte[] decrypt(byte[] arrB,String strKey) {
        byte[] bytes = null;
        try {
            Cipher decryptCipher = createCipher(Cipher.DECRYPT_MODE, strKey);
            bytes = decryptCipher.doFinal(arrB);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 创建解密对象
     * @param mode Cipher.ENCRYPT_MODE=1 加密，Cipher.DECRYPT_MODE=2 解密
     * @param strKey
     * @return
     */
    private static Cipher createCipher(int mode,String strKey){
        
        Cipher cipher = null;
        try {
            
            Key key = getKey(strKey.getBytes());
            cipher = Cipher.getInstance("DES");
            cipher.init(mode, key);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return cipher;
    }

    /**
     * 将byte数组转换为表16进制值的字符串， 如：byte[]{8,18}转换为：0813，和public static byte[]
     * hexStr2ByteArr(String strIn) 16转换为可解密的数组
     * 
     * @param arrB 要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     */
    private static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;

        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];

            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }

            // 小于0F的数据在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组 和public static String byteArr2HexStr(byte[] arrB)数据转换为16进制字符串
     * 
     * @param strIn 要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception
     */
    private static byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示16字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 从指定字符串生成密钥
     * 
     * @param arrBTmp
     *            构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception
     */
    private static Key getKey(byte[] arrBTmp) throws Exception {

        // 创建空的8位字节数组
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

        return key;
    }
}
