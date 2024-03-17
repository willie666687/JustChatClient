package willie.handler;

import willie.Enum.Status;

public class LoginMessageHandler{
	public static void handleLoginMessage(String decrypted, ConnectionMessageHandler connectionMessageHandler){
		if(decrypted.equals("Login successful.")){
			System.out.println("Login successful.");
			connectionMessageHandler.status = Status.LOGINED;
		}else{
			System.err.println(decrypted);
			connectionMessageHandler.runLoginThread();
		}
	}
}
