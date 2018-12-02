package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JTextField;

import hauptmenu.PongFrame;
import pongtoolkit.ImageLoader;

public class MenuTextField extends JTextField {
	private static final long serialVersionUID = -6725442579572733516L;
	private BufferedImage background = ImageLoader.loadBufferedImage("Hintergrund.png");
	private Dimension size = new Dimension(400, 100);
	private Dimension backgroundSize = new Dimension(400, 100);
	private boolean drawBackground = true, drawBorder = true;

	public MenuTextField(PongFrame pongFrame, String text) {
		super.setText(text);
		setSize(size);

		this.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
		this.setOpaque(false);
	}

	public void setSize(Dimension size) {
		super.setSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.setPreferredSize(size);
		setBackgroundSize(size);
	}

	public String toString() {
		return this.getText();
	}

	public void setAutoFontSize(boolean autoFontSize) {
	}

	public void drawBackground(boolean drawBackground) {
		this.drawBackground = drawBackground;
	}

	public void setBackgroundSize(Dimension size) {
		this.backgroundSize = size;
	}

	public void setBorderDrawing(boolean drawBorder) {
		this.drawBorder = drawBorder;
	}

	public void setBorderColor(Color borderColor) {
	}

	@Override
	protected void paintComponent(Graphics g) {
		this.setForeground(getContrastColor(new Color(background.getRGB(10, 10))));
		super.paintComponent(g);
		if (drawBackground)
			g.drawImage(background, 0, 0, backgroundSize.width, backgroundSize.height, null);
		if (drawBorder) {
			g.drawLine(0, 0, backgroundSize.width, 0); // Linie oben
			g.drawLine(0, 0, 0, backgroundSize.height); // Linie Links
			g.drawLine(0, backgroundSize.height - 1, backgroundSize.width, backgroundSize.height - 1); // Linie unten
			g.drawLine(backgroundSize.width - 1, 0, backgroundSize.width - 1, backgroundSize.height); // Linie Rechts
		}
	}

	private Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}
}
