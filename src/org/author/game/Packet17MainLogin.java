package org.author.game;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Packet17MainLogin extends Packet{
	
	public String username, password;
	public int accountID = 0, version = 1;
	
	public Packet17MainLogin(byte[] data) {
		super(17);
		username = read(data)[1];
		password = read(data)[2];
		accountID = Integer.parseInt(read(data)[3]);
		version = Integer.parseInt(read(data)[4]);
	}
	
	public Packet17MainLogin(String user, String pass, int acc, int ver) {
		super(17);
		this.username = user;
		this.password = pass;
		this.accountID = acc;
		this.version = ver;
	}
	
	public byte[] getData() {
		return ("17#"+username+"#"+password+"#"+accountID+"#"+version).getBytes();
	}
}
