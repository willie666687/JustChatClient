package willie.thread;

import willie.Enum.ConnectionMessageType;
import willie.handler.ConnectionMessageHandler;
import willie.util.InputUtils;

public class LoginThread extends Thread{
	ConnectionMessageHandler connectionMessageHandler;
	public LoginThread(ConnectionMessageHandler connectionMessageHandler){
		this.connectionMessageHandler = connectionMessageHandler;
	}
	@Override
	public void run(){
		System.out.println("Please enter your username and password to login.");
		System.out.println("Input \"\\\" to cancel.");
		System.out.print("Enter Username: ");
		String account = InputUtils.getInput();
		if(account.equals("\\")){
			connectionMessageHandler.runStartMenuThread();
			return;
		}
		System.out.print("Enter password: ");
		String password = InputUtils.getInput();
		if(password.equals("\\")){
			connectionMessageHandler.runStartMenuThread();
			return;
		}
		connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.LOGIN, account, password);
		interrupt();
		
	}
}
