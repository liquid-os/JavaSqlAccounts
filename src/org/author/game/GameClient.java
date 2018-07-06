package org.author.game;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.author.game.Packet.PacketType;

public class GameClient extends Thread{
	
	InetAddress ip;
	DatagramSocket socket;
	
	public GameClient(String address){
		System.out.println("Client connecting to IP address "+address+"...");
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}			
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}
		
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String d = new String(data).trim();
		String[] packetData = d.split("#");
		PacketType type = Packet.lookup(Integer.parseInt(packetData[0]));
		String name = null;
		PanelBase panel = Display.currentScreen;
		//name = packetData[1];
		Packet packet;
		int roomID = -1;
		switch(type){
		case INVALID:
			System.out.println("Invalid packet.");
		case LOGIN:
			Display.currentScreen = new PanelMainmenu();
			Display.cam.refocus();
		break;
		}
	}
	
	public void send(byte[] data){
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, Properties.port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
