package golad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class GameSetupWindow {
	private JFrame frmGameSetup = new JFrame();
	
	
	public static Game createGame(){
		GameLocal g = new GameLocal(null, null);
		GameSetupWindow win = new GameSetupWindow();
		Thread winThread = new Thread(new Runnable(){
			@Override
			public void run() {
				win.initialize(g);
			}
		});
		winThread.start();
		return g;
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize(GameLocal g) {
		frmGameSetup.setTitle("Game Setup");
		frmGameSetup.setBounds(100, 100, 280, 330);
		frmGameSetup.getContentPane().setLayout(null);
		frmGameSetup.setLocationRelativeTo(null);
		frmGameSetup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SpinnerNumberModel spinModel = new SpinnerNumberModel(2, 2, 4, 1);
		
		spinModel = new SpinnerNumberModel(20, 5, 100, 1);
		
		JSpinner spinnerSize = new JSpinner(spinModel);
		spinnerSize.setBounds(214, 228, 40, 20);
		frmGameSetup.getContentPane().add(spinnerSize);
		
		JCheckBox chckbxToroidalBoard = new JCheckBox("Toroidal Board");
		chckbxToroidalBoard.setBounds(6, 227, 131, 23);
		frmGameSetup.getContentPane().add(chckbxToroidalBoard);
		
		JComboBox<ShuffleAlgorithm> comboBoxShuffle = new JComboBox<ShuffleAlgorithm>();
		comboBoxShuffle.setBounds(10, 100, 244, 20);
		frmGameSetup.getContentPane().add(comboBoxShuffle);
		ShuffleAlgorithm[] arr = ShuffleAlgorithm.shufflers;
		for(int i = 0; i < arr.length; i++){
			comboBoxShuffle.addItem(arr[i]);
		}
		
		JComboBox<LifeRules> comboBoxRules = new JComboBox<LifeRules>();
		comboBoxRules.setBounds(10, 149, 244, 20);
		frmGameSetup.getContentPane().add(comboBoxRules);
		Iterator<LifeRules> itlr = LifeRules.getRuleList().iterator();
		while(itlr.hasNext()){
			comboBoxRules.addItem(itlr.next());
		}
		comboBoxRules.setSelectedItem(LifeRules.rulesGOL);
		comboBoxRules.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				((LifeRules)comboBoxRules.getSelectedItem()).onSelected();
			}
		});
		
		JCheckBox cbPL1 = new JCheckBox("PL1");
		cbPL1.setSelected(true);
		cbPL1.setBounds(6, 7, 55, 23);
		frmGameSetup.getContentPane().add(cbPL1);
		
		JCheckBox cbPL2 = new JCheckBox("PL2");
		cbPL2.setSelected(true);
		cbPL2.setBounds(63, 7, 55, 23);
		frmGameSetup.getContentPane().add(cbPL2);
		
		JCheckBox cbPL3 = new JCheckBox("PL3");
		cbPL3.setBounds(115, 7, 55, 23);
		frmGameSetup.getContentPane().add(cbPL3);
		
		JCheckBox cbPL4 = new JCheckBox("PL4");
		cbPL4.setBounds(172, 7, 55, 23);
		frmGameSetup.getContentPane().add(cbPL4);
		
		JCheckBox cbPL1Bot = new JCheckBox("Bot");
		cbPL1Bot.setSelected(true);
		cbPL1Bot.setBounds(6, 33, 55, 23);
		frmGameSetup.getContentPane().add(cbPL1Bot);
		
		JCheckBox cbPL2Bot = new JCheckBox("Bot");
		cbPL2Bot.setSelected(true);
		cbPL2Bot.setBounds(63, 33, 55, 23);
		frmGameSetup.getContentPane().add(cbPL2Bot);
		
		JCheckBox cbPL3Bot = new JCheckBox("Bot");
		cbPL3Bot.setSelected(true);
		cbPL3Bot.setBounds(115, 33, 55, 23);
		frmGameSetup.getContentPane().add(cbPL3Bot);
		
		JCheckBox cbPL4Bot = new JCheckBox("Bot");
		cbPL4Bot.setSelected(true);
		cbPL4Bot.setBounds(172, 33, 55, 23);
		frmGameSetup.getContentPane().add(cbPL4Bot);
		
		JButton btnBegin = new JButton("Begin");
		btnBegin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.setBoard(new Board((Integer) spinnerSize.getValue()));
				g.getBoard().setToroidal(chckbxToroidalBoard.isSelected());
				g.setRules((LifeRules) comboBoxRules.getSelectedItem());
				ShuffleAlgorithm sa = (ShuffleAlgorithm) comboBoxShuffle.getSelectedItem();
				sa.shuffle(g.getBoard());
				if(cbPL1.isSelected()){
					if(cbPL1Bot.isSelected()){
						g.addPlayer(new Player(Cellstate.PL1,"Player 1 (Bot)"));
					}else{
						g.addPlayer(new PlayerLocalHuman(Cellstate.PL1,"Player 1 (Human)"));
					}
				}
				if(cbPL2.isSelected()){
					if(cbPL2Bot.isSelected()){
						g.addPlayer(new Player(Cellstate.PL2,"Player 2 (Bot)"));
					}else{
						g.addPlayer(new PlayerLocalHuman(Cellstate.PL2,"Player 2 (Human)"));
					}
				}
				if(cbPL3.isSelected()){
					if(cbPL3Bot.isSelected()){
						g.addPlayer(new Player(Cellstate.PL3,"Player 3 (Bot)"));
					}else{
						g.addPlayer(new PlayerLocalHuman(Cellstate.PL3,"Player 3 (Human)"));
					}
				}
				if(cbPL4.isSelected()){
					if(cbPL4Bot.isSelected()){
						g.addPlayer(new Player(Cellstate.PL4,"Player 4 (Bot)"));
					}else{
						g.addPlayer(new PlayerLocalHuman(Cellstate.PL4,"Player 4 (Human)"));
					}
				}
				if(g.getPlayer(0) instanceof PlayerLocalHuman){
					g.prepareForHumanStart();
				}
				frmGameSetup.dispose();
			}
		});
		btnBegin.setBounds(10, 257, 244, 23);
		frmGameSetup.getContentPane().add(btnBegin);
		
		JLabel lblSize = new JLabel("Size:");
		lblSize.setBounds(172, 231, 32, 14);
		frmGameSetup.getContentPane().add(lblSize);
		
		JLabel lblNewLabel = new JLabel("Start Generator");
		lblNewLabel.setBounds(10, 81, 244, 14);
		frmGameSetup.getContentPane().add(lblNewLabel);
		
		JLabel lblLifeRules = new JLabel("Life Rules");
		lblLifeRules.setBounds(10, 131, 244, 14);
		frmGameSetup.getContentPane().add(lblLifeRules);
		
		frmGameSetup.setVisible(true);
	}
}
