package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class PanelLogin extends PanelBase {
	
	GUITextBox accname = new GUITextBox(Bank.searchbar, Bank.searchbar, Properties.width / 2 - (Properties.width / 3 / 2), Properties.height / 2 - (Properties.height / 10) - 20, Properties.width / 3, Properties.height / 10);
	GUITextBox accpword = new GUITextBox(Bank.searchbar, Bank.searchbar, Properties.width / 2 - (Properties.width / 3 / 2), Properties.height / 2 + 40, Properties.width / 3, Properties.height / 10);
	GUI sel = accname;
	boolean connecting = false;
	private GUIButton login, newacc;

	public PanelLogin(){
		int boxicosize = (int) (85 * scale);
		guis.add(new GUIButton(Bank.serverIco, Bank.serverIcoHover, 8, 15, 15, boxicosize, boxicosize).setText(""));
		guis.add(accname);
		guis.add(accpword);
		int bw = Properties.width / 3;
		int bh = bw / 7;
		login = new GUIButton(Bank.button, Bank.buttonHover, "Login", 10, Properties.width / 2 - bw / 2, Properties.height - Properties.height / 3, bw, bh);
		newacc = new GUIButton(Bank.button, Bank.buttonHover, "Create New Account", 11, Properties.width / 2 - bw / 2, Properties.height - Properties.height / 3 + bh + 20, bw, bh);
		guis.add(login);
		guis.add(newacc);
	}
	
	public void buttonReact(int id){
		if(id == 5){
			try {
				java.awt.Desktop.getDesktop().browse(new URI(Util.MANUAL_LINK));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if(id == 6){
			try {
				java.awt.Desktop.getDesktop().browse(new URI(Util.SITE_LINK));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if(id==8){
			//Util.music.stop();
			Bank.server = new MainServer();
			Bank.server.start();
			Display.currentScreen = new PanelServerconsole();
			Display.frame.setSize(800, 600);
			Display.cam.refocus();
		}
		if(id == 10){
			login();
		}
		if(id == 11){
			if(!accname.text.isEmpty() &! accpword.text.isEmpty()){
				Bank.client = new GameClient(Util.getServerIP());
				Bank.client.start();
				String email = JOptionPane.showInputDialog("Please enter a valid E-Mail address to create your account.");
				Packet21CreateAccount pkt = new Packet21CreateAccount(accname.text, accpword.text, email);
				pkt.write(Bank.client);
			}
		}
	}
	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_TAB){
			if(sel == accname)sel = accpword; else sel = accname;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			login();
		}
		sel.keyPressed(e);
	}
	
	public void login(){
		connecting = true;
		Util.clearPatches(new File(Bank.path+Util.getServerIP()));
		Bank.client = new GameClient(Util.getServerIP());
		Bank.client.start();
		Packet17MainLogin pkt = new Packet17MainLogin(accname.text, accpword.text, -1, -1);
		pkt.write(Bank.client);
	}
	
	public void onUpdate() {
		if(clicking){
			if(accname.getRect().contains(getMousePoint())){
				sel = accname;
			}
			if(accpword.getRect().contains(getMousePoint())){
				sel = accpword;
			}
		}
	}

	public void drawScreen(Graphics g) {
		g.drawImage(Bank.bg, 0, 0, Properties.width, Properties.height, null);
		//Bank.drawOvalOutlineOut(g, sel.x, sel.y, sel.w, sel.h, Color.GREEN, 10);	
		//g.drawImage(Bank.borderScreen, 0, 0, Properties.width, Properties.height, null);
		int logow = Properties.width / 5 + Properties.width / 25;
		int logoh = logow / 4 * 3;
		//g.drawImage(Bank.logo, Properties.width / 2 - logow / 2, (int) (Properties.height / 5 - logoh / 1.8), logow, logoh, null);
		//g.setFont(Util.hugeTitleFont);
		g.setColor(Color.BLACK);
		String s1 = "Log In", s2 = "Username", s3 = "Password";
		//g.drawString(s1, Properties.width/2 - g.getFontMetrics().stringWidth(s1) / 2, Properties.height / 5);
		g.setFont(Util.spellNameFont);
		g.drawString(s2, accname.x + accname.w / 2 - g.getFontMetrics().stringWidth(s2) / 2, accname.y - 25);
		g.drawString(s3, accpword.x + accpword.w / 2 - g.getFontMetrics().stringWidth(s2) / 2, accpword.y - 25);
		g.setColor(Color.WHITE);
		//g.setFont(Util.hugeTitleFont);
		//g.drawString(s1, Properties.width/2 - g.getFontMetrics().stringWidth(s1) / 2 + 2, Properties.height / 5 + 2);
		g.setFont(Util.spellNameFont);
		g.drawString(s2, accname.x + accname.w / 2 - g.getFontMetrics().stringWidth(s2) / 2 + 1, accname.y - 25 + 1);
		g.drawString(s3, accpword.x + accpword.w / 2 - g.getFontMetrics().stringWidth(s2) / 2 + 1, accpword.y - 25 + 1);
		//if(connecting)g.drawImage(Bank.loading, mousePoint.x, mousePoint.y, 32, 32, null);
	}
}
