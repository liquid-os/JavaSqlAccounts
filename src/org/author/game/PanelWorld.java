package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PanelWorld extends GridPanel{
	ArrayList<MapObject> objs = new ArrayList<MapObject>();
	Player player = new Player();

	public PanelWorld() {
		this.grid = new Grid(100, 100, this);
		Grid g = this.grid;
		for(int i = 0; i < 100; ++i) {
			for(int j = 0; j < 100; ++j) {
				g.getCore()[i][j] = (byte) (rand.nextInt(5) == 1? GridBlock.grass.getID() : GridBlock.ground.getID());
				if(rand.nextInt(10) == 1) {
					g.getHeightmap()[i][j] = 1;
				}
			}
		}
		this.renderForeground = false;
		for(int i = 0; i < 50; ++i) {
			objs.add(new MapObject(Bank.tree, rand.nextInt(g.getCore()[0].length), rand.nextInt(g.getCore()[1].length), 2, 3));
		}
		for(int i = 0; i < 30; ++i) {
			objs.add(new MapObject(Bank.shrub, rand.nextInt(g.getCore()[0].length), rand.nextInt(g.getCore()[1].length), 1, 1));
		}
		for(int i = 0; i < 12; ++i) {
			objs.add(new MapObject(Bank.lotus, rand.nextInt(g.getCore()[0].length), rand.nextInt(g.getCore()[1].length), 1, 1));
		}
		player.setPosX(100);
		player.setPosY(100);
		player.setWidth(32);
		player.setHeight(50);
		objects.add(player);
	}
	
	public void releaseClick(boolean b) {
		if(!b) {
			player.move(this, (int)((mousePoint.x - this.getScrollX()) / scale), (int)((mousePoint.y - this.getScrollY()) / scale));
		}
	}

	public void onUpdate() {
		if(mousePoint.x < 10) {
			scrollX(1);
		}
		if(mousePoint.x >= Properties.width-30) {
			scrollX(-1);
		}
		if(mousePoint.y < 50) {
			scrollY(1);
		}
		if(mousePoint.y >= Properties.height-50) {
			scrollY(-1);
		}
		
		for(Unit u : objects) {
			u.movementUpdate(this);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_S) {
			Util.autoServer = true;
			Display.currentScreen = new PanelServerconsole();
			Bank.server = new MainServer();
			Bank.server.start();
		}
	}

	public void renderScene(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, Properties.width, Properties.height);
		//grid.render(g, this, getTileSize());

		grid.renderIsometric(g, this, getTileSize());
		for(MapObject obj : objs) {
			obj.render(g, this);
		}
		
		g.drawImage(Bank.scroll, 10, 10, (int)(110 * scale), (int)(180 * scale), null);
	}
	
	public int getTileSize() {
		return 64;
	}
}
