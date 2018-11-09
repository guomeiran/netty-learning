package com.gmr.netty.chapter04.line.client;

import io.netty.buffer.ByteBuf;
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
public class TimeClientHandler extends ChannelHandlerAdapter {

	private static final Logger LOGGER = Logger.getLogger(TimeClientHandler.class.getName());

	private int counter;

	private final byte[] req;

	public TimeClientHandler() {
		req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.warning("Unexpected exception from downstream : " + cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf msg = null;
		for (int i = 0; i < 100; i++) {
			msg = Unpooled.buffer(req.length);
			msg.writeBytes(req);
			ctx.writeAndFlush(msg);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String)msg;
		System.out.println("Now is : " + body + " ; the counter is : " + ++counter);
	}

}
