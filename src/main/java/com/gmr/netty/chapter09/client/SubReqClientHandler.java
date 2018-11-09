package com.gmr.netty.chapter09.client;

import com.gmr.netty.chapter08.protobuf.SubscribeReqProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 9:45
 */
public class SubReqClientHandler extends ChannelHandlerAdapter {

	private static final Logger LOGGER = Logger.getLogger(SubReqClientHandler.class.getName());

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.warning("Unexpected exception from downstream : " + cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}

	private SubscribeReqProto.SubscribeReq subReq(int i) {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(i);
		builder.setUserName("Guo Meiran");
		builder.setProductName("Netty Book");
		List<String> address = new ArrayList<>();
		address.add("Nanjing YuHuaTai");
		address.add("BeiJing LiuLiChang");
		address.add("ShenZhen HongShuLin");
		builder.addAllAddress(address);
		return builder.build();
	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		SubscribeRespProto.SubscribeResp resp = (SubscribeRespProto.SubscribeResp) msg;
		System.out.println("Client receive the response : [" + msg + "]");
//		ctx.write(msg);
	}

//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.flush();
//	}
}
