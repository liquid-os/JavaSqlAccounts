package org.author.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class GUITextBox extends GUI {
	
	Color color = Color.WHITE, textColor = Color.white;
	String text = "", label = "";
	Font font = Util.cooldownFont, hoverFont = Util.cooldownBold;
	Image img = Bank.gradient, hover = null;
	boolean canEdit = true;

	public GUITextBox(String s, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.text = s;
		setColor(Color.BLACK);
		this.showSelection = false;
	}
	
	public GUITextBox(Image img, Image hover, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.img = img;
		this.hover = hover;
		this.showSelection = false;
	}
	
	public GUITextBox(Image img, Image hover, String txt, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.img = img;
		this.hover = hover;
		this.text = txt;
		this.showSelection = false;
	}

	public void tick(double delta) {
	}
	
	public GUITextBox setEditable(boolean b){
		this.canEdit = b;
		return this;
	}
	
	public GUITextBox setLabel(String s){
		this.label = s;
		return this;
	}
	
	public GUITextBox setColor(Color c){
		color = new Color(c.getRed(),c.getGreen(),c.getBlue(),100);
		return this;
	}
	
	public GUITextBox setColor(int r, int g, int b){
		color = new Color(r,g,b,100);
		return this;
	}
	
	public void keyPressed(KeyEvent e){
		char c = e.getKeyChar();
		if(this.canEdit){
			if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
				char[] chars = text.toCharArray();
				text="";
				for(int i = 0; i < chars.length-1; ++i){
					text+=chars[i];
				}
			}
			if(Character.isLetterOrDigit(c)||c==' '||c=='_'||c=='='||c==','){
				text+=c;
			}
		}
	}
	
	public void draw(Graphics g, int x, int y) {
		if(img!=null){
			g.drawImage(hovered()?hover:img, x, y, w, h, null);
			if(img==Bank.gradient){
				g.setColor(hovered()?Color.WHITE:Color.BLACK);
				g.drawRect(x, y, w, h);
			}
			if(text!=null){
				g.setColor(textColor);
				g.setFont(hovered()?hoverFont:font);
				g.drawString(text, x+w/2-g.getFontMetrics().stringWidth(text)/2, y+h/2);
			}
		}else{
			if(hovered()){
				g.drawImage(Bank.buttonHover, x, y, w, h, null);
				g.setColor(color);
				g.fillRect(x, y, w, h);
				g.setColor(textColor);
				g.setFont(hoverFont);
				g.drawString(text, x+w/2-g.getFontMetrics().stringWidth(text)/2, y+h/2);
			}else{
				g.drawImage(Bank.button, x, y, w, h, null);
				g.setColor(color);
				g.fillRect(x, y, w, h);
				g.setColor(textColor);
				g.setFont(font);
				g.drawString(text, x+w/2-g.getFontMetrics().stringWidth(text)/2, y+h/2);
			}
		}
		if(label!=null){
			g.setFont(Util.descTitleFont);
			int l = g.getFontMetrics().stringWidth(label);
			int l1 = g.getFontMetrics().stringWidth(label)+10;
			g.setColor(Color.BLACK);
			//g.fillRect(x+w/2-l1/2, y-20, l1, 20);
			g.setColor(Color.WHITE);
			g.drawString(label, x+w/2-l/2, y-5);
		}
	}
}
