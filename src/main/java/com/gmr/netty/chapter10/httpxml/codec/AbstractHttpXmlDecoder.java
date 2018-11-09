package com.gmr.netty.chapter10.httpxml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.jibx.runtime.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/9 11:09
 */
public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {

	private IBindingFactory factory;

	private StringReader reader;

	private Class<?> clazz;

	private boolean isPrint;

	private final static String CHARSET_NAME = "UTF-8";

	private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

	protected AbstractHttpXmlDecoder(Class<?> clazz) {
		this(clazz, false);
	}

	protected AbstractHttpXmlDecoder(Class<?> clazz, boolean isPrint) {
		this.clazz = clazz;
		this.isPrint = isPrint;
	}

	protected Object decode(ChannelHandlerContext ctx, ByteBuf msg) throws JiBXException, IOException {
		String body = msg.toString(UTF_8);
		if (isPrint)
			System.out.println("The body is : " + body);
		factory = BindingDirectory.getFactory(clazz);
		reader = new StringReader(body);
		IUnmarshallingContext uctx = factory.createUnmarshallingContext();
		Object result = uctx.unmarshalDocument(reader);
		reader.close();
		reader = null;
		return result;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (reader != null) {
			reader.close();
			reader = null;
		}
	}
}
