package com.gmr.netty.chapter06.serializable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 19:39
 */
public class TestUserInfo {

	public static void main(String[] args) throws IOException {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(100);
		userInfo.setUserName("welcome to netty");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(userInfo);
		oos.flush();
		oos.close();
		byte[] b = bos.toByteArray();
		System.out.println("The jdk serializable length is : " + b.length);
		System.out.println("The jdk serializable content is : " + Arrays.toString(b));
		bos.close();
		System.out.println("The byte array serializable length is : " + userInfo.codeC().length);
		System.out.println("The byte array serializable content is : " + Arrays.toString(userInfo.codeC()));
	}
}
