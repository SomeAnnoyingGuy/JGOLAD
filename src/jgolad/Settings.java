package jgolad;

import java.awt.event.KeyEvent;

public class Settings {

	public int MENU_1;
	public int MENU_2;
	public int MENU_3;
	public int MENU_4;

	public int tglPred;
	public int SaveImg;
	
	public Settings() {
		setDefaultControls();
	}
	
	public void setDefaultControls() {
		MENU_1 = KeyEvent.VK_1;
		MENU_2 = KeyEvent.VK_2;
		MENU_3 = KeyEvent.VK_3;
		MENU_4 = KeyEvent.VK_4;
		
		//("Space", KeyEvent.VK_SPACE);
		//("Esc", KeyEvent.VK_ESCAPE);
		
		tglPred = KeyEvent.VK_P;
		SaveImg = KeyEvent.VK_S;
		
		return;
	}
}
