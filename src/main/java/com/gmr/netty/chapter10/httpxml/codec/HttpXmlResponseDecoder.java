package com.gmr.netty.chapter10.httpxml.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/9 14:10
 */
public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse> {

	public HttpXmlResponseDecoder(Class<?> clazz) {
		this(clazz, false);
	}

	public HttpXmlResponseDecoder(Class<?> clazz, boolean isPrint) {
		super(clazz, isPrint);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws Exception {
		HttpXmlResponse httpXmlResponse = new HttpXmlResponse(msg, decode(ctx, msg.content()));
		out.add(httpXmlResponse);
	}
}
