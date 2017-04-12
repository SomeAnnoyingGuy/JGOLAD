package golad;

public class LifeRulesCustom extends LifeRulesSimple {
	public LifeRulesCustom(String name) {
		super(name, new int[0], new int[0]);
	}

	@Override
	public void onSelected(){
		String input = WinUtil.getInput("Enter a standard rulestring according\nExample: 23/3/1 or 23/3\n", "23/3");
		String[] inputs = input.split("/");
		try{
			int[] s = getAllInside(inputs[0]);
			int[] b = getAllInside(inputs[1]);
			int corpse = 0;
			if(inputs.length > 2){
				corpse = Integer.parseInt(inputs[2]);
			}
			this.survivals = s;
			this.births = b;
			if(corpse > Cellstate.getMaxCorpses()){
				WinUtil.display("Currently, only corpses below " + Cellstate.getMaxCorpses() + " are allowed.\n"
						+ "Setiing corpses to the maximum instead");
				corpse = Cellstate.getMaxCorpses();
			}
			this.corpse = corpse;
		}catch(Exception e){
			e.printStackTrace();
			WinUtil.display("Invalid rulestring: " + input);
		}
	}
	
	private int[] getAllInside(String big){
		int[] ret = new int[big.length()];
		for(int i = 0; i < ret.length; i++){
			ret[i] = Integer.parseInt(big.substring(i, i+1));
		}
		return ret;
	}
}
