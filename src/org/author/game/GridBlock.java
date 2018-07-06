package org.author.game;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GridBlock {
	
	private byte id = 0, shape = 0;
	private boolean psolid = false, osolid, canBreak = false;
	static GridBlock[] all = new GridBlock[200];
	private Image image;
	private String name;
	private Color color = Color.WHITE;
	
	public static final GridBlock air = new GridBlock("Air", 0, null, null).setSolid(false);
	public static final GridBlock ground = new GridBlock("Dirt", 1, Color.yellow, Bank.dirt).setSolid(true);
	public static final GridBlock water = new GridBlock("Water", 2, Color.BLUE, Bank.dirt).setSolid(true);
	public static final GridBlock grass = new GridBlock("Grass", 3, Color.GREEN, Bank.grass).setSolid(true);
	
	
	public GridBlock(String name, int id, Color c, Image img){
		this.id = (byte) id;
		this.setName(name);
		this.color = c;
		all[id] = this;
		Grid.imgs[id] = img;
	}
	
	public Image getImage(){
		return image;
	}
	
	public GridBlock setImage(BufferedImage img){
		this.image = img;
		return this;
	}

	private GridBlock setPlayerSolid(boolean f){
		psolid = f;
		return this;
	}
	
	private GridBlock setObjSolid(boolean f){
		osolid = f;
		return this;
	}
	
	private GridBlock setSolid(boolean f){
		psolid = f;
		osolid = f;
		return this;
	}
	
	public boolean playerSolid(){
		return psolid;
	}
	
	public boolean objSolid(){
		return osolid;
	}
	
	public int getID(){
		return this.id;
	}

	public void onUpdate(Grid grid, int x, int i) {
	}

	public boolean getBreakable() {
		return canBreak;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getShape() {
		return shape;
	}

	public GridBlock setShape(byte i) {
		this.shape = i;
		return this;
	}

	public Color getColor() {
		return color;
	}
}
