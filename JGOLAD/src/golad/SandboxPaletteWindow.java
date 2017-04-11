package golad;

import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SandboxPaletteWindow {

	private JFrame frmPalette;
	private JComboBox<Cellstate> comboBoxCellstate;
	
	public SandboxPaletteWindow() {
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void initialize() {
		frmPalette = new JFrame();
		frmPalette.setTitle("Palette");
		frmPalette.setBounds(100, 100, 200, 300);
		frmPalette.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmPalette.setType(Type.UTILITY);
		frmPalette.getContentPane().setLayout(null);
		frmPalette.setAlwaysOnTop(true);
		frmPalette.setResizable(false);

		comboBoxCellstate = new JComboBox<Cellstate>();
		comboBoxCellstate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.sandboxByte = ((Cellstate) comboBoxCellstate.getSelectedItem()).getID();
			}
		});
		Iterator<Cellstate> it = Cellstate.getAll().iterator();
		while (it.hasNext()) {
			comboBoxCellstate.addItem(it.next());
		}
		comboBoxCellstate.setSelectedItem(Cellstate.NEUTRAL);
		comboBoxCellstate.setBounds(10, 11, 174, 20);
		frmPalette.getContentPane().add(comboBoxCellstate);

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
		btnRandomize.setBounds(10, 203, 174, 23);
		frmPalette.getContentPane().add(btnRandomize);

		JButton buttonClear = new JButton("Clear");
		buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.getCurrentBoard().clear();
			}
		});
		buttonClear.setBounds(10, 169, 174, 23);
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
		comboBoxRules.setBounds(10, 42, 174, 20);
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
		btnSave.setBounds(10, 237, 81, 23);
		frmPalette.getContentPane().add(btnSave);

		JCheckBox chckbxToroid = new JCheckBox("Sides loop (Toroidal)");
		chckbxToroid.setBounds(10, 105, 178, 23);
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
		btnLoad.setBounds(103, 237, 81, 23);
		frmPalette.getContentPane().add(btnLoad);

		JButton buttonResize = new JButton("Resize");
		buttonResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int size = WinUtil.getInputInt("Enter size", 10);
				Main.getCurrentBoard().resize(size, size);
			}
		});
		buttonResize.setBounds(10, 135, 174, 23);
		frmPalette.getContentPane().add(buttonResize);
		
		JCheckBox chckbxCrazy = new JCheckBox("Rainbow mode");
		chckbxCrazy.setBounds(10, 79, 174, 23);
		chckbxCrazy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Board.crazymode = chckbxCrazy.isSelected();
			}
		});
		frmPalette.getContentPane().add(chckbxCrazy);

		frmPalette.setVisible(true);
	}

	public void setActiveCellstate(Cellstate cs){
		this.comboBoxCellstate.setSelectedItem(cs);
	}
	
	public void kill() {
		Main.sandboxByte = 0;
		frmPalette.dispose();
	}
}
