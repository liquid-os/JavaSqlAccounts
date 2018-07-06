package org.author.game;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Packet19GameInvite extends Packet{
	
	public int roomID;
	String username;
	
	public Packet19GameInvite(byte[] data) {
		super(19);
		roomID = Integer.parseInt(read(data)[1]);
		username = read(data)[2];
	}
	
	public Packet19GameInvite(int i, String u) {
		super(19);
		this.roomID = i;
		this.username = u;
	}
	
	public byte[] getData() {
		return ("19#"+roomID+"#"+username).getBytes();
	}
}
