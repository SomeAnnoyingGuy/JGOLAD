package golad;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;

public class Cellstate {
	public static HashMap<Byte,Cellstate> cellstates = new HashMap<>();
	
	private Color c;
	private byte id;
	private String name;
	private boolean alive = true;
	private int competitiveness = 10;
	private Cellstate corpse;
	private boolean dies = true;
	private boolean autoDie = false;
	private int corpseID = 0;
	private int surroundPoints = 1;
	
	private static byte idCounter = 0;
	
	public Cellstate(Color c, String name){
		this.id = idCounter++;
		cellstates.put(id, this);
		this.c = c;
		this.name = name;
	}
	
	public Color getColor(){
		return c;
	}

	public byte getID() {
		return id;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public int getCompetitiveness() {
		return competitiveness;
	}

	public Cellstate setCompetitiveness(int competitiveness) {
		this.competitiveness = competitiveness;
		return this;
	}

	public boolean isCompetitive() {
		return getCompetitiveness() > 0 && this.isAlive();
	}
	
	public Cellstate setLiving(boolean alive){
		this.alive = alive;
		return this;
	}
	
	public int getCompeteBoost(HashMap<Byte, Integer> retVals, int x, int y){
		return 1;
	}
	
	public static Cellstate getStateFromID(byte id){
		return cellstates.get(id);
	}
	
	public static Collection<Cellstate> getAll(){
		return cellstates.values();
	}
	
	public static Cellstate getRandom() {
		Collection<Cellstate> all = getAll();
		return all.toArray(new Cellstate[0])[MathUtil.randInt(0, cellstates.size()-1)];
	}
	
	public Cellstate getCorpse() {
		return corpse;
	}
	public Cellstate getCorpse(Cellstate def) {
		if(corpse == null){
			return def;
		}
		return corpse;
	}

	public Cellstate setCorpse(Cellstate corpse) {
		this.corpse = corpse;
		return this;
	}

	public boolean isAlive() {
		return alive;
	}
	
	public Cellstate setDies(boolean b) {
		this.dies = b;
		return this;
	}
	
	public boolean getDies(){
		return dies;
	}
	
	public int getCorpseID() {
		return corpseID;
	}
	
	public Cellstate setCorpseID(int id){
		this.corpseID = id;
		return this;
	}
	
	public int getSurroundPoints(){
		return surroundPoints;
	}
	
	public Cellstate setSurroundPoints(int points){
		surroundPoints = points;
		return this;
	}

	public boolean isCorpse() {
		for(int i = 0; i < corpseStates.length; i++){
			if(corpseStates[i].getID() == this.getID()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isIrregular(){
		return false;
	}
	
	public void doIrregularUpdate(Game game, Board board, byte[][] boardNew, int x, int y){
	}
	

	public boolean getAutoDies() {
		return autoDie;
	}
	
	public Cellstate setAutoDies(boolean b){
		this.autoDie = b;
		return this;
	}
	
	public static int getMaxCorpses(){
		return corpseStates.length;
	}

	public static Cellstate DEAD = new Cellstate(Color.black,"Dead").setCompetitiveness(0).setLiving(false);
	public static Cellstate NEUTRAL = new Cellstate(Color.white,"Neutral").setCompetitiveness(0);
	public static Cellstate PL1 = new Cellstate(Color.red,"P1");
	public static Cellstate PL2 = new Cellstate(Color.blue,"P2");
	public static Cellstate PL3 = new Cellstate(new Color(204,204,0),"P3");
	public static Cellstate PL4 = new Cellstate(Color.green,"P4");
	public static Cellstate DUMMY = new Cellstate(Color.orange,"Dummy").setCompetitiveness(0);
	public static Cellstate SHY = new Cellstate(Color.pink.darker(),"Shy").setCompetitiveness(1);
	public static Cellstate VOID = new Cellstate(Color.DARK_GRAY,"Always Dead").setCompetitiveness(0).setLiving(false).setDies(false);
	public static Cellstate LIFEGIVER = new Cellstate(Color.pink,"Always Living").setCompetitiveness(0).setDies(false);
	public static Cellstate NEVERDEAD = new Cellstate(Color.LIGHT_GRAY,"Never Dead").setDies(false);
	public static Cellstate NEVERDEADSHY = new Cellstate(new Color(166,175,86),"Shyly Never Dead").setDies(false).setCompetitiveness(1);
	public static Cellstate KILLER = new Cellstate(new Color(100,0,0),"Killer Cell").setSurroundPoints(-100).setDies(false).setCompetitiveness(0);
	public static Cellstate NEGATIVE = new Cellstate(new Color(160,0,0),"Negative Cell").setSurroundPoints(-1).setDies(false).setCompetitiveness(0);
	public static Cellstate DOUBLE = new Cellstate(new Color(160,180,80),"Double Cell").setSurroundPoints(2).setDies(false).setCompetitiveness(0);
	public static Cellstate TRIPLE = new Cellstate(new Color(210,210,110),"Triple Cell").setSurroundPoints(3).setDies(false).setCompetitiveness(0);
	public static Cellstate NEGATIVESPREAD = new Cellstate(new Color(160,0,0).darker(),"Negative Cell (Spreads)").setSurroundPoints(-1);
	public static Cellstate DOUBLESPREAD = new Cellstate(new Color(160,180,80).darker(),"Double Cell (Spreads)").setSurroundPoints(2);
	public static Cellstate TRIPLESPREAD = new Cellstate(new Color(210,210,110).darker(),"Triple Cell (Spreads)").setSurroundPoints(3);
	
	public static Cellstate ANTIEATER = new Cellstate(new Color(247,110,120),"Anti-Eater"){
		@Override
		public int getCompeteBoost(HashMap<Byte, Integer> retVals, int x, int y){
			int eaters = Main.getCurrentBoard().getSurroundingOfKind(x, y, EATER.getID());
			if(eaters > 0){
				retVals.put(EATER.getID(), -1000);
			}
			return 1+eaters*100;
		}
		@Override
		public int getCompetitiveness() {
			return 1000;
		}
	};

	public static Cellstate EATER = new Cellstate(new Color(6,208,248),"Eater"){
		@Override
		public int getCompeteBoost(HashMap<Byte, Integer> retVals, int x, int y){
			return 10;
		}
	};
	
	public static Cellstate WIRE = new CellstateWire(new Color(0,53,3),"Wire").setLiving(false).setDies(false);
	public static Cellstate ELECTRONTAIL = new Cellstate(new Color(116,120,122),"Electron Tail").setLiving(false).setCorpse(WIRE);
	public static Cellstate ELECTRON = new Cellstate(new Color(0,190,122),"Electron").setLiving(false).setAutoDies(true).setCorpse(ELECTRONTAIL);
	
	public static Cellstate MACHINE_LIFEPLACER = new CellstateMachinePlacer(new Color(0,116,122), "Life Placer Machine",DEAD.getID(),NEUTRAL.getID(),ELECTRON.getID(),1);
	public static Cellstate MACHINE_ELECTRONPLACER = new CellstateMachinePlacer(new Color(0,116,122).darker(), "Electron Placer Machine",WIRE.getID(),ELECTRON.getID(),NEUTRAL.getID(),1);
	public static Cellstate MACHINE_ELECTRONPLACERIQUICK = new CellstateMachinePlacer(new Color(0,116,122).darker(), "IQuick Electron P. Machine",WIRE.getID(),ELECTRON.getID(),NEUTRAL.getID(),0){
		@Override
		public void doIrregularUpdate(Game game, Board board, byte[][] boardNew, int x, int y){
			int surround = board.getSurroundingOfKind(x, y, getPowerCellID());
			if(((LifeRulesSimple)game.getRules()).births(surround)){
				onPower(board, boardNew, x, y);
			}else{
				boardNew[x][y] = getID();
			}
		}
		
	};

	public static Cellstate CHEATER = new Cellstate(new Color(200,125,60), "Forced Cell"){
		public boolean isIrregular(){
			return true;
		}
		public void doIrregularUpdate(Game game, Board board, byte[][] boardNew, int x, int y){
			LifeRulesSimple cheatRules = (LifeRulesSimple) LifeRules.rulesGOL;
			for(int placeX = x-1; placeX<=x+1; placeX++){
				for(int placeY = y-1; placeY<=y+1; placeY++){
					if(board.isInBounds(placeX, placeY)){
						byte cs = board.getAt(placeX, placeY);
						if(cs == Cellstate.DEAD.getID() || cs == this.getID()){
							int surrounding = board.getSurroundingOfKind(placeX, placeY, getID());
							byte here = board.getAt(placeX, placeY);
							if(here == DEAD.getID()){
								if(cheatRules.births(surrounding)){
									boardNew[placeX][placeY] = getID();
								}
							}else if(here == getID()){
								if(cheatRules.survives(surrounding)){
									boardNew[placeX][placeY] = getID();
								}else{
									boardNew[placeX][placeY] = cheatRules.getDefaultCorpse().getID();
								}
							}
						}
					}
				}
			}
		}
	}.setLiving(false);
	
	
	public static Cellstate[] corpseStates = new Cellstate[50];
	private static BufferedImage corpseImg = ImageUtil.loadImage("/images/corpsecolors.png");
	static{
		Cellstate last = null;
		for(int i = 1; i <= corpseStates.length; i++){
			Cellstate cs = new Cellstate(new Color(corpseImg.getRGB(i-1, 0)), "Corpse " + i).setLiving(false);
			cs.setCorpseID(i);
			if(last != null){
				cs.setCorpse(last);
			}
			last = cs;
			corpseStates[i-1] = cs;
		}
	}
	
	public static Cellstate CORPSEBOMB = new Cellstate(new Color(178,0,255),"Corpse Bomb").setCorpse(corpseStates[corpseStates.length-1]);
}
