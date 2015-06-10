package com.palmyou.v7data.ms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class UTest extends UnitTestBase {
	
	public UTest(String springXmlPath) {
		super("classpath*:spring-ioc.xml");
	}

	@Test
	public void testSay(){
		System.out.println("junit");
	}
}
