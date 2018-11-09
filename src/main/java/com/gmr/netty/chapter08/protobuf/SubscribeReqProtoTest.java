package com.gmr.netty.chapter08.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件说明
 *
 * @author hzguomeiran
 * @since 2018/11/7 13:50
 */
public class SubscribeReqProtoTest {

	private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
		return req.toByteArray();
	}

	private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}

	private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(1);
		builder.setUserName("Guo Meiran");
		builder.setProductName("Netty Book");
		List<String> address = new ArrayList<>();
		address.add("Nanjing YuHuaTai");
		address.add("BeiJing LiuLiChang");
		address.add("ShenZhen HongShuLin");
		builder.addAllAddress(address);
		return builder.build();
	}

	public static void main(String[] args) throws InvalidProtocolBufferException {
		SubscribeReqProto.SubscribeReq subscribeReq = createSubscribeReq();
		System.out.println("Before encode : " + subscribeReq.toString());
		SubscribeReqProto.SubscribeReq req = decode(encode(subscribeReq));
		System.out.println("After decode : " + req.toString());
		System.out.println("Assert equal : --> " + req.equals(subscribeReq));
	}
}
