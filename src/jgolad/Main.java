package jgolad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ToolTipManager;

import game.Game;
import game.GameLocal;
import game.GameSandbox;
import game.Player;
import game.board.Board;
import game.board.Cellstate;
import game.rules.LifeRules;
import util.ImageUtil;
import util.IntroCrap;
import util.TinyThing;
import window.ExtrasMenu;
import window.GameSetupWindow;

public class Main {
	public static int FPS = 20;

	public static Dimension PANEL_SIZE = new Dimension(1000, 700);

	private static Game currentGame = null;

	private static BufferedImage boardCacheImage;
	private static boolean spaceDown = false;
	private static boolean predictions,mousePreview;
	private static int cellSize = 10;
	private static int cSize = 300;
	
	public static int mouseCellX,mouseCellY;
	
	public static byte sandboxByte = 0;
	
	private static JPanel panel;
	private static JFrame frame;
	
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

		BufferedImage logo = ImageUtil.loadImage("/Logo.png");

		Font optionFont = new Font("Calibri", 0, 40);
		Font plrFont = new Font("Tahoma", 0, 20);
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			TinyThing tinything = IntroCrap.getRandomTinyThing();
			int timeThingMax = FPS * 8;
			int timeThing = 0;
			double timeFade = timeThingMax / 4;
			double fade = timeFade;

			@Override
			public void paintComponent(Graphics gee) {
				Graphics2D g = (Graphics2D) gee;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int w = this.getWidth();
				int h = this.getHeight();
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, w, h);
				
				cSize = w-h;
				int cStart = w-cSize;
				
				Board board = getCurrentBoard();
				
				if (board != null) {
					if(board.isChanged()){
						boolean showP = predictions;
						boolean showMouseOver = mousePreview;
						int cacheSize = cellSize;
						if(!(board.getWidth() <= 50 || board.getHeight() <= 50)){
							cacheSize = 1;
							showP = false;
							showMouseOver = false;
						}
						boardCacheImage = new BufferedImage(board.getWidth()*cacheSize,board.getHeight()*cacheSize,BufferedImage.TYPE_INT_RGB);
						Graphics2D imgG = boardCacheImage.createGraphics();
						
						for (int x = 0; x < getCurrentBoard().getWidth(); x++) {
							for (int y = 0; y < getCurrentBoard().getHeight(); y++) {
								Color cellColor = getCurrentBoard().getColorFor(x, y);
								int rx = x * cacheSize;
								int ry = y * cacheSize;
								int rs = cacheSize;
								if(cellColor != null){
									imgG.setColor(cellColor);
									imgG.fillRect(rx, ry, rs, rs);
								}
								if(showP){
									Board pb = board.getPredictionBoard(getCurrentGame(),getCurrentGame().getRules());
									cellColor = pb.getColorFor(x, y);
									if(cellColor != null){
										int pSize = rs/3;
										imgG.setColor(cellColor);
										imgG.fillRect(rx+(rs/2)-(pSize/2), ry+(rs/2)-(pSize/2), pSize, pSize);
									}
								}
								if(showMouseOver){
									cellColor = Cellstate.getStateFromID(sandboxByte).getColor();
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
					g.drawImage(boardCacheImage, 0, 0, cStart, h, null);
					board.setChanged(false);
					
					g.setColor(Color.DARK_GRAY);
					g.fillRect(cStart, 0, cSize, h);
					Iterator<Player> it = currentGame.getPlayers().iterator();
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
						g.drawString(currentGame.getPlayerScore(p)+"", cStart + 10, yP+g.getFontMetrics().getHeight()*2);
						if(pCount == getCurrentGame().getHighlightedPlayerID()){
							int osize = 20;
							g.setColor(Color.WHITE);
							g.fillOval(w-osize, yP + hP/2 - osize/2,osize, osize);
						}
						pCount++;
					}
				} else {
					int lh = (int) ((double) logo.getHeight() * ((double) w / (double) logo.getWidth()));
					g.drawImage(logo, 0, 0, w, lh, null);
					timeThing++;
					tinything.render(g,lh);
					g.setColor(new Color(0, 0, 0, (int) (255 * (fade / timeFade))));
					g.fillRect(550, lh+50, 300, 300);
					if (timeThing >= timeThingMax) {
						timeThing = 0;
						tinything = IntroCrap.getRandomTinyThing();
						fade = timeFade;
					} else if (timeThing >= timeThingMax - timeFade || timeThing - timeFade <= 0) {
						if (fade < timeFade) {
							fade++;
						}
					} else {
						if (fade > 0) {
							fade--;
						}
					}
					g.setColor(Color.LIGHT_GRAY);
					g.setFont(optionFont);
					g.drawString("[1] Local Game (WIP)", 100, lh+100);
					g.drawString("[2] Sandbox", 125, lh+150);
					g.drawString("[3] Extras", 150, lh+200);
				}
			}
		};
		panel.setIgnoreRepaint(true);
		panel.setPreferredSize(PANEL_SIZE);
		frame.add(panel);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		
		panel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (getCurrentBoard() != null) {
					Board b = getCurrentBoard();
					double panelToImageScaleW = (double)(b.getWidth()*cellSize)/(panel.getWidth()-cSize);
					double panelToImageScaleH = (double)(b.getHeight()*cellSize)/(panel.getHeight());
					mouseCellX = (int) (((double)(e.getX()*panelToImageScaleW))/cellSize);
					mouseCellY = (int) (((double)(e.getY()*panelToImageScaleH))/cellSize);
					if(mousePreview){
						getCurrentBoard().setChanged(true);
					}
				}
			}
		});
		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				Game game = getCurrentGame();
				if(game != null){
					game.onMousePress(e);
				}
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

			@Override
			public void keyPressed(KeyEvent e) {
				if (currentGame == null) {
					spaceDown = false;
					if (e.getKeyCode() == KeyEvent.VK_2) {
						Board b = new Board(20);
						setCurrentGame(new GameSandbox(b, LifeRules.rulesGOL));
						musicPlayer.stop();
						musicPlayer.setList(gameMusicQueue);
						musicPlayer.play();
					}else if (e.getKeyCode() == KeyEvent.VK_1) {
						setCurrentGame(GameSetupWindow.createGame());
						musicPlayer.stop();
						musicPlayer.setList(gameMusicQueue);
						musicPlayer.play();
					}else if (e.getKeyCode() == KeyEvent.VK_3) {
						ExtrasMenu em = new ExtrasMenu();
						em.setVisible(true);
					}
				}else{
					if (e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
						spaceDown = true;
					}else if(e.getKeyCode() == KeyEvent.VK_P){
						predictions = !predictions;
						Board b = getCurrentBoard();
						if(b != null){
							b.setChanged(true);
						}
					}else if(e.getKeyCode() == KeyEvent.VK_M){
						mousePreview = !mousePreview;
						getCurrentBoard().setChanged(true);
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SLASH) {
					spaceDown = false;
				}else if(e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET){
					Board b = getCurrentBoard();
					if(b != null){
						b.updateAll(getCurrentGame(),getCurrentGame().getRules());
					}
				}else if(e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET){
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
						for(int i = 0; i < amount; i++){
							b.updateAll(getCurrentGame(),getCurrentGame().getRules());
						}
					}
				}else if(e.getKeyCode() == KeyEvent.VK_C){
					Game ga = getCurrentGame();
					if(ga != null){
						ga.getBoard().updateCensus();
					}
				}else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					if(currentGame != null){
						currentGame.kill();
						currentGame = null;
						musicPlayer.stop();
						musicPlayer = new Music(menuMusicQueue);
						musicPlayer.play();
					}
					

				}else if(e.getKeyCode() == KeyEvent.VK_SPACE){
					Game g = getCurrentGame();
					if(g instanceof GameLocal){
						((GameLocal)g).space();
					}
				}else if(e.getKeyCode() == KeyEvent.VK_S){
					Board board = getCurrentBoard();
					if(board != null){
						board.saveImage();
					}
				}
			}
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

	public static JFrame getFrame() {
		return frame;
	}

	public static void invalidateBoardCache() {
		boardCacheImage = null;
	}
}
