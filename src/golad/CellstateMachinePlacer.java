package golad;

import java.awt.Color;

public class CellstateMachinePlacer extends CellstateMachine {
	private byte placeID,replaceID;

	public CellstateMachinePlacer(Color c, String str, byte replaceID, byte placeID, byte powerID, int powerNeeded) {
		super(c, str);
		this.setPlaceID(placeID);
		this.setReplaceID(replaceID);
		this.setPowerCellID(powerID);
		this.setPowerCellCount(powerNeeded);
	}
	
	@Override
	public void onPower(Board board, byte[][] boardNew, int x, int y) {
		boardNew[x][y] = this.getID();
		for(int placeX = x-1; placeX<=x+1; placeX++){
			for(int placeY = y-1; placeY<=y+1; placeY++){
				attemptPlace(board,boardNew,placeX,placeY,getPlaceID());
			}
		}
	}
	
	private void attemptPlace(Board board, byte[][] boardNew, int x, int y, byte place){
		if(board.getAt(x, y)==getReplaceID()){
			boardNew[x][y] = place;
		}
	}

	public byte getPlaceID() {
		return placeID;
	}

	public CellstateMachinePlacer setPlaceID(byte placeID) {
		this.placeID = placeID;
		return this;
	}

	public byte getReplaceID() {
		return replaceID;
	}

	public CellstateMachinePlacer setReplaceID(byte replaceID) {
		this.replaceID = replaceID;
		return this;
	}
}