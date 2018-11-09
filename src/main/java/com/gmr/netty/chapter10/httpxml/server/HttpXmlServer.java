package com.gmr.netty.chapter10.httpxml.server;

import com.gmr.netty.chapter09.marshaller.MarshallingCodeCFactory;
import com.gmr.netty.chapter10.httpxml.client.HttpXmlClientHandler;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlRequestDecoder;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlRequestEncoder;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlResponseDecoder;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlResponseEncoder;
import com.gmr.netty.chapter10.httpxml.pojo.Order;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/5 19:51
 */
public class HttpXmlServer {

	private void bind(int port) throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup,workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
							socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
							socketChannel.pipeline().addLast("xml-decoder", new HttpXmlRequestDecoder(Order.class, true));
							socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
							socketChannel.pipeline().addLast("xml-encoder", new HttpXmlResponseEncoder());
							socketChannel.pipeline().addLast(new HttpXmlServerHandler());
						}
					});

			ChannelFuture future = bootstrap.bind(port).sync();
			System.out.println("HTTP订购服务器启动，网址是 : " + "http://localhost:" + port);

			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
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
		new HttpXmlServer().bind(port);
	}
}
