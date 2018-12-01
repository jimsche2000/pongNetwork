package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class ShadowLabel extends JLabel {

	private String text;
	private Font f;
	private Dimension size;
	private boolean invertColors = false;
	private static final long serialVersionUID = 1L;
	
	public ShadowLabel() {
		super();
	}

	public ShadowLabel(String text, int size, int width, int height) {
		super();
		this.text = text;
		f = new Font("Dialog", 1, size);
		this.size = new Dimension(width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D) g;
		// ////////////////////////////////////////////////////////////// //
		// antialiasing
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// ////////////////////////////////////////////////////////////// //

		/**
		 * draw text
		 */
		if (!invertColors) {
			g2D.setFont(f);
			g2D.setColor(new Color(0, 0, 0));
			g2D.drawString(this.text, 1, size.height);
			g2D.setColor(new Color(255, 255, 255, 230));
			g2D.drawString(this.text, 0, size.height - (int) Math.round(f.getSize() / 7));
		} else {
			g2D.setFont(f);
			g2D.setColor(new Color(255, 255, 255, 230));
			g2D.drawString(this.text, 1, size.height);
			g2D.setColor(new Color(0, 0, 0));
			g2D.drawString(this.text, 0, size.height - (int) Math.round(f.getSize() / 7));
		}
		g2D.dispose();

	}

	public void setInvertColors(boolean invertColors) {
		this.invertColors = invertColors;
	}

	public void setText(String text) {
		this.text = text;
		repaint();
	}
}