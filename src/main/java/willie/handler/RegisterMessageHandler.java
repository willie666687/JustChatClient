package willie.handler;

import willie.util.DebugOutput;

public class RegisterMessageHandler{
	public static void handleRegisterMessage(String decrypted, ConnectionMessageHandler connectionMessageHandler){
		DebugOutput.printResponse(decrypted);
		connectionMessageHandler.menuThread.responseReceived = true;
	}
}
