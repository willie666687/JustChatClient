package willie.handler;

import willie.Enum.Status;
import willie.util.DebugOutput;

public class LoginMessageHandler{
	public static void handleLoginMessage(String decrypted, ConnectionMessageHandler connectionMessageHandler){
		DebugOutput.printResponse(decrypted);
		if(decrypted.equals("Login successful.")){
			connectionMessageHandler.status = Status.LOGINED;
		}
		connectionMessageHandler.menuThread.responseReceived = true;
	}
}
