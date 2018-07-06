package org.author.game;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Packet21CreateAccount extends Packet{
	
	String name, pass, email;
	
	public Packet21CreateAccount(byte[] data) {
		super(21);
		name = read(data)[1];
		pass = read(data)[2];
		email = read(data)[3];
	}
	
	public Packet21CreateAccount(String n, String p, String e) {
		super(21);
		this.name = n;
		this.pass = p;
		this.email = e;
	}
	
	public byte[] getData() {
		return ("21#"+name+"#"+pass+"#"+email).getBytes();
	}
}
