package window;

import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.JPanel;

import game.board.Cellstate;
import game.board.CellstateGroup;

public class JPanelCellGroup extends JPanel {
	private static final long serialVersionUID = 1L;
	
	int size = 15;
	int cushion = 3;
	int totalSize = size+cushion;
	int perRow = getWidth()/totalSize;
	
	protected CellstateGroup group;
	
	public JPanelCellGroup(CellstateGroup group){
		this.group = group;
	}
	
	@Override
	public void paintComponent(Graphics g){
		perRow = getWidth()/totalSize;
		super.paintComponent(g);
		Iterator<Cellstate> it = group.iterator();
		int counter = 0;
		while(it.hasNext()){
			int x=counter%perRow,y=counter/perRow;
			if(counter > 0){
				Cellstate cs = it.next();
				g.setColor(cs.getColor());
			}else{
				g.setColor(Cellstate.DEAD.getColor());
			}
			g.fillRect(cushion + totalSize*x, cushion+totalSize*y, size, size);
			counter++;
		}
	}
	
	public Cellstate getAt(int x, int y){
		x /= totalSize;
		y /= totalSize;
		int index = perRow*y + x;
		if(index > 0 && index <= group.size()){
			return group.get(index-1);
		}else{
			return Cellstate.DEAD;
		}
	}
}
