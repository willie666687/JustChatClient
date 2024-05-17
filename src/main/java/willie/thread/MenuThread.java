package willie.thread;

import willie.Enum.ConnectionMessageType;
import willie.Enum.MenuStatus;
import willie.Enum.Status;
import willie.Main;
import willie.handler.ConnectionMessageHandler;
import willie.util.DebugOutput;
import willie.util.InputUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MenuThread extends Thread{
	ConnectionMessageHandler connectionMessageHandler;
	public boolean responseReceived = false;

	public MenuThread(ConnectionMessageHandler connectionMessageHandler){
		this.connectionMessageHandler = connectionMessageHandler;
	}

	public static MenuStatus menuStatus;

	@Override
	public void run(){
		while(!isInterrupted()){
			if(connectionMessageHandler.status.equals(Status.LOGINED)){
				if(menuStatus == null){
					menuStatus = MenuStatus.MAINMENU;
				}
				switch(menuStatus){
					case MAINMENU -> {
						mainMenu();
					}
					case FRIENDMENU -> {
						friendMenu();
					}
					case CHATWITHFRIEND -> {
						chatWithFriendMenu();
					}
				}
			}else if(connectionMessageHandler.status.equals(Status.KEYEXCHANGED)){
				startMenu();
			}else{
				interrupt();
			}
		}
	}

	public void startMenu(){
		DebugOutput.clearOutput();
		System.out.println("Please select what to do:");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("3. Exit");
		System.out.print("Input: ");
		String input = InputUtils.getInput();
		switch(Objects.requireNonNull(input)){
			case "1" -> {
				loginMenu();
			}
			case "2" -> {
				registerMenu();
			}
			case "3" -> {
				Main.connectionThead.stopClient();
				interrupt();
			}
			default -> {
				DebugOutput.printResponse("Invalid input");
			}
		}
	}

	public void loginMenu(){
		DebugOutput.clearOutput();
		System.out.println("Please enter your username and password to login.");
		System.out.println("Input \"\\\" to cancel.");
		System.out.print("Enter Username: ");
		String account = InputUtils.getInput();
		if(account.equals("\\")){
			return;
		}
		System.out.print("Enter password: ");
		String password = InputUtils.getInput();
		if(password.equals("\\")){
			return;
		}
		connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.LOGIN, account, password);
		waitForResponse();
	}

	public void registerMenu(){
		DebugOutput.clearOutput();
		System.out.println("Please enter your username and password to register.");
		System.out.println("Input \"\\\" to cancel.");
		System.out.print("Enter Username: ");
		String username = InputUtils.getInput();
		if(username.equals("\\")){
			return;
		}
		System.out.print("Enter Password: ");
		String password = InputUtils.getInput();
		if(password.equals("\\")){
			return;
		}
		connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.REGISTER, username, password);
		waitForResponse();
	}

	public void mainMenu(){
		DebugOutput.clearOutput();
		System.out.println("Please select what to do:");
		System.out.println("1. Friend Menu");
//		System.out.println("2. Group Chat");
		System.out.println("2. Logout");
		System.out.print("Input: ");
		String input = InputUtils.getInput();
		switch(Objects.requireNonNull(input)){
			case "1" -> {
				friendMenu();
			}
//			case "2" -> {
//			}
			case "2" -> {
				connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.LOGOUT);
				waitForResponse();
				return;
			}
			default -> {
				DebugOutput.printResponse("Invalid input");
			}
		}
	}

	public Set<String> friends;

	public void friendMenu(){
		menuStatus = MenuStatus.FRIENDMENU;
		DebugOutput.clearOutput();
		friends = new HashSet<>();
		System.out.println("Friend List:");
		connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.FRIENDLIST);
		waitForResponse();
		if(friends.isEmpty()){
			System.out.println("You have no friends.");
		}else{
			for(String friend : friends){
				System.out.println(friend);
			}
		}
		System.out.println("Please select what to do:");
		System.out.println("1. Add Friend");
		System.out.println("2. View Friend Requests");
		System.out.println("3. Chat with Friend");
		System.out.println("4. Back");
		System.out.print("Input: ");
		String input = InputUtils.getInput();
		switch(Objects.requireNonNull(input)){
			case "1" -> {
				addFriendMenu();
			}
			case "2" -> {
				viewFriendRequestsMenu();
			}
			case "3" -> {
				chatWithFriendMenu();
			}
			case "4" -> {
				mainMenu();
			}
			default -> {
				DebugOutput.printResponse("Invalid input");
				friendMenu();
			}
		}
	}

	public void addFriendMenu(){
		DebugOutput.clearOutput();
		System.out.println("Enter the username of the friend you want to add: ");
		System.out.println("Input \"\\\" to cancel.");
		System.out.print("Input: ");
		String friend = InputUtils.getInput();
		if(friend.equals("\\")){
			return;
		}
		connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.ADDFRIEND, friend);
		waitForResponse();
	}

	public Set<String> friendRequests;

	public void viewFriendRequestsMenu(){
		DebugOutput.clearOutput();
		friendRequests = new HashSet<>();
		System.out.println("Friend Requests:");
		connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.FRIENDREQUEST);
		waitForResponse();
		if(friendRequests.isEmpty()){
			System.out.println("You have no friend requests.");
		}else{
			for(String friendRequest : friendRequests){
				System.out.println(friendRequest);
			}
			System.out.println("Type the username of the friend you want to accept or reject.");
		}
		System.out.println("Input \"\\\" to cancel.");
		System.out.print("Input: ");
		String input = InputUtils.getInput();
		if(input.equals("\\")){
			return;
		}
		if(friendRequests.contains(input)){
			DebugOutput.clearOutput();
			System.out.println("Please select what to do:");
			System.out.println("1. Accept");
			System.out.println("2. Reject");
			System.out.println("3. Back");
			System.out.print("Input: ");
			String choice = InputUtils.getInput();
			switch(Objects.requireNonNull(choice)){
				case "1" -> {
					connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.ACCEPTFRIEND, input);
					waitForResponse();
				}
				case "2" -> {
					connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.REJECTFRIEND, input);
					waitForResponse();
				}
				case "3" -> {
					return;
				}
				default -> {
					DebugOutput.printResponse("Invalid input");
					viewFriendRequestsMenu();
				}
			}
		}else{
			DebugOutput.printResponse("Invalid input");
			viewFriendRequestsMenu();
		}
	}

	public String[] chatHistory;

	public void chatWithFriendMenu(){
		menuStatus = MenuStatus.CHATWITHFRIEND;
		DebugOutput.clearOutput();
		System.out.println("Chat Menu:");
		System.out.println("Please select who to chat with:");
		System.out.println("Input \"\\\" to cancel.");
		for(String friend : friends){
			System.out.println(friend);
		}
		System.out.print("Input: ");
		String input = InputUtils.getInput();
		if(input.equals("\\")){
			menuStatus = MenuStatus.FRIENDMENU;
			return;
		}
		if(friends.contains(input)){
			connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.FRIENDCHATHISTORY, input);
			waitForResponse();
			System.out.println("Input \"\\\" to cancel.");
			System.out.println("Input directly to send message to " + input);
			if(chatHistory != null){
				for(String chatMessage : chatHistory){
					System.out.println(chatMessage);
				}
			}else{
				System.out.println("No chat history with " + input);
			}
			String message = InputUtils.getInput();
			while(!message.equals("\\")){
				connectionMessageHandler.sendEncryptedMessage(ConnectionMessageType.CHATWITHFRIEND, input, message);
				waitForResponse();
				message = InputUtils.getInput();
			}
		}else{
			DebugOutput.printResponse("Invalid input");
			chatWithFriendMenu();
		}
	}

	public void waitForResponse(){
		responseReceived = false;
		while(!responseReceived){
			try{
				sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
