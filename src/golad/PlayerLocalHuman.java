package golad;

public class PlayerLocalHuman extends Player {
	private int selectX = -1, selectY = -1;

	public PlayerLocalHuman(byte cell, String name) {
		super(cell, name);
	}

	public PlayerLocalHuman(Cellstate cs, String name2) {
		super(cs.getID(), name2);
	}

	public boolean doMove(Board board, Game game) {
		if (board.isInBounds(selectX, selectY)) {
			board.setAt(selectX, selectY, cell);
			return true;
		}
		return false;
	}

	@Override
	public void prepForMove(){
		selectX = -1;
		selectY = -1;
	}
	
	@Override
	public void onCellClick(Board board, int x, int y) {
		selectX = x;
		selectY = y;
	}
}
