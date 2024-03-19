package willie.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class InputUtils{
	public static String getInput(){
		String input;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		do{
			try{
				while(!br.ready()) {
					sleep(100);
				}
				input = br.readLine();
			}catch(IOException ignored) {
				return null;
			}catch(InterruptedException e){
				throw new RuntimeException(e);
			}
		}while ("".equals(input));
		return input;
	}
//	public static String getInput() {
//		String input = "";
//		Scanner scanner = new Scanner(System.in);
//		try {
//			while (!Thread.interrupted()) {
//				if (scanner.hasNextLine()) {
//					input = scanner.nextLine();
//					break; // Break the loop if input is received
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return input;
//	}
}
