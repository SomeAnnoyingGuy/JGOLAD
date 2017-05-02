package jgolad.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import jgolad.Main;

public class SettingsMenu extends Screen {
	
	private int scroll = 0;
	private Font optionFont = new Font("Calibri", 0, 40);
	
	@Override
	public void render(Graphics g) {
		int w = Main.getWidth();
		int h = Main.getHeight();
		g.setColor(new Color(0,0,255));
		g.setFont(optionFont);
		g.drawString("Settings", w/2-100, 50-scroll);
		int line = 1;
		
	}
}
