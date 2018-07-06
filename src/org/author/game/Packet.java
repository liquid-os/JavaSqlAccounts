package org.author.game;

public abstract class Packet {
	
	public static enum PacketType{
		INVALID(-1), JOINGAME(00), DISCONNECT(01), PLAYCARD(02), MOVEUNIT(03), TURNCHANGE(04), DRAWCARD(05), ATTACKUNIT(06), CARDABILITY(07), COMM(10), ENDGAME(11), CHAT(12), CHANGETILE(13), SHOWPICKS(14), SELECTPICK(15), MYSTERYCARD(16), LOGIN(17), NOTIFY(18), INVITE(19), PATCH(20), CREATEACCOUNT(21);
		
		private int id;
		
		private PacketType(int i){
			id = i;
		}
		
		public int getID(){
			return id;
		}
	}
	
	private byte packetID;
	
	public Packet(int id){
		packetID = (byte) id;
	}
	
	public final void write(GameClient c){
		if(c!=null)
		c.send(getData());
	}
	
	public final void write(GameServer c){
		if(c!=null)
		c.sendToAll(getData());
	}
	
	public final void write(MainServer m, int room){
		GameServer c = m.getRoom(room);
		if(c!=null)
		c.sendToAll(getData());
	}
	
	public String[] read(byte[] values){
		String data = new String(values).trim();
		return data.split("#");
	}
	
	public static PacketType lookup(String id){
		try{
			return lookup(Integer.parseInt(id));
		}catch(NumberFormatException e){
			return PacketType.INVALID;
		}
	}
	
	public static PacketType lookup(int id){
		for(PacketType p : PacketType.values()){
			if(p.getID() == id){
				return p;
			}
		}
		return PacketType.INVALID;
	}
	
	public abstract byte[] getData();
}
