package game.board;

import java.util.ArrayList;

import util.MathUtil;

public class CellstateGroup extends ArrayList<Cellstate>{
	private static final long serialVersionUID = 1;
	
	private static ArrayList<CellstateGroup> groupList = new ArrayList<CellstateGroup>();
	
	private String name;
	
	public CellstateGroup(String name){
		setName(name);
		getGroupList().add(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static ArrayList<CellstateGroup> getGroupList(){
		return groupList;
	}
	
	public Cellstate getRandom() {
		if(this.isEmpty()){
			return Cellstate.NEUTRAL;
		}else{
			return this.get(MathUtil.randInt(0, this.size()-1));
		}
	}
	
	public static final CellstateGroup ALL = new CellstateGroup("All");
	public static final CellstateGroup BASIC = new CellstateGroup("Basic");
	public static final CellstateGroup ODD = new CellstateGroup("Odd");
	public static final CellstateGroup WIRE = new CellstateGroup("Electronic");
	public static final CellstateGroup CORPSE = new CellstateGroup("Corpse");
}
