package org.author.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JFileChooser;

public class PanelServerconsole extends PanelBase{
	
	public PanelServerconsole(){
	}
	
	int MAX_LOG_SIZE = 30;

	public void onUpdate() {
	}

	public void drawScreen(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Properties.width, Properties.height);
		g.setColor(Color.WHITE);
		g.drawLine(0, 80, Properties.width, 80);
		g.setFont(Util.cooldownBold);
		g.drawString("Server Console", 20, 50);
		g.setColor(Color.GREEN);
		g.setFont(Util.descFont);
		if(Bank.server.getLog().size() > MAX_LOG_SIZE)Bank.server.getLog().remove(0);
		for(int i = 0; i < Bank.server.getLog().size(); ++i){
			g.drawString(Bank.server.getLog().get(i), 10, 100+i*20);
		}
	}
	
	public void keyReleased(KeyEvent e){
		/*if(e.getKeyCode() == KeyEvent.VK_L){
			JFileChooser fc = new JFileChooser(Bank.path+"plugins/");
			fc.showOpenDialog(null);
			File file = fc.getSelectedFile();
			Bank.server.loadPlugin(file, file.getName());
		}*/
	}
}
