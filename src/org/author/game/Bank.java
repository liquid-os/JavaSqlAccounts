package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Bank {
	
	//static MainServer mainserver = null;
	static MainServer server = null;
	static GameClient client = null;
	
	public static String path = getPath();
	public static File accFile = new File(Bank.path+"server_accounts.png");
	
	public static String getPath(){
		if(Util.autoServer){
			try {
				return Bank.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("cardgameserver.jar", "")+"/"+Properties.gameName+"/";
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} 
		}else{
			return System.getenv("APPDATA")+"/"+Properties.gameName+"/";
		}
		return System.getenv("APPDATA")+"/"+Properties.gameName+"/";
	}
	
	static MainServer getServer(){
		return server;
	}
	
	public static Image grass = Filestream.getImageFromPath("grass.png");
	public static Image dirt = Filestream.getImageFromPath("dirt.png");
	public static Image house = Filestream.getImageFromPath("house.png");
	public static Image scroll = Filestream.getImageFromPath("vertscroll.png");
	public static Image tree = Filestream.getImageFromPath("tree.png");
	public static Image tree1 = Filestream.getImageFromPath("tree1.png");
	public static Image shrub = Filestream.getImageFromPath("shrub.png");
	public static Image lotus = Filestream.getImageFromPath("lotus.png");
	public static Image button = Filestream.getImageFromPath("button.png");
	public static Image buttonHover = Filestream.getImageFromPath("buttonHover.png");
	public static Image texture_tweed = Filestream.getImageFromPath("texture_tweed.png");
	public static Image texture_bark = Filestream.getImageFromPath("texture_bark.png");
	public static Image gradient = Filestream.getImageFromPath("gradient.png");
	public static Image serverIco = Filestream.getImageFromPath("server.png");
	public static Image serverIcoHover = Filestream.getImageFromPath("serverHover.png");
	public static Image searchbar = Filestream.getImageFromPath("searchbar.png");
	public static Image mossbrick = Filestream.getImageFromPath("mossbricks.png");
	public static Image bg = Filestream.getImageFromPath("bg.png");
	
	public static void drawOutline(Graphics g, int x, int y, int w, int h, Color c, int size){
		g.setColor(c);
		g.fillRect(x, y, w, size);
		g.fillRect(x, y, size, h);
		g.fillRect(x+w-size, y, size, h);
		g.fillRect(x, y+h-size, w, size);
	}
	
	public static void drawOutlineMid(Graphics g, int x, int y, int w, int h, Color c, int size){
		g.setColor(c);
		g.fillRect(x-size/2, y, w, size);
		g.fillRect(x-size/2, y, size, h);
		g.fillRect(x+w-size/2, y, size, h);
		g.fillRect(x, y+h-size/2, w, size);
	}
	
	public static void drawOutlineOut(Graphics g, int x, int y, int w, int h, Color c, int size){
		g.setColor(c);
		g.fillRect(x-size, y-size, w+size, size);
		g.fillRect(x-size, y-size, size, h+size);
		g.fillRect(x+w, y-size, size, h+size);
		g.fillRect(x-size, y+h, w+size*2, size);
	}
	
	public static void drawOvalOutlineOut(Graphics g, int x, int y, int w, int h, Color c, int size){
		g.setColor(c);
		for(int i = 0; i < size; ++i){
			g.drawOval(x-i, y-i, w+i*2, h+i*2);
		}
	}
	
	public static BufferedImage[] getSpriteSheet(String sheet, int size){
		if(!Bank.isServer()){
			BufferedImage bi = (BufferedImage) Filestream.getImageFromPath(sheet);
			int x = bi.getWidth() / size, y = bi.getHeight() / size;
			BufferedImage[] imgs = new BufferedImage[x * y];
			for (int i = 0; i < y; i++){
			    for (int j = 0; j < x; j++){
			    	imgs[(i * x) + j] = (bi).getSubimage(j*size,i*size,size,size);
			    }
			}
			return imgs;
		}
		return null;
	}
	
	static BufferedImage cropImage(BufferedImage src, Rectangle rect) {
	      BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
	      return dest; 
	}
	
	public static String getLevelData(String lvl){
		InputStream is = Bank.class.getResourceAsStream("levels/"+lvl+Properties.fileExt); 
		return convertStreamToString(is);
	}
	
	public static String[] getBio(String str){
		InputStream is = Bank.class.getResourceAsStream(str); 
		String[] s = convertStreamToStringArray(is);
		return s;
	}
	
	public static String getRawLevelData(String lvl){
		return Bank.getRawdirDataLine(lvl);
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    Scanner s = new Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	/**
	 * Reads an IS to a 1D string array with 30 or less pieces
	 * @param is
	 * @return
	 */
	static String[] convertStreamToStringArray(java.io.InputStream is) {
	    Scanner s = new Scanner(is).useDelimiter("\\A");
	    int index = 0;
	    String[] ret = new String[30];
	    while(s.hasNextLine()){
	    	ret[index] = s.nextLine();
	    	++index;
	    }
	    return ret;
	}
	
	public static boolean hasLockVal(String f, int index){
		return getRawdirDataLine(path+"data/"+f).split("0")[index].contains("1");
	}
	
	public static boolean[] produceLockvalArray(String f){
		String[] sa = getRawdirDataLine(path+"data/"+f).split("0");
		boolean[] ba = new boolean[sa.length];
		for(int i = 0; i < sa.length; ++i){
			if(sa[i].contains("1"))ba[i]=true;
		}
		return ba;
	}
	
	public static boolean setLockVal(String p, int index, boolean state){
		boolean ret = false;
		StringBuilder sb = new StringBuilder();
		String s = getRawdirDataLine(Bank.path+"data/"+p);
		String[] ns = s.split("0");
		if(ns[index].equals("1"))ret=true;
		if(state){
			ns[index] = "1";
		}else{
			ns[index] = (""+(2+Util.rand.nextInt(8)));
		}
		for(int i = 0; i < ns.length; ++i){
			sb.append(ns[i]+"0");
		}
		setContentsRawdir(Bank.path+"data/"+p, sb.toString());
		return ret;
	}
	
	public static void createLockFile(String s1){
		String s = "";
		for(int i = 0; i < 255; ++i){
			s+=((2+Util.rand.nextInt(8))+"0");
		}
		Bank.setContentsRawdir(Bank.path+s1, s);
	}
	
	public static void init(){
		//CardList.initLists();
		//CardList.flushCards();
		int count = 0;
		//Util.initResourcepack();
		new File(path).mkdirs();
		File wFolder = new File(path+"maps");
		File dFolder = new File(path+"data");
		File tFolder = new File(path+"plugins");
		File rFolder = new File(path+"resourcepacks");
		File serverFolder = new File(path+"server_patches");
		File serverPropertiesFile = new File(path+"server.properties");
		wFolder.mkdirs();
		dFolder.mkdirs();
		tFolder.mkdirs();
		rFolder.mkdirs();
		serverFolder.mkdirs();
		File core = new File(path+"data/core"+Properties.fileExt);
		File keybinds = new File(path+"data/keybinds"+Properties.fileExt);
		File settings = new File(path+"data/settings"+Properties.fileExt);
		File realmlist = new File(path+"data/realmlist"+Properties.fileExt);
		try {
			if(keybinds.createNewFile()){}
			if(core.createNewFile()){}
			if(settings.createNewFile()){}
			accFile.createNewFile();
			if(realmlist.createNewFile()){
				Bank.setContentsRawdir(realmlist.getPath(), "logon.talesofterroth.com");
			}
			if(serverPropertiesFile.createNewFile()){
				Analysis.setKey(serverPropertiesFile, "ip","0.0.0.0");
				Analysis.setKey(serverPropertiesFile, "min_players","2");
				Analysis.setKey(serverPropertiesFile, "max_players","2");
				Analysis.setKey(serverPropertiesFile, "maxTurnTime","120000");
				Analysis.setKey(serverPropertiesFile, "maxUnits","9");
				Analysis.setKey(serverPropertiesFile, "autostartPlayers","2");
				Analysis.setKey(serverPropertiesFile, "localMapID","-1");
				Analysis.setKey(serverPropertiesFile, "gameEvents","1");
				Analysis.setKey(serverPropertiesFile, "maxRooms","10");
				Analysis.setKey(serverPropertiesFile, "gameVersion","0");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static BufferedImage rotate(BufferedImage img, double angle)
	{
		if(Bank.isServer() || Util.autoServer){
			return null;
		}
	    double sin = Math.abs(Math.sin(Math.toRadians(angle))),
	           cos = Math.abs(Math.cos(Math.toRadians(angle)));

	    int w = img.getWidth(null), h = img.getHeight(null);

	    int neww = (int) Math.floor(w*cos + h*sin),
	        newh = (int) Math.floor(h*cos + w*sin);

	    BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = bimg.createGraphics();

	    g.translate((neww-w)/2, (newh-h)/2);
	    g.rotate(Math.toRadians(angle), w/2, h/2);
	    g.drawRenderedImage(img, null);
	    g.dispose();

	    return bimg;
	}
	
	 public static BufferedImage flip(BufferedImage bi) {
		 if(Bank.isServer() || Util.autoServer){
				return null;
			}
	        BufferedImage flipped = new BufferedImage(
	                bi.getWidth(),
	                bi.getHeight(),
	                BufferedImage.TYPE_INT_ARGB);
	        AffineTransform tran = AffineTransform.getTranslateInstance(bi.getWidth(), 0);
	        AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
	        tran.concatenate(flip);

	        Graphics2D g = flipped.createGraphics();
	        g.setTransform(tran);
	        g.drawImage(bi, 0, 0, null);
	        g.dispose();
	       return flipped;
	   }
	 
		public static String getRawdirDataLine(String f){
			File file = new File(f);
			try {
				file.createNewFile();
				FileReader w = new FileReader(file);
				BufferedReader bw = new BufferedReader(w);
				return bw.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static String readAll(File file){
			String str = "";
			try {
				file.createNewFile();
				FileReader w = new FileReader(file);
				BufferedReader br = new BufferedReader(w);
				String line = br.readLine();
			    while (line != null) {
			        str+=line;
			        line = br.readLine();
			    }

			    br.close();			
			    } catch (IOException e) {
				e.printStackTrace();
			}
			return str;
		}
		
		public static void setContents(String file, String n){
			File f = new File(path+file);
			try {
				f.createNewFile();
				FileWriter w = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(w);
				bw.flush();
				bw.write(n);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public static void setContentsRawdir(String file, String n){
			File f = new File(file);
			try {
				f.createNewFile();
				FileWriter w = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(w);
				bw.flush();
				bw.write(n);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static boolean isServer() {
			return server!=null;
		}

		public static boolean isClient() {
			return client!=null;
		}
}
