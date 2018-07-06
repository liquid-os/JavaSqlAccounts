package org.author.game;

import java.awt.Point;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.author.game.Packet.PacketType;

public class MainServer extends Thread{


	DatagramSocket socket;
	ArrayList<GameServer> rooms = new ArrayList<GameServer>();
	ArrayList<PlayerMP> connected = new ArrayList<PlayerMP>();
	//GameServer authRoom = new GameServer();
	private ArrayList<String> log = new ArrayList<String>();
	String hostIP = Analysis.getKey(new File(Bank.path+"server.properties"), "ip");
	private ArrayList<PlayerMP> queue = new ArrayList<PlayerMP>();
	static int MAX_GAMEROOMS = Integer.parseInt(Analysis.getKey(new File(Bank.path+"server.properties"), "maxRooms"));
	static int VERSION = Integer.parseInt(Analysis.getKey(new File(Bank.path+"server.properties"), "gameVersion"));
	private File patchDirectory = new File(Bank.path+"server_patches");
	Connection dbc;
	Statement dbs;
	public MainServer(){
		Util.version = VERSION;
		Bank.init();
		Util.readPatches(patchDirectory);
		log("Server starting on port "+Properties.port);
		log("...");
		try {
			//InetAddress address = InetAddress.getByName(hostIP);
			socket = new DatagramSocket(Properties.port);
			dbc = DriverManager.getConnection("jdbc:mysql://localhost:3306/terroth", "root", "ascent");
			dbs = dbc.createStatement();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		log("Server started successfully on IP Address "+hostIP+".");
	}
	
	public void run(){
		log("Checking for plugins...");
		File[] plugins = new File(Bank.path+"plugins/").listFiles();
		if(plugins.length > 0){
			for(int i = 0; i < plugins.length; ++i){
				File f = new File(Bank.path+"plugins/"+plugins[i].getName());
				if(f.isFile() && f.exists()){
					//this.loadPlugin(f, f.getName());
				}
			}
		}
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort(), getRoomIDForClientIP(packet.getAddress(), packet.getPort()));
		}
	}
	
	public GameServer getRoomForClientIP(InetAddress ip, int port){
		/*for(GameServer s : rooms){
			for(PlayerMP c : s.clients){
				if(c.ip.equals(ip))return s;
			}
		}*/
		PlayerMP pl = this.getSender(ip, port);
		if(pl != null)
			return this.getRoom(pl.currentRoom);
		else return null;
	}
	
	public int getRoomIDForClientIP(InetAddress ip, int port){
		GameServer room = getRoomForClientIP(ip, port);
		if(room == null){
			return -1;
		}else{
			return room.getId();
		}
	}
	
	public PlayerMP getSender(InetAddress address, int port){
		for(PlayerMP pl : connected){
			if(pl.ip.equals(address) && pl.port == port){
				return pl;
			}
		}
		return null;
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port, int room) {
		String d = new String(data).trim();
		String[] packetData = d.split("#");
		PacketType type = Packet.lookup(packetData[0]);
		Packet packet;
		if(room <= -1 || type == Packet.PacketType.INVITE || type == Packet.PacketType.LOGIN || type == Packet.PacketType.JOINGAME || type == Packet.PacketType.CREATEACCOUNT){
			if(type == Packet.PacketType.LOGIN){
				log("Player attempting to connect with username "+packetData[1]);
				String[] accInfo = getAccountInfo(packetData[1]);
				if(accInfo != null){
					log("> Account under username "+packetData[1]+" found...");
					if(accInfo[1].equalsIgnoreCase(packetData[2])){
						log(">> Password is a MATCH for account "+packetData[1]);
						packet = new Packet17MainLogin(packetData[1], packetData[2], Integer.parseInt(packetData[3]), VERSION);
						PlayerMP player = new PlayerMP(packetData[1], address, port);
						System.out.println("Player connected with username "+((Packet17MainLogin)packet).username);
						registerConnection(player, (Packet17MainLogin)packet);
						send(packet.getData(), address, port);
						log("> Player "+((Packet17MainLogin)packet).username+" connected to main server.");
						send(new Packet18Notification("Success!", "You are now logged in as "+packetData[1]+".", 0).getData(), address, port);
						Util.distributePatches(patchDirectory, player);
					}else{
						send(new Packet18Notification("Error!", "The password you entered does not match the one stored on our system.", 1).getData(), address, port);
						log(">> Password does not match for account "+packetData[1]);
					}
				}else{
					send(new Packet18Notification("Error!", "That account does not exist.", 1).getData(), address, port);
					log("> Account under username "+packetData[1]+" NOT found...");
				}
			}
			if(type == Packet.PacketType.INVITE){
				PlayerMP pl = this.getPlayer(packetData[2]);
				//queuePlayer(pl);
			}
			if(type == Packet.PacketType.JOINGAME){
				room = Integer.parseInt(packetData[5]);
				getRoom(room).parsePacket(data, d, packetData, address, port);
				log("User "+packetData[1]+" attempting to join GameRoom ["+room+"]. Redirecting packet.");
			}
			if(type == Packet.PacketType.CREATEACCOUNT){
				String name = packetData[1];
				String pass = packetData[2];
				String email = packetData[3];
				int newid = addAccount(name, pass, email);
				if(newid == -1){
					send(new Packet18Notification("Error!", "That username is already taken, sorry!", 1).getData(), address, port);
				}else{
					send(new Packet18Notification("Success!", "Account '"+name+"' has been created.", 2).getData(), address, port);
				}
			}
		}else{
			if(room > -1)
			getRoom(room).parsePacket(data, d, packetData, address, port);
		}
	}
	
	public String[] getAccountInfo(String s){
		String[] arr = new String[3];
		try {
			dbs = dbc.createStatement();
			ResultSet ln = dbs.executeQuery("SELECT password FROM accounts WHERE username = '"+s+"'");
			arr[0] = s;
			if (ln.next())
				arr[1] = ln.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	public int addAccount(String user, String pass, String email){
		int id = 1;
		try {
			dbs.executeUpdate("INSERT INTO accounts (username, password, email) VALUES ('"+user+"', '"+pass+"', '"+email+"')");
			ResultSet rs = dbs.executeQuery("SELECT accountID FROM accounts WHERE username = '"+user+"'");
			if(rs.next()) {
				id = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		log("Account '"+user+"' has been created.");
		return id;
	}
	
	public GameServer getRoom(int id){
		for(GameServer s : rooms){
			if(s.getId() == id)return s;
		}
		return null;
	}
	
	private GameServer findRoom(int id){
		for(GameServer s : rooms){
			if(s.getId() == id)return s;
		}
		return null;
	}
	
	private GameServer addRoom(){
		int newID = generateNewRoomID();
		GameServer s = new GameServer(newID);
		s.roomID = newID;
		rooms.add(s);
		return s;
	}
	
	private int generateNewRoomID(){
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(GameServer s : rooms){
			if(s != null){
				ids.add((int) s.getId());
			}
		}
		for(int i = 1; i < MAX_GAMEROOMS; ++i){
			if(!ids.contains(i))return i;
		}
		return 0;
	}

	public int getIDForPlayer(String name){
		for(PlayerMP p : connected){
			if(name.equalsIgnoreCase(p.name)){
				return connected.indexOf(p);
			}
		}
		return 0;
	}

	private void breakConnection(PlayerMP player, Packet01Disconnect packet) {	
		connected.remove(getPlayerIndex(packet.username));
		//GameServer s = this.getRoomForClientIP(player.ip, player.port);
		GameServer s = this.getRoom(player.currentRoom);
		if(s!=null){
			packet.write(s);
		}else{
			packet.write(this, -1);
		}
	}
	
	/*private void registerConnection(PlayerMP player, Packet17MainLogin packet) {
		boolean c = false;
		for(PlayerMP p : connected){
			if(player.name.equalsIgnoreCase(p.name)){
				if(p.ip == null){
					p.ip = player.ip;
				}
				if(p.port == -1){
					p.port = player.port;
				}
				c = true;
			}
		}
		if(!c){
			PlayerMP newplayer = player;
			newplayer.playerID = -1;
			connected.add(newplayer);
		}
	}*/
	
	
	 private void registerConnection(PlayerMP player, Packet17MainLogin packet) {
		boolean c = false;
		for(PlayerMP p : connected){
			if(player.name.equalsIgnoreCase(p.name)){
				Packet01Disconnect pkt = new Packet01Disconnect(player.name);
				this.send(pkt.getData(), p.ip, p.port);
				connected.remove(p);
				for(GameServer s : rooms){
					s.removePlayer(p);
				}
				break;
			}
		}
		if(!c){
			PlayerMP newplayer = player;
			newplayer.playerID = -1;
			connected.add(newplayer);
		}
	}
	 
	public PlayerMP getPlayer(String user){
		for(int i = 0; i < connected.size(); ++i){
			if(connected.get(i).name.equalsIgnoreCase(user))
			return connected.get(i);
		}
		return null;
	}
	
	public int getPlayerIndex(String user){
		int index = 0;
		for(int i = 0; i < connected.size(); ++i){
			if(connected.get(i).name.equals(user))
				break;
		}
		index++;
		return index;
	}

	public void send(byte[] data, InetAddress ip, int port){
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendToAll(byte[] data) {
		for(PlayerMP p : connected){
			send(data, p.ip, p.port);
		}
	}

	public ArrayList<String> getLog() {
		return log;
	}

	public void setLog(ArrayList<String> log) {
		this.log = log;
	}

	public int generateGUID(int roomID) {
		return this.getRoom(roomID).generateGUID();
	}
	
	private void log(String s){
		log.add(s);
		System.out.println(s);
	}

	public Grid getGrid(int roomID) {
		return this.getRoom(roomID).grid;
	}

	public ArrayList<PlayerMP> getClients(int roomID) {
		return this.getRoom(roomID).clients;
	}

	public void purgeRoom(GameServer room) {
		for(PlayerMP pl : room.clients){
			pl.currentRoom = -1;
		}
		this.rooms.remove(room);
	}
}
