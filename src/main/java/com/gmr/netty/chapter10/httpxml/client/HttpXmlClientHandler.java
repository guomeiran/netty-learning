package com.gmr.netty.chapter10.httpxml.client;

import com.gmr.netty.chapter08.protobuf.SubscribeReqProto;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlRequest;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlResponse;
import com.gmr.netty.chapter10.httpxml.pojo.OrderFactory;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 9:45
 */
public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

	private static final Logger LOGGER = Logger.getLogger(HttpXmlClientHandler.class.getName());

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.warning("Unexpected exception from downstream : " + cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.create(123));
		ctx.writeAndFlush(request);
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
		System.out.println("Client receive response of http header is : " + msg.getResponse().headers().names());
		System.out.println("Client receive response of http body is : " + msg.getResult());
	}
}
