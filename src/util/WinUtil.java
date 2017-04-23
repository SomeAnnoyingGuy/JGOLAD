package util;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
		Object[][] data = new Object[width][height];
		type = type.toLowerCase();
		if (!Arrays.asList(types).contains(type))
			throw new IllegalArgumentException("Invalid or unsuported type");
		
		TableDialog td = new TableDialog(Main.getFrame(), msg, width, height, type);
		td.setVisible(true);
		td.dispose();
		data = td.getData();
		return data;
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
