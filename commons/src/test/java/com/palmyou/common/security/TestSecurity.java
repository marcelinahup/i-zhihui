package com.palmyou.common.security;

import org.junit.Test;

import com.hk.common.security.EncryptAES;
import com.hk.common.security.EncryptDES;
import com.hk.common.security.EncryptMD5;
import com.hk.common.security.IrreversibleEncrypt;

public class TestSecurity {
	
	@Test
	public void testCrypto() throws Exception{
		System.out.println(EncryptAES.encrypt("123456789012345678901234567890", "KEY"));
		System.out.println(EncryptAES.decrypt(EncryptAES.encrypt("123456789012345678901234567890", "KEY"), "KEY"));
		
		
//		CryptoTools tools = new CryptoTools();
//		System.out.println(tools.decode("1234567812345678"));
//		System.out.println(tools.encode(tools.decode("123")));
		String strIn = "1baca63924ae3c39";
		String strIn1 = "223cf02767630b3dc875e80b34d2d5a0";
		System.out.println(EncryptDES.decrypt(strIn));
		System.out.println(EncryptDES.decrypt(strIn1));
	}
	
	@Test
	public void testAes(){
		String strIn = "123";
		System.out.println(EncryptDES.encrypt(strIn));
		System.out.println(EncryptDES.decrypt(EncryptDES.encrypt(strIn)));
	}

	@Test
	public void testMd5(){
		System.out.println(EncryptMD5.encrypt("1"));
	}
	
	@Test
	public void testNe(){
		String src = "123";
		System.out.println(IrreversibleEncrypt.md5(src));
		System.out.println(EncryptMD5.encrypt(src));
	}

}
