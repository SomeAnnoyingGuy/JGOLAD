package jgolad.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import jgolad.Main;
import util.ImageUtil;
import util.IntroCrap;
import util.TinyThing;

public class MainMenu extends Screen {
	
	private BufferedImage logo;
	private TinyThing tinything = IntroCrap.getRandomTinyThing();
	private int timeThingMax = Main.FPS * 8;
	private int timeThing = 0;
	private double timeFade = timeThingMax / 4;
	private double fade = timeFade;
	private Font optionFont = new Font("Calibri", 0, 40);
	
	public MainMenu() {
		logo = ImageUtil.loadImage("/Logo.png");
	}
	
	@Override
	public void render(Graphics g) {
		int w = Main.getWidth();
		int lh = (int) ((double) logo.getHeight() * ((double) w / (double) logo.getWidth()));
		g.drawImage(logo, 0, 0, w, lh, null);
		timeThing++;
		tinything.render((Graphics2D) g,lh);
		g.setColor(new Color(0, 0, 0, (int) (255 * (fade / timeFade))));
		g.fillRect(550, lh+50, 300, 300);
		if (timeThing >= timeThingMax) {
			timeThing = 0;
			tinything = IntroCrap.getRandomTinyThing();
			fade = timeFade;
		} else if (timeThing >= timeThingMax - timeFade || timeThing - timeFade <= 0) {
			if (fade < timeFade) {
				fade++;
			}
		} else {
			if (fade > 0) {
				fade--;
			}
		}
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(optionFont);
		g.drawString("[1] Local Game (WIP)", 100, lh+100);
		g.drawString("[2] Sandbox", 125, lh+150);
		g.drawString("[3] Extras", 150, lh+200);
	}

}
