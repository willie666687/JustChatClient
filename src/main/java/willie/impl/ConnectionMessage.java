package willie.impl;

import willie.Enum.ConnectionMessageType;

public class ConnectionMessage{
	public ConnectionMessageType type;
	public String message;
	public ConnectionMessage(ConnectionMessageType type, String message){
		this.type = type;
		this.message = message;
	}
}
