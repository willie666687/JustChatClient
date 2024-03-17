package willie.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import willie.Enum.ConnectionMessageType;
import willie.impl.ConnectionMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder{
	int totalLength = 0;
	boolean readingByteAmount = true;
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
		if(readingByteAmount && in.readableBytes() < 4){
			return;
		}
		if(totalLength == 0){
			totalLength = in.readInt();
		}
		if(in.readableBytes() < totalLength){
			return;
		}
		readingByteAmount = false;
		int readingBytes = in.readInt();
		String type = readString(in, readingBytes);
		int messageAmount = in.readInt();
		String[] messages = new String[messageAmount];
		for(int i = 0; i < messageAmount; i++){
			readingBytes = in.readInt();
			messages[i] = readString(in, readingBytes);
		}
		out.add(new ConnectionMessage(ConnectionMessageType.valueOf(type), messages));
		totalLength = 0;
	}
	private String readString(ByteBuf in, int length){
		byte[] bytes = new byte[length];
		in.readBytes(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
