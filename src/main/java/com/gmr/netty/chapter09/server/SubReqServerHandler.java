package com.gmr.netty.chapter09.server;

import com.gmr.netty.chapter08.protobuf.SubscribeReqProto;
import com.gmr.netty.chapter08.protobuf.SubscribeRespProto;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 9:20
 */
@Sharable
public class SubReqServerHandler extends ChannelHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
		if ("Guo Meiran".equalsIgnoreCase(req.getUserName())) {
			System.out.println("Service accept client subscribe req : [" + req.toString() + "]");
			ctx.writeAndFlush(resp(req.getSubReqID()));
		}
	}

	private SubscribeRespProto.SubscribeResp resp(int subReqID) {
		SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
		builder.setSubRespID(subReqID);
		builder.setRespCode("0");
		builder.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
		return builder.build();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
}
