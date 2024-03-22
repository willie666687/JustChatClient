package willie.handler;

import willie.Enum.MenuStatus;
import willie.thread.MenuThread;
import willie.util.DebugOutput;

import java.util.Set;

public class FriendMessageHandler{
	public static void handleAddFriendMessage(String decrypted, ConnectionMessageHandler connectionMessageHandler){
		DebugOutput.printResponse(decrypted);
		connectionMessageHandler.menuThread.responseReceived = true;
	}
	public static void handleFriendListMessage(String[] decrypted, ConnectionMessageHandler connectionMessageHandler){
		connectionMessageHandler.menuThread.friends = Set.of(decrypted);
		connectionMessageHandler.menuThread.responseReceived = true;
	}
	public static void handleFriendRequestMessage(String[] decrypted, ConnectionMessageHandler connectionMessageHandler){
		connectionMessageHandler.menuThread.friendRequests = Set.of(decrypted);
		connectionMessageHandler.menuThread.responseReceived = true;
	}
	public static void handleAcceptFriendMessage(String decrypted, ConnectionMessageHandler connectionMessageHandler){
		DebugOutput.printResponse(decrypted);
		connectionMessageHandler.menuThread.responseReceived = true;
	}
	public static void handleChatWithFriendMessage(String user, String message, ConnectionMessageHandler connectionMessageHandler){
		if(MenuThread.menuStatus == MenuStatus.CHATWITHFRIEND){
			System.out.println(user + ": " + message);
		}
		connectionMessageHandler.menuThread.responseReceived = true;
	}
	public static void handleChatWithFriendDebugMessage(String decrypted, ConnectionMessageHandler connectionMessageHandler){
		DebugOutput.printResponse(decrypted);
		connectionMessageHandler.menuThread.responseReceived = true;
	}
	public static void handleFriendChatHistoryMessage(String[] decrypted, ConnectionMessageHandler connectionMessageHandler){
		if(decrypted.length != 0){
			connectionMessageHandler.menuThread.chatHistory = decrypted;
		}
		connectionMessageHandler.menuThread.responseReceived = true;
	}
}
