package com.gmr.netty.chapter02.aio.server;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 14:45
 */
public class TimeServer {
	public static void main(String[] args) {
		int port = 8080;
		if (null != args && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException ignored) {
			}
		}
		AsyncTimeServerHandler timeServerHandler = new AsyncTimeServerHandler(port);
		new Thread(timeServerHandler, "AIO-AsyncTimeServerHandler-001").start();
	}
}
