package com.gmr.netty.chapter05.fixedlength.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 9:45
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

	private static final Logger LOGGER = Logger.getLogger(EchoClientHandler.class.getName());

	private int counter;

	private static final String ECHO_REQ = "Hi, Lilinfeng. Welcome to Netty.$_";

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.warning("Unexpected exception from downstream : " + cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("This is " + ++counter + " times receive server : ["+ msg + "]");
	}
}
