package jgolad;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Settings {
	
	private HashMap<String,Integer> Controls = new HashMap<String,Integer>();
	
	public Settings() {
		Controls = getDefaultControls();
	}
	
	
	public static HashMap<String,Integer> getDefaultControls() {
		HashMap<String,Integer> defaultControls = new  HashMap<String,Integer>();
		defaultControls.put("MENU_1", KeyEvent.VK_1);
		defaultControls.put("MENU_2", KeyEvent.VK_2);
		defaultControls.put("MENU_3", KeyEvent.VK_3);
		defaultControls.put("MENU_4", KeyEvent.VK_4);
		
		defaultControls.put("Space", KeyEvent.VK_SPACE);
		defaultControls.put("Esc", KeyEvent.VK_ESCAPE);
		
		defaultControls.put("tglPred", KeyEvent.VK_P);
		defaultControls.put("SaveImg", KeyEvent.VK_S);
		
		return defaultControls;
	}
	
	public void setKey(String name, int KeyCode) {
		if (Controls.containsKey(name)) {
			Controls.put(name, KeyCode);
		}
	}
	
	public int getKey(String name) {
		return Controls.get(name);
	}
}
