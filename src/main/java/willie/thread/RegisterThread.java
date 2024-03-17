package willie.thread;

import willie.Enum.ConnectionMessageType;
import willie.handler.ConnectionMessageHandler;
import willie.util.InputUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RegisterThread extends Thread{
	ConnectionMessageHandler connectionMessageHandler;
	public RegisterThread(ConnectionMessageHandler connectionMessageHandler){
		this.connectionMessageHandler = connectionMessageHandler;
	}
	@Override
	public void run(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter your username and password to register.");
		System.out.println("Input \"\\\" to cancel.");
		System.out.print("Enter Username: ");
		String username = InputUtils.getInput();
		if(username.equals("\\")){
			connectionMessageHandler.runStartMenuThread();
			interrupt();
			return;
		}
		System.out.print("Enter Password: ");
		String password = InputUtils.getInput();
		if(password.equals("\\")){
			connectionMessageHandler.runStartMenuThread();
			interrupt();
			return;
		}
		connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.REGISTER, username, password);
		interrupt();
	}
}
