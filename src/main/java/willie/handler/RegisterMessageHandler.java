package willie.handler;

import willie.Enum.Status;

public class RegisterMessageHandler{
	public static void handleRegisterMessage(String decrypted, ConnectionMessageHandler connectionMessageHandler){
		if(decrypted.equals("Account created successfully.")){
			System.out.println("Account created successfully.");
			connectionMessageHandler.status = Status.LOGINED;
			connectionMessageHandler.runStartMenuThread();
		}else{
			System.err.println(decrypted);
			connectionMessageHandler.runRegisterThread();
		}
	}
}
