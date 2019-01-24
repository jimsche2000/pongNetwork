package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class Ball extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ball() {
		super();
		setBackground(Color.BLACK);
//		this.setLayout(null);
		this.setDoubleBuffered(true);
	}

	public void paint(Graphics gr) {
		super.paint(gr);
		Graphics2D g2d = (Graphics2D) gr;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
//		g2d.translate(tx, ty);
		g2d.fillOval(0, 0, this.getWidth() - 1, this.getHeight()-1);
//		g2d.drawOval(0, 0, this.getWidth() - 1, this.getHeight());
//		System.out.println("Ball double Buffered? "+isDoubleBuffered());
	}
}