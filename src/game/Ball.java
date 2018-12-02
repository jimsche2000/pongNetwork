package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class Ball extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ball() {
		super();
		setBackground(Color.BLACK);
		this.setLayout(null);
	}

	public void paint(Graphics gr) {
		Graphics2D g2d = (Graphics2D) gr;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.fillOval(0, 0, this.getWidth() - 1, this.getHeight());
	}
}