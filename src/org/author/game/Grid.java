package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Grid {
	public static int SMALL = 0, MEDIUM = 1, LARGE = 2, ENORMOUS = 3, MAX = 4;
	private int tileSize = 5;
	private byte[][] core;
	private byte[][] heightmap;
	private int displayX = 800, displayY = 600;
	GridPanel p;
	static Image[] imgs = new Image[250];
	public Grid(int size, GridPanel p){
		this.p = p;
		int gridScale = size == 0 ? 256 : size == 1 ? 512 : size == 2 ? 768 : size == 3 ? 1024 : size == 4 ? Integer.MAX_VALUE : size;
		setCore(new byte[gridScale][gridScale]);
	}
	
	public int getTileSize(GridPanel p) {
		return (int) (p.scale * tileSize);
	}
	
	public Grid(int w, int h, GridPanel p){
		System.out.println("> PR-PROGRESS-LOG: Constructing grid of "+w+"x"+h);
		setCore(new byte[w][h]);
		setHeightmap(new byte[w][h]);
		core[0] = new byte[w];
		core[1] = new byte[h];
		heightmap[0] = new byte[w];
		heightmap[1] = new byte[h];
		//renderview = new byte[Properties.width/tileSize+10+Properties.renderdist][Properties.height/tileSize+10+Properties.renderdist];
		//renderview[0] = new byte[Properties.width/tileSize+10+Properties.renderdist];
		//renderview[1] = new byte[Properties.height/tileSize+10+Properties.renderdist];
		System.out.println("> PR-PROGRESS-LOG: Construction grid of "+core[0].length+"x"+core[1].length+" completed");
		this.p = p;
	}
	
	public void setTileID(int x, int y, int i){
		if(x>=0&&x<getCore()[0].length&&y>=0&&y<getCore()[1].length)getCore()[x][y] = (byte)i;
	}
	
	public int getTileID(int x, int y){
		if(x>=0&&x<getCore()[0].length&&y>=0&&y<getCore()[1].length){
			return getCore()[(x)][(y)];
		}else return 0;
	}
	
	public void setGRTileID(int px, int py, byte tileID){
		int x = px/tileSize, y = py/tileSize;
		if(x>0&&x<getCore()[0].length&&y>0&&y<getCore()[1].length)getCore()[x][y] = tileID;
	}
	
	public int getGRTileID(int px, int py){
		int x = px/tileSize, y = py/tileSize;
		if(x>=0&&x/tileSize<getCore()[0].length&&y>=0&&y/tileSize<getCore()[1].length){
			return getCore()[x][y];
		}else return 0;
	}
	
	/*public int getTileID(int x, int y){
		if(x+p.scrollX>0&&x+p.scrollX<getCore()[0].length*tileSize&&y+p.scrollY>0&&y+p.scrollY<getCore()[1].length*tileSize)return getCore()[(x+p.scrollX)/tileSize][(y+p.scrollY)/tileSize];else return 0;
	}*/
	
	/*public int getTileID(int x, int y){
		if(x>=0&&x/tileSize<getCore()[0].length&&y>=0&&y/tileSize<getCore()[1].length){
			return getCore()[(x)/tileSize][(y)/tileSize];
		}else return 0;
	}
	
	public byte getTileIDWithScroll(int x, int y) {
		if((x+p.scrollX)/tileSize < 0 || (y+p.scrollY)/tileSize < 0)return 0;else
		return (x<0||x>core[0].length*tileSize||y<0||y>core[1].length*tileSize)?0:getCore()[(x+p.scrollX)/tileSize][(y+p.scrollY)/tileSize];
	}
	
	public int getGridRelativeTileID(int x, int y){
		if(x+p.scrollX/tileSize>0&&x+p.scrollX/tileSize<getCore()[0].length&&y+p.scrollY/tileSize>0&&y+p.scrollY/tileSize<getCore()[1].length)return getCore()[x+p.scrollX/tileSize][y+p.scrollY/tileSize];else return 0;
	}
	
	public void setTileID(int x, int y, byte tileID){
		if(x>0&&x<getCore()[0].length*tileSize&&y>0&&y<getCore()[1].length*tileSize)getCore()[x/getTileSize()][y/getTileSize()] = tileID;
	}
	
	public void setGridRelativeTileIDWithScroll(int x, int y, byte tileID){
		if(x+p.scrollX/tileSize-p.scrollX%tileSize>0&&x+p.scrollX/tileSize-p.scrollX%tileSize<getCore()[0].length&&y+p.scrollY/tileSize-p.scrollY%tileSize>0&&y+p.scrollX/tileSize-p.scrollY%tileSize<getCore()[1].length)getCore()[x+p.scrollX/tileSize-p.scrollX%tileSize][y+p.scrollY/tileSize-p.scrollY%tileSize] = tileID;
	}
	
	public void setGridRelativeTileID(int x, int y, byte tileID){
		if(x>0&&x<getCore()[0].length&&y>0&&y<getCore()[1].length)getCore()[x][y] = tileID;
	}
	
	public void setGridRelativeTileIDNotify(int x, int y, byte tileID){
		if(x>0&&x<getCore()[0].length&&y>0&&y<getCore()[1].length){getCore()[x][y] = tileID;
		this.updateSurroundingGridRelative(x, y);}
	}*/
	
	public Rectangle formTileBox(int x, int y, int xTiles, int yTiles){
		Rectangle ret = new Rectangle();
		boolean b = false;
		//byte shape = 0;
		//if(GridBlock.all[this.getTileID(x, y)].playerSolid())
		for(int i = 0; i < xTiles; ++i){
			for(int j = 0; j < yTiles; ++j){
				if(getTileID(x, y) > 0 && GridBlock.all[this.getTileID(x+i, y+j)].playerSolid())b = true;
				//shape = GridBlock.all[this.getTileID(x+i*tileSize, y+j*tileSize)].getShape();
			}
		}
		if(b)
		ret.setBounds(x*tileSize, y*tileSize, tileSize*xTiles, tileSize*yTiles);
		return ret;
	}
	
	public Rectangle formGRTileBox(int x, int y, int xTiles, int yTiles){
		Rectangle ret = new Rectangle();
		boolean b = false;
		//byte shape = 0;
		//if(GridBlock.all[this.getTileID(x, y)].playerSolid())
		for(int i = 0; i < xTiles; ++i){
			for(int j = 0; j < yTiles; ++j){
				if(getTileID(x/tileSize, y/tileSize) > 0 && GridBlock.all[this.getTileID((x+i*tileSize)/tileSize, (y+j*tileSize)/tileSize)].playerSolid())b = true;
				//shape = GridBlock.all[this.getTileID(x+i*tileSize, y+j*tileSize)].getShape();
			}
		}
		if(b)
		ret.setBounds(x-x%tileSize, y-y%tileSize, tileSize*xTiles, tileSize*yTiles);
		//Display.frame.getGraphics().setColor(Color.GREEN);
		//Display.frame.getGraphics().drawRect(x-x%tileSize-Display.cam.x, y-y%tileSize-Display.cam.y, tileSize*xTiles, tileSize*yTiles);
		return ret;
	}
	
	public void render(Graphics g, GridPanel p, int ts){
		for(int i = 0; i < displayX; ++i){
			for(int j = 0; j < displayY; ++j){
				if(i > 0 && i < this.core[0].length && j > 0 && j < this.core[1].length){
					if(getCore()[i][j]>0&&getCore()[i][j]!=26){
						g.setColor(GridBlock.all[getCore()[i][j]].getColor());
						g.fillRect(i*ts, j*ts, ts, ts);
					}
				}
			}
		}
	}
	
	public void renderIsometric(Graphics g, GridPanel p, int ts){
		int w = p.getTileSize();
		int h = w / 2;
		for(int i = 0; i < getCore()[0].length; ++i) {
			for(int j = 0; j < getCore()[1].length; ++j) {
				int xmod = 0, ymod = 0;
				Image img = Grid.imgs[getCore()[i][j]];
				if(j % 2 == 0) {
					xmod = (w/2);
				}else {
					ymod = -h;
				}
				byte th = getHeightOfTerrain(i, j);
				if(th == 0) {
					g.drawImage(img, (int)((i * w + xmod + p.getScrollX())*p.scale), (int)((j * h/2 + ymod + p.getScrollY()) * p.scale), (int)(w*p.scale), (int)(h*p.scale), null);
				}
			}
		}
		for(int i = 0; i < getCore()[0].length; ++i) {
			for(int j = 0; j < getCore()[1].length; ++j) {
				int xmod = 0, ymod = 0;
				Image img = Grid.imgs[getCore()[i][j]];
				if(j % 2 == 0) {
					xmod = (w/2);
				}else {
					ymod = -h;
				}
				byte th = getHeightOfTerrain(i, j);
				Polygon poly = new Polygon();
				if(th > 0) {
					g.setColor(Color.BLACK);
					poly.addPoint((int)((i * w + xmod + p.getScrollX())*p.scale), (int)((j * h/2 + ymod + p.getScrollY()) * p.scale) + h / 2);
					poly.addPoint((int)((i * w + xmod + p.getScrollX())*p.scale) + w / 2, (int)((j * h/2 + ymod + p.getScrollY()) * p.scale));
					poly.addPoint((int)((i * w + xmod + p.getScrollX())*p.scale) + w, (int)((j * h/2 + ymod + p.getScrollY()) * p.scale) + h / 2);
					poly.addPoint((int)((i * w + xmod + p.getScrollX())*p.scale) + w / 2, (int)((j * h/2 + ymod + p.getScrollY()) * p.scale) + h);
					g.fillPolygon(poly);
					ymod -= h / 2;
					g.fillRect((int)((i * w + xmod + p.getScrollX())*p.scale), (int)((j * h/2 + ymod + p.getScrollY() + h / 2) * p.scale), (int)((w/2)*p.scale), (int)((h/2)*p.scale));
					g.setColor(Color.DARK_GRAY);
					g.fillRect((int)((i * w + xmod + p.getScrollX()+w/2)*p.scale), (int)((j * h/2 + ymod + p.getScrollY() + h / 2) * p.scale), (int)((w/2)*p.scale), (int)((h/2)*p.scale));
					
				}
				g.drawImage(img, (int)((i * w + xmod + p.getScrollX())*p.scale), (int)((j * h/2 + ymod + p.getScrollY()) * p.scale), (int)(w*p.scale), (int)(h*p.scale), null);
				if(getHeightOfTerrain(i, j+1) < th) {
					g.setColor(Util.transparent_buttonwhite);
					poly = new Polygon();
					poly.addPoint((int)((i * w + xmod + p.getScrollX())*p.scale), (int)((j * h/2 + ymod + p.getScrollY()) * p.scale) + h / 2);
					poly.addPoint((int)((i * w + xmod + p.getScrollX())*p.scale) + w / 2, (int)((j * h/2 + ymod + p.getScrollY()) * p.scale));
					poly.addPoint((int)((i * w + xmod + p.getScrollX())*p.scale) + w, (int)((j * h/2 + ymod + p.getScrollY()) * p.scale) + h / 2);
					poly.addPoint((int)((i * w + xmod + p.getScrollX())*p.scale) + w / 2, (int)((j * h/2 + ymod + p.getScrollY()) * p.scale) + h);
					g.fillPolygon(poly);
				}
			}
		}
	}
	
	public byte getHeightOfTerrain(int x, int y) {
		if(x >= heightmap[0].length || x < 0) {
			return 0;
		}
		if(y >= heightmap[1].length || y < 0) {
			return 0;
		}
		return heightmap[x][y];
	}
	
	public void render(Graphics g, GridPanel p, int ts, Color c){
		g.setColor(c);
		for(int i = 0; i < (Properties.width)/ts+1; ++i){
			for(int j = 0; j < (Properties.height)/ts+1; ++j){
				if(i > 0 && i < this.core[0].length && j > 0 && j < this.core[1].length){
					if(getCore()[i][j]>0&&getCore()[i][j]!=26){
						g.setColor(GridBlock.all[getCore()[i][j]].getColor());
						g.fillRect(i*ts, j*ts, ts, ts);
					}
				}
			}
		}
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public byte[][] getCore() {
		return core;
	}

	public void setCore(byte[][] core) {
		this.core = core;
	}
	
	public String save(PanelBase pan, String m){
			System.out.println("> PR-PROGRESS-LOG: Save initiated...");
			saving = true;
			
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < getCore()[0].length; ++i){
				for(int j = 0; j < getCore()[1].length; ++j){	
					if(getCore()[i][j]!=0)sb.append(i+":"+j+":"+getCore()[i][j]+(";"));
				}
			}
			String s = sb.toString();
			saving = false;
			System.out.println("> PR-PROGRESS-LOG: Save complete.");
			return (s.isEmpty()||s.equals(""))?"0":sb.toString();
	}
	
	public void load(String worldname, int x){
		File file = new File(Bank.path+"maps/"+worldname+".HB");
		FileReader fw;
		System.out.println("Attempting to load grid #"+x+" from "+Bank.path+"maps/"+worldname+".HB");
		String line = null;
		try {
			fw = new FileReader(file);
			BufferedReader bw = new BufferedReader(fw);
			line = bw.readLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		load(line, x, true);
	}
	
	public void load(String dat, int x, boolean isString){
		String[] data = null;
		String line = dat.split("%")[x];
		if(line.length() > 1){
			data = line.split(";");
			String[] stra = data[data.length-1].split(":");
			this.core = new byte[800][600];
			for(int i = 0; i < data.length; ++i){
				String[] str = data[i].split(":");
				byte tid = Byte.parseByte(str[2]);
				if(GridBlock.all[tid]==null){tid = 1;}
				this.setTileID(Integer.parseInt(str[0]), Integer.parseInt(str[1]), tid);
			}
		}
	}
	
	public void loadRaw(File file, GridPanel pan, int x){
		FileReader fw;
		String line = null;
		try {
			fw = new FileReader(file);
			BufferedReader bw = new BufferedReader(fw);
			line = bw.readLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		load(line, x, true);
	}
	
	public int getGreatest(String[] data, int x){
		int r = 0;
		for(int i = 0; i < data.length; ++i){
			String[] str = data[i].split(":");
			if(Integer.parseInt(str[x]) > r)r = Integer.parseInt(str[x]);
		}
		return r;
	}
	
	public byte[][] getHeightmap() {
		return heightmap;
	}

	public void setHeightmap(byte[][] heightmap) {
		this.heightmap = heightmap;
	}

	Random rand = new Random();
	public boolean saving = false;
}
