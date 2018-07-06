package org.author.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GUINotify extends GUI {
	
	long start = -1;
	int destY = 0, maxTime = 5000, fallTime = 600;
	Color color = Color.WHITE, textColor = Color.white, titleColor = Color.GREEN;
	String text, title;
	Image icon;

	public GUINotify(Image img, String title, String text, int x, int y) {
		super(x, 0, 0, 80);
		int count = 0;
		for(GUI gui : Util.persistentGuis){
			if(gui instanceof GUINotify){
				++count;
			}
		}
		this.destY = count*h+y;
		this.setColor(Util.transparent_dark);
		this.start = System.currentTimeMillis();
		this.icon = img;
		this.text = text;
		this.title = title;
		w = 300;
		if(Display.frame.getGraphics().getFontMetrics(Util.descTitleFont).stringWidth(title)+100 > w)w = Display.frame.getGraphics().getFontMetrics(Util.descTitleFont).stringWidth("Achievement Get!")+100;
		int nameLength = Display.frame.getGraphics().getFontMetrics(Util.descTitleFont).stringWidth(text)+100;
		if(nameLength > w)w = nameLength;
		this.showSelection = false;
	}

	public void tick(double delta) {
	}
	
	public void click(boolean b){
	}
	
	public GUINotify setColor(Color c){
		color = new Color(c.getRed(),c.getGreen(),c.getBlue(),100);
		return this;
	}
	
	public GUINotify setColor(int r, int g, int b){
		color = new Color(r,g,b,100);
		return this;
	}

	public void draw(Graphics g, int x, int y) {
		if(System.currentTimeMillis()-start >= maxTime){
			Util.persistentGuis.remove(this);
		}
		int newy = (int) ((System.currentTimeMillis()-start) * destY / fallTime);
		if(newy>destY)newy=destY;
		y = newy;
		g.drawImage(Bank.gradient, x, y, h, h, null);
		g.setColor(Util.transparent_dark);
		g.fillRect(x, y, h, h);
		g.drawImage(icon, x, y, h, h, null);
		g.drawImage(Bank.gradient, x+h, y, w-h, h, null);
		g.setColor(color);
		g.fillRect(x+h, y, w-h, h);
		g.setColor(Color.BLACK);
		g.drawRect(x+h, y, w-h, h);
		g.setColor(Color.GREEN);
		g.setFont(Util.descTitleFont);
		g.drawString(title, x+h+5, y+20);
		g.setColor(Color.WHITE);
		g.drawString(text, x+h+5, y+40);
		g.setColor(Util.transparent_white);
	}
}
