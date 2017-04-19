package game;

import java.awt.event.MouseEvent;

import game.board.Board;
import game.board.Cellstate;
import game.rules.LifeRules;
import jgolad.Main;
import window.SandboxPaletteWindow;

public class GameSandbox extends Game {
	private static SandboxPaletteWindow spw;

	public GameSandbox(Board b, LifeRules r) {
		super(b, r);
	}

	@Override
	public void start() {
		this.addPlayer(new Player(Cellstate.PL1.getID(), "Player 1"));
		this.addPlayer(new Player(Cellstate.PL2.getID(), "Player 2"));
		this.addPlayer(new Player(Cellstate.PL3.getID(), "Player 3"));
		this.addPlayer(new Player(Cellstate.PL4.getID(), "Player 4"));
		this.addPlayer(new Player(Cellstate.EATER.getID(), "Eaters"));
		this.addPlayer(new Player(Cellstate.ANTIEATER.getID(), "Anti-Eaters"));
		spw = new SandboxPaletteWindow();
		spw.initialize();
		Main.getFrame().requestFocus();
	}

	@Override
	public void space() {
		getBoard().updateAll(this,getRules());
	}

	@Override
	public void kill() {
		Board.crazymode = false;
		spw.kill();
	}

	public void onMousePress(MouseEvent e) {
		Board b = getBoard();
		if (b.isInBounds(Main.mouseCellX, Main.mouseCellY)) {
			if (e.getButton() == 1) {
				b.setAt(Main.mouseCellX, Main.mouseCellY, Main.sandboxByte);
			} else {
				//Main.sandboxByte = b.getAt(Main.mouseCellX, Main.mouseCellY);
				Byte id = b.getAt(Main.mouseCellX, Main.mouseCellY);
				Cellstate cs = Cellstate.getStateFromID(id);
				spw.setActiveCellstate(cs);
			}
		}
	}
}
