package com.gmr.netty.chapter09.marshaller;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/7 15:21
 */
public class MarshallingCodeCFactory {

	public static MarshallingDecoder buildMarshallingDecoder() {
		MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
		MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		DefaultUnmarshallerProvider unmarshallerProvider = new DefaultUnmarshallerProvider(factory, configuration);
		return new MarshallingDecoder(unmarshallerProvider);
	}

	public static MarshallingEncoder buildMarshallingEncoder() {
		MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
		MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		MarshallerProvider marshallerProvider = new DefaultMarshallerProvider(factory, configuration);
		return new MarshallingEncoder(marshallerProvider);
	}
}
