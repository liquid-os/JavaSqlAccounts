package org.author.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GUIButton extends GUI {
	
	Color color = Color.WHITE, textColor = Color.white;
	String text = "";
	Font font = new Font(Font.DIALOG_INPUT, Font.PLAIN, 28), hoverFont = new Font(Font.DIALOG_INPUT, Font.BOLD, 28);
	int reactID = 0, arrowDir = -1;
	Image img = null, hover = null, icon = null;
	boolean turnButton = false;
	private byte ICON_STYLE_FILL = 0;
	private byte ICON_STYLE_TRAIL = 1;
	private byte iconStyle = ICON_STYLE_FILL;
	public boolean active = true;

	public GUIButton(String s, int id, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.text = s;
		this.reactID = id;
		setColor(Color.BLUE);
		this.showSelection = false;
	}
	
	public GUIButton(Image img, Image hover, int id, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.img = img;
		this.hover = hover;
		this.reactID = id;
		this.showSelection = false;
	}
	
	public GUIButton(Image img, Image hover, String txt, int id, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.img = img;
		this.hover = hover;
		this.reactID = id;
		this.text = txt;
		this.showSelection = false;
	}

	public void tick(double delta) {
	}
	
	public GUIButton setColor(Color c){
		color = new Color(c.getRed(),c.getGreen(),c.getBlue(),100);
		return this;
	}
	
	public GUIButton setColor(int r, int g, int b){
		color = new Color(r,g,b,100);
		return this;
	}
	
	public void click(boolean l){
		if(this.getRect().contains(Display.currentScreen.mousePoint)){
			confirm();
		}
	}
	
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE){
			confirm();
		}
	}
	
	public void confirm(){
		Display.currentScreen.buttonReact(reactID);
	}

	public void draw(Graphics g, int x, int y) {
		boolean drawNormal = true;
				if(this.turnButton){
					if(Util.turn != Util.clientID){
						drawNormal = false;
						g.drawImage(hover, x, y, w, h, null);
						g.setColor(Util.transparent);
						g.fillRect(x, y, w, h);
						g.setColor(Color.WHITE);
						g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 28));
						g.drawString("Enemy Turn", x+w/2-g.getFontMetrics().stringWidth("Enemy Turn")/2, y+h/2);
						Bank.drawOutlineOut(g, x, y, w, h, Color.RED, 2);
					}
				}
				if(drawNormal){
				if(hovered()&&active){
					g.drawImage(hover, x, y, w, h, null);
					g.setColor(color);
					//g.fillRect(x, y, w, h);
					g.setColor(textColor);
					g.setFont(hoverFont);
					g.drawString(text, x+w/2-g.getFontMetrics().stringWidth(text)/2, y+h/2);
				}else{
					g.drawImage(img, x, y, w, h, null);
					g.setColor(color);
					//g.fillRect(x, y, w, h);
					g.setColor(textColor);
					g.setFont(font);
					g.drawString(text, x+w/2-g.getFontMetrics().stringWidth(text)/2, y+h/2);
				}
				if(icon!=null){
					if(getIconStyle() == ICON_STYLE_FILL){
						g.drawImage(icon, x+w/2-w/4, y+h/2-h/4, w/2, h/2, null);
					}
					if(getIconStyle()  == ICON_STYLE_TRAIL){
						g.drawImage(icon, x+w/2+g.getFontMetrics().stringWidth(text)/2 + 5, y+h/2-h/4, h/2, h/2, null);
					}					
				}
				if(img == Bank.button)
					Bank.drawOutline(g, x, y, w, h, (this.hovered() ? Color.YELLOW : Color.BLACK), 3);
				}
				if(!active){
					g.setColor(Util.transparent_dark);
					g.fillRect(x, y, w, h);
				}
	}

	public GUI setArrow(int i) {
		arrowDir = i;
		return this;
	}

	public GUIButton setText(String string) {
		this.text = string;
		return this;
	}

	public GUIButton setIcon(Image icon) {
		this.icon = icon;
		return this;
	}

	public byte getIconStyle() {
		return iconStyle;
	}

	public void setIconStyle(byte iconStyle) {
		this.iconStyle = iconStyle;
	}

	public void setFont(Font font) {
		this.font = font;
	}
}
