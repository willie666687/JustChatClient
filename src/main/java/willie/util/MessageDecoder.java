package willie.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import willie.Enum.ConnectionMessageType;
import willie.impl.ConnectionMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder{
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
		System.out.println(in.readableBytes());
		int bytes = in.readInt();
//		out.add(in.readBytes(bytes).toString(StandardCharsets.UTF_8));
		String type = in.readBytes(bytes).toString(StandardCharsets.UTF_8);
		bytes = in.readInt();
		System.out.println("in.readInt(): " + bytes);
//		out.add(in.readBytes(bytes).toString(StandardCharsets.UTF_8));
		String message = in.readBytes(bytes).toString(StandardCharsets.UTF_8);
		out.add(new ConnectionMessage(ConnectionMessageType.valueOf(type), message));
	}
}
