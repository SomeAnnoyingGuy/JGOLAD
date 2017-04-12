package golad;

import java.util.HashMap;

public class CellCensus extends HashMap<Byte,Integer>{
	private static final long serialVersionUID = 1L;
	
	public int getScore(byte b){
		return MathUtil.unboxInteger(this.get(b));
	}
	
	public int getScore(Cellstate cs){
		return getScore(cs.getID());
	}
	
	public void setScore(byte b, int amount){
		this.put(b, amount);
	}
	
	public void setScore(Cellstate cs, int amount){
		setScore(cs.getID(),amount);
	}
	
	public void modScore(byte b, int mod){
		this.setScore(b, this.getScore(b)+mod);
	}
	
	public void modScore(Cellstate cs, int mod){
		modScore(cs.getID(),mod);
	}
}
