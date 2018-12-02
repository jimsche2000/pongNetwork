package game;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import pongtoolkit.ImageLoader;

public class Schlag extends JLabel {
	/**
		 * 
		 */
	private static final long serialVersionUID = 3819804074948555813L;
	private ImageIcon schlagIcon;

	public Schlag(int x, int y, int width, int height) {
		this.setLocation(x, y);
		this.setSize(width, height);
		schlagIcon = ImageLoader.loadIcon("Schläger.png", width, height);
		this.setIcon(schlagIcon);
		setBackground(Color.BLACK);
	}
}
