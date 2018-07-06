package org.author.game;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Packet20Patch extends Packet{
	
	String patch;
	
	public Packet20Patch(byte[] data) {
		super(20);
		patch = read(data)[1];
	}
	
	public Packet20Patch(String p) {
		super(20);
		this.patch = p;
	}
	
	public byte[] getData() {
		return ("20#"+patch).getBytes();
	}
}
