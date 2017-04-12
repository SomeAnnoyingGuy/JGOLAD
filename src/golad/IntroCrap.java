package golad;

public class IntroCrap {
	private static TinyThing[] things = new TinyThing[]{
		TinyThing.blinker,TinyThing.beacon,TinyThing.toad,TinyThing.block,TinyThing.beehive,TinyThing.loaf,TinyThing.boat,TinyThing.tub,TinyThing.eater1,TinyThing.eater5,TinyThing.tieBoat,TinyThing.tieShip
	};
	
	public static TinyThing getRandomTinyThing(){
		return things[MathUtil.randInt(0, things.length-1)];
	}
}
