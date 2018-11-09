package com.gmr.netty.chapter09.client;

import com.gmr.netty.chapter08.protobuf.SubscribeRespProto;
import com.gmr.netty.chapter09.marshaller.MarshallingCodeCFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 9:40
 */
public class SubReqClient {

	private void connect(int port, String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
							socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
							socketChannel.pipeline().addLast(new SubReqClientHandler());
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
		new SubReqClient().connect(port, "127.0.0.1");
	}
}
