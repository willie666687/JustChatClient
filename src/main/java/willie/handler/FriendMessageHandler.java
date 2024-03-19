package willie.handler;

import willie.util.DebugOutput;
import willie.util.InputUtils;

import java.util.Set;

public class FriendMessageHandler{
	public static void handleAddFriendMessage(String decrypted, ConnectionMessageHandler connectionMessageHandler){
		DebugOutput.printResponse(decrypted);
		connectionMessageHandler.menuThread.responseReceived = true;
	}
	public static void handleFriendListMessage(String[] decrypted, ConnectionMessageHandler connectionMessageHandler){
		if(decrypted.length == 0){
			System.out.println("You have no friends.");
		}else{
			for(String friend : decrypted){
				System.out.println(friend);
			}
		}
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
}
