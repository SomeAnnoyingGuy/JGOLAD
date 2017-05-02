package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import game.board.Board;
import game.board.Cellstate;
import game.rules.LifeRules;
import jgolad.Main;
import jgolad.screens.Screen;
import util.WinUtil;

public abstract class Game extends Screen {
	private Board board = null;
	private LifeRules rules = null;
	
	private BufferedImage boardCacheImage = null;
	
	private ArrayList<Player> players = new ArrayList<Player>();
	protected int mouseCellX;
	protected int mouseCellY;
	private int cSize;
	
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
	
	@Override
	public void render(Graphics g) {
		int w = Main.getWidth();
		int h = Main.getHeight();
		Font plrFont = new Font("Tahoma", 0, 20);
		if (board.isChanged()) {
			boolean showP = Main.getPredictions();
			boolean showMouseOver = Main.getMousePreview();
			int cacheSize = Main.getCellSize();
			if(!(board.getWidth() <= 50 || board.getHeight() <= 50)) {
				cacheSize = 1;
				showP = false;
				showMouseOver = false;
			}
			boardCacheImage = new BufferedImage(board.getWidth()*cacheSize,board.getHeight()*cacheSize,BufferedImage.TYPE_INT_RGB);
			Graphics2D imgG = boardCacheImage.createGraphics();
			
			for (int x = 0; x < board.getWidth(); x++) {
				for (int y = 0; y < board.getHeight(); y++) {
					Color cellColor = board.getColorFor(x, y);
					int rx = x * cacheSize;
					int ry = y * cacheSize;
					int rs = cacheSize;
					if(cellColor != null){
						imgG.setColor(cellColor);
						imgG.fillRect(rx, ry, rs, rs);
					}
					if(showP){
						Board pb = board.getPredictionBoard(this,this.getRules());
						cellColor = pb.getColorFor(x, y);
						if(cellColor != null){
							int pSize = rs/3;
							imgG.setColor(cellColor);
							imgG.fillRect(rx+(rs/2)-(pSize/2), ry+(rs/2)-(pSize/2), pSize, pSize);
						}
					}
					if(showMouseOver){
						cellColor = Cellstate.getStateFromID(Main.sandboxByte).getColor();
						imgG.setColor(cellColor);
						if(cellColor != null && x==mouseCellX && y==mouseCellY){
							g.setColor(cellColor);
							imgG.drawRect(rx+1, ry+1, rs-2, rs-2);
						}
					}
					if(board.getWidth() <= 50 || board.getHeight() <= 50){
						imgG.setColor(Color.darkGray);
						imgG.drawRect(rx, ry, rs, rs);
					}
				}
			}
		}
		cSize = w-h;
		int cStart = w-cSize; //w-(w-h) = h ???
		g.drawImage(boardCacheImage, 0, 0, cStart, h, null);
		board.setChanged(false);
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(cStart, 0, cSize, h);
		Iterator<Player> it = this.getPlayers().iterator();
		g.setFont(plrFont);
		int pCount = 0;
		while(it.hasNext()){
			Player p = it.next();
			int hP = 70;
			int yP = hP*pCount;
			g.setColor(p.getColor());
			g.fillRect(cStart, yP, cSize, hP);
			g.setColor(Color.white);
			g.drawString(p.getName(), cStart + 10, yP+g.getFontMetrics().getHeight());
			g.drawString(this.getPlayerScore(p)+"", cStart + 10, yP+g.getFontMetrics().getHeight()*2);
			if(pCount == this.getHighlightedPlayerID()){
				int osize = 20;
				g.setColor(Color.WHITE);
				g.fillOval(w-osize, yP + hP/2 - osize/2,osize, osize);
			}
			pCount++;
		}
	}
	
	public void onMouseMotion(MouseEvent e) {
		double panelToImageScaleW = (double)(board.getWidth()*Main.getCellSize())/(Main.getWidth()-(Main.getWidth()-Main.getHeight()));
		double panelToImageScaleH = (double)(board.getHeight()*Main.getCellSize())/(Main.getHeight());
		mouseCellX = (int) (((double)(e.getX()*panelToImageScaleW))/Main.getCellSize());
		mouseCellY = (int) (((double)(e.getY()*panelToImageScaleH))/Main.getCellSize());
		if(Main.getMousePreview()){
			board.setChanged(true);
		}
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
	
	public void invalidateBoardCache() {
		boardCacheImage = null;
	}
}