package golad;

import java.io.File;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class WinUtil {
	public static File getUserFile(JFrame frame) {
		JFileChooser fileChooser = new JFileChooser();
		try {
			fileChooser.setCurrentDirectory(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
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
		String ret = JOptionPane.showInputDialog(message, def);
		if(ret == null){
			ret = def;
		}
		return ret;
	}
	
	public static int getInputInt(String msg, int i){
		try{
			return Integer.parseInt(getInput(msg,i+""));
		}catch(Exception e){
			return i;
		}
	}

	public static void display(String string) {
		JOptionPane.showMessageDialog(null, string);
	}
}
