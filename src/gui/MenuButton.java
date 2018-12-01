package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import hauptmenu.PongFrame;

public class MenuButton extends JButton {

	private static final long serialVersionUID = 7138911179396227718L;
	private Dimension size = new Dimension(800, 100);
	private Dimension backgroundSize = new Dimension(800, 100);
	private Color hoverBackgroundColor;
	private Color pressedBackgroundColor;
	private String text;
	private boolean autoResizeFont = false, firstTime = true;
	private PongFrame pongFrame;

	public MenuButton(PongFrame pongFrame, String text) {
		this.text = text;
		this.pongFrame = pongFrame;
		this.setText(text);
//		Dimension size = new Dimension(Math.round(this.size.width*pongFrame.getASPECT_RATIO()), Math.round(this.size.height*pongFrame.getASPECT_RATIO()));
		this.setSize(size);
		this.setRolloverEnabled(true);
		this.setForeground(Color.white);
		this.setFocusPainted(false);
		this.setBackground(Color.black);

		setHoverBackgroundColor(Color.gray);
		setPressedBackgroundColor(Color.white);

		this.setFont(pongFrame.getGLOBAL_FONT().deriveFont(1, getWidth() * getHeight() / 3000 + 10));
//		System.out.println(text + ": " + this.getFont().getSize());
		this.setAutoFontSize(true);
//		this.getSize();
	}

	public void setText(String text) {
		this.text = text;
	}
	public void setSize(Dimension size) {
		super.setSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
		setBackgroundSize(size);
	}
//	public void setSize(Dimension size) {
////		System.out.print("[MenuPanel]Called setSize (width,height): "+width+","+height+" : ");
//		if(size.width<pongFrame.getGraphicResolution().width && size.height < pongFrame.getGraphicResolution().height) {
//		size.width = Math.round(size.width*pongFrame.getASPECT_RATIO());
//		size.height = Math.round(size.height*pongFrame.getASPECT_RATIO());
////		System.out.println(+width+","+height+" set?");
//		size = new Dimension(size.width, size.height);
//		}
//		this.setMinimumSize(size);
//		this.setMaximumSize(size);
//		this.setPreferredSize(size);
//		super.setSize(size.width, size.height);
////		System.out.println("getSize: "+getSize()+" setSize: "+width+","+height);
//	}
//	public void setSize(int width, int height) {
////		System.out.print("Called setSize (width,height): "+width+","+height+" : ");
//		Dimension size = new Dimension(width, height);
//		if(width<pongFrame.getGraphicResolution().width && height < pongFrame.getGraphicResolution().height) {
//		width = Math.round(width*pongFrame.getASPECT_RATIO());
//		height = Math.round(height*pongFrame.getASPECT_RATIO());
////		System.out.println(+width+","+height+" set?");
//		size = new Dimension(width, height);
//		}
//		this.setMinimumSize(size);
//		this.setMaximumSize(size);
//		this.setPreferredSize(size);
//		super.setSize(width, height);
////		System.out.println("getSize: "+getSize()+" setSize: "+width+","+height);
//	}	
//	public void setBounds(int x, int y, int width, int height) {
//		x = Math.round(x*pongFrame.getASPECT_RATIO());
//		x = Math.round(y*pongFrame.getASPECT_RATIO());
//		width = Math.round(width*pongFrame.getASPECT_RATIO());
//		height = Math.round(height*pongFrame.getASPECT_RATIO());
//		super.setBounds(x, y, width, height);
//	}
//	public void setLocation(Point p) {
//		p.x = Math.round(p.x*pongFrame.getASPECT_RATIO());
//		p.y = Math.round(p.y*pongFrame.getASPECT_RATIO());
//		super.setLocation(p);
//	}
//	public void setLocation(int x, int y) {
//		x = Math.round(x*pongFrame.getASPECT_RATIO());
//		y = Math.round(y*pongFrame.getASPECT_RATIO());
//		super.setLocation(x,y);
//	}
	public void setBackgroundSize(Dimension size) {
		this.backgroundSize = size;
	}

	public void setMouseOverIcon(Icon rolloverIcon) {
		this.setRolloverEnabled(true);
		this.setRolloverIcon(rolloverIcon);
	}

	public void setClickedIcon(Icon pressedIcon) {
		this.setPressedIcon(pressedIcon);
	}

	public void updateIcons(ImageIcon i, ImageIcon i_mo, ImageIcon i_pr) {
		this.setIcon(i); // standard-icon
		this.setRolloverIcon(i_mo);// mouse-over-icon
		this.setPressedIcon(i_pr);// pressed-/clicked-icon
	}

	public void setFont(Font font) {
		autoResizeFont = false;
		super.setFont(font);
	}

	public void setAutoFontSize(boolean autoFontSize) {
		autoResizeFont = autoFontSize;
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (getModel().isPressed() || !isEnabled()) {
			g.setColor(pressedBackgroundColor);
		} else if (getModel().isRollover()) {
			g.setColor(hoverBackgroundColor);
		} else {
			g.setColor(getBackground());
		}

		g.fillRect(0, 0, backgroundSize.width, backgroundSize.height);
		g.setColor(getContrastColor(g.getColor()));

//		somethingHasChanged = !oldSize.equals(this.getSize());
		Font tempFont = this.getFont();
		if (autoResizeFont && (firstTime)) {
			firstTime = false;
			MenuButton temp = this;
			temp.setSize(backgroundSize);
			temp.setBorder(new EmptyBorder(5, 5, 5, 5));
			float size = getMaxFontSizeForControl(temp, text, g, this.getFont());
			tempFont = tempFont.deriveFont(size);
			this.setFont(tempFont);

		} else {
			tempFont = this.getFont();
		}
		drawCenteredString(g, text.toUpperCase(), new Rectangle(0, 0, backgroundSize.width, backgroundSize.height),
				tempFont);
	}

	private Color getContrastColor(Color color) {
		double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		return y >= 128 ? Color.black : Color.white;
	}

	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g    The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
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

	private int getMaxFontSizeForControl(MenuButton button, String text, Graphics g, Font font) {
		Rectangle r = button.getBounds();
		Insets m = button.getMargin();
		Insets i = button.getBorder().getBorderInsets(button);
		Rectangle viewableArea = new Rectangle(r.width - (m.right + m.left + i.left + i.right),
				r.height - (m.top + m.bottom + i.top + i.bottom));
		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();
		int size = 1;
		boolean tooBig = false;
		while (!tooBig) {
			Font f = font.deriveFont((float) size);
			GlyphVector gv = f.createGlyphVector(frc, text);
			Rectangle2D box = gv.getVisualBounds();
			if (box.getHeight() > viewableArea.getHeight() || box.getWidth() > viewableArea.getWidth()) {
				tooBig = true;
				size--;
			}
			size++;
		}
//		System.out.println("MAX_SIZE FOR " + text + " IS " + size);
		return size;
	}
	// private int getMaxFontSize(Graphics g, String text, Dimension compSize) {
	// System.out.println("GETTING MAX FONT SIZE FROM "+text);
	//
	//// int textWidth = metrics.stringWidth(text);
	//// int textHeight = metrics.getMaxDescent()+metrics.getMaxAscent();
	// int size = 1;
	//// System.out.println(text+"::: TEXT-WIDTH: "+textWidth+" TEXT-HEIGHT:
	// "+textHeight);
	// int maxTries = 100;
	// while(size<maxTries) {
	// g.setFont(g.getFont().deriveFont(size));
	// FontMetrics metrics = g.getFontMetrics(g.getFont());
	// Dimension d = new Dimension(metrics.stringWidth(text),
	// metrics.getMaxDescent()+metrics.getMaxAscent());
	// System.out.println("actual Size"+size+" text.width: "+d.width+"
	// compSize.width "+compSize.width+" text.height: "+d.getHeight()+"
	// compSize.height "+compSize.height);
	// if(d.getWidth() < compSize.width && d.getHeight() < compSize.height) {
	// size++;
	//
	//
	// }else{
	// System.out.println("YASSS GOT SIZE: "+(size-1));
	// return size-1;
	// }
	//
	//
	// }
	// System.out.println("Didn't got it");
	// return size;
	// }

//	@Override
//	public void setContentAreaFilled(boolean b) {
//	}

	public Color getHoverBackgroundColor() {
		return hoverBackgroundColor;
	}

	public void setHoverBackgroundColor(Color hoverBackgroundColor) {
		this.hoverBackgroundColor = hoverBackgroundColor;
	}

	public Color getPressedBackgroundColor() {
		return pressedBackgroundColor;
	}

	public void setPressedBackgroundColor(Color pressedBackgroundColor) {
		this.pressedBackgroundColor = pressedBackgroundColor;
	}
}
