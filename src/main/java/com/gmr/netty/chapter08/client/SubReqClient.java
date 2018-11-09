package com.gmr.netty.chapter08.client;

import com.gmr.netty.chapter07.msgpack.MsgpackDecoder;
import com.gmr.netty.chapter07.msgpack.MsgpackEncoder;
import com.gmr.netty.chapter08.protobuf.SubscribeReqProto;
import com.gmr.netty.chapter08.protobuf.SubscribeRespProto;
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
							socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
							socketChannel.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
							socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
							socketChannel.pipeline().addLast(new ProtobufEncoder());
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
