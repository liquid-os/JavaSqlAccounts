package org.author.game;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Packet00Login extends Packet{
	
	public String username;
	int playerID, classid = 0, roomID = -1;
	
	public Packet00Login(byte[] data) {
		super(00);
		username = read(data)[1];
		classid = Integer.parseInt(read(data)[2]);
		playerID = Integer.parseInt(read(data)[3]);
		roomID = Integer.parseInt(read(data)[4]);
	}
	
	public Packet00Login(String user, int heroID, int playerID, int r) {
		super(00);
		this.playerID = playerID;
		this.username = user;
		this.classid = heroID;
		this.roomID = r;
	}
	
	public byte[] getData() {
		return ("00#"+username+"#"+classid+"#"+playerID+"#"+roomID).getBytes();
	}
}
