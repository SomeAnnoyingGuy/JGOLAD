package window;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ExtrasMenu extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ExtrasMenu() {
		setResizable(false);
		setTitle("Extras");
		setBounds(100, 100, 300, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblText = new JLabel("<html><b>JGOLAD</b> is a game based off of Carykh's GOLAD. "
				+ "The goal is to raise awareness and appreciation for cellular automata."
				+ "</html>");
		Font font = lblText.getFont();
		int style = font.getStyle();
		// remove bold from it
		style &= ~Font.BOLD;
		font = font.deriveFont(style);
		lblText.setFont(font);
		lblText.setVerticalAlignment(SwingConstants.TOP);
		lblText.setBounds(5, 5, 279, 56);
		contentPane.add(lblText);
	}

}
