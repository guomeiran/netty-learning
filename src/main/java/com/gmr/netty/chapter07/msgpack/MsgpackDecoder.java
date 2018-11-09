package com.gmr.netty.chapter07.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 20:06
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		final byte[] array;
		final int length = byteBuf.readableBytes();
		array = new byte[length];
		byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
		MessagePack msgpack = new MessagePack();
		list.add(msgpack.read(array));
	}
}
