package golad;

import java.awt.Graphics2D;

public abstract class TinyThing {
	int tick,tickMax;
	int tickBuffer = 0;
	int tickBufferMax = Main.FPS/3;
	public TinyThing(int ticks){
		tick = 0;
		tickMax = ticks;
	}
	public void renderStart(Graphics2D g, int yOff){
		render(g,yOff);
		tickBuffer++;
		if(tickBuffer > tickBufferMax){
			tick++;
			tickBuffer = 0;
		}
		if(tick > tickMax){
			tick = 0;
		}
	}
	protected abstract void render(Graphics2D g, int yOff);
	
	public static TinyThing blinker = new TinyThingImage(2, "blinker");
	public static TinyThing beacon = new TinyThingImage(2, "beacon");
	public static TinyThing toad = new TinyThingImage(2, "toad");
	
	public static TinyThing block = new TinyThingImage(1, "block");
	public static TinyThing beehive = new TinyThingImage(1, "beehive");
	public static TinyThing loaf = new TinyThingImage(1, "loaf");
	public static TinyThing boat = new TinyThingImage(1, "boat");
	public static TinyThing tub = new TinyThingImage(1, "tub");
	
	public static TinyThing tieBoat = new TinyThingImage(1, "boattie");
	public static TinyThing tieShip = new TinyThingImage(1, "shiptie");

	public static TinyThing eater1 = new TinyThingImage(1, "eater1");
	public static TinyThing eater5 = new TinyThingImage(1, "eater5");

}
