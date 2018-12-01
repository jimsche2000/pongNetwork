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
//	public void setFont(Font font) {
//		autoResizeFont = false;
//		super.setFont(font);
//	}
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
//		g.setColor(getContrastColor(g.getColor()));
//		g.setColor(Color.black);
		this.setForeground(getContrastColor(new Color(background.getRGB(10, 10))));
		super.paintComponent(g);
		if(drawBackground)g.drawImage(background, 0, 0, backgroundSize.width, backgroundSize.height, null);
		if(drawBorder) {
			g.drawLine(0, 0, backgroundSize.width, 0); //Linie oben
			g.drawLine(0, 0, 0, backgroundSize.height); //Linie Links
			g.drawLine(0, backgroundSize.height-1, backgroundSize.width, backgroundSize.height-1); //Linie unten
			g.drawLine(backgroundSize.width-1, 0, backgroundSize.width-1, backgroundSize.height); //Linie Rechts
		}
//		Font tempFont = PongFrame.GLOBAL_FONT;
//		if (autoResizeFont && (firstTime)) {
//			firstTime = false;
//
//			float size = getMaxFontSizeForControl(this, this.getText(), g, this.getFont());
//			tempFont = tempFont.deriveFont(size);
//			this.setFont(tempFont);
//			
//		} else {
//			tempFont = this.getFont();
//		}
//		drawCenteredString(g, this.getText(), new Rectangle(0, 0, getWidth(), getHeight()), tempFont);
		
	}
	
	private Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}
//	private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
//		// Get the FontMetrics
//		FontMetrics metrics = g.getFontMetrics(font);
//		// Determine the X coordinate for the text
//		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
//		// Determine the Y coordinate for the text (note we add the ascent, as in java
//		// 2d 0 is top of the screen)
//		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
//		// Set the font
//		g.setFont(font);
//		// Draw the String
//		g.drawString(text, x, y);
//	}
//
//	private int getMaxFontSizeForControl(MenuTextField button, String text, Graphics g, Font font) {
//		Rectangle r = button.getBounds();
////		Insets m = button.getMargin();
//		Insets i = button.getBorder().getBorderInsets(button);
////		Rectangle viewableArea = new Rectangle(r.width - (m.right + m.left + i.left + i.right),
////				r.height - (m.top + m.bottom + i.top + i.bottom));
//		Graphics2D g2d = (Graphics2D) g;
//		FontRenderContext frc = g2d.getFontRenderContext();
//		int size = 1;
//		boolean tooBig = false;
//		while (!tooBig) {
//			Font f = font.deriveFont((float) size);
//			GlyphVector gv = f.createGlyphVector(frc, text);
//			Rectangle2D box = gv.getVisualBounds();
//			if (box.getHeight() > r.getHeight() || box.getWidth() > r.getWidth()) {
//				tooBig = true;
//				size--;
//			}
//			size++;
//		}
////		System.out.println("MAX_SIZE FOR " + text + " IS " + size);
//		return size;
//	}
}
