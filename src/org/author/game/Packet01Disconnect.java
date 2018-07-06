package org.author.game;

public class Packet01Disconnect extends Packet{
	
	public String username;
	
	public Packet01Disconnect(byte[] data) {
		super(01);
		username = read(data)[1];
	}
	
	public Packet01Disconnect(String user) {
		super(01);
		username = user;
	}
	
	public byte[] getData() {
		return ("01#"+username).getBytes();
	}
}
