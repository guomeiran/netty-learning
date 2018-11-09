package com.gmr.netty.chapter05.delimiter.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 9:20
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

	private int counter = 0;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String  body = (String)msg;
		System.out.println("This is " + ++counter + " times receive client : [" + body + "]");
		body += "$_";
		ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
		ctx.writeAndFlush(resp);
	}
}
