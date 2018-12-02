package multiplayer.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import game.MultiPlayerMatchField;
import gui.ShadowLabel;
import hauptmenu.PongFrame;
import main.PongMain;

/*
 * ServerLiveGamePanel
 * 
 * 	*Tell all Clients if a Bingo Sentence is ACTIVE
 *  *Show Server Thread
 *  *Show 3x3 Button-Raster for activating sentences
 */
public class ServerSpectatorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane = new JPanel();
	private MultiPlayerMatchField spielfeld;
	private PongFrame pongFrame;
	
	public ServerSpectatorPanel(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		this.setSize(pongFrame.getSize());
		contentPane.setLayout(new BorderLayout());
		contentPane.setSize(pongFrame.getSize());
		
		spielfeld = new MultiPlayerMatchField(pongFrame);
		spielfeld.configSF(spielfeld.SPECTATOR);
		contentPane.add(spielfeld, BorderLayout.CENTER);
		
		this.setOpaque(false);
		this.add(contentPane);
		
	}

	public void setConsole(boolean visible) {
		if (visible) {
			contentPane.add(pongFrame.getHostServerConsole(), BorderLayout.PAGE_END);
		} else {
			contentPane.remove(pongFrame.getHostServerConsole());
		}
	}
}