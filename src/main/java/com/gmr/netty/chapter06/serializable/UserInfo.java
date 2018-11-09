package com.gmr.netty.chapter06.serializable;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 19:33
 */
public class UserInfo implements Serializable {

	private static final long serialVersionUID = -4023218954684242681L;

	private String userName;

	private Integer userId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public byte[] codeC() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] value = this.userName.getBytes();
		buffer.putInt(value.length);
		buffer.put(value);
		buffer.putInt(this.getUserId());
		buffer.flip();
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}
}
