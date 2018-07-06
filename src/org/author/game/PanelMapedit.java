package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.KeyEvent;

public class PanelMapedit extends GridPanel {
	
	int tile = 0;
	
	public PanelMapedit() {
		this.grid = new Grid(100, 100, this);
		for(int i = 0; i < 100; ++i) {
			for(int j = 0; j < 100; ++j) {
				grid.getCore()[i][j] = (byte) GridBlock.ground.getID();
			}
		}
	}

	public void onUpdate() {
		
	}
	
	public void releaseClick(boolean b) {
		
	}
	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_1) {
			tile = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_2) {
			tile = 2;
		}
		if(e.getKeyCode() == KeyEvent.VK_3) {
			tile = 3;
		}
		if(e.getKeyCode() == KeyEvent.VK_4) {
			tile = 4;
		}
		if(e.getKeyCode() == KeyEvent.VK_0) {
			tile = 0;
		}
	}

	public void renderScene(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, Properties.width, Properties.height);
		//grid.render(g, this, getTileSize());

		int w = this.getTileSize();
		int h = w / 2;
		for(int i = 0; i < grid.getCore()[0].length; ++i) {
			for(int j = 0; j < grid.getCore()[1].length; ++j) {
				int xmod = 0, ymod = 0;
				Image img = Grid.imgs[grid.getCore()[i][j]];
				if(j % 2 == 0) {
					xmod = (w/2);
				}else {
					ymod = -h;
				}
				Polygon poly = new Polygon();
				poly.addPoint((int)((i * w + xmod + getScrollX())*scale), (int)((j * h/2 + ymod + getScrollY()) * scale) + h / 2);
				poly.addPoint((int)((i * w + xmod + getScrollX())*scale) + w / 2, (int)((j * h/2 + ymod + getScrollY()) * scale));
				poly.addPoint((int)((i * w + xmod + getScrollX())*scale) + w, (int)((j * h/2 + ymod + getScrollY()) * scale) + h / 2);
				poly.addPoint((int)((i * w + xmod + getScrollX())*scale) + w / 2, (int)((j * h/2 + ymod + getScrollY()) * scale) + h);
				g.drawImage(img, (int)((i * w + xmod + getScrollX())*scale), (int)((j * h/2 + ymod + getScrollY()) * scale), (int)(w*scale), (int)(h*scale), null);				
				if(poly.contains(mousePoint)) {
					g.setColor(Color.WHITE);
					g.drawPolygon(poly);
					if(clicking) {
						grid.getCore()[i][j] = (byte) tile;
					}
					if(rightClicking) {
						grid.getCore()[i][j] = (byte) 0;
					}
				}
			}
		}
	}
	
	public int getTileSize() {
		return 64;
	}
}
