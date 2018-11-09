package com.gmr.netty.chapter07.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 20:03
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
		MessagePack msgpack = new MessagePack();
		byte[] raw = msgpack.write(o);
		byteBuf.writeBytes(raw);
	}
}
