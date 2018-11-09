package com.gmr.netty.chapter10.httpxml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.setContentLength;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/9 14:00
 */
public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List<Object> out) throws Exception {
		ByteBuf body = encode(ctx, msg.getResult());
		FullHttpResponse response = msg.getResponse();
		if (response == null) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
		} else {
			response = new DefaultFullHttpResponse(msg.getResponse().getProtocolVersion(), msg.getResponse().getStatus(), body);
		}
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/xml");
		setContentLength(response, body.readableBytes());
		out.add(response);
	}
}
