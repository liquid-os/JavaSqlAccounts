package org.author.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class MapObject {
	
	private Image sprite = null;
	private int posX, posY, width, height;

	public MapObject(int x, int y) {
		this.posX = x;
		this.posY = y;
	}
	
	public MapObject(Image s, int x, int y) {
		this.posX = x;
		this.posY = y;
		this.sprite = s;
	}
	
	public MapObject(Image s, int x, int y, int w, int h) {
		this.posX = x;
		this.posY = y;
		this.width = w;
		this.height = h;
		this.sprite = s;
	}
	
	public void render(Graphics g, GridPanel panel) {
		int ts = panel.getTileSize();
		Point pos = panel.getIsometricTilePosition(posX, posY, ts);
		g.drawImage(sprite, (int)((panel.getScrollX() + pos.x - ((width * ts) / 2))*panel.scale), (int)((panel.getScrollY() + pos.y - (height * ts))*panel.scale), (int)((width * ts)*panel.scale), (int)((height * ts)*panel.scale), null);
	}
}
