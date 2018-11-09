package com.gmr.netty.chapter10.httpxml.client;

import com.gmr.netty.chapter09.marshaller.MarshallingCodeCFactory;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlRequestDecoder;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlRequestEncoder;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlResponseDecoder;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlResponseEncoder;
import com.gmr.netty.chapter10.httpxml.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 9:40
 */
public class HttpXmlClient {

	private void connect(int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast("http-decoder", new HttpResponseDecoder());
							socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
							socketChannel.pipeline().addLast("xml-decoder", new HttpXmlResponseDecoder(Order.class, true));
							socketChannel.pipeline().addLast("http-encoder", new HttpRequestEncoder());
							socketChannel.pipeline().addLast("xml-encoder", new HttpXmlRequestEncoder());
							socketChannel.pipeline().addLast(new HttpXmlClientHandler());
						}
					});
			ChannelFuture f = bootstrap.connect(new InetSocketAddress(port)).sync();
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
		new HttpXmlClient().connect(port);
	}
}
