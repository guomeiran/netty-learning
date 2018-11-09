package com.gmr.netty.chapter07.client;

import com.gmr.netty.chapter07.user.User;
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

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.warning("Unexpected exception from downstream : " + cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		User[] users = createUser();
		for (User user : users) {
			ctx.writeAndFlush(user);
		}
//		ctx.flush();
	}

	private User[] createUser() {
		User[] users = new User[1000];
		for (int i = 0; i < 1000; i ++) {
			User user = new User();
			user.setUserId(i);
			user.setUserName("abcdefg --->" + i);
			user.setAge(i);
			users[i] = user;
		}
		return users;
	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Client receive the msgpack message : " + msg);
//		ctx.write(msg);
	}

//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.flush();
//	}
}
