package com.gmr.netty.chapter10.httpfile;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/7 17:46
 */
public class HttpFileServer {

	private static final String DEFAULT_URL = "/src/main/java/com/gmr/netty";

	public void bind(int port, final String url) throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup,workerGroup)
					.channel(NioServerSocketChannel.class)
//					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
							socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
							socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
							socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
							socketChannel.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
						}
					});

			ChannelFuture future = bootstrap.bind(port).sync();

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
		new HttpFileServer().bind(port, DEFAULT_URL);
	}
}
