package willie.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import willie.Enum.ConnectionMessageType;
import willie.impl.ConnectionMessage;

@ChannelHandler.Sharable
public class ClientMessageHandler extends ChannelInboundHandlerAdapter{
	ChannelHandlerContext ctx;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(!(msg instanceof ConnectionMessage message)){
			return;
		}
		System.out.println("Received type: " +  message.type+ ", message: " + message.message);
//		ctx.close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	public void sendMessage(String message){
		try{
			ctx.writeAndFlush(new ConnectionMessage(ConnectionMessageType.LOGIN, message));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
//	@Override
//	public void channelActive(final ChannelHandlerContext ctx) {
//		ctx.writeAndFlush("TimeChannel");
//	}
}
