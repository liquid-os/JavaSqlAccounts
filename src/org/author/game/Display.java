package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Display extends JPanel implements Runnable {

	public static PanelBase currentScreen = null;
	public Thread mainThread;
	static boolean inGUI = false, hasLoadedPanel = false;
	public int fps = Properties.framerate;
	public static Frame frame;
	static boolean reload = false, running = true;
	static Camera cam = new Camera();

	public Display(Frame f) {
		frame = f;
		mainThread = new Thread(this);
		mainThread.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Properties.width = e.getComponent().getWidth();
				Properties.height = e.getComponent().getHeight();
				if(currentScreen != null) {
					currentScreen.scale = (Properties.width / Properties.baseWidth) * (Properties.height / Properties.baseHeight);
					//currentScreen.rescale();
				}
			}
		});
		if (!hasLoadedPanel && Properties.startPanel != null) {
			currentScreen = Properties.startPanel;
			hasLoadedPanel = true;
			setBackground(Color.BLACK);
			cam.focus(currentScreen);
		} else {
			setBackground(Color.BLACK);
		}
		f.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (currentScreen.guis.size() > 0 && currentScreen.selGui != -1)
					currentScreen.guis.get(currentScreen.selGui).keyPressed(e);
				else
					currentScreen.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					currentScreen.selGui = -1;
			}

			public void keyReleased(KeyEvent e) {
				if (currentScreen.guis.size() > 0 && currentScreen.selGui != -1)
					currentScreen.guis.get(currentScreen.selGui).keyReleased(e);
				else
					currentScreen.keyReleased(e);
			}
		});
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
				boolean hitGui = false;
				for (int i = 0; i < currentScreen.guis.size(); ++i) {
					if (currentScreen.guis.get(i) != null) {
						if (currentScreen.guis.get(i).getRect().contains(e.getPoint())) {
							currentScreen.selGui = i;
							hitGui = true;
						}
					}
				}
				if (!hitGui)
					currentScreen.selGui = -1;
				if (currentScreen.guis.size() > 0 && currentScreen.selGui > -1) {
				}
				if (e.getButton() == 1)
					currentScreen.clicking = true;
				if (e.getButton() == 2) {
					currentScreen.midClick();
					currentScreen.midclicking = true;
				}
				if (e.getButton() == 3)
					currentScreen.rightClicking = true;
			}

			public void mouseReleased(MouseEvent e) {
				boolean hitGui = false;
				for (int i = 0; i < currentScreen.guis.size(); ++i) {
					if (currentScreen.guis.get(i) != null) {
						if (currentScreen.guis.get(i).getRect().contains(e.getPoint())) {
							hitGui = true;
						}
					}
				}
				if (e.getButton() == 1)
					currentScreen.releaseClick(true);
				if (e.getButton() == 3)
					currentScreen.releaseClick(false);
				if (e.getButton() == 1)
					currentScreen.clicking = false;
				if (e.getButton() == 2)
					currentScreen.midclicking = false;
				if (e.getButton() == 3)
					currentScreen.rightClicking = false;
			}

			public void mouseClicked(MouseEvent e) {
				/*
				 * boolean hitGui = false; for(int i = 0; i <
				 * currentScreen.guis.size(); ++i){
				 * if(currentScreen.guis.get(i).getRect().contains(e.getPoint())
				 * ){ currentScreen.selGui = i; hitGui = true; } } //if(!hitGui)
				 */
				currentScreen.selGui = -1;
				if (e.getButton() == 1)
					currentScreen.click(true);
				if (e.getButton() == 3)
					currentScreen.click(false);
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		});
	}

	public static void reloadBackground() {
		reload = true;
	}

	public void run() {
		/*
		 * long lastTime = System.nanoTime(); double amountOfTicks = fps; double
		 * ns = 1000000000 / amountOfTicks; double delta = 0; long timer =
		 * System.currentTimeMillis(); int updates = 0; int frames = 0;
		 * while(running){ long now = System.nanoTime(); delta += (now -
		 * lastTime) / ns; lastTime = now; while(delta >= 1){
		 */
		while (running) {
			PointerInfo a = MouseInfo.getPointerInfo();
			if (currentScreen != null) {
				if (a.getLocation() != null) {
					currentScreen.mousePoint = new Point(a.getLocation());
					SwingUtilities.convertPointFromScreen(currentScreen.mousePoint, this);
				} else {
					currentScreen.mousePoint = new Point(0, 0);
					SwingUtilities.convertPointFromScreen(currentScreen.mousePoint, this);
				}
			}
			if (currentScreen != null)
				currentScreen.update(1);
			repaint();

			/*
			 * updates++; delta--; } repaint(); //this.paintImmediately(0, 0,
			 * Properties.width, Properties.height); frames++;
			 * 
			 * if(System.currentTimeMillis() - timer > 1000){ timer += 1000;
			 * //frame.setTitle("FPS: " + frames + " TICKS: " + updates); frames
			 * = 0; updates = 0; }
			 */
			if (Bank.isServer()) {
				if(!Bank.server.rooms.isEmpty()){
					for(GameServer room : Bank.server.rooms){
					}
				}
			}
		}
	}
	/*
	 * while(true) { PointerInfo a = MouseInfo.getPointerInfo();
	 * currentScreen.mousePoint = new Point(a.getLocation());
	 * SwingUtilities.convertPointFromScreen(currentScreen.mousePoint, this);
	 * if(currentScreen!=null) currentScreen.update(); repaint(); if(reload){
	 * setBackground(currentScreen.background); reload = false; } if(!inGUI) {
	 * long time = System.currentTimeMillis(); time = (1000 / fps) -
	 * (System.currentTimeMillis() - time);
	 * 
	 * if (time > 0) { try { Thread.sleep(time); } catch(Exception e){} }
	 * 
	 * if(currentScreen!=null) currentScreen.update(); } }
	 */

	/*
	 * public void gameLoop() { long lastLoopTime = System.nanoTime(); final int
	 * TARGET_FPS = 60; final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	 * 
	 * // keep looping round til the game ends while (gameRunning) { // work out
	 * how long its been since the last update, this // will be used to
	 * calculate how far the entities should // move this loop long now =
	 * System.nanoTime(); long updateLength = now - lastLoopTime; lastLoopTime =
	 * now; double delta = updateLength / ((double)OPTIMAL_TIME);
	 * 
	 * // update the frame counter lastFpsTime += updateLength; fps++;
	 * 
	 * // update our FPS counter if a second has passed since // we last
	 * recorded if (lastFpsTime >= 1000000000) {
	 * System.out.println("(FPS: "+fps+")"); lastFpsTime = 0; fps = 0; }
	 * 
	 * // update the game logic currentScreen.update();
	 * 
	 * // draw everyting repaint();
	 * 
	 * // we want each frame to take 10 milliseconds, to do this // we've
	 * recorded when we started the frame. We add 10 milliseconds // to this and
	 * then factor in the current time to give // us our final value to wait for
	 * // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
	 * try{Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000
	 * )}; } }
	 * 
	 * private void doGameUpdates(double delta) { for (int i = 0; i <
	 * stuff.size(); i++) { // all time-related values must be multiplied by
	 * delta! Stuff s = stuff.get(i); s.velocity += Gravity.VELOCITY * delta;
	 * s.position += s.velocity * delta;
	 * 
	 * // stuff that isn't time-related doesn't care about delta... if
	 * (s.velocity >= 1000) { s.color = Color.RED; } else { s.color =
	 * Color.BLUE; } } }
	 */

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (true) {
			cam.displayCamera(g);
		}
	}
}
