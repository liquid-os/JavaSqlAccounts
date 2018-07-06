package org.author.game;

public class Packet12Chat extends Packet{
	
	private String msg;
	private int sender = 0;
	
	public Packet12Chat(byte[] data) {
		super(12);
		msg = read(data)[1];
		sender = Integer.parseInt(read(data)[2]);
	}
	
	public Packet12Chat(String msg, int sender) {
		super(12);
		this.msg = msg;
		this.sender = sender;
	}
	
	public byte[] getData() {
		return ("12#"+msg+"#"+sender).getBytes();
	}
}
