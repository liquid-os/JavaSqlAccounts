package org.author.game;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Main{
	
	public static Frame f;
	
	public static void main(String[] args)
	{
		if(args.length > 0){
			if(args[0].equalsIgnoreCase("-s")){
				Util.autoServer = true;
				File set = new File(Bank.path+"data/coreset.png");
				try {
					if(set.createNewFile()){
						//Bank.fillCoreFile(set);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				Bank.server = new MainServer();
				Bank.server.start();
				Display.currentScreen = new PanelServerconsole();
			}
		}
		if(!Util.autoServer){
			Properties.startPanel = new PanelLogin();
			Main m = new Main();
			m.start(args);
		}
	}
		
	public void start(String[] args){
		Bank.init();
		f = new Frame();
		Display.frame.setFocusTraversalKeysEnabled(false);
		//CardList.flushCards();
	}
}
