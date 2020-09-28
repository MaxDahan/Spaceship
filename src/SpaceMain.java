 import java.awt.Color;
import java.awt.Toolkit;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JFrame;

// Welcome to Spaceship!
// This program was written by Paul Karmel in 2018.
// Please view the Readme for more information.
// I apologize for the poor program style. It was developed in two days.
// Press "R" to reset your high-score.
// Press "C" to close the program.

public class SpaceMain extends JFrame {
	public final static int BORDER_SIZE = 50;
	public final static int GWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - BORDER_SIZE*2;
	public final static int GHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - BORDER_SIZE*2;
	
	public static void main(String[] args) throws Exception {
		new SpaceMain();
	}
	public SpaceMain() throws Exception {
		requestFocus();
		setUndecorated(true);
		//setAlwaysOnTop(true);
		setSize(GWIDTH + BORDER_SIZE * 2, GHEIGHT + BORDER_SIZE * 2);
		setBackground(new Color(84, 15, 221, 50));
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, BORDER_SIZE));
		
		SpaceWindow sw = new SpaceWindow();
		sw.setLayout(null);
		addKeyListener(sw);
		add(sw);
		
		setVisible(true);
	}
}
