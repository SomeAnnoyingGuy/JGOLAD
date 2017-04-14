package game.board;

import java.awt.Color;

import game.Game;

public class CellstateWire extends Cellstate {
	private byte powerID = (byte) (getID()+2); //electron is 2 IDs away
	
	public CellstateWire(Color c, String str) {
		super(c,str);
	}

	public boolean isIrregular(){
		return true;
	}
	
	@Override
	public void doIrregularUpdate(Game game, Board board, byte[][] boardNew, int x, int y){
		int surround = board.getSurroundingOfKind(x, y, powerID);
		if(surround==1||surround==2){
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

	public CellstateWire setPowerID(byte powerID) {
		this.powerID = powerID;
		return this;
	}
}
