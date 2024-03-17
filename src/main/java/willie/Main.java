package willie;

import willie.thread.ConnectionThead;
import willie.util.KeyUtils;

import java.util.Scanner;

public class Main{
	public static String host = "127.0.0.1";
	public static int port = 8080;
	public static ConnectionThead connectionThead;
	public static void main(String[] args){
//		Scanner scanner = new Scanner(System.in);
//		System.out.print("Enter the host: ");
//		host = scanner.nextLine();
//		System.out.print("Enter the port: ");
//		port = scanner.nextInt();
		KeyUtils.generateKey();
		connectionThead = new ConnectionThead(host, port);
		connectionThead.start();
		Runtime.getRuntime().addShutdownHook(new Thread(connectionThead::stopClient));
	}
}