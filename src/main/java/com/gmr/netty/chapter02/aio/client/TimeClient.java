package com.gmr.netty.chapter02.aio.client;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 15:19
 */
public class TimeClient {
	public static void main(String[] args) {
		int port = 8080;
		if (null != args && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception ignored) {

			}
		}
		new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-AsyncTimeClientHandler-001").start();
	}
}
