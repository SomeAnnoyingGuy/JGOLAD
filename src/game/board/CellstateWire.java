package game.board;

import java.awt.Color;

import game.Game;

public class CellstateWire extends Cellstate {
	private byte powerID; //electron is 2 IDs away
	private int maxSurroundingPowerCells = 2;
	private Cellstate electronCorpse;
	
	public CellstateWire(Color c, String str, Cellstate electron) {
		super(c,str);
		this.setPowerID(electron.getID());
		electronCorpse = new Cellstate(this.getColor().brighter().brighter(), this.toString() + " Tail").setGroup(CellstateGroup.WIRE).setLiving(false).setCorpse(this);
	}

	public boolean isIrregular(){
		return true;
	}
	
	@Override
	public void doIrregularUpdate(Game game, Board board, byte[][] boardNew, int x, int y){
		int surround = board.getSurroundingOfKind(x, y, powerID);
		if(surround>0 && surround<=maxSurroundingPowerCells){
			boardNew[x][y] = powerID;
		}else{
			if(boardNew[x][y] == Cellstate.DEAD.getID()){
				boardNew[x][y] = getID();
			}
		}
	}

	public byte getPowerID() {
		return powerID;
	}

	public Cellstate getPowerCell(){
		return Cellstate.getStateFromID(powerID);
	}
	
	public CellstateWire setPowerID(byte powerID) {
		this.powerID = powerID;
		return this;
	}

	public int getMaxSurroundingPowerCells() {
		return maxSurroundingPowerCells;
	}

	public CellstateWire setMaxSurroundingPowerCells(int maxSurroundingPowerCells) {
		this.maxSurroundingPowerCells = maxSurroundingPowerCells;
		return this;
	}

	public Cellstate getElectronCorpse() {
		return electronCorpse;
	}

	public void setElectronCorpse(Cellstate electronCorpse) {
		this.electronCorpse = electronCorpse;
	}
}
