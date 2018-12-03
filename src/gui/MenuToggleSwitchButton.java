package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import hauptmenu.PongFrame;

@SuppressWarnings("serial")
public class MenuToggleSwitchButton extends JPanel {
	private boolean activated = false;
	private static final int ALPHA = 50; // how much see-thru. 0 to 255
	private static final Color GP_BG = new Color(0, 0, 0, ALPHA);
	private Color switchColor = Color.white, buttonColor = Color.green, borderColor = Color.black;
	private Color activeSwitch = new Color(0, 125, 255);
	private BufferedImage puffer;
	private Graphics2D g;
	private PongFrame pongFrame;
	private volatile boolean mouseHover = false;

	public MenuToggleSwitchButton(PongFrame pongFrame, int width, int height) {
		super();
		this.pongFrame = pongFrame;
		setVisible(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				activated = !activated;
				repaint();
			}
		});
		setPreferredSize(new Dimension(width, height));
		setSize(width, height);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mouseHover = true;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseHover = false;
				repaint();
			}
		});
	}

	@Override
	public void paint(Graphics gr) {
		if (g == null) {
			puffer = (BufferedImage) createImage(getWidth(), getHeight());
			g = (Graphics2D) puffer.getGraphics();
			RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHints(rh);
		}
		int roundness = Math.round(5 * pongFrame.getASPECT_RATIO());
		int fourty = Math.round((getWidth() - 1));
		int twenty = Math.round((getHeight() - 1));
		int eighteen = Math.round((getHeight())) - 1;
		g.setColor(switchColor); // Hintergrund
		g.fillRoundRect(0, 0, fourty, twenty, roundness, roundness);
		g.setColor(borderColor); // Rahmen
		g.drawRoundRect(0, 0, fourty, twenty, roundness, roundness);
		g.setColor(buttonColor); // Farbe des Aktivierten Bereichs. GRÜN
		if (activated) { // Rechts
			g.fillRoundRect(twenty + 1, 1, eighteen, eighteen, roundness, roundness);
			g.setColor(borderColor);
			g.drawRoundRect(twenty, 0, fourty, twenty, roundness, roundness);
		} else { // Links
			g.fillRoundRect(1, 1, eighteen, eighteen, roundness, roundness);
			g.setColor(borderColor);
			g.drawRoundRect(0, 0, twenty, twenty, roundness, roundness);
		}
		if (mouseHover) {
			g.setColor(GP_BG);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		gr.drawImage(puffer, 0, 0, null);
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Color getSwitchColor() {
		return switchColor;
	}

	/**
	 * Unactivated Background Color of switch
	 */
	public void setSwitchColor(Color switchColor) {
		this.switchColor = switchColor;
	}

	public Color getButtonColor() {
		return buttonColor;
	}

	/**
	 * Switch-Button color
	 */
	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * Border-color of whole switch and switch-button
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public Color getActiveSwitch() {
		return activeSwitch;
	}

	public void setActiveSwitch(Color activeSwitch) {
		this.activeSwitch = activeSwitch;
	}
}