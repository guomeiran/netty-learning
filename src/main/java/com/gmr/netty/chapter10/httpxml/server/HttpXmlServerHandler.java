package com.gmr.netty.chapter10.httpxml.server;

import com.gmr.netty.chapter08.protobuf.SubscribeReqProto;
import com.gmr.netty.chapter08.protobuf.SubscribeRespProto;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlRequest;
import com.gmr.netty.chapter10.httpxml.codec.HttpXmlResponse;
import com.gmr.netty.chapter10.httpxml.pojo.Address;
import com.gmr.netty.chapter10.httpxml.pojo.Order;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.ArrayList;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/6 9:20
 */
@Sharable
public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HttpXmlRequest msg) throws Exception {
		System.out.println("111111111111111111111");
		FullHttpRequest request = msg.getRequest();
		Order order = (Order)msg.getBody();
		System.out.println("Http Server receive request : " + order);
		dobusiness(order);
		ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponse(null, order));
		if (!HttpHeaders.isKeepAlive(request)) {
			future.addListener(new GenericFutureListener<Future<? super Void>>() {
				@Override
				public void operationComplete(Future<? super Void> future) throws Exception {
					ctx.close();
				}
			});
		}
	}

	private void dobusiness(Order order) {
		order.getCustomer().setFirstName("狄");
		order.getCustomer().setLastName("仁杰");
		List<String> midNames = new ArrayList<String>();
		midNames.add("李元芳");
		order.getCustomer().setMiddleNames(midNames);
		Address address = order.getBillTo();
		address.setCity("洛阳");
		address.setCountry("大唐");
		address.setState("河南道");
		address.setPostCode("123456");
		order.setBillTo(address);
		order.setShipTo(address);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if (ctx.channel().isActive()) {
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer("失败: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
}
