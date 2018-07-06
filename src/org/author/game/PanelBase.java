package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public abstract class PanelBase{
	
	public Random rand = new Random();
		
	public boolean clicking = false, midclicking = false, rightClicking = false, renderObj = true, renderGuis = true, runBaseProfiler = this instanceof GridPanel;
	long shakeStart = -1;
	int shakeMillis = 0, shakeAmt = 20;
	double scale = 1.0;
	
	int viewDistY = 2000, scrollBorder = 400;
	
	private int wind = 0;
	
	public Point mousePoint;
	
	public ArrayList<Unit> objects = new ArrayList<Unit>();
		
	public Point getMousePoint() {
		return (mousePoint==null?new Point(0,0):mousePoint);
	}

	public void setMousePoint(Point mousePoint) {
		this.mousePoint = mousePoint;
	}
	Grid grid;
	
	//public ArrayList<Animation> particles = new ArrayList<Animation>();
	public ArrayList<GUI> guis = new ArrayList<GUI>();
	int selGui = -1, startGenY = Properties.height/2;
			
	public PanelBase(){
		this.scale = Properties.width / Properties.baseWidth;
	}
	
	public void buttonReact(int id){
		
	}
	
	public int getTileSize() {
		if(this instanceof GridPanel) {
			if(grid!=null) {
				return grid.getTileSize((GridPanel)this);
			}else {
				return 0;
			}
		}else {
			return 0;
		}
	}
	
	public void shake(int millis, int amt){
		this.shakeStart = System.currentTimeMillis();
		this.shakeMillis = millis;
		this.shakeAmt = amt;
	}
	
	public final void update(double delta)
	{
		if(guis.size()<=selGui){
			selGui=-1;
		}
		if(shakeStart>-1){
			Display.frame.p.setLocation(rand.nextInt(shakeAmt)-rand.nextInt(shakeAmt), rand.nextInt(shakeAmt)-rand.nextInt(shakeAmt));
			if(System.currentTimeMillis()-shakeStart>=shakeMillis){
				Display.frame.p.setLocation(0, 0);
			}
		}
		
		/*for(int i = 0; i < particles.size(); ++i){
			if(i<particles.size() && particles.get(i)!=null){
				if(!particles.get(i).isRemove()){
					particles.get(i).updateBase();
				}else{
					particles.remove(i);
				}
			}
		}*/
		onUpdate();
	}
	
	public void renderGuis(Graphics g){
		for(int i = 0; i < guis.size(); ++i){
			guis.get(i).drawBase(g);
		}
	}
	
	public final void drawOverlay(Graphics g)
	{	
		if(renderGuis){
			for(int i = 0; i < guis.size(); ++i){
				guis.get(i).drawBase(g);
			}
		}
		for(int i = 0; i < Util.persistentGuis.size(); ++i){
			Util.persistentGuis.get(i).drawBase(g);
		}
	}

	public abstract void onUpdate();
	
	public abstract void drawScreen(Graphics g);
	public void renderScene(Graphics g){}
	public void renderForeground(Graphics g){}
	
	public Class getItemsInList(ArrayList l)
	{
		for(int i = 0; i < l.size(); ++i)
		{
			return (Class) l.get(i);
		}
		return null;
	}
	
	public void keyPressed(KeyEvent e){
	}
	
	public void keyReleased(KeyEvent e){
	}

	public void releaseClick(boolean b) {
		if(guis.size() > 0){
			for(int i = 0; i < guis.size(); ++i){
				if(i < guis.size()){
					if(guis.get(i).getRect().contains(Display.currentScreen.getMousePoint()) && guis.get(i).solid){
						guis.get(i).releaseClick(b);
					}
				}
			}
		}
	}

	public void click(boolean b) {
		if(guis.size() > 0){
			for(int i = 0; i < guis.size(); ++i){
				if(i < guis.size()){
					if(guis.get(i).getRect().contains(Display.currentScreen.getMousePoint()) && guis.get(i).solid){
						guis.get(i).click(b);
					}
				}
			}
		}
	}
	
	public void midClick() {
	}

	public Grid getGrid() {
		return grid;
	}

	public int getWind() {
		return wind;
	}

	public void setWind(int wind) {
		this.wind = wind;
	}

	public void removeGUI(GUI gui) {
		if(this.guis.contains(gui))guis.remove(gui);
	}

	public void rescale() {
		for(GUI gui : guis) {
			gui.x *= scale;
			gui.y *= scale;
			gui.w *= scale;
			gui.h *= scale;

		}
	}
}
