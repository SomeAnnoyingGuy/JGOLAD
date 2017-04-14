package game;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import game.board.Board;
import game.rules.LifeRules;
import jgolad.Main;
import util.WinUtil;

public abstract class Game {
	private Board board = null;
	private LifeRules rules = null;

	private ArrayList<Player> players = new ArrayList<Player>();
	
	public Game(Board board, LifeRules rules){
		this.board = board;
		this.setRules(rules);
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	public abstract void start();

	public abstract void space();
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Player getPlayer(int id){
		if(id < 0 || id >= players.size()){
			return null;
		}else{
			return players.get(id);
		}
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public void addPlayer(Player p){
		players.add(p);
	}

	public LifeRules getRules() {
		return rules;
	}

	public void setRules(LifeRules rules) {
		this.rules = rules;
	}
		
	public int getPlayerScore(Player p){
		return this.getBoard().getPlayerScore(p);
	}

	public void removeDeadPlayers() {
		Iterator<Player> it = players.iterator();
		while(it.hasNext()){
			Player p = it.next();
			if(getPlayerScore(p) < 1){
				it.remove();
			}
		}
	}
	
	public void checkForWinner() {
		removeDeadPlayers();
		if(players.size() < 1){
			WinUtil.display("Nobody won, tie game!");
			end();
		}else if(players.size() == 1){
			WinUtil.display(players.get(0).getName() + " won!");
			end();
		}
	}

	private void end() {
		Main.setCurrentGame(null);
	}

	public abstract void kill();

	public void onMousePress(MouseEvent e) {
	}
	
	public int getHighlightedPlayerID(){
		return -1;
	}
}