package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public abstract class GUI {
	
	int x, y, w, h;
	boolean stationed = true, showSelection = true;
	public boolean solid = true;
	
	public GUI(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, w, h);
	}
	
	private final void tickBase(double delta){
		tick(delta);
	}
	
	public boolean hovered(){
		return (Display.currentScreen.guis.size()>0)?(this.getRect().contains(Display.currentScreen.mousePoint)) :false;
	}
	
	public abstract void tick(double delta);
	
	public void keyPressed(KeyEvent e){
		
	}
	
	public void keyReleased(KeyEvent e){
		
	}
	
	public void click(boolean l){
		
	}
	
	public void releaseClick(boolean l){
		
	}
	public final void drawBase(Graphics g){
		boolean clicked = (this.getRect().contains(Display.currentScreen.mousePoint)&&Display.currentScreen.clicking);
		draw(g, x, y-(clicked?1:0));
		if(hovered()&&showSelection){
			g.setColor(Color.YELLOW);
			g.drawRect(x, y-(clicked?1:0), w, h);
		}
	}
	
	public abstract void draw(Graphics g, int x, int y);
}
