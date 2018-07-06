package org.author.game;

import java.awt.*;

import javax.swing.*;


public class Frame extends JFrame{
	public Display p;
	
	public Frame()
	{
		p = new Display(this);
		this.setLayout(new GridLayout(1, 1, 0, 0));
		this.setSize(Properties.width, Properties.height);
		this.setVisible(true);
		this.setEnabled(true);
		add(p);
		//this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Bank.blank, new Point(0,0), "BLANK_CURSOR"));
	}
	
	public void init(){
		Bank.init();
	}
}
