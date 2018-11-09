package com.gmr.netty.chapter10.httpxml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import sun.rmi.log.LogInputStream;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/9 10:20
 */
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {

	private IBindingFactory factory = null;

	private StringWriter writer = null;

	private final static String CHARSET_NAME = "UTF-8";

	private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

	protected ByteBuf encode(ChannelHandlerContext ctx, Object body) throws JiBXException, IOException {
		factory = BindingDirectory.getFactory(body.getClass());
		writer = new StringWriter();
		IMarshallingContext mctx = factory.createMarshallingContext();
		mctx.setIndent(2);
		mctx.marshalDocument(body, CHARSET_NAME, null, writer);
		String xmlStr = writer.toString();
		writer.close();
		writer = null;
		return Unpooled.copiedBuffer(xmlStr, UTF_8);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (writer != null) {
			writer.close();
			writer = null;
		}
	}
}
