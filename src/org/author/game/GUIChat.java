package org.author.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

public class GUIChat extends GUI {
	
	private String text = "";
	private Font font = Util.cooldownFont, hoverFont = Util.cooldownBold;
	private boolean canEdit = true;

	public GUIChat() {
		super(Properties.width / 2 - 200, Properties.height / 2 - 50, 400, 100);
	}

	public void tick(double delta) {
	}

	public void draw(Graphics g, int x1, int y) {
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, h);
		Font font1 = new Font(Font.MONOSPACED, Font.BOLD, h/2);
		int x = x1;
		g.setFont(font);
		int w = this.w, excess = 0;
		if(g.getFontMetrics().stringWidth(text)+20 > w){
			w = g.getFontMetrics().stringWidth(text)+20;
			excess = this.w - w;
		}
		x = this.x + excess/2;
		int tw = g.getFontMetrics(font1).stringWidth("CHAT MESSAGE") + 10;
		g.drawImage(Bank.texture_tweed, x, y-h/2, tw, h/2, null);
		g.drawImage(Bank.texture_bark, x, y, w, h, null);
		g.setColor(Color.BLACK);
		g.drawString(text, x + 10, y + h - h / 4);
		g.setFont(font1);
		g.setColor(Color.WHITE);
		g.drawString("CHAT MESSAGE", x+5, y-h/10);
		Bank.drawOutlineOut(g, x, y, w, h, Color.BLACK, 3);
		Bank.drawOutlineOut(g, x, y-h/2, tw, h/2-3, Color.BLACK, 3);
	}
	
	public void send(){
		if(!text.isEmpty()){
			Packet12Chat packet = new Packet12Chat(text, -1);
			packet.write(Bank.client);
		}
		Display.currentScreen.guis.remove(this);
	}
	
	public void keyPressed(KeyEvent e){
		char c = e.getKeyChar();
		if(this.canEdit){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				send();
			}else{
				if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
					char[] chars = text.toCharArray();
					text="";
					for(int i = 0; i < chars.length-1; ++i){
						text+=chars[i];
					}
				}
				if(Character.isLetterOrDigit(c)||c==' '||c=='_'){
					text+=c;
				}
			}
		}
	}
}
