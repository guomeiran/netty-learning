package com.gmr.netty.chapter02.aio.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 15:20
 */
public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {

	private AsynchronousSocketChannel channel;
	private String host;
	private int port;
	private CountDownLatch latch;

	public AsyncTimeClientHandler(String host, int port) {
		this.host = host;
		this.port = port;
		try {
			channel = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		latch = new CountDownLatch(1);
		channel.connect(new InetSocketAddress(host, port), this, this);
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void completed(Void result, AsyncTimeClientHandler attachment) {
		byte[] req = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuff = ByteBuffer.allocate(req.length);
		writeBuff.put(req);
		writeBuff.flip();
		channel.write(writeBuff, writeBuff, new CompletionHandler<Integer, ByteBuffer>() {
			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				if (attachment.hasRemaining()) {
					channel.write(attachment, attachment, this);
				} else {
					ByteBuffer readBuff = ByteBuffer.allocate(1024);
					channel.read(readBuff, readBuff, new CompletionHandler<Integer, ByteBuffer>() {
						@Override
						public void completed(Integer result, ByteBuffer attachment) {
							attachment.flip();
							byte[] bytes = new byte[attachment.remaining()];
							attachment.get(bytes);
							String body;
							try {
								body = new String(bytes, "UTF-8");
								System.out.println("Now is : " + body);
								latch.countDown();
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void failed(Throwable exc, ByteBuffer attachment) {
							try {
								channel.close();
								latch.countDown();
							} catch (IOException ignored) {
							}
						}
					});
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				try {
					channel.close();
					latch.countDown();
				} catch (IOException ignored) {
				}
			}
		});
	}

	@Override
	public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
		exc.printStackTrace();
		try {
			channel.close();
			latch.countDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
