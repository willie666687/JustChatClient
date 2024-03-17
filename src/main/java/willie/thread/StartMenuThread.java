package willie.thread;

import willie.Main;
import willie.handler.ConnectionMessageHandler;
import willie.util.InputUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartMenuThread extends Thread{
	ConnectionMessageHandler connectionMessageHandler;
	public StartMenuThread(ConnectionMessageHandler connectionMessageHandler){
		this.connectionMessageHandler = connectionMessageHandler;
	}
	@Override
	public void run(){
		while(!isInterrupted()){
			System.out.println("Please select what to do:");
			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("3. Exit");
			System.out.print("Input: ");
			String input = InputUtils.getInput();
			switch(input){
				case "1":
					connectionMessageHandler.runLoginThread();
					interrupt();
					break;
				case "2":
					connectionMessageHandler.runRegisterThread();
					interrupt();
					break;
				case "3":
					interrupt();
					Main.connectionThead.stopClient();
					break;
				default:
					System.err.println("Invalid input");
					break;
			}
		}
	}
}
