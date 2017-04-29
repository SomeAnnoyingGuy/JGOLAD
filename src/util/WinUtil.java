package util;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jgolad.Main;

public class WinUtil {
	public static File getUserFile(JFrame frame) {
		JFileChooser fileChooser = new JFileChooser();
		try {
			fileChooser.setCurrentDirectory(
					new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile;
		}
		return null;
	}

	public static String getInput(String message, String def) {
		String ret = JOptionPane.showInputDialog(Main.getFrame(),message, def);
		if (ret == null) {
			ret = def;
		}
		return ret;
	}

	public static int getInputInt(String msg, int i) {
		try {
			return Integer.parseInt(getInput(msg, i + ""));
		} catch (Exception e) {
			return i;
		}
	}

	public static void display(String string) {
		JOptionPane.showMessageDialog(null, string);
	}

	// IQuick's tabular input getter
	public static String[] types = { "int", "string", "bool", "boolean" };

	public static Object[][] getInputTable(String msg, int width, int height, String type)
			throws IllegalArgumentException {
		type = type.toLowerCase();
		if (!Arrays.asList(types).contains(type))
			throw new IllegalArgumentException("Invalid or unsuported type");
		
		TableDialog td = new TableDialog(Main.getFrame(), msg, width, height, type);
		td.setVisible(true);
		td.dispose();
		return td.getData();
	}
	
	public static String getChoice(String msg, String[] choices) {
		JDialog dialog = new JDialog(Main.getFrame());
		dialog.setModal(true);
		JComboBox<String> choice = new JComboBox<String>(choices);
		choice.setSelectedIndex(0);
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		dialog.add(new JLabel(msg), c);
		c.gridx = 0;
		c.gridy = 1;
		dialog.add(choice, c);
		JButton confirm = new JButton("Ok");
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}});
		c.gridx = 0;
		c.gridy = 2;
		dialog.add(confirm, c);
		dialog.pack();
		dialog.setLocationRelativeTo(Main.getFrame());
		dialog.setVisible(true);
		return (String) choice.getSelectedItem();
	}
	
	public static boolean getYN(String msg) {
		int reply = JOptionPane.showConfirmDialog(Main.getFrame(), msg, "", JOptionPane.YES_NO_OPTION);
		return reply == JOptionPane.YES_OPTION;
	}
	
	public static String getLargeInput(String msg) {
		JDialog dialog = new JDialog(Main.getFrame());
		dialog.setModal(true);
		JTextArea inputArea= new JTextArea();
		inputArea.setFont(new Font("monospaced", Font.PLAIN, 12));
		inputArea.setColumns(25);
		inputArea.setRows(5);
		inputArea.setEditable(true);
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		dialog.add(new JLabel(msg), c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = 2;
		dialog.add(inputArea, c);
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		JButton confirm = new JButton("Ok");
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}});
		dialog.add(confirm, c);
		dialog.pack();
		dialog.setLocationRelativeTo(Main.getFrame());
		dialog.setVisible(true);
		return inputArea.getText();
	}
}

@SuppressWarnings("serial")
class TableDialog extends JDialog {

	private int width;
	private int height;
	private String type;
	private Component[][] inputs;
	
	public TableDialog(JFrame parrent, String msg, int width, int height, String type) {
		super(parrent);
		this.width = width;
		this.height = height;
		this.type = type;
		this.inputs = new Component[width][height];
		this.setModal(true);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = width;
		add(new JLabel(msg), c);
		c.gridwidth = 1;
		for (int i = 0; i < width; i++) {
			c.gridx = i;
			for (int j = 0; j < height; j++) {
				c.gridy = j + 1;
				Component inputObject = null;
				if (type == "int" || type == "string") {
					inputObject = new JTextField();
				}
				if (type == "bool" || type == "boolean") {
					inputObject = new JCheckBox();
				}
				inputs[i][j] = inputObject;
				add(inputObject, c);
			}
		}
		
		c.gridx = 0;
		c.gridy = height+1;
		c.gridwidth = width;
		JButton confirm = new JButton("Confirm");
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}});
		add(confirm, c);
		
		this.pack();
		this.setLocationRelativeTo(parrent);
	}
	
	public Object[][] getData() {
		Object[][] data = new Object[this.width][this.height];
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (type == "string") {
					data[i][j] = ((JTextField) this.inputs[i][j]).getText();
				}
				if (type == "int") {
					try {
						data[i][j] = Integer.parseInt(((JTextField) this.inputs[i][j]).getText());
					} catch (Exception e) {
						data[i][j] = 0;
					}
				}
				if (type == "bool" || type == "boolean") {
					data[i][j] = ((JCheckBox) this.inputs[i][j]).isSelected();
				}
			}
		}
		return data;
	}
}
