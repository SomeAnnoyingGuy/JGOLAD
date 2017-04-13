package game.rules;

import java.util.ArrayList;

import game.board.Board;

public abstract class LifeRules {
	private static ArrayList<LifeRules> rules = new ArrayList<LifeRules>();
	
	private String name;
	
	public LifeRules(String name, boolean addToList){
		if(addToList){
			rules.add(this);
		}
		this.name = name;
	}
	
	public LifeRules(String name){
		this(name,true);
	}
	
	public abstract byte getNew(Board board, int x, int y, byte current, int surrounding, byte alreadyThere);
	
	public static ArrayList<LifeRules> getRuleList(){
		return rules;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString(){
		return this.getName();
	}
	
	public void onSelected(){
	}

	public static LifeRules rulesCUSTOM = new LifeRulesCustom("Custom...");
	
	public static LifeRules rulesGOL = new LifeRulesSimple("Game of Life", new int[] { 2, 3 }, new int[] { 3 });
	public static LifeRules rulesHL = new LifeRulesSimple("High Life", new int[] { 2, 3 }, new int[] { 3, 6 });
	public static LifeRules rulesPGOL = new LifeRulesSimple("Pseudo-Life", new int[] { 2, 3, 8 }, new int[] { 3, 5, 7 });
	public static LifeRules rulesLL = new LifeRulesSimple("Long Life", new int[] { 5 }, new int[] { 3, 4, 5 });
	public static LifeRules rules2X2 = new LifeRulesSimple("2X2", new int[] { 1,2,5 }, new int[] { 3, 6 });
	public static LifeRules rulesDN = new LifeRulesSimple("Day and Night", new int[] { 3,4,5,7 }, new int[] { 3,6,7,8 });
	public static LifeRules rulesASIM = new LifeRulesSimple("Assimilation", new int[] { 4,5,6,7 }, new int[] { 3, 4, 5 });
	public static LifeRules rulesWC = new LifeRulesSimple("Walled Cities", new int[] { 2,3,4,5 }, new int[] { 4,5,6,7,8 });
	public static LifeRules rulesAMOEBA = new LifeRulesSimple("Amoeba", new int[] { 1,3,5,8 }, new int[] { 3,5,7 });
	public static LifeRules rulesDAMOEBA = new LifeRulesSimple("Diamoeba", new int[] { 5,6,7,8 }, new int[] { 3,5,6,7,8 });
	public static LifeRules rulesMYS = new LifeRulesSimple("Mystery", new int[] { 0,5,6,7,8 }, new int[] { 3,4,5,8 });
	public static LifeRules rulesMOVE = new LifeRulesSimple("MOVE", new int[] { 2,4,5 }, new int[] { 3,6,8 });
	public static LifeRules rules34 = new LifeRulesSimple("34", new int[] { 3,4 }, new int[] { 3,4 });
	public static LifeRules rulesSTAIN = new LifeRulesSimple("Stains", new int[] { 2,3,4,5,6,7,8 }, new int[] { 2,3,5,6,7,8 });
	public static LifeRules rulesMAZE = new LifeRulesSimple("Maze", new int[] { 1,2,3,4,5 }, new int[] { 3 });
	public static LifeRules rulesLWD = new LifeRulesSimple("Life Without Death", new int[] { 0,1,2,3,4,5,6,7,8 }, new int[] { 3 });
	public static LifeRules rulesCOAG = new LifeRulesSimple("Coagulations", new int[] { 2,3,4,5,6 }, new int[] { 3,7,8 });
	public static LifeRules rulesREP = new LifeRulesSimple("Replicator", new int[] { 1,3,5,7 }, new int[] { 1,3,5,7 });
	public static LifeRules rulesSEED = new LifeRulesSimple("Seeds", new int[] { }, new int[] { 2 });
	public static LifeRules rulesGNARL = new LifeRulesSimple("Gnarl", new int[] { 1 }, new int[] { 1 });
	public static LifeRules rulesFLASH = new LifeRulesSimple("FLASH", new int[] { }, new int[] { 0 });
	
	public static LifeRules rulesZGOL = new LifeRulesSimple("Game of Life and Corpses", new int[] { 2, 3 }, new int[] { 3 }, 1);
	public static LifeRules rulesSW = new LifeRulesSimple("Star Wars", new int[] { 3,4,5 }, new int[] { 2 }, 4);
	public static LifeRules rulesLOTE = new LifeRulesSimple("Living on the Edge", new int[] { 3,4,5,8 }, new int[] { 3,7 }, 4);
	public static LifeRules rulesB6 = new LifeRulesSimple("Brian 6", new int[] { 6 }, new int[] { 2,4,6 }, 3);
	public static LifeRules rulesFROGS = new LifeRulesSimple("Frogs", new int[] { 1,2 }, new int[] { 3,4 }, 3);
	public static LifeRules rulesLFROGS = new LifeRulesSimple("Like Frogs", new int[] { 1,2,4 }, new int[] { 3 }, 3);
}

