package multiplayer.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

import game.Spielfeld;
import hauptmenu.PongFrame;
import main.PongMain;

/*
 * ClientLiveGamePanel
 * 
 * show Chat
 * Spielfeld
 */
public class ClientLiveGamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JPanel contentPane = new JPanel();
	private Spielfeld spielfeld;


	private PongFrame pongFrame;
	
	public ClientLiveGamePanel(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		this.setSize(pongFrame.getSize());
		this.setLayout(null);
		contentPane.setSize(pongFrame.getSize());
		contentPane.setLayout(null);
		
		spielfeld = new Spielfeld(pongFrame);
		spielfeld.configSF(spielfeld.SPECTATOR);
		
		contentPane.add(spielfeld);
//		contentPane.setOpaque(false);
//		this.setOpaque(false);
		this.add(contentPane);
	}

	public void setChat(boolean visible) {
		if (visible) {
			contentPane.add(pongFrame.getClientChat(), BorderLayout.PAGE_END);
		} else {
			contentPane.remove(pongFrame.getClientChat());
		}
	}	
	public Spielfeld getSpielfeld() {
		return spielfeld;
	}

	public void setSpielfeld(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
	}
}