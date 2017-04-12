package golad;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class Board implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected static transient boolean crazymode;
	protected static transient int crazyCounter = 0;

	protected transient Board predictionBoardCache;
	protected CellCensus census = new CellCensus();
	
	private byte[][] cellArr;
	private int size;
	private boolean isToroidal;
	
	private transient boolean isChanged = true;

	public Board() {
		this(20);
	}

	public Board(int size) {
		setSize(size);
		setCellArr(new byte[getWidth()][getHeight()]);
	}

	public byte[][] getCellArr() {
		return cellArr;
	}

	public void setCellArr(byte[][] cellArr) {
		this.setChanged(true);
		this.cellArr = cellArr;
		this.size = cellArr.length;
		this.updateCensus();
	}

	public int getWidth() {
		return getSize();
	}

	public int getHeight() {
		return getSize();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setAt(int x, int y, byte b) {
		setChanged(true);
		census.modScore(getAt(x,y), -1);
		census.modScore(b, 1);
		getCellArr()[x][y] = b;
	}

	public byte getAt(int x, int y) {
		if(isToroidal()){
			while(x >= this.getWidth()){
				x -= this.getWidth();
			}
			while(y >= this.getHeight()){
				y -= this.getHeight();
			}
			while(x < 0){
				x += this.getWidth();
			}
			while(y < 0){
				y += this.getHeight();
			}
		}
		if (!isInBounds(x,y)) {
			return Cellstate.DEAD.getID();
		}else{
			return getCellArr()[x][y];
		}
	}

	public boolean isAlive(int x, int y) {
		return Cellstate.getStateFromID(getAt(x,y)).isAlive();
	}
	
	public boolean isInBounds(int x, int y){
		return !(x < 0 || y < 0 || x > getWidth() - 1 || y > getHeight() - 1);
	}

	public int getSurroundingLiving(int xPos, int yPos) {
		int counter = 0;
		counter += modCounter(xPos+1,yPos);
		counter += modCounter(xPos+1,yPos+1);
		counter += modCounter(xPos,yPos+1);
		counter += modCounter(xPos-1,yPos+1);
		counter += modCounter(xPos-1,yPos);
		counter += modCounter(xPos-1,yPos-1);
		counter += modCounter(xPos,yPos-1);
		counter += modCounter(xPos+1,yPos-1);
		return counter;
	}
	
	private int modCounter(int x, int y){
		if(isAlive(x,y)){
			return Cellstate.getStateFromID(getAt(x,y)).getSurroundPoints();
		}
		return 0;
	}
	
	public Color getColorFor(int x, int y){
		byte b = getAt(x,y);
		if(crazymode){
			Cellstate cs = Cellstate.getStateFromID(b);
			if(x == 0 && y == 0){
				crazyCounter++;
			}
			if(crazyCounter > HueSine.getMax()){
				crazyCounter = 0;
			}
			if(cs.isAlive()||cs.isCorpse()){
				return HueSine.get(crazyCounter+Cellstate.getStateFromID(b).getCorpseID()*10);
			}
		}
		return getColorForByte(b);
	}

	public void updateAll(Game game, LifeRules rules) {
		byte[][] newBoard = new byte[getWidth()][getHeight()];
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getHeight(); y++){
				byte here = getAt(x,y);
				Cellstate cshere = Cellstate.getStateFromID(here);
				if(cshere.isIrregular()){
					cshere.doIrregularUpdate(game,this,newBoard,x,y);
				}else{
					newBoard[x][y] = rules.getNew(this,x,y,here,getSurroundingLiving(x,y),newBoard[x][y]);
				}
			}
		}
		setCellArr(newBoard);
	}

	private HashMap<Byte,Integer> retVals = new HashMap<>();
	public byte getMostSurrounding(int x, int y){
		retVals.clear();
		for(int xPos = x-1; xPos <= x+1; xPos++){
			for(int yPos = y-1; yPos <= y+1; yPos++){
				byte b = getAt(xPos,yPos);
				Cellstate cs = Cellstate.getStateFromID(b);
				int current = MathUtil.unboxInteger(retVals.get(b));
				retVals.put(b,(int) Math.min(cs.getCompetitiveness(), current+cs.getCompeteBoost(retVals, xPos, yPos)));
			}
		}
		int winAmount = -1;
		Cellstate winner = Cellstate.NEUTRAL;
		Iterator<Cellstate> it = Cellstate.getAll().iterator();
		while(it.hasNext()){
			Cellstate cs = it.next();
			if(cs.isCompetitive()){
				int amount = MathUtil.unboxInteger(retVals.get(cs.getID()));
				if(amount > 0){
					if(amount > winAmount){
						winAmount = amount;
						winner = cs;
					}else if(amount == winAmount){
						if(winner.getCompetitiveness() < cs.getCompetitiveness()){
							winner = cs;
						}else{
							//Ties can't risk breaking fairness
							return Cellstate.NEUTRAL.getID();
						}
					}
				}
			}
		}
		return winner.getID();
	}

	public int getSurroundingOfKind(int x, int y, byte id){
		int ret=0;
		for(int xPos = x-1; xPos <= x+1; xPos++){
			for(int yPos = y-1; yPos <= y+1; yPos++){
				byte b = getAt(xPos,yPos);
				Cellstate cs = Cellstate.getStateFromID(b);
				if(cs.getID() == id && (xPos != x || yPos != y)){
					ret++;
				}
			}
		}
		return ret;
	}

	public Color getColorForByte(byte b) {
		Cellstate cs = Cellstate.getStateFromID(b);
		if(cs == null){
			return null;
		}
		return cs.getColor();
	}

	public void clear() {
		setCellArr(new byte[getWidth()][getHeight()]);
	}

	public void resize(int w, int h) {
		if(w > 0 && h > 0){
			setCellArr(new byte[w][h]);
		}
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		if(isChanged){
			this.predictionBoardCache = null;
		}
		this.isChanged = isChanged;
	}

	public void setToroidal(boolean b) {
		isToroidal = b;
	}
	
	public boolean isToroidal(){
		return isToroidal;
	}
	
	public Board getPredictionBoard(Game game, LifeRules rules){
		if(predictionBoardCache == null){
			updatePrediction(game,rules);
		}
		return predictionBoardCache;
	}
	
	public void updatePrediction(Game game, LifeRules rules){
		Board p = new Board();
		p.setCellArr(this.getCellArr());
		p.setToroidal(this.isToroidal());
		p.updateAll(game,rules);
		this.predictionBoardCache = p;
	}

	public int getPlayerScore(Player p){
		return census.getScore(p.getCellID());
	}

	public Board duplicate() {
		byte[][] newBoard = new byte[getWidth()][getHeight()];
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getHeight(); y++){
				newBoard[x][y] = this.getCellArr()[x][y];
			}
		}
		Board ret = new Board();
		ret.setCellArr(newBoard);
		ret.setToroidal(isToroidal());
		return ret;
	}

	public void updateCensus() {
		census.clear();
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getHeight(); y++){
				census.modScore(getAt(x,y), 1);
			}
		}
	}
}
