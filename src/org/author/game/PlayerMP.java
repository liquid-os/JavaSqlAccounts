package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.InetAddress;
import java.util.ArrayList;

public class PlayerMP extends Player{
	
	InetAddress ip;
	int port = -1, playerID;
	String name;
	int currentRoom = -1;
	
	Color color = Color.RED;

	public PlayerMP(String user, InetAddress ip, int port) {
		this.name = user;
		this.ip = ip;
		this.port = port;
	}
	
	public Image getUserIcon(){
		return null;
	}
}
