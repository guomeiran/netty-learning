package com.gmr.netty.chapter07.client;

import com.gmr.netty.chapter07.msgpack.MsgpackDecoder;
import com.gmr.netty.chapter07.msgpack.MsgpackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 9:40
 */
public class EchoClient {

	private void connect(int port, String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
//							ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
//							socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
//							socketChannel.pipeline().addLast(new StringDecoder());
							socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
							socketChannel.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
							socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
							socketChannel.pipeline().addLast("msgpack encoder",new MsgpackEncoder());
							socketChannel.pipeline().addLast(new EchoClientHandler());
						}
					});
			ChannelFuture f = bootstrap.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (null != args && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception ignored) {

			}
		}
		new EchoClient().connect(port, "127.0.0.1");
	}
}
