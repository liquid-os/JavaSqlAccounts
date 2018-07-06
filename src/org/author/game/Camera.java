package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Camera {
	
	int x = 0, y = 0, mx = 0, my=0;
	//private int min = Grid.tileSize, max = Grid.tileSize*370;
	PanelBase panel;
	private Unit unit = null;
	

	public Camera() {
	}
	
	public void focus(PanelBase panel){
		this.panel = panel;
	}
	
	public void setUnitFocus(Unit u){
		this.unit = u;
	}
	
	public void reset(){
		x = 0 ;
		y = 0;
	}
	
	public void physUpdate(){
		int div = 10;
		if(mx > 0){
			mx--;
			x+=mx/div;
		}
		if(mx < 0){
			mx++;
			x-=mx/div;
		}
		if(my > 0){
			my--;
			y+=my/div;
		}
		if(my < 0){
			my++;
			y-=my/div;
		}
	}
	
	int bx = Util.boardOffsetX;
	int by = Util.boardOffsetY;
	
	public void displayCamera(Graphics g){
		int x = this.x, y = this.y;
		//if(x < min)x=min;
		//if(x > max)x=max;
		this.physUpdate();
		panel.renderScene(g);
		//renderTileGrid(g, 0, 0);
		panel.drawScreen(g);
		for(int i = 0; i < panel.objects.size(); ++i){
			if(i < panel.objects.size()){
				Unit unit = panel.objects.get(i);
				if(unit!=null)
				unit.originDraw(g, (GridPanel)panel);
			}
		}
		if(!panel.guis.isEmpty() && panel.renderGuis){
			if(panel.guis!=null){
				if(!panel.guis.isEmpty()){
					for(GUI gui : panel.guis){
						if(gui!=null)
						gui.draw(g, gui.x, gui.y);
					}
				}
			}
		}
		panel.renderForeground(g);
		/*for(int i = 0; i < panel.particles.size(); ++i){
			if(i < panel.particles.size()){
				Animation unit = panel.particles.get(i);
				if(unit!=null)
				unit.draw(g, unit.posX, unit.posY);
			}
		}*/
		panel.drawOverlay(g);
		g.drawImage(null, panel.mousePoint.x, panel.mousePoint.y, 32, 32, null);
	}
	
	public void renderTileGrid(Graphics g, int bx1, int by1){
		if(panel instanceof GridPanel){
		g.setColor(Util.transparent);
		GridPanel pan = ((GridPanel)panel);
		int tileSize = panel.getTileSize();
		//int bx = Properties.width/2-(tileSize*pan.grid.getCore()[0].length)/2-Properties.width/8;
		byte[][] core =  pan.grid.getCore();
			for(int i = 0; i < core[0].length; ++i){
				for(int j = 0; j < core[1].length; ++j){
					if(core[i][j]>0&&GridBlock.all[core[i][j]]!=null){
						if(GridBlock.all[core[i][j]].getImage()!=null){
							g.drawImage(GridBlock.all[core[i][j]].getImage(), i*tileSize+bx, j*tileSize+by, tileSize, tileSize, null);
						}
						g.setColor(Util.transparent_white);
						g.drawRect(i*tileSize+bx, j*tileSize+by, tileSize, tileSize);
						Rectangle rect = new Rectangle(i*tileSize+bx, j*tileSize+by, tileSize, tileSize);
						if(rect.contains(Display.currentScreen.mousePoint))
						g.fillRect(i*tileSize+bx, j*tileSize+by, tileSize, tileSize);
					}
				}
			}
			Bank.drawOutlineOut(g, bx, by, pan.grid.getCore()[0].length*tileSize, pan.grid.getCore()[1].length*tileSize, Color.BLACK, 3);
		}
	}

	public void moveX(int dif){
		x+=dif;
	}
	
	public void moveY(int dif){
		y+=dif;
	}

	public void refocus() {
		focus(Display.currentScreen);
	}
}
