package golad;

public class LifeRulesSimple extends LifeRules {
	int[] survivals,births;
	int corpse = 0;
	
	public LifeRulesSimple(String name, int[] survivals, int[] births){
		this(name,survivals,births,0);
	}
	
	public LifeRulesSimple(String name, int[] survivals, int[] births, int corpse){
		super(name);
		this.survivals = survivals;
		this.births = births;
		this.corpse = Math.min(Cellstate.getMaxCorpses(), corpse);
	}
	
	public boolean survives(int surrounding){
		return isIn(surrounding,survivals);
	}
	
	public boolean births(int surrounding){
		return isIn(surrounding,births);
	}
	
	private boolean isIn(int num, int[] arr){
		for(int i = 0; i < arr.length; i++){
			if(arr[i] == num){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public byte getNew(Board board, int x, int y, byte current, int surrounding, byte alreadyThere) {
		Cellstate here = Cellstate.getStateFromID(current);
		if(current == Cellstate.DEAD.getID()){
			if(births(surrounding)){
				return board.getMostSurrounding(x, y);
			}
		}else{
			if(here.getDies()){
				if(here.isAlive() && !here.getAutoDies()){
					if(survives(surrounding)){
						return current;
					}else{
						return here.getCorpse(getDefaultCorpse()).getID();
					}
				}else{
					return here.getCorpse(Cellstate.DEAD).getID();
				}
			}else{
				return here.getID();
			}
		}
		return alreadyThere;
	}
	
	public Cellstate getDefaultCorpse(){
		int id = corpse-1;
		if(id < 0){
			return Cellstate.DEAD;
		}else{
			return Cellstate.corpseStates[id];
		}
	}
}
