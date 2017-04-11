package golad;

import java.awt.event.MouseEvent;
import java.util.Iterator;

public class GameLocal extends Game {
	private int playerID = 0;
	private int playerIDPrev = -1;
	private Board maskBoard = null;
	private boolean waitingOnHuman = false;
	private boolean firstRoundComplete = false;
	private byte waitingHumanCell = 0;
	
	public GameLocal(Board board, LifeRules rules) {
		super(board, rules);
	}
	
	@Override
	public Board getBoard() {
		if(maskBoard != null && waitingOnHuman){
			return maskBoard;
		}
		return super.getBoard();
	}

	@Override
	public void start() {
	}

	@Override
	public void space() {
		doNextTurn();
	}
	
	private void doNextTurn(){
		if (this.getBoard() != null && super.getBoard() != null) {
			if(!waitingOnHuman){
				if (playerID >= this.getPlayers().size()) {
					playerID = 0;
					firstRoundComplete = true;
					this.checkForWinner();
				}
			}
			if(this.getPlayers().size() < 1){
				return;
			}
			Player p = getPlayers().get(playerID);
			waitingOnHuman = p instanceof PlayerLocalHuman;
			if(!waitingOnHuman){
				maskBoard = null;
			}else{
				waitingHumanCell = p.getCellID();
			}
			if(playerIDPrev != playerID){
				p.prepForMove();
			}

			playerIDPrev = playerID;
			
			if(p.doMove(super.getBoard(), this)){
				super.getBoard().updateAll(this,getRules());
				if(firstRoundComplete){
					this.removeDeadPlayers();
				}
				playerID++;
				if(this.getPlayer(playerID) instanceof PlayerLocalHuman){
					prepareForHumanStart();
				}else{
					waitingOnHuman = false;
				}
			}
		}
	}
	
	public void prepareForHumanStart(){
		Player waitPlr = this.getPlayer(playerID);
		summonMask();
		waitingOnHuman = true;
		waitingHumanCell = waitPlr.getCellID();
	}
	
	private void summonMask(){
		maskBoard = super.getBoard().duplicate();
	}

	@Override
	public void kill() {
	}

	@Override
	public void onMousePress(MouseEvent e) {
		Iterator<Player> it = this.getPlayers().iterator();
		while(it.hasNext()){
			it.next().onCellClick(super.getBoard(),Main.mouseCellX,Main.mouseCellY);
		}
		if(waitingOnHuman){
			Main.invalidateBoardCache();
			maskBoard = super.getBoard().duplicate();
			if(maskBoard.isInBounds(Main.mouseCellX,Main.mouseCellY)){
				maskBoard.setAt(Main.mouseCellX,Main.mouseCellY,waitingHumanCell);
			}
		}
	}
}
