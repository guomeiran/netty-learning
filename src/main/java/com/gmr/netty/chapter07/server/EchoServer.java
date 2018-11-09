package com.gmr.netty.chapter07.server;

import com.gmr.netty.chapter07.msgpack.MsgpackDecoder;
import com.gmr.netty.chapter07.msgpack.MsgpackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/5 19:51
 */
public class EchoServer {

	private void bind(int port) throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup,workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChildChannelHandler());

			ChannelFuture future = bootstrap.bind(port).sync();

			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel socketChannel) throws Exception {
//			ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
//			socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
//			socketChannel.pipeline().addLast(new StringDecoder());
			socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
			socketChannel.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
			socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
			socketChannel.pipeline().addLast("msgpack encoder",new MsgpackEncoder());
			socketChannel.pipeline().addLast(new EchoServerHandler());
		}
	}

	public static void main(String[] args) throws Exception {

		int port = 8080;
		if (null != args && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException ignored) {
			}
		}
		new EchoServer().bind(port);
	}
}
