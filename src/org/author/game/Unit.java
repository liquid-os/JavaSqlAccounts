package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Unit {
	
	private int posX, posY, width, height;
	private int health, healthMax;
	private int movespeed = 1, speed = 3;
	private Point moveEnd;
	private long lastMove = System.currentTimeMillis();

	public Unit() {
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void originDraw(Graphics g, GridPanel panel) {	
		g.setColor(Color.WHITE);
		g.fillRect((int)((posX+panel.getScrollX()) * panel.scale), (int)((posY+panel.getScrollY()) * panel.scale), (int)(getWidth() * panel.scale), (int)(getHeight() * panel.scale));
		
		if(moveEnd != null){
			g.setColor(Color.red);
			g.drawLine(posX, posY, moveEnd.x, moveEnd.y);
			g.fillRect(moveEnd.x-2, moveEnd.y-2, 4, 4);
		}
	}
	
	public void move(GridPanel panel, int destX, int destY) {
		moveEnd = new Point(destX, destY);
	}
	
	public void movementUpdate(GridPanel panel) {
		if(moveEnd!=null) {
			if(new Rectangle(posX, posY, width, height).intersects(new Rectangle(moveEnd.x - 2, moveEnd.y - 2, 4, 4))) {
				moveEnd = null;
			}else {
				if(System.currentTimeMillis() - lastMove >= 20 - movespeed) {
					if(posX + width/2 < moveEnd.x) {
						posX += speed;
					}
					if(posX + width / 2 > moveEnd.x) {
						posX -= speed;
					}
					if(posY + height < moveEnd.y) {
						posY += speed;
					}
					if(posY + height > moveEnd.y) {
						posY -= speed;
					}
					lastMove = System.currentTimeMillis();
				}
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
