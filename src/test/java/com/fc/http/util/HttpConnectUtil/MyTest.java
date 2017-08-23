package com.fc.http.util.HttpConnectUtil;

import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.fc.http.HttpConnectFacory;
import com.fc.http.HttpConnectFacory.HttpConnect;
import com.fc.http.HttpMethordEnum;

public class MyTest {

	CountDownLatch latch=new CountDownLatch(1);
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void testCheck(){
		HttpConnect connect;
		try {
			connect = HttpConnectFacory.getHttpConnect("http","192.168.0.22","8080","twbs/a/sys/user/list");
			connect.setHeader("Cookie","jeesite.session.id=c5e6909ff5f841c4bffd3fdbecf772b9");
			connect.openConnect(HttpMethordEnum.POST);
			connect.sendRequest("");
			connect.read();
			connect.closeConneect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		latch.countDown();
	}
}
