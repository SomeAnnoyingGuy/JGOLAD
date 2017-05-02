package jgolad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ToolTipManager;

import game.Game;
import game.GameLocal;
import game.GameSandbox;
import game.board.Board;
import game.rules.LifeRules;
import jgolad.screens.MainMenu;
import jgolad.screens.Screen;
import jgolad.screens.SettingsMenu;
import util.ImageUtil;
import window.ExtrasMenu;
import window.GameSetupWindow;

public class Main {
	public static int FPS = 20;
	
	public static Dimension PANEL_SIZE = new Dimension(1000, 700);
	
	private static Screen currentScreen = null;
	private static Game currentGame = null;
	
	private static boolean spaceDown = false;
	private static boolean predictions,mousePreview;
	private static int cellSize = 10;
	
	public static byte sandboxByte = 1;

	private static int w;
	private static int h;
	
	private static JPanel panel;
	private static JFrame frame;
	public static Settings settings = new Settings();
	
	private static final String[] menuMusicQueue = {"JGOLAD Menu Theme.wav"};
	private static final String[] gameMusicQueue = {"gameMus1.wav","gameMus2.wav"};
	private static Music musicPlayer = new Music(menuMusicQueue);
	
	public static void main(String[] args) {
		
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		
		frame = new JFrame("JGOLAD Beta");
		frame.setIgnoreRepaint(true);
		frame.setIconImage(ImageUtil.loadImage("/tinylogo.png"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics gee) {
				Graphics2D g = (Graphics2D) gee;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				w = this.getWidth();
				h = this.getHeight();
				System.out.println(h+" "+w);
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, w, h);
				
				getCurrentScreen().render(g);
			}
		};
		panel.setIgnoreRepaint(true);
		panel.setPreferredSize(PANEL_SIZE);
		setCurrentScreen(new MainMenu());
		frame.add(panel);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		
		panel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				Game g = getCurrentGame();
				if (g != null) {
					g.onMouseMotion(e);
				}
			}
		});
		
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				currentScreen.onMousePress(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
		
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			//TAKE CODES FOR DEFAULT FROM HERE
			@Override
			public void keyPressed(KeyEvent e) {
				if (currentGame == null) {
					spaceDown = false;
					if (e.getKeyCode() == settings.MENU_2) {
						Board b = new Board(20);
						setCurrentGame(new GameSandbox(b, LifeRules.rulesGOL));
						musicPlayer.stop();
						musicPlayer.setList(gameMusicQueue);
						musicPlayer.play();
					} else if (e.getKeyCode() == settings.MENU_1) {
						setCurrentGame(GameSetupWindow.createGame());
						musicPlayer.stop();
						musicPlayer.setList(gameMusicQueue);
						musicPlayer.play();
					} else if (e.getKeyCode() == settings.MENU_3) {
						ExtrasMenu em = new ExtrasMenu();
						em.setVisible(true);
					} else if (e.getKeyCode() == settings.MENU_4) {
						setCurrentScreen(new SettingsMenu());
					}
				} else {
					if (e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
						spaceDown = true;
					} else if (e.getKeyCode() == settings.tglPred){
						predictions = !predictions;
						Board b = getCurrentBoard();
						if(b != null){
							b.setChanged(true);
						}
					} else if (e.getKeyCode() == KeyEvent.VK_M) {
						mousePreview = !mousePreview;
						getCurrentBoard().setChanged(true);
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
					spaceDown = false;
				} else if(e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) {
					Board b = getCurrentBoard();
					if(b != null){
						b.updateAll(getCurrentGame(),getCurrentGame().getRules());
					}
				} else if(e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) {
					Board b = getCurrentBoard();
					if(b != null){
						int amount = 0;
						if(e.isShiftDown()){
							amount = 100;
						}else if(e.isControlDown()){
							amount = 1000;
						}else{
							amount = 10;
						}
						for(int i = 0; i < amount; i++) {
							b.updateAll(getCurrentGame(),getCurrentGame().getRules());
						}
					}
				} else if(e.getKeyCode() == KeyEvent.VK_C) {
					Game ga = getCurrentGame();
					if(ga != null){
						ga.getBoard().updateCensus();
					}
				} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if(currentGame != null){
						currentGame.kill();
						currentGame = null;
					}
					musicPlayer.stop();
					musicPlayer.setList(menuMusicQueue);
					musicPlayer.play();
					
				} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					Game g = getCurrentGame();
					if(g instanceof GameLocal){
						((GameLocal)g).space();
					}
				} else if (e.getKeyCode() == settings.SaveImg) {
					Board board = getCurrentBoard();
					if(board != null){
						board.saveImage();
					}
				}
			}
			//STOP HERE
		});
		
		Timer repainter = new Timer(1000 / FPS, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(spaceDown && currentGame != null){
					getCurrentGame().space();
				}
				frame.repaint();
			}
		});
		//Static declarations may need to 'catch up'
		repainter.setInitialDelay(repainter.getDelay()*2);
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		repainter.start();
		musicPlayer.play();
	}
	
	public static void setCurrentGame(Game g){
		currentGame = g;
		if(g != null){
			g.start();
			setCurrentScreen(g);
		}
		
	}
	
	public static void setCurrentScreen(Screen s) {
		if (s != null) {
			currentScreen = s;
		}
	}
	
	public static Board getCurrentBoard(){
		if(currentGame == null){
			return null;
		}
		return currentGame.getBoard();
	}

	public static Game getCurrentGame() {
		return currentGame;
	}
	
	public static Screen getCurrentScreen() {
		return currentScreen;
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	
	public static boolean getPredictions() {
		return predictions;
	}
	
	public static boolean getMousePreview() {
		return mousePreview;
	}
	
	public static int getCellSize() {
		return cellSize;
	}
	
	public static int getWidth() {
		return w;
	}
	
	public static int getHeight() {
		return h;
	}
}