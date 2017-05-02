package jgolad.screens;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Screen {
	
	public abstract void render(Graphics g);
	
	public void onMousePress(MouseEvent e) {
		
	}
	
	public void onKeyPress(KeyEvent e) {
		
	}
}
