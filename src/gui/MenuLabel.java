package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import hauptmenu.PongFrame;
import pongtoolkit.ImageLoader;

public class MenuLabel extends JLabel {

	private static final long serialVersionUID = -6725442579572733516L;
	private BufferedImage background = ImageLoader.loadBufferedImage("Hintergrund.png");
	private Dimension size = new Dimension(400, 100);
	private boolean autoResizeFont = false, firstTime = true, drawBackground = true, isForegroundColorSet = false,
			drawBottomLine = false;;
	private PongFrame pongFrame;

	public MenuLabel(PongFrame pongFrame, String text) {
		this.pongFrame = pongFrame;
		super.setText(text);
		setSize(size);

		this.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12f)); // 20f
		this.setOpaque(false);
	}

	public void setSize(Dimension size) {
		super.setSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
	}

	public void setFont(Font font) {
		autoResizeFont = false;
		super.setFont(font);
	}

	public void setAutoFontSize(boolean autoFontSize) {
		autoResizeFont = autoFontSize;
	}

	public void setDrawBackground(boolean drawBackground) {
		this.drawBackground = drawBackground;
	}

	public void setForeground(Color c) {
		isForegroundColorSet = true;
		super.setForeground(c);
	}

	public void setDrawBottomLine(boolean drawBottomLine) {
		this.drawBottomLine = drawBottomLine;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (drawBackground)
			g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);

		if (!isForegroundColorSet)
			this.setForeground(getContrastColor(new Color(background.getRGB(10, 10))));

		Font tempFont = pongFrame.getGLOBAL_FONT();
		if (autoResizeFont && (firstTime)) {
			firstTime = false;

			float size = getMaxFontSizeForControl(this, this.getText(), g, this.getFont());
			tempFont = tempFont.deriveFont(size);
			this.setFont(tempFont);

		} else {
			tempFont = this.getFont();
		}
		if (this.getHorizontalTextPosition() == SwingConstants.LEFT) {
			FontMetrics metrics = g.getFontMetrics(tempFont);
			// Determine the Y coordinate for the text (note we add the ascent, as in java
			// 2d 0 is top of the screen)
			int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
			g.setFont(tempFont);
			g.drawString(this.getText(), 0, y);
		} else {
			drawCenteredString(g, this.getText(), new Rectangle(0, 0, getWidth(), getHeight()), tempFont);
		}
		if (!isForegroundColorSet)
			g.setColor(Color.black);
		if (drawBottomLine)
			g.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
	}

	private Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}

	private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
		// Get the FontMetrics
		FontMetrics metrics = g.getFontMetrics(font);
		// Determine the X coordinate for the text
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		// Determine the Y coordinate for the text (note we add the ascent, as in java
		// 2d 0 is top of the screen)
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		// Set the font
		g.setFont(font);
		// Draw the String
		g.drawString(text, x, y);
	}

	private int getMaxFontSizeForControl(MenuLabel button, String text, Graphics g, Font font) {
		Rectangle r = button.getBounds();
//		Insets m = button.getMargin();
//		Insets i = button.getBorder().getBorderInsets(button);
//		Rectangle viewableArea = new Rectangle(r.width - (m.right + m.left + i.left + i.right),
//				r.height - (m.top + m.bottom + i.top + i.bottom));
		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();
		int size = 1;
		boolean tooBig = false;
		while (!tooBig) {
			Font f = font.deriveFont((float) size);
			GlyphVector gv = f.createGlyphVector(frc, text);
			Rectangle2D box = gv.getVisualBounds();
			if (box.getHeight() > r.getHeight() || box.getWidth() > r.getWidth()) {
				tooBig = true;
				size--;
			}
			size++;
		}
		return size;
	}
}
