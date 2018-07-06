package org.author.game;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Packet18Notification extends Packet{
	
	public String title, msg;
	public int iconType;
	
	public Packet18Notification(byte[] data) {
		super(18);
		title = read(data)[1];
		msg = read(data)[2];
		iconType = Integer.parseInt(read(data)[3]);
	}
	
	public Packet18Notification(String t, String m, int i) {
		super(18);
		this.title = t;
		this.msg = m;
		this.iconType = i;
	}
	
	public byte[] getData() {
		return ("18#"+title+"#"+msg+"#"+iconType).getBytes();
	}
}
