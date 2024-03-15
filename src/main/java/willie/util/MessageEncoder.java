package willie.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import willie.impl.ConnectionMessage;

import java.util.Arrays;

public class MessageEncoder extends MessageToByteEncoder<ConnectionMessage>{
	@Override
	protected void encode(ChannelHandlerContext ctx, ConnectionMessage msg, ByteBuf out){
		out.writeInt(msg.type.toString().getBytes().length);
		out.writeBytes(msg.type.toString().getBytes());
		out.writeInt(msg.message.getBytes().length);
		out.writeBytes(msg.message.getBytes());
		System.out.println("msg.type.toString().getBytes().length: " + msg.type.toString().getBytes().length);
		System.out.println("Encoded message: " + Arrays.toString(msg.type.toString().getBytes()) + " " + Arrays.toString(msg.message.getBytes()));
	}
}
