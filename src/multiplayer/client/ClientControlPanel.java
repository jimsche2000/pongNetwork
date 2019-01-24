package multiplayer.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import gui.MenuButton;
import gui.MenuLabel;
import gui.MenuTextField;
import hauptmenu.PongFrame;
import multiplayer.datapacks.GameRequestData;
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
	//
	// Components for GAME-REQUEST LIST (Requests from Server)
	private static JScrollPane requestGameScrollPane;
	private JPanel gameRequestListPanel, gameRequestListTitlePanel, requestGameWrapperPanel;
	private MenuLabel requestPlayerLeftTitle, requestPlayerRightTitle, acceptRequestAndStartGameTitle, declineRequest, gameRequestListTitleLabel;
	private ArrayList<MenuTextField> requestPlayerLeftNames;
	private ArrayList<MenuTextField> requestPlayerRightNames;
	private ArrayList<MenuButton> acceptNStartButtons;
	private ArrayList<MenuButton> declineRequestButtons;
	
	
	
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

		JPanel borderPanel = new JPanel();
		borderPanel.setPreferredSize(new Dimension(preferredSize.width, Math.round(800 * pongFrame.getASPECT_RATIO())));
		borderPanel.setLayout(new BorderLayout());
		borderPanel.setOpaque(false);
		
		title = new MenuLabel(pongFrame, "Das Spiel wird vorbereitet...");
		title.setPreferredSize(new Dimension(preferredSize.width, Math.round(100 * pongFrame.getASPECT_RATIO())));
		title.setFont(pongFrame.getGLOBAL_FONT().deriveFont(30f * pongFrame.getASPECT_RATIO()));
		title.setDrawBackground(false);
		title.setAlignment(title.ALIGN_MID);
		backgroundLabel.add(title);
//		borderPanel.add(title);

		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new FlowLayout());
		chatPanel.setOpaque(false);
		chatPanel.setPreferredSize(new Dimension(preferredSize.width, Math.round(745f * pongFrame.getASPECT_RATIO()))); //

		chatTitle = new MenuLabel(pongFrame, "Chat");
		chatTitle.setPreferredSize(new Dimension(preferredSize.width, Math.round(100 * pongFrame.getASPECT_RATIO())));
		chatTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(25f * pongFrame.getASPECT_RATIO()));
		chatTitle.setDrawBackground(false);
		chatTitle.setAlignment(chatTitle.ALIGN_MID);
		chatPanel.add(chatTitle);

		pongFrame.getClientChat().setPreferredSize(new Dimension(Math.round(1300 * pongFrame.getASPECT_RATIO()),
				Math.round(675 * pongFrame.getASPECT_RATIO())));
		chatPanel.add(pongFrame.getClientChat());
//		backgroundLabel.add(chatPanel);
		borderPanel.add(chatPanel, BorderLayout.CENTER);
		
		//GAME-REQUEST-PANEL
		
		requestPlayerLeftNames = new ArrayList<MenuTextField>();
		requestPlayerRightNames = new ArrayList<MenuTextField>();
		acceptNStartButtons = new ArrayList<MenuButton>();
		declineRequestButtons = new ArrayList<MenuButton>();
		
		//Alle Werte mit 1,34 dividieren!
		
		requestGameWrapperPanel = new JPanel();
		requestGameWrapperPanel.setPreferredSize(new Dimension(Math.round(503 * pongFrame.getASPECT_RATIO()), Math.round(265 * pongFrame.getASPECT_RATIO())));
//		requestGameWrapperPanel.setMaximumSize(new Dimension(675, 355));
		requestGameWrapperPanel.setBackground(Color.LIGHT_GRAY);
		requestGameWrapperPanel.setOpaque(false);
//		requestGameWrapperPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		requestGameWrapperPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.LINE_START;
//		requestGameWrapperPanel.setLocation(1000, 410);
		gameRequestListPanel = new JPanel();
		gameRequestListPanel.setPreferredSize(new Dimension(Math.round(410*pongFrame.getASPECT_RATIO()), Math.round(223*pongFrame.getASPECT_RATIO())));
		gameRequestListPanel.setBackground(Color.white);
		gameRequestListPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		
		gameRequestListTitlePanel = new JPanel();
		gameRequestListTitlePanel.setPreferredSize(new Dimension(Math.round(491*pongFrame.getASPECT_RATIO()), Math.round(16.4f*pongFrame.getASPECT_RATIO())));
		gameRequestListTitlePanel.setBackground(Color.black);
		gameRequestListTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		
		
			gameRequestListTitleLabel = new MenuLabel(pongFrame, "Liste der Spiel-Anfragen");
			gameRequestListTitleLabel.setSize(new Dimension(Math.round(447.7f*pongFrame.getASPECT_RATIO()), Math.round(22.38f*pongFrame.getASPECT_RATIO())));
			gameRequestListTitleLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(18f * pongFrame.getASPECT_RATIO()));
//			gameRequestListTitleLabel.setAlignment(gameListTitleLabel.ALIGN_MID);
			gameRequestListTitleLabel.setDrawBackground(false);
//			gameRequestListTitleLabel.setdrawJustTextBackground(false);
		
			requestPlayerLeftTitle = new MenuLabel(pongFrame, "Linker Spieler");
			requestPlayerLeftTitle.setSize(new Dimension(Math.round(120*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));
			requestPlayerLeftTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(7.5f * pongFrame.getASPECT_RATIO()));
			requestPlayerLeftTitle.setAlignment(requestPlayerLeftTitle.ALIGN_MID);
//			requestPlayerLeftTitle.setDrawBackground(false);
			requestPlayerLeftTitle.setdrawJustTextBackground(false);
			
			requestPlayerRightTitle = new MenuLabel(pongFrame, "Rechter Spieler");
			requestPlayerRightTitle.setSize(new Dimension(Math.round(120*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));
			requestPlayerRightTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(7.5f * pongFrame.getASPECT_RATIO()));
			requestPlayerRightTitle.setAlignment(requestPlayerRightTitle.ALIGN_MID);
//			requestPlayerRightTitle.setDrawBackground(false);
			requestPlayerRightTitle.setdrawJustTextBackground(false);
			
			acceptRequestAndStartGameTitle = new MenuLabel(pongFrame, "Annehmen");
			acceptRequestAndStartGameTitle.setSize(new Dimension(Math.round(90f*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));
			acceptRequestAndStartGameTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(7.5f * pongFrame.getASPECT_RATIO()));
			acceptRequestAndStartGameTitle.setAlignment(acceptRequestAndStartGameTitle.ALIGN_MID);
//			acceptRequestAndStartGameTitle.setDrawBackground(false);
			acceptRequestAndStartGameTitle.setdrawJustTextBackground(false);
			
			declineRequest = new MenuLabel(pongFrame, "Ablehnen");
			declineRequest.setSize(new Dimension(Math.round(73f*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));
			declineRequest.setFont(pongFrame.getGLOBAL_FONT().deriveFont(7.5f * pongFrame.getASPECT_RATIO()));
			declineRequest.setAlignment(declineRequest.ALIGN_MID);
//			declineRequest.setDrawBackground(false);
			declineRequest.setdrawJustTextBackground(false);

			gameRequestListTitlePanel.add(requestPlayerLeftTitle);
			gameRequestListTitlePanel.add(requestPlayerRightTitle);
			gameRequestListTitlePanel.add(acceptRequestAndStartGameTitle);
			gameRequestListTitlePanel.add(declineRequest);

			gbc.gridy = 0;
			requestGameWrapperPanel.add(gameRequestListTitleLabel, gbc);

			gbc.gridy = 1;
			requestGameWrapperPanel.add(gameRequestListTitlePanel, gbc);
//		gameRequestListPanel.add(gameRequestListTitlePanel);
		
		requestGameScrollPane = new JScrollPane(gameRequestListPanel);
//		requestGameScrollPane.setBounds(1000, 750, 547, 299);
		requestGameScrollPane.setPreferredSize(new Dimension(Math.round(502*pongFrame.getASPECT_RATIO()), Math.round(223f*pongFrame.getASPECT_RATIO())));
		requestGameScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		requestGameScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		requestGameScrollPane.getVerticalScrollBar().setUnitIncrement(16);

		gbc.gridy = 2;
		requestGameWrapperPanel.add(requestGameScrollPane, gbc);
//		borderRightPanel.add(requestGameWrapperPanel, SwingConstants.CENTER);
		borderPanel.add(requestGameWrapperPanel, BorderLayout.LINE_END);
//		contentPane.add(requestGameScrollPane);
	
		//--\\\\GAME-REQUEST-PANEL
		backgroundLabel.add(borderPanel);

		skipBack = new MenuButton(pongFrame, "Server verlassen");
		skipBack.setSize(new Dimension(Math.round(350 * pongFrame.getASPECT_RATIO()),
				Math.round(50 * pongFrame.getASPECT_RATIO())));
		skipBack.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f * pongFrame.getASPECT_RATIO()));
		skipBack.addActionListener(this);
		backgroundLabel.add(skipBack);
		add(backgroundLabel, BorderLayout.CENTER);
	}
	
	private void addGameRequestListLine(GameRequestData gRD) {
//		MenuButton declineRequestButton = new MenuButton(pongFrame, "");
//			String ID;
//			if (id < 10)
//				ID = "0" + Integer.toString(id);
//			else
//				ID = Integer.toString(id);

		String leftPlayerNameString = gRD.getPlayerLeftName();
		String rightPlayerNameString = gRD.getPlayerRightName();
//		int ID 
		
		MenuTextField leftPlayerNameTextField = new MenuTextField(pongFrame, leftPlayerNameString);
		leftPlayerNameTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
		leftPlayerNameTextField.setEditable(false);
		leftPlayerNameTextField.setSize(new Dimension(Math.round(120*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));
		
		MenuTextField rightPlayerNameTextField = new MenuTextField(pongFrame, rightPlayerNameString);
		rightPlayerNameTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
		rightPlayerNameTextField.setEditable(false);
		rightPlayerNameTextField.setSize(new Dimension(Math.round(120*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));

		MenuButton acceptNStartButton = new MenuButton(pongFrame, "Annehmen");
		acceptNStartButton.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
		acceptNStartButton.setSize(new Dimension(Math.round(90f*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));
		
		MenuButton declineRequestButton = new MenuButton(pongFrame, "Ablehnen");
		declineRequestButton.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
		declineRequestButton.setSize(new Dimension(Math.round(73f*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));

		/*
		 * Beim klick auf einen der beiden buttons müssen diese komponenten wieder entfernt werden
		 * 
		 */
		declineRequestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Von der Request-Liste löschen
				requestPlayerLeftNames.remove(leftPlayerNameTextField);
				requestPlayerRightNames.remove(rightPlayerNameTextField);
				acceptNStartButtons.remove(acceptNStartButton);
				declineRequestButtons.remove(declineRequestButton);

				gameRequestListPanel.remove(leftPlayerNameTextField);
				gameRequestListPanel.remove(rightPlayerNameTextField);
				gameRequestListPanel.remove(acceptNStartButton);
				gameRequestListPanel.remove(declineRequestButton);
				
				//Nachricht an den Server senden, der diese anfrage geschickt hat, dass dieser Spieler abgelehnt hat.
				pongFrame.getClientThread().sendAnswerToServersGameRequest(gRD, false);
			}
		});
		acceptNStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Von der Request-Liste löschen
				requestPlayerLeftNames.remove(leftPlayerNameTextField);
				requestPlayerRightNames.remove(rightPlayerNameTextField);
				acceptNStartButtons.remove(acceptNStartButton);
				declineRequestButtons.remove(declineRequestButton);

				gameRequestListPanel.remove(leftPlayerNameTextField);
				gameRequestListPanel.remove(rightPlayerNameTextField);
				gameRequestListPanel.remove(declineRequestButton);
				gameRequestListPanel.remove(acceptNStartButton);
				
				System.out.println("Client: Akzeptiere Anfrage");
				
				//-> Nun auf das ClientLiveGamePanel umschalten, und dem Server eine Nachricht senden, dass dieser Client akzeptiert hat
				pongFrame.getClientLiveGamePanel().getSpielfeld().refreshNames(gRD.getPlayerLeftName(), gRD.getPlayerRightName());
				pongFrame.showPane(pongFrame.CLIENT_LIVE_GAME_PANEL);
				System.out.println("Client: Sende Antwort an Server");
				pongFrame.getClientThread().sendAnswerToServersGameRequest(gRD, true);
			}
		});
		
		requestPlayerLeftNames.add(leftPlayerNameTextField);
		requestPlayerRightNames.add(rightPlayerNameTextField);
		acceptNStartButtons.add(acceptNStartButton);
		declineRequestButtons.add(declineRequestButton);

		gameRequestListPanel.add(leftPlayerNameTextField);
		gameRequestListPanel.add(rightPlayerNameTextField);
		gameRequestListPanel.add(acceptNStartButton);
		gameRequestListPanel.add(declineRequestButton);
		
		gameRequestListPanel.revalidate();
		gameRequestListPanel.repaint();
	}
	public void processGameRequestFromServer(GameRequestData gRD) {
//		int id = acceptNStartButtons.size()-1;
//		if(id<0)id=0;
//		addGameRequestListLine(id, leftPlayer, rightPlayer);
//		System.out.println("Processing GameRequest From Server; left_name: "+gRD.getPlayerLeftName()+" right_name: "+gRD.getPlayerRightName());
		
		addGameRequestListLine(gRD);
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