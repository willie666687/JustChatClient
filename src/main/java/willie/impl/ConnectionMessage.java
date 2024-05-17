package willie.impl;

import willie.Enum.ConnectionMessageType;

public class ConnectionMessage{
	public ConnectionMessageType type;
	public String[] messages;
	public int messageAmount = 0;
	public int totalLength = 0;

	public ConnectionMessage(ConnectionMessageType type, String... messages){
		this.type = type;
		this.messages = messages;
		messageAmount = messages.length;
		totalLength = getTotalLength();
	}

	private int getTotalLength(){
		int totalLength = 0;
		for(String s : messages){
			totalLength += s.getBytes().length;
		}
		totalLength += type.toString().getBytes().length;
		return totalLength;
	}
}
