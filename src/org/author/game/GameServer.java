package org.author.game;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.author.game.Packet.PacketType;

public class GameServer {

	ArrayList<PlayerMP> clients = new ArrayList<PlayerMP>();
	long turnStart = System.currentTimeMillis();
	int maxTurnTime = Util.maxTurnTime, playerTurn = -1;
	private ArrayList<String> log = new ArrayList<String>();
	int currentGUID = 0;
	int playersToStart = 2;
	int baseGold = 0, turn = 0;
	public Grid grid = new Grid(Util.boardWidth, null);
	int roomID = 0;
	boolean gameover = false;
	
	public MainServer getMainServer(){
		return Bank.server;
	}
	
	public int getId(){
		return roomID;
	}
	
	public GameServer(){
		log.add("Server room on port "+Properties.port);
		log.add("...");
	}

	public GameServer(int newID) {
		this();
		this.roomID = newID;
		log.add("Game Room ["+newID+"] has started.");
	}

	public void run(){
		log.add("Checking for plugins...");
		File[] plugins = new File(Bank.path+"plugins/").listFiles();
		if(plugins.length > 0){
			for(int i = 0; i < plugins.length; ++i){
				File f = new File(Bank.path+"plugins/"+plugins[i].getName());
				if(f.isFile() && f.exists()){
				}
			}
		}
		/*while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}*/
	}

	public PlayerMP getSender(InetAddress address, int port){
		for(PlayerMP pl : clients){
			if(pl.ip.equals(address) && pl.port == port){
				return pl;
			}
		}
		return null;
	}
	
	void parsePacket(byte[] data, String d, String[] packetData, InetAddress address, int port) {
		PacketType type = Packet.lookup(packetData[0]);
		Packet packet;
		PlayerMP player = null;
		int senderID = ((this.getSender(address, port) != null) ? this.clients.indexOf(this.getSender(address, port)) : 0);
		switch(type){
			case INVALID:
			break;
					}
	}
	
	public int getIDForPlayer(String name){
		for(PlayerMP p : clients){
			if(name.equalsIgnoreCase(p.name)){
				return clients.indexOf(p);
			}
		}
		return 0;
	}

	private void breakConnection(PlayerMP player, Packet01Disconnect packet) {	
		clients.remove(getPlayerIndex(packet.username));
		packet.write(this);
	}

	private void registerConnection(PlayerMP player, Packet00Login packet) {
		boolean connected = false;
		for(PlayerMP p : clients){
			if(player.name.equalsIgnoreCase(p.name)){
				if(p.ip == null){
					p.ip = player.ip;
				}
				if(p.port == -1){
					p.port = player.port;
				}
				connected = true;
			}else{
				send(packet.getData(), p.ip, p.port);
			}
		}
		if(!connected){
			PlayerMP newplayer = player;
			newplayer.playerID = clients.size();
			clients.add(newplayer);
		}
	}
	
	public PlayerMP getPlayer(String user){
		for(int i = 0; i < clients.size(); ++i){
			return clients.get(i).name == user ? clients.get(i) : null;
		}
		return null;
	}
	
	public int getPlayerIndex(String user){
		int index = 0;
		for(int i = 0; i < clients.size(); ++i){
			if(clients.get(i).name.equals(user))
				break;
		}
		index++;
		return index;
	}

	public void send(byte[] data, InetAddress ip, int port){
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			this.getMainServer().socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendToAll(byte[] data) {
		for(PlayerMP p : clients){
			send(data, p.ip, p.port);
		}
	}

	public ArrayList<String> getLog() {
		return log;
	}

	public void setLog(ArrayList<String> log) {
		this.log = log;
	}

	public int generateGUID() {
		++currentGUID;
		return currentGUID-1;
	}

	public void addPlayer(PlayerMP p1) {
		p1.currentRoom = this.getId();
		Packet19GameInvite pkt = new Packet19GameInvite(this.getId(), p1.name);
		this.send(pkt.getData(), p1.ip, p1.port);
		//clients.add(p1);
		/*if(clients.size() >= this.playersToStart){
			//this.startGame();
		}*/
	}

	public void removePlayer(PlayerMP player) {
		for(PlayerMP p : clients){
		}
	}

	public boolean isGameOver() {
		return gameover;
	}
}
