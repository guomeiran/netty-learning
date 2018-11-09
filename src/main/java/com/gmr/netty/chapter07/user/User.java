package com.gmr.netty.chapter07.user;

import org.msgpack.annotation.Message;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 20:27
 */
@Message
public class User {

	private Integer userId;

	private Integer age;

	private String userName;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
