package willie.thread;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import willie.handler.ConnectionMessageHandler;
import willie.util.DebugOutput;
import willie.util.MessageDecoder;
import willie.util.MessageEncoder;

public class ConnectionThead extends Thread{
	String host;
	int port;
	Boolean stopped = false;
	public ConnectionThead(String host, int port){
		this.host = host;
		this.port = port;
	}
	@Override
	public void run(){
		try{
			connect();
			sleep(3000);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	public ConnectionMessageHandler connectionMessageHandler = new ConnectionMessageHandler();
	public void connect() throws InterruptedException{
		Bootstrap b = new Bootstrap();
		b.group(workerGroup);
		b.channel(NioSocketChannel.class);
		b.option(ChannelOption.SO_KEEPALIVE, true);
		b.handler(new ChannelInitializer<SocketChannel>(){
			@Override
			public void initChannel(SocketChannel ch){
				ch.pipeline().addLast(new MessageEncoder(), new MessageDecoder(), connectionMessageHandler);
			}
		});
		try{
			ChannelFuture f = b.connect(host, port).sync();
			System.out.println("Connected to the server.");
//			f.channel().closeFuture().sync();
			f.channel().closeFuture().addListener(future -> {
				if(stopped){
					return;
				}
				connectionMessageHandler.menuThread.interrupt();
				DebugOutput.printError("Connection lost, retrying in 3 seconds.");
				sleep(3000);
				connect();
			});
		}catch(Exception e){
			DebugOutput.printError("Unable to connect to the server, retrying in 3 seconds.");
			sleep(3000);
			connect();
		}
	}
	public void stopClient(){
		stopped = true;
		workerGroup.shutdownGracefully();
	}
}
