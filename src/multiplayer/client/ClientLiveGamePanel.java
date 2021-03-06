package multiplayer.client;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import game.MultiPlayerMatchField;
import hauptmenu.PongFrame;

/*
 * ClientLiveGamePanel
 * 
 * show Chat
 * Spielfeld
 */
public class ClientLiveGamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JPanel contentPane = new JPanel();
	private MultiPlayerMatchField spielfeld;

	private PongFrame pongFrame;

	public ClientLiveGamePanel(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		this.setSize(pongFrame.getGraphicResolution());
		this.setLayout(null);
		contentPane.setSize(pongFrame.getGraphicResolution());
		contentPane.setLayout(null);

		spielfeld = new MultiPlayerMatchField(pongFrame);
		spielfeld.configSF(spielfeld.SPECTATOR);

		contentPane.add(spielfeld);
		this.add(contentPane);
	}

	public void setChat(boolean visible) {
		if (visible) {
			contentPane.add(pongFrame.getClientChat(), BorderLayout.PAGE_END);
		} else {
			contentPane.remove(pongFrame.getClientChat());
		}
	}

	public MultiPlayerMatchField getSpielfeld() {
		return spielfeld;
	}

	public void setSpielfeld(MultiPlayerMatchField spielfeld) {
		this.spielfeld = spielfeld;
	}

	public void wakeUp() {
		spielfeld.laufen();
	}
}