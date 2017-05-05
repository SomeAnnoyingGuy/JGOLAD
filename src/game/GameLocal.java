package game;

import java.awt.event.MouseEvent;
import java.util.Iterator;

import game.board.Board;
import game.rules.LifeRules;

public class GameLocal extends Game {
	private int playerID = 0;
	private Board maskBoard = null;
	private boolean waitingOnHuman = false;
	private boolean firstRoundComplete = false;
	private byte waitingHumanCell = 0;

	public GameLocal(Board board, LifeRules rules) {
		super(board, rules);
	}

	@Override
	public Board getBoard() {
		if (maskBoard != null && waitingOnHuman) {
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

	private void doNextTurn() {
		if(firstRoundComplete){
			this.checkForWinner();
		}
		
		Player p = this.getPlayer(playerID);
		
		if(p == null){
			return;
		}
		
		if(p.doMove(super.getBoard(), this)){
			waitingOnHuman = false;
			
			super.getBoard().updateAll(this, getRules());
			if(firstRoundComplete){
				this.removeDeadPlayers();
			}
			
			playerID++;
			
			if(playerID >= this.getPlayers().size()){
				playerID = 0;
				firstRoundComplete = true;
			}
			
			Player pNext = this.getPlayer(playerID);
			if(pNext instanceof PlayerLocalHuman){
				prepareForHumanStart();
			}
			pNext.prepForMove();
		}
	}

	public void prepareForHumanStart() {
		waitingOnHuman = true;
		Player waitPlr = this.getPlayer(playerID);
		waitingHumanCell = waitPlr.getCellID();
		summonMask();
	}

	private void summonMask() {
		maskBoard = super.getBoard().duplicate();
	}

	@Override
	public void kill() {
	}

	@Override
	public void onMousePress(MouseEvent e) {
		Iterator<Player> it = this.getPlayers().iterator();
		while (it.hasNext()) {
			it.next().onCellClick(super.getBoard(), mouseCellX, mouseCellY);
		}
		if (waitingOnHuman) {
			super.invalidateBoardCache();
			maskBoard = super.getBoard().duplicate();
			if (maskBoard.isInBounds(mouseCellX, mouseCellY)) {
				maskBoard.setAt(mouseCellX, mouseCellY, waitingHumanCell);
			}
		}
	}
	
	@Override
	public int getHighlightedPlayerID(){
		return playerID;
	}
}
