package window;

import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import game.board.Board;
import game.board.Cellstate;
import game.board.CellstateGroup;
import game.rules.LifeRules;
import golad.Main;
import util.SerializeUtil;
import util.ShuffleAlgorithm;
import util.WinUtil;

import javax.swing.JLabel;

public class SandboxPaletteWindow {

	private JLabel lblSelected;
	private JFrame frmPalette;
	//private JComboBox<Cellstate> comboBoxCellstate;
	
	public SandboxPaletteWindow() {
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void initialize() {
		frmPalette = new JFrame();
		frmPalette.setTitle("Palette");
		frmPalette.setBounds(100, 100, 250, 440);
		frmPalette.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmPalette.setType(Type.UTILITY);
		frmPalette.getContentPane().setLayout(null);
		frmPalette.setAlwaysOnTop(true);
		frmPalette.setResizable(false);

//		comboBoxCellstate = new JComboBox<Cellstate>();
//		comboBoxCellstate.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Main.sandboxByte = ((Cellstate) comboBoxCellstate.getSelectedItem()).getID();
//			}
//		});
//		Iterator<Cellstate> it = Cellstate.getAll().iterator();
//		while (it.hasNext()) {
//			comboBoxCellstate.addItem(it.next());
//		}
//		comboBoxCellstate.setSelectedItem(Cellstate.NEUTRAL);
//		comboBoxCellstate.setBounds(10, 11, 174, 20);
		//frmPalette.getContentPane().add(comboBoxCellstate);

		JButton btnRandomize = new JButton("Generate New...");
		btnRandomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] possibilities = ShuffleAlgorithm.shufflers;
				Object o = JOptionPane.showInputDialog(frmPalette, "Select a board generator", "JGOLAD",
						JOptionPane.PLAIN_MESSAGE, null, possibilities, possibilities[0]);
				if(o instanceof ShuffleAlgorithm){
					ShuffleAlgorithm sa = (ShuffleAlgorithm) o;
					Board b = Main.getCurrentBoard();
					b.clear();
					sa.shuffle(b, b.getWidth(), b.getHeight());
				}
			}
		});
		btnRandomize.setBounds(96, 343, 138, 23);
		frmPalette.getContentPane().add(btnRandomize);

		JButton buttonClear = new JButton("Clear");
		buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getCurrentBoard().clear();
			}
		});
		buttonClear.setBounds(10, 343, 76, 23);
		frmPalette.getContentPane().add(buttonClear);

		JComboBox<LifeRules> comboBoxRules = new JComboBox<LifeRules>();
		Iterator<LifeRules> itRules = LifeRules.getRuleList().iterator();
		while (itRules.hasNext()) {
			comboBoxRules.addItem(itRules.next());
		}
		comboBoxRules.setSelectedItem(LifeRules.rulesGOL);
		comboBoxRules.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.getCurrentGame().setRules((LifeRules) comboBoxRules.getSelectedItem());
				Main.getCurrentGame().getRules().onSelected();
			}
		});
		comboBoxRules.setBounds(10, 232, 224, 20);
		frmPalette.getContentPane().add(comboBoxRules);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = WinUtil.getUserFile(frmPalette);
				if (f != null) {
					SerializeUtil.save(Main.getCurrentBoard(), f);
				}
			}
		});
		btnSave.setBounds(10, 377, 101, 23);
		frmPalette.getContentPane().add(btnSave);

		JCheckBox chckbxToroid = new JCheckBox("Sides loop (Toroidal)");
		chckbxToroid.setBounds(10, 279, 174, 23);
		frmPalette.getContentPane().add(chckbxToroid);
		chckbxToroid.setSelected(Main.getCurrentBoard().isToroidal());
		chckbxToroid.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Main.getCurrentBoard().setToroidal(chckbxToroid.isSelected());
			}
		});

		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = WinUtil.getUserFile(frmPalette);
				if (f != null) {
					Object o = SerializeUtil.load(f);
					if (o instanceof Board) {
						((Board) o).setChanged(true);
						Main.getCurrentGame().setBoard((Board) o);
						chckbxToroid.setSelected(((Board) o).isToroidal());
					}
				}
			}
		});
		btnLoad.setBounds(121, 377, 113, 23);
		frmPalette.getContentPane().add(btnLoad);

		JButton buttonResize = new JButton("Resize");
		buttonResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int size = WinUtil.getInputInt("Enter size", 10);
				Main.getCurrentBoard().resize(size, size);
			}
		});
		buttonResize.setBounds(10, 309, 224, 23);
		frmPalette.getContentPane().add(buttonResize);
		
		JCheckBox chckbxCrazy = new JCheckBox("Rainbow mode");
		chckbxCrazy.setBounds(10, 259, 174, 23);
		chckbxCrazy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Board.crazymode = chckbxCrazy.isSelected();
			}
		});
		frmPalette.getContentPane().add(chckbxCrazy);
		
		lblSelected = new JLabel("");
		lblSelected.setBounds(10, 208, 224, 14);
		frmPalette.getContentPane().add(lblSelected);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 224, 186);
		Iterator<CellstateGroup> itGroups = CellstateGroup.getGroupList().iterator();
		while(itGroups.hasNext()){
			CellstateGroup cg = itGroups.next();
			JPanelCellGroup jpcg =  new JPanelCellGroup(cg);
			jpcg.addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent e) {
				}
				@Override
				public void mousePressed(MouseEvent e) {
					Cellstate cs = jpcg.getAt(e.getX(), e.getY());
					setActiveCellstate(cs);
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
			jpcg.addMouseMotionListener(new MouseMotionListener(){
				@Override
				public void mouseDragged(MouseEvent e) {
					mouseMoved(e);
				}
				@Override
				public void mouseMoved(MouseEvent e) {
					Cellstate cs = jpcg.getAt(e.getX(), e.getY());
					jpcg.setToolTipText(cs.toString());
					jpcg.createToolTip().setVisible(true);
				}
			});
			tabbedPane.add(cg.getName(), jpcg);
		}
		frmPalette.getContentPane().add(tabbedPane);
		
		frmPalette.setVisible(true);
	}

	public void setActiveCellstate(Cellstate cs){
		Main.sandboxByte = cs.getID();
		lblSelected.setText("Selected: "+cs.toString());
		lblSelected.setToolTipText("<html>"+lblSelected.getText()+"<br>"+cs.getHTMLSummary()+"</html>");
	}
	
	public void kill() {
		Main.sandboxByte = 0;
		frmPalette.dispose();
	}
}
