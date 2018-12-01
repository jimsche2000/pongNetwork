package multiplayer.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gui.ShadowLabel;
import hauptmenu.PongFrame;

/*
 * ClientControlPanel:
 * 
 * 	*Give suggestions for Bingo-sentences
 *  *Chat
 *  *Load and Save Suggestion-Packs(less or more as 9 Sentences) from data saved on HardDrive
 */
public class ClientControlPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
//	private static JButton stopServer;
	private JButton skipBack;
	private JPanel contentPane;
	private JTextField serverNameTextField;
	private JTextField maxUserTextField;
	private ShadowLabel maxUserLabel, title, labelServerName, chatTitle;

	// Components for Suggesting Bingo-Sentences
//	private JPanel bingoSentencesPanel;
//	private JTextField bingoSentenceTextField;
//	private JButton sendBingoSentence;
//	private ShadowLabel bingoSentenceTitle, SuggestionSendWarningLabel;
//	private long lastSuggestionTimestamp = 0;
	private PongFrame pongFrame;

	public ClientControlPanel(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		this.setLayout(new BorderLayout());
		contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setBackground(Color.black);
		this.setSize(pongFrame.getSize());
		contentPane.setPreferredSize(new Dimension((int) (pongFrame.getWidth() * 0.5), (int) (pongFrame.getHeight() * 0.5)));
//		contentPane.setMaximumSize(new Dimension((int) (pongFrame.getWidth() * 0.5), (int) (pongFrame.getHeight() * 0.5)));
		contentPane.setLayout(null);

		title = new ShadowLabel("Spiel wird konfiguriert", 30, 300, 49);
		title.setBounds(25, 0, 400, 60);
		contentPane.add(title);

		contentPane.setAlignmentX(SwingConstants.CENTER);
		contentPane.setAlignmentY(SwingConstants.CENTER);
		this.setAlignmentX(SwingConstants.CENTER);
		this.setAlignmentY(SwingConstants.CENTER);

		labelServerName = new ShadowLabel("Server-Name", 24, 300, 31);
		labelServerName.setBounds(25, 60, 300, 35);
		contentPane.add(labelServerName);

		serverNameTextField = new JTextField("");
		serverNameTextField.setBounds(25, 100, 300, 25);
		serverNameTextField.setColumns(10);
		serverNameTextField.setEditable(false);
		contentPane.add(serverNameTextField);

		maxUserLabel = new ShadowLabel("Maximale Anzahl der User", 24, 300, 31);
		maxUserLabel.setBounds(25, 130, 300, 31);
		contentPane.add(maxUserLabel);

		maxUserTextField = new JTextField("");
		maxUserTextField.setBounds(25, 165, 300, 25);
		maxUserTextField.setEditable(false);
		contentPane.add(maxUserTextField);

		skipBack = new JButton("Server verlassen");
		skipBack.setBounds(25, 255, 305, 50);
		skipBack.addActionListener(this);
		contentPane.add(skipBack);

		chatTitle = new ShadowLabel("Chat", 25, 200, 35);
		chatTitle.setBounds(25, 400, 200, 39);
		contentPane.add(chatTitle);
		pongFrame.getClientChat().setBounds(25, 450, 877, 340);// 877 340
		contentPane.add(pongFrame.getClientChat());
		this.add(contentPane, BorderLayout.CENTER);
	}

	public void setChatVisible(boolean visible) {
		if (visible) {
			contentPane.add(pongFrame.getClientChat());
		} else {
			contentPane.remove(pongFrame.getClientChat());
		}
	}

//	public void reload() {
//
//		serverNameTextField.setText(pongFrame.getHostServer().getName());
//		maxUserTextField.setText(pongFrame.getHostServer().getMaxUser());
////		stopServer.setEnabled(CreateServerPanel.serverIsRunning);
//	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (actionCommand.equals(skipBack.getText())) {


			pongFrame.showPane(pongFrame.MULTI_PLAYER);
			pongFrame.getClientThread().disconnectFromServer(true);

		} else if (actionCommand.equals("sendSentence")) {
			// Satz-Vorschlag an den Server schicken
			// System.out.println(lastSuggestionTimestamp-System.currentTimeMillis());
//			if (lastSuggestionTimestamp == 0 || (lastSuggestionTimestamp - System.currentTimeMillis()) < -10000) {
//				ClientMainThread.sendMessageToServer(ClientMainThread.NO_CHAT_MESSAGE + "VORSCHLAG:"
//						+ ClientMainThread.name + " schlägt vor: \"" + bingoSentenceTextField.getText() + "\"");
//				lastSuggestionTimestamp = System.currentTimeMillis();
//				bingoSentenceTextField.setText("");
//				SuggestionSendWarningLabel.setText("Vorschlag wurde abgeschickt.");
//			} else {
//				SuggestionSendWarningLabel.setText(
//						"Warte noch " + (((lastSuggestionTimestamp - System.currentTimeMillis()) / 1000) + 10) + "s");
//			}
		}
	}

	public class SendPressEnterListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				JTextField comp = (JTextField) e.getComponent();
				pongFrame.getClientThread().executeCommand(comp.getText());
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}
}