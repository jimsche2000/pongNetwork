package multiplayer.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.MenuButton;
import gui.MenuLabel;
import hauptmenu.PongFrame;
import pongtoolkit.ImageLoader;

/*
 * ClientControlPanel:
 * 
 * 	*Give suggestions for Bingo-sentences
 *  *Chat
 *  *Load and Save Suggestion-Packs(less or more as 9 Sentences) from data saved on HardDrive
 */
public class ClientControlPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MenuButton skipBack;
	private MenuLabel title, chatTitle;
	private ImageIcon background;
	private JLabel backgroundLabel;

	// Components for Suggesting Bingo-Sentences
	private PongFrame pongFrame;

	public ClientControlPanel(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		Dimension preferredSize = pongFrame.getGraphicResolution();
		Insets resInsets = pongFrame.getGraphicInsets();
		setPreferredSize(preferredSize);
		setBackground(Color.black);
		this.setLayout(new BorderLayout());

		background = ImageLoader.loadIcon("ClientWallpaper.jpg", preferredSize);

		backgroundLabel = new JLabel();
		backgroundLabel.setPreferredSize(preferredSize);
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new FlowLayout());

		JPanel fillPanel = new JPanel();
		fillPanel.setOpaque(false);
		fillPanel.setPreferredSize(new Dimension(preferredSize.width, resInsets.top));

		backgroundLabel.add(fillPanel);

		title = new MenuLabel(pongFrame, "Das Spiel wird vorbereitet...");
		title.setPreferredSize(new Dimension(preferredSize.width, Math.round(100 * pongFrame.getASPECT_RATIO())));
		title.setFont(pongFrame.getGLOBAL_FONT().deriveFont(30f * pongFrame.getASPECT_RATIO()));
		title.setDrawBackground(false);
		title.setAlignment(title.ALIGN_MID);
		backgroundLabel.add(title);

		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new FlowLayout());
		chatPanel.setOpaque(false);
		chatPanel.setPreferredSize(new Dimension(preferredSize.width, Math.round(725f * pongFrame.getASPECT_RATIO()))); //

		chatTitle = new MenuLabel(pongFrame, "Chat");
		chatTitle.setPreferredSize(new Dimension(preferredSize.width, Math.round(100 * pongFrame.getASPECT_RATIO())));
		chatTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(25f * pongFrame.getASPECT_RATIO()));
		chatTitle.setDrawBackground(false);
		chatTitle.setAlignment(chatTitle.ALIGN_MID);

		chatPanel.add(chatTitle);

		pongFrame.getClientChat().setPreferredSize(new Dimension(Math.round(1300 * pongFrame.getASPECT_RATIO()),
				Math.round(675 * pongFrame.getASPECT_RATIO())));
		chatPanel.add(pongFrame.getClientChat());
		backgroundLabel.add(chatPanel);

		skipBack = new MenuButton(pongFrame, "Server verlassen");
		skipBack.setSize(new Dimension(Math.round(350 * pongFrame.getASPECT_RATIO()),
				Math.round(50 * pongFrame.getASPECT_RATIO())));
		skipBack.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f * pongFrame.getASPECT_RATIO()));
		skipBack.addActionListener(this);
		backgroundLabel.add(skipBack);

		add(backgroundLabel, BorderLayout.CENTER);
	}

	public void setChatVisible(boolean visible) {
		if (visible) {
			backgroundLabel.add(pongFrame.getClientChat());
		} else {
			backgroundLabel.remove(pongFrame.getClientChat());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (actionCommand.equals(skipBack.getText())) {

			pongFrame.showPane(pongFrame.MULTI_PLAYER);
			pongFrame.getClientThread().disconnectFromServer(true);

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