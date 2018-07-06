package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class PanelMainmenu extends PanelBase {
	
	int mapScale = 3;
	long lastRescroll = System.currentTimeMillis();
	long lastScrollTick = System.currentTimeMillis();
	int rescrollTime = 3000, scrollTickTime = 10;
	static boolean autoServer = false;
	
	int bw = Properties.width/3;
	int bh = Properties.width / 6 / 4;
	int by = (int) (Properties.height / 3 * 1.2);
	int bx = Properties.width / 20;
	
	long start = System.currentTimeMillis();
	boolean introFinished = false;
	private int maxCards = 0, currentCollection = 0;

	private GUIButton shop;
	
	public PanelMainmenu(){
		//Util.music = new ClipShell("music", "menumusic.wav");
		//Util.music.loop(-1);
		
		int spacer = (int) (bh * 1.5);
		this.renderObj = false;
		guis.add(new GUIButton(Bank.button, Bank.buttonHover, 1, bx, by, bw, bh).setText("Enter World"));
		guis.add(new GUIButton(Bank.button, Bank.buttonHover, 2, bx, by+spacer, bw, bh).setText("Select Deck"));
		guis.add(new GUIButton(Bank.button, Bank.buttonHover, 3, bx, by+spacer*2, bw, bh).setText("Deck Forge"));
		guis.add(new GUIButton(Bank.button, Bank.buttonHover, 4, bx, by+spacer*3, bw, bh).setText("Load Resource Pack"));
		int boxicosize = 85;
		guis.add(new GUIButton(Bank.serverIco, Bank.serverIcoHover, 2, 15 + 15 + 15 + 15 + boxicosize + boxicosize + boxicosize, 15, boxicosize, boxicosize).setText(""));

		/*for(int i = 0; i < Util.collection.length; ++i){
			Util.collection[i] = 1;
		}*/
		//guis.add(new GUIButton(Bank.button, Bank.buttonHover, 4, Properties.width/2-bw/2, Properties.height/10 + bw + bw, bw, bw/3).setText("View Lore"));		
	}
	
	public void buttonReact(int id){
		if(id==1) {
			Display.currentScreen = new PanelWorld();
			Display.cam.refocus();
		}
		if(id==4){
			JFileChooser fc = new JFileChooser(Bank.path+"resourcepacks/");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setAcceptAllFileFilterUsed(false);
			fc.showOpenDialog(null);
			File file = fc.getSelectedFile();
			Util.loadResourcePack(file);
		}
		if(id == 5){
			try {
				java.awt.Desktop.getDesktop().browse(new URI(Util.MANUAL_LINK));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
		if(id == 6){
			try {
				java.awt.Desktop.getDesktop().browse(new URI(Util.SITE_LINK));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	public void onUpdate() {
		if(autoServer){
			Util.music.stop();
			//Bank.server = new GameServer();
			//Bank.server.start();
			Display.currentScreen = new PanelServerconsole();
			Display.frame.setSize(800, 600);
			Display.cam.refocus();
		}	
	}
	
	public void renderForeground(Graphics g){
		if(!introFinished){
			int a = 400 - (int) ((double)((double)(System.currentTimeMillis() - start) / (double)4000) * (double)255);
			if(a < 0)a = 0; if(a > 255)a = 255;
			g.setColor(new Color(0,0,0,a));
			g.fillRect(0, 0, Properties.width, Properties.height);
			if(a <= 0)introFinished = true;
		}
	}
	
	Rectangle dd = new Rectangle(Properties.width / 2, Properties.height / 2 - Properties.height / 6, Properties.width / 3, Properties.height / 3);
	
	@Override
	public void releaseClick(boolean b) {
		super.releaseClick(b);
		if(dd.contains(getMousePoint())){
			this.buttonReact(2);
		}
	}

	
	public void drawScreen(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Properties.width, Properties.height);
		//g.drawImage(Bank.mapOriginal, Properties.width/2 - (Properties.width*mapScale)/2+scrollX, Properties.height/2 - (Properties.height*mapScale)/2+scrollY, Properties.width*mapScale, Properties.height*mapScale, null);
		int s = Properties.width / 5;
		for(int i = 0; i < Properties.width/s + 1; ++i){
			for(int j = 0; j < Properties.height/s + 1; ++j){
				g.drawImage(Bank.mossbrick, i*s, j*s, s, s, null);
			}
		}
		g.setColor(Color.DARK_GRAY);
		g.fillRect(dd.x, dd.y, dd.width, dd.height);
		Bank.drawOutlineOut(g, dd.x, dd.y, dd.width, dd.height, dd.contains(getMousePoint()) ? Color.YELLOW : Color.GREEN, 4);
		if(Util.heroImg != null){
			g.drawImage(Util.heroImg, dd.x, dd.y, dd.width, dd.height, null);
		}
		g.setFont(Util.largeNameFont);
		g.setColor(Color.BLACK);
		g.drawString(Util.deckName, dd.x + dd.width / 2 - g.getFontMetrics().stringWidth(Util.deckName) / 2, dd.y - 60);
		g.setColor(Color.WHITE);
		g.drawString(Util.deckName, dd.x + dd.width / 2 - g.getFontMetrics().stringWidth(Util.deckName) / 2 + 2, dd.y - 60 + 2);

		/*int tw = Properties.width / 15, th = Properties.height / 2;
		g.drawImage(Bank.torch, Properties.width / 4 - tw / 2 - tw, Properties.height - th, tw, th, null);
		g.drawImage(Bank.torch, Properties.width - Properties.width / 4 - tw / 2 + tw, Properties.height - th, tw, th, null);
		
		if(Util.rand.nextInt(1) == 0){
			Point xy = new Point(Properties.width / 4 - tw / 2 - tw + tw / 2 - tw / 6, Properties.height - th);
			int sz = 16 + Util.rand.nextInt(32);
			AnimatedParticle part = new AnimatedParticle(xy.x, xy.y, sz, 500 + Util.rand.nextInt(400));
			part.addFrameSet(torchset);
			int mm = Properties.width / 3 - (Util.distance(getMousePoint(), xy));
			mm/=10;
			if(mm<0)mm = 0;
			part.phys.motionX += (Util.rand.nextInt(50+mm) - Util.rand.nextInt(50+mm));
			part.phys.motionY += (Util.rand.nextInt(75+mm) - 20);
			this.particles.add(part);
		}
		if(Util.rand.nextInt(1) == 0){
			int sz = 16 + Util.rand.nextInt(32);
			Point xy = new Point(Properties.width - Properties.width / 4 - tw / 2 + tw + tw / 2 - tw / 6, Properties.height - th);
			AnimatedParticle part = new AnimatedParticle(xy.x, xy.y, sz, 500 + Util.rand.nextInt(400));
			part.addFrameSet(torchset);
			int mm = Properties.width / 3 - (Util.distance(getMousePoint(), xy));
			mm/=10;
			if(mm<0)mm = 0;
			part.phys.motionX += (Util.rand.nextInt(50+mm) - Util.rand.nextInt(50+mm));
			part.phys.motionY += (Util.rand.nextInt(75+mm) - 20);
			this.particles.add(part);
		}*/
		//g.drawImage(Bank.borderScreen, 0, 0, Properties.width, Properties.height, null);
		int ch = Properties.height / 4 * 3;
		int cw = Properties.width / 2;
		int logow = Properties.width / 5 + Properties.width / 25;
		int logoh = logow / 4 * 3;
		//g.drawImage(Bank.logo, bx + bw / 2 - logow / 2, by - logoh, logow, logoh, null);
		g.setFont(Util.dialogFont);
		String vs = Properties.gameTitle+" v"+Util.version+" (Rev "+Util.revision+")";
		String cs = "Collection "+(int)((double)currentCollection/(double)maxCards*100.0d)+"% complete. ("+currentCollection+"/"+maxCards+")";
		g.drawString(vs, Properties.width - g.getFontMetrics().stringWidth(vs) - 20, 35);
		g.setFont(Util.descFont);
		g.drawString(cs, Properties.width - g.getFontMetrics().stringWidth(cs) - 20, 60);
		
		//g.drawImage(Bank.coin, Properties.width - 80, 90, 64, 64, null);
		g.setFont(Util.cooldownBold);
		//g.drawString(""+Util.coins, Properties.width - 85 - g.getFontMetrics().stringWidth(Util.coins+""), 90 + 40);
		//g.drawImage(Bank.crest, Properties.width/2, Properties.height/2-ch/2, cw, ch, null);
		/*g.setColor(Color.LIGHT_GRAY);
		g.fillOval(Properties.width/2 + Properties.width / 6, Properties.height/2-120+300-Properties.height/4, 480, 150);
		g.setColor(Color.BLACK);
		g.drawOval(Properties.width/2 + Properties.width / 6, Properties.height/2-120+300-Properties.height/4, 480, 150);
		for(int i = 0; i < 20; ++i)
		g.drawImage(Bank.cardbackClosed, Properties.width/2 + Properties.width / 4-i*4, Properties.height/2-120+i*2-Properties.height/4+40, 240, (int)(240 * 1.28), null);
		*/
	}
}
