package golad;

import java.awt.Color;

public abstract class CellstateMachine extends Cellstate {
	private byte powerCellID = Cellstate.ELECTRON.getID();
	private int powerCellCount = 1;
	
	public CellstateMachine(Color c, String str) {
		super(c,str);
		this.setDies(false);
		this.setLiving(false);
		this.setCompetitiveness(0);
	}

	public boolean isIrregular(){
		return true;
	}
	
	@Override
	public void doIrregularUpdate(Game game, Board board, byte[][] boardNew, int x, int y){
		int surround = board.getSurroundingOfKind(x, y, getPowerCellID());
		if(surround>=getPowerCellCount(game.getRules())){
			onPower(board, boardNew, x, y);
		}else{
			boardNew[x][y] = getID();
		}
	}
	
	public abstract void onPower(Board board, byte[][] boardNew, int x, int y);

	public byte getPowerCellID() {
		return powerCellID;
	}

	public CellstateMachine setPowerCellID(byte powerCellID) {
		this.powerCellID = powerCellID;
		return this;
	}

	public int getPowerCellCount(LifeRules r) {
		return powerCellCount;
	}

	public CellstateMachine setPowerCellCount(int powerCellCount) {
		this.powerCellCount = powerCellCount;
		return this;
	}
}
