package util;

import java.awt.Graphics2D;

import game.board.Board;
import game.board.Cellstate;
import game.board.CellstateGroup;
import game.rules.LifeRules;

public class TinyThing extends Board {
	private static final long serialVersionUID = 1L;
	
	LifeRules rules;
	
	public TinyThing(){
		super(60);
		for(int x = 0; x < this.getWidth(); x++){
			for(int y = 0; y < this.getHeight(); y++){
				if(MathUtil.randInt(1, 3) == 1){
					byte place;
					int rand = MathUtil.randInt(1, 10);
					if(rand < 10){
						place = Cellstate.NEUTRAL.getID();
					}else{
						if(MathUtil.randInt(0, 60) == 5){
							place = CellstateGroup.ODD.getRandom().getID();
						}else{
							place = CellstateGroup.BASIC.getRandom().getID();
						}
					}
					this.setAt(x, y, place);
				}
			}
		}
		if(MathUtil.randInt(0, 2)==1){
			rules = LifeRules.rulesGOL;
		}else{
			rules = LifeRules.getRuleList().get(MathUtil.randInt(0, LifeRules.getRuleList().size()-1));
		}
	}
	
	public void render(Graphics2D g, int yOff) {
		this.updateAll(null, rules);
		int size = 5;
		for(int x = 0; x < this.getWidth(); x++){
			for(int y = 0; y < this.getHeight(); y++){
				g.setColor(this.getColorFor(x, y));
				g.fillRect(550+x*size, y*size + yOff + 50, size, size);
			}
		}
	}
}
