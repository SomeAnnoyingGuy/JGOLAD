package window;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import jgolad.Guesser;
import util.OSUtil;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
				+ "The goal is to raise awareness and appreciation for cellular automata." + "</html>");
		Font font = lblText.getFont();
		int style = font.getStyle();
		// remove bold from it
		style &= ~Font.BOLD;
		font = font.deriveFont(style);
		lblText.setFont(font);
		lblText.setVerticalAlignment(SwingConstants.TOP);
		lblText.setBounds(5, 5, 279, 49);
		contentPane.add(lblText);

		JLabel lblGit = new JLabel("<html><FONT color=\"#000099\"><U>GitHub Repository</U></FONT></html>");
		lblGit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				OSUtil.openWebpage("https://github.com/SomeAnnoyingGuy/JGOLAD");
			}
		});
		lblGit.setHorizontalAlignment(SwingConstants.CENTER);
		lblGit.setBounds(5, 65, 279, 14);
		contentPane.add(lblGit);
		
		JLabel lblDiscord = new JLabel("<html><FONT color=\"#000099\"><U>Join Discord Server</U></FONT></html>");
		lblDiscord.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				OSUtil.openWebpage("https://discord.gg/5aB36ub");
			}
		});
		lblDiscord.setHorizontalAlignment(SwingConstants.CENTER);
		lblDiscord.setBounds(5, 90, 279, 14);
		contentPane.add(lblDiscord);
		
		JButton btnRuleGuesser = new JButton("Rule Guesser");
		btnRuleGuesser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Guesser.start();
			}
		});
		btnRuleGuesser.setBounds(10, 237, 274, 23);
		contentPane.add(btnRuleGuesser);
	}
}
