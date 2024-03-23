package willie.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import willie.Enum.ConnectionMessageType;
import willie.Enum.Status;
import willie.impl.ConnectionMessage;
import willie.thread.MenuThread;
import willie.util.DebugOutput;
import willie.util.KeyUtils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ChannelHandler.Sharable
public class ConnectionMessageHandler extends ChannelInboundHandlerAdapter{
	ChannelHandlerContext serverCTX;
	PublicKey serverPublicKey;
	public Status status;
	public MenuThread menuThread;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if(!(msg instanceof ConnectionMessage message)){
			return;
		}
		DebugOutput.printArray(2, "Received: ", message.messages);
		switch(message.type){
			case KEYEXCHANGE -> {
				byte[] publicBytes = Base64.getDecoder().decode(message.messages[0]);
				X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
				try{
					KeyFactory keyFactory = KeyFactory.getInstance("RSA");
					serverPublicKey = keyFactory.generatePublic(keySpec);
					status = Status.KEYEXCHANGED;
				}catch(NoSuchAlgorithmException | InvalidKeySpecException e){
					DebugOutput.printError("Error while setting public key: " + e.getMessage());
				}
				sendEncryptedMessage(ConnectionMessageType.DEBUGENCRYPTED, "Hello, this is a test message.", "This is another test message.");
				runMenuThread();
			}
			case DEBUGENCRYPTED -> {
				DebugOutput.printArray(2, "Received encrypted message: ", message.messages);
				DebugOutput.printArray("Decrypted message: ", decryptMessages(message.messages));
			}
			case REGISTER -> {
				String decrypted =  decryptMessages(message.messages)[0];
				RegisterMessageHandler.handleRegisterMessage(decrypted, this);
			}
			case LOGIN -> {
				String decrypted =  decryptMessages(message.messages)[0];
				LoginMessageHandler.handleLoginMessage(decrypted, this);
			}
			case ADDFRIEND -> {
				String decrypted =  decryptMessages(message.messages)[0];
				FriendMessageHandler.handleAddFriendMessage(decrypted, this);
			}
			case FRIENDLIST -> {
				String[] decrypted =  decryptMessages(message.messages);
				FriendMessageHandler.handleFriendListMessage(decrypted, this);
			}
			case FRIENDREQUEST -> {
				String[] decrypted =  decryptMessages(message.messages);
				FriendMessageHandler.handleFriendRequestMessage(decrypted, this);
			}
			case LOGOUT -> {
				status = Status.KEYEXCHANGED;
				MenuThread.menuStatus = null;
				menuThread.responseReceived = true;
			}
			case ACCEPTFRIEND -> {
				String decrypted =  decryptMessages(message.messages)[0];
				FriendMessageHandler.handleAcceptFriendMessage(decrypted, this);
			}
			case CHATWITHFRIEND -> {
				String friend =  decryptMessages(message.messages)[0];
				String chatMessage =  decryptMessages(message.messages)[1];
				FriendMessageHandler.handleChatWithFriendMessage(friend, chatMessage, this);
			}
			case CHATWITHFRIENDDEBUG -> {
				String decrypted =  decryptMessages(message.messages)[0];
				FriendMessageHandler.handleChatWithFriendDebugMessage(decrypted, this);
			}
			case FRIENDCHATHISTORY -> {
				String[] decrypted =  decryptMessages(message.messages);
				FriendMessageHandler.handleFriendChatHistoryMessage(decrypted, this);
			}
		}
	}
	public void runMenuThread(){
		menuThread = new MenuThread(this);
		menuThread.start();
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		this.serverCTX = ctx;
		status = Status.CONNECTED;
		MenuThread.menuStatus = null;
		sendMessage(ConnectionMessageType.KEYEXCHANGE, Base64.getEncoder().encodeToString(KeyUtils.publicKey.getEncoded()));
	}
	public void sendMessage(ConnectionMessageType type, String... messages){
		try{
			serverCTX.writeAndFlush(new ConnectionMessage(type, messages));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void sendEncryptedMessage(ConnectionMessageType type, String... messages){
		try{
			String[] encryptedMessages = new String[messages.length];
			for(int i = 0; i < messages.length; i++){
				encryptedMessages[i] = KeyUtils.encrypt(serverPublicKey, messages[i]);
			}
			serverCTX.writeAndFlush(new ConnectionMessage(type, encryptedMessages));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public String[] decryptMessages(String[] messages){
		String[] decryptedMessages = new String[messages.length];
		for(int i = 0; i < messages.length; i++){
			try{
				decryptedMessages[i] = KeyUtils.decrypt(messages[i]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return decryptedMessages;
	}
}
