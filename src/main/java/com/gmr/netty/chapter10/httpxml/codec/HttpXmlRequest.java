package com.gmr.netty.chapter10.httpxml.codec;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/9 10:48
 */
public class HttpXmlRequest {

	private FullHttpRequest request;

	private Object body;

	public HttpXmlRequest(FullHttpRequest request, Object body) {
		this.request = request;
		this.body = body;
	}

	public FullHttpRequest getRequest() {
		return request;
	}

	public void setRequest(FullHttpRequest request) {
		this.request = request;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}
