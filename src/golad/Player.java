package golad;

import java.awt.Color;
import java.util.Iterator;

public class Player {
	protected byte cell = Cellstate.DUMMY.getID();
	
	String name;
	
	public Player(byte cell,String name){
		this.name = name;
		this.cell = cell;
	}
	
	public Player(Cellstate cs, String name2) {
		this(cs.getID(),name2);
	}

	public Color getColor() {
		return getCellstate().getColor();
	}

	public String getName() {
		return name;
	}
	
	public void prepForMove(){
	}
	
	public boolean doMove(Board board, Game game){
		int winX = 0,winY = 0;
		double winScore= -1;
		
		for(int x = 0; x < board.getWidth(); x++){
			for(int y = 0; y < board.getHeight(); y++){
				if(!Cellstate.getStateFromID(board.getAt(x, y)).isAlive()){
					Board b = board.duplicate();
					b.setAt(x, y, cell);
					b.updateAll(game,game.getRules());
					
					double antiscore = 0;
					Iterator<Player> itEnemy = game.getPlayers().iterator();
					while(itEnemy.hasNext()){
						Player enemy = itEnemy.next();
						antiscore += b.getPlayerScore(enemy);
					}
					
					double friendlyscore = b.getPlayerScore(this) - antiscore;
					double score = friendlyscore/antiscore;
					if(score > winScore){
						winX = x;
						winY = y;
						winScore = score;
					}
				}
			}
		}
		
		board.setAt(winX, winY, cell);
		
		return true;
	}
	
	public Cellstate getCellstate(){
		return Cellstate.getStateFromID(cell);
	}

	public Cellstate getCell() {
		return Cellstate.getStateFromID(cell);
	}

	public byte getCellID() {
		return cell;
	}

	public void onCellClick(Board board, int mouseCellX, int mouseCellY) {
	}
}
