package willie.thread;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import willie.handler.ClientMessageHandler;
import willie.util.MessageEncoder;
import willie.util.MessageDecoder;

public class ConnectionThead extends Thread{
	String host;
	int port;
	public ConnectionThead(String host, int port){
		this.host = host;
		this.port = port;
	}
	@Override
	public void run(){
		while(true){
			try{
				connect();
				sleep(3000);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	ClientMessageHandler clientHandler = new ClientMessageHandler();
	public void connect(){
		Bootstrap b = new Bootstrap();
		b.group(workerGroup);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.handler(new ChannelInitializer<SocketChannel>(){
			@Override
			public void initChannel(SocketChannel ch){
				ch.pipeline().addLast(new MessageEncoder(), new MessageDecoder(), clientHandler);
			}
		});
		try{
			ChannelFuture f = b.connect(host, port).sync();
			System.out.println("Connected to the server.");
			System.out.println("send hello server.");
			clientHandler.sendMessage("Hello, server!");
			f.channel().closeFuture().sync();
		}catch(Exception e){
			System.err.println("Unable to connect to the server, retrying in 3 seconds.");
		}
	}
	public void stopClient(){
		workerGroup.shutdownGracefully();
	}
}
