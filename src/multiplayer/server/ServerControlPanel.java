package multiplayer.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import gui.MenuButton;
import gui.MenuLabel;
import gui.MenuTextField;
import gui.ShadowLabel;
import hauptmenu.PongFrame;
import multiplayer.datapacks.ClientAttributes;
import multiplayer.server.createGame.CreateGameAction;
import pongtoolkit.ImageLoader;

//import Main.BingoMain;
/*
 * ServerControlPanel:
 * 
 * 	*Set 9 Sentences for Bingo
 *  *Players can join
 *  *Players can give suggestions for Bingo-sentences
 */
public class ServerControlPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	static JButton stopServer, createGame, skipBack;
	static int actualPlayerOneID = 0, actualPlayerTwoID;
	private static JPanel contentPane;
	private JTextField userNameTextField, maxUserTextField;
	private ShadowLabel maxUserLabel, title, labelServerName, consoleTitle;

	// Components for PLAYER-LIST //TODO: Change up to names, kick, mute, (probably but not needed: private chat)
	private static JScrollPane playerListScrollPane;
	private static JPanel playerListPanel, playerListTitlePanel, playerListWrapperPanel;
//	private MenuLabel playerNameTitle, playerOneTitle, playerTwoTitle, spectatorTitle, playerListTitleLabel;
	private MenuLabel playerNameTitle, mutePlayerTitle, kickPlayerTitle, banPlayerTitle, ipAdressTitle, playerListTitleLabel;
	private static ArrayList<MenuTextField> playerNames;
	private static ArrayList<MenuTextField> playerIPs;
	private static ArrayList<MenuButton> mutePlayerButtons;
	private static ArrayList<MenuButton> kickPlayerButtons;
	private static ArrayList<MenuButton> banPlayerButtons;
	
	// Components for GAME-LIST
	private static JScrollPane gameListScrollPane;
	private JPanel gameListPanel, gameListTitlePanel, gameListWrapperPanel;
	private MenuLabel gameIDTitle, gamePlayerLeftTitle, gamePlayerRightTitle, gameSpectateTitle, gameStopTitle, gameListTitleLabel;
	private ArrayList<MenuTextField> gameIDs;
	private ArrayList<MenuTextField> gamePlayerLeftNames;
	private ArrayList<MenuTextField> gamePlayerRightNames;
	private ArrayList<MenuButton> gameSpectateButtons;
	private ArrayList<MenuButton> gameStopButtons;
		
	// Components for GAME-REQUEST LIST (Requests from Clients)
	private static JScrollPane requestGameScrollPane;
	private JPanel gameRequestListPanel, gameRequestListTitlePanel, requestGameWrapperPanel;
	private MenuLabel requestPlayerLeftTitle, requestPlayerRightTitle, acceptRequestAndStartGameTitle, declineRequest, gameRequestListTitleLabel;
	private ArrayList<MenuTextField> requestPlayerLeftNames;
	private ArrayList<MenuTextField> requestPlayerRightNames;
	private ArrayList<MenuButton> acceptNStartButtons;
	private ArrayList<MenuButton> declineRequestButtons;
	
	
	private Thread t = new Thread(new ReloadClientNameThread());
	private static ImageIcon spectatorIcon = ImageLoader.loadIcon("user_glasses.png", 50, 50);
	private boolean createGameWasVisible = false;
//	private boolean spielLäuftBereits = false;
	private PongFrame pongFrame;

	private CreateGameAction createGameAction;
	
	private Object lock;
	
	public ServerControlPanel(PongFrame pongFrame) {
		Dimension preferredSize = pongFrame.getGraphicResolution();
		this.pongFrame = pongFrame;
		this.setLayout(new BorderLayout());
		contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setBackground(Color.black);
		this.setSize(pongFrame.getSize());
		contentPane.setPreferredSize(
				new Dimension((int) (pongFrame.getWidth() * 0.5), (int) (pongFrame.getHeight() * 0.5)));
		contentPane.setLayout(null);
		
		createGameAction = new CreateGameAction(pongFrame);
		
		//OBEN LINKS------------------------------------------------------------------
		title = new ShadowLabel("Spiel konfigurieren", 30, 300, 49);
		title.setBounds(25, 0, 400, 60);
		contentPane.add(title);

		labelServerName = new ShadowLabel("Server-Name", 24, 300, 31);
		labelServerName.setBounds(25, 60, 300, 35);
		contentPane.add(labelServerName);

		userNameTextField = new JTextField(System.getProperty("user.name"));
		userNameTextField.setBounds(25, 100, 300, 25);
		userNameTextField.setColumns(10);
		userNameTextField.setEditable(false);
		contentPane.add(userNameTextField);

		maxUserLabel = new ShadowLabel("Maximale Anzahl der User", 24, 300, 31);
		maxUserLabel.setBounds(25, 130, 300, 31);
		contentPane.add(maxUserLabel);

		maxUserTextField = new JTextField("100");
		maxUserTextField.setBounds(25, 165, 300, 25);
		maxUserTextField.setEditable(false);
		contentPane.add(maxUserTextField);

		stopServer = new JButton("Server Stoppen"); // Nachricht "Server geschlossen" an alle Clients senden und
													// Interrupt in allen ServerThreads auslösen
		stopServer.setBounds(25, 200, 150, 50);
		stopServer.addActionListener(this);
		contentPane.add(stopServer);

		createGame = new JButton("Neues Spiel");
		createGame.setBounds(180, 200, 150, 50);
//		createGame.setEnabled(false);
		createGame.addActionListener(this);
		contentPane.add(createGame);

		skipBack = new JButton("Zurück zum Hauptmenü");
		skipBack.setBounds(25, 255, 305, 50);
		skipBack.addActionListener(this);
		contentPane.add(skipBack);
		//\\\\OBEN LINKS--------------------------------------------------------------
		
		//PLAYER-LISTS----------------------------------------------------------------
		playerNames = new ArrayList<MenuTextField>();
		playerIPs = new ArrayList<MenuTextField>();
		mutePlayerButtons = new ArrayList<MenuButton>();
		kickPlayerButtons = new ArrayList<MenuButton>();
		banPlayerButtons = new ArrayList<MenuButton>();

//		playerNameTitle = new ShadowLabel("Spieler-Name", 25, 166, 35);
//		playerNameTitle.setPreferredSize(new Dimension(348, 37));
//
//		playerOneTitle = new ShadowLabel("Spieler 1", 12, 50, 35);
//		playerOneTitle.setPreferredSize(new Dimension(50, 37));
//
//		playerTwoTitle = new ShadowLabel("Spieler 2", 12, 50, 35);
//		playerTwoTitle.setPreferredSize(new Dimension(50, 37));
//
//		spectatorTitle = new ShadowLabel("Spectator", 10, 50, 35);
//		spectatorTitle.setPreferredSize(new Dimension(50, 37));
		
// playerNameTitle, mutePlayerTitle, kickPlayerTitle, banPlayerTitle, playerListTitleLabel;
		
		playerListTitleLabel = new MenuLabel(pongFrame, "Liste aller Spieler");
		playerListTitleLabel.setSize(new Dimension(550, 30));
//		playerListTitleLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
//		playerListTitleLabel.setAlignment(gameListTitleLabel.ALIGN_MID);
		playerListTitleLabel.setDrawBackground(false);
//		playerListTitleLabel.setdrawJustTextBackground(false);
	
		playerNameTitle = new MenuLabel(pongFrame, "Name"); //Vielleicht als tool-tip die ip
		playerNameTitle.setSize(new Dimension(220, 20));
		playerNameTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
		playerNameTitle.setAlignment(playerNameTitle.ALIGN_MID);
//		playerNameTitle.setDrawBackground(false);
		playerNameTitle.setdrawJustTextBackground(false);
		
		
		ipAdressTitle = new MenuLabel(pongFrame, "IP"); //Vielleicht als tool-tip die ip
		ipAdressTitle.setSize(new Dimension(132, 20));
		ipAdressTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
		ipAdressTitle.setAlignment(ipAdressTitle.ALIGN_MID);
//		ipAdressTitle.setDrawBackground(false);
		ipAdressTitle.setdrawJustTextBackground(false);
		
		
		mutePlayerTitle = new MenuLabel(pongFrame, "Mute");
		mutePlayerTitle.setSize(new Dimension(50, 20));
		mutePlayerTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
		mutePlayerTitle.setAlignment(mutePlayerTitle.ALIGN_MID);
//		mutePlayerTitle.setDrawBackground(false);
		mutePlayerTitle.setdrawJustTextBackground(false);
		
		kickPlayerTitle = new MenuLabel(pongFrame, "Kick");
		kickPlayerTitle.setSize(new Dimension(50, 20));
		kickPlayerTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
		kickPlayerTitle.setAlignment(kickPlayerTitle.ALIGN_MID);
//		kickPlayerTitle.setDrawBackground(false);
		kickPlayerTitle.setdrawJustTextBackground(false);
		
		banPlayerTitle = new MenuLabel(pongFrame, "Ban");
		banPlayerTitle.setSize(new Dimension(50, 20));
		banPlayerTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
		banPlayerTitle.setAlignment(banPlayerTitle.ALIGN_MID);
//		banPlayerTitle.setDrawBackground(false);
		banPlayerTitle.setdrawJustTextBackground(false);

		
		//--
		playerListWrapperPanel = new JPanel();
		playerListWrapperPanel.setSize(new Dimension(525, 355));
		playerListWrapperPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		playerListWrapperPanel.setLocation(372, 50);
		
		playerListPanel = new JPanel();
//		playerListPanel.setBounds(0, 0, 505, 299);// 505,299
		playerListPanel.setPreferredSize(new Dimension(500, 299));
		playerListPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		playerListPanel.setBackground(Color.white);

		playerListTitlePanel = new JPanel();
		playerListTitlePanel.setPreferredSize(new Dimension(508, 22));
		playerListTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		playerListTitlePanel.setBackground(Color.black);
		
//		playerListTitlePanel.add(playerNameTitle);
//		playerListTitlePanel.add(playerOneTitle);
//		playerListTitlePanel.add(playerTwoTitle);
//		playerListTitlePanel.add(spectatorTitle);
		playerListTitlePanel.add(playerNameTitle);
		playerListTitlePanel.add(ipAdressTitle);
		playerListTitlePanel.add(mutePlayerTitle);
		playerListTitlePanel.add(kickPlayerTitle);
		playerListTitlePanel.add(banPlayerTitle);

		playerListWrapperPanel.add(playerListTitleLabel);
		playerListWrapperPanel.add(playerListTitlePanel);
//		playerListPanel.add(playerListTitlePanel);
//		playerListPanel.setOpaque(false);
		playerListScrollPane = new JScrollPane(playerListPanel);

		playerListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		playerListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		playerListScrollPane.getVerticalScrollBar().setUnitIncrement(16);

		playerListScrollPane.setPreferredSize(new Dimension(525, 299));
//		playerListScrollPane.setBounds(355, 100, 547, 299);// 522,299
//		playerListScrollPane.setLocation(355, 100);
//		playerListScrollPane.setOpaque(false);
		playerListWrapperPanel.add(playerListScrollPane);
		contentPane.add(playerListWrapperPanel);
		//\\\\\PLAYER-LISTS------------------------------------------------------------
		
		//GAME-LIST--------------------------------------------------------------------

		gameIDs = new ArrayList<MenuTextField>();
		gamePlayerLeftNames = new ArrayList<MenuTextField>();
		gamePlayerRightNames = new ArrayList<MenuTextField>();
		gameSpectateButtons = new ArrayList<MenuButton>();
		gameStopButtons = new ArrayList<MenuButton>();
		
		gameListWrapperPanel = new JPanel();
//		gameListWrapperPanel.setPreferredSize(new Dimension(555, 430));
		gameListWrapperPanel.setSize(new Dimension(675, 355));
//		gameListWrapperPanel.setBackground(Color.yellow);
		gameListWrapperPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		gameListWrapperPanel.setLocation(1000, 50);
		
		gameListPanel = new JPanel();
		gameListPanel.setPreferredSize(new Dimension(550, 299));
		gameListPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		gameListPanel.setBackground(Color.white);
//		gameListPanel.add(new JLabel("HUHUU"));
		
		gameListTitlePanel = new JPanel();
		gameListTitlePanel.setPreferredSize(new Dimension(658, 22));
		gameListTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		gameListTitlePanel.setBackground(Color.black);
			//TITLES
		
			gameListTitleLabel = new MenuLabel(pongFrame, "Liste der aktuellen Spiele");
			gameListTitleLabel.setSize(new Dimension(700, 30));
//			gameListTitleLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
//			gameListTitleLabel.setAlignment(gameListTitleLabel.ALIGN_MID);
			gameListTitleLabel.setDrawBackground(false);
			gameListTitleLabel.setdrawJustTextBackground(false);
		
			gameIDTitle = new MenuLabel(pongFrame, "Spiel-ID");
			gameIDTitle.setSize(new Dimension(85, 20));
			gameIDTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
			gameIDTitle.setAlignment(gameIDTitle.ALIGN_MID);
//			gameIDTitle.setDrawBackground(false);
			gameIDTitle.setdrawJustTextBackground(false);
			
			gamePlayerLeftTitle = new MenuLabel(pongFrame, "Linker Spieler");
			gamePlayerLeftTitle.setSize(new Dimension(160, 20));
			gamePlayerLeftTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
			gamePlayerLeftTitle.setAlignment(gamePlayerLeftTitle.ALIGN_MID);
//			gamePlayerLeftTitle.setDrawBackground(false);
			gamePlayerLeftTitle.setdrawJustTextBackground(false);
			
			gamePlayerRightTitle = new MenuLabel(pongFrame, "Rechter Spieler");
			gamePlayerRightTitle.setSize(new Dimension(160, 20));
			gamePlayerRightTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
			gamePlayerRightTitle.setAlignment(gamePlayerRightTitle.ALIGN_MID);
//			gamePlayerRightTitle.setDrawBackground(false);
			gamePlayerRightTitle.setdrawJustTextBackground(false);
			
			gameSpectateTitle = new MenuLabel(pongFrame, "Zuschauen");
			gameSpectateTitle.setSize(new Dimension(95, 20));
			gameSpectateTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
			gameSpectateTitle.setAlignment(gameSpectateTitle.ALIGN_MID);
//			gameSpectateTitle.setDrawBackground(false);
			gameSpectateTitle.setdrawJustTextBackground(false);
			
			gameStopTitle = new MenuLabel(pongFrame, "Spiel abbrechen");
			gameStopTitle.setSize(new Dimension(152, 20));
			gameStopTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
			gameStopTitle.setAlignment(gameStopTitle.ALIGN_MID);
//			gameStopTitle.setDrawBackground(false);
			gameStopTitle.setdrawJustTextBackground(false);
			
			gameListTitlePanel.add(gameIDTitle);
			gameListTitlePanel.add(gamePlayerLeftTitle);
			gameListTitlePanel.add(gamePlayerRightTitle);
			gameListTitlePanel.add(gameSpectateTitle);
			gameListTitlePanel.add(gameStopTitle);
			//\\\TITLES
		gameListWrapperPanel.add(gameListTitleLabel);
		gameListWrapperPanel.add(gameListTitlePanel);
//		gameListPanel.add(gameListTitlePanel);
		gameListScrollPane = new JScrollPane(gameListPanel);
//		gameListScrollPane.setBounds(1000, 100, 697, 299);
		gameListScrollPane.setPreferredSize(new Dimension(675, 299));
//		gameListScrollPane.setSize(new Dimension(505, 299));
		gameListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		gameListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		gameListScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		gameListWrapperPanel.add(gameListScrollPane);
		contentPane.add(gameListWrapperPanel);
		//\\\\GAME-LIST----------------------------------------------------------------
				
		//GAME-REQUEST-LIST------------------------------------------------------------

		requestPlayerLeftNames = new ArrayList<MenuTextField>();
		requestPlayerRightNames = new ArrayList<MenuTextField>();
		acceptNStartButtons = new ArrayList<MenuButton>();
		declineRequestButtons = new ArrayList<MenuButton>();
		
		requestGameWrapperPanel = new JPanel();
		requestGameWrapperPanel.setSize(new Dimension(675, 405));
		requestGameWrapperPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		requestGameWrapperPanel.setLocation(1000, 410);
		
		gameRequestListPanel = new JPanel();
		gameRequestListPanel.setPreferredSize(new Dimension(550, 299));
		gameRequestListPanel.setBackground(Color.white);
		gameRequestListPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		
		gameRequestListTitlePanel = new JPanel();
		gameRequestListTitlePanel.setPreferredSize(new Dimension(658, 22));
		gameRequestListTitlePanel.setBackground(Color.black);
		gameRequestListTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		
		
			gameRequestListTitleLabel = new MenuLabel(pongFrame, "Liste der Spiel-Anfragen");
			gameRequestListTitleLabel.setSize(new Dimension(600, 30));
	//		gameRequestListTitleLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
//			gameRequestListTitleLabel.setAlignment(gameListTitleLabel.ALIGN_MID);
			gameRequestListTitleLabel.setDrawBackground(false);
//			gameRequestListTitleLabel.setdrawJustTextBackground(false);
		
			requestPlayerLeftTitle = new MenuLabel(pongFrame, "Linker Spieler");
			requestPlayerLeftTitle.setSize(new Dimension(165, 20));
			requestPlayerLeftTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
			requestPlayerLeftTitle.setAlignment(requestPlayerLeftTitle.ALIGN_MID);
//			requestPlayerLeftTitle.setDrawBackground(false);
			requestPlayerLeftTitle.setdrawJustTextBackground(false);
			
			requestPlayerRightTitle = new MenuLabel(pongFrame, "Rechter Spieler");
			requestPlayerRightTitle.setSize(new Dimension(165, 20));
			requestPlayerRightTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
			requestPlayerRightTitle.setAlignment(requestPlayerRightTitle.ALIGN_MID);
//			requestPlayerRightTitle.setDrawBackground(false);
			requestPlayerRightTitle.setdrawJustTextBackground(false);
			
			acceptRequestAndStartGameTitle = new MenuLabel(pongFrame, "Akzeptieren & Starten");
			acceptRequestAndStartGameTitle.setSize(new Dimension(225, 20));
			acceptRequestAndStartGameTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
			acceptRequestAndStartGameTitle.setAlignment(acceptRequestAndStartGameTitle.ALIGN_MID);
//			acceptRequestAndStartGameTitle.setDrawBackground(false);
			acceptRequestAndStartGameTitle.setdrawJustTextBackground(false);
			
			declineRequest = new MenuLabel(pongFrame, "Ablehnen");
			declineRequest.setSize(new Dimension(98, 20));
			declineRequest.setFont(pongFrame.getGLOBAL_FONT().deriveFont(10f));
			declineRequest.setAlignment(declineRequest.ALIGN_MID);
//			declineRequest.setDrawBackground(false);
			declineRequest.setdrawJustTextBackground(false);

			gameRequestListTitlePanel.add(requestPlayerLeftTitle);
			gameRequestListTitlePanel.add(requestPlayerRightTitle);
			gameRequestListTitlePanel.add(acceptRequestAndStartGameTitle);
			gameRequestListTitlePanel.add(declineRequest);
			
			requestGameWrapperPanel.add(gameRequestListTitleLabel);
			requestGameWrapperPanel.add(gameRequestListTitlePanel);
//		gameRequestListPanel.add(gameRequestListTitlePanel);
		
		requestGameScrollPane = new JScrollPane(gameRequestListPanel);
//		requestGameScrollPane.setBounds(1000, 750, 547, 299);
		requestGameScrollPane.setPreferredSize(new Dimension(675, 299));
		requestGameScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		requestGameScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		requestGameScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		requestGameWrapperPanel.add(requestGameScrollPane);
		contentPane.add(requestGameWrapperPanel);
//		contentPane.add(requestGameScrollPane);
		//\\\\GAME-REQUEST-LIST--------------------------------------------------------
		
		consoleTitle = new ShadowLabel("Server-Konsole", 25, 200, 35);
		consoleTitle.setBounds(25, 380, 200, 39);
		contentPane.add(consoleTitle);
		pongFrame.getHostServerConsole().setBounds(25, 420, 877, 370);// 877 340
		contentPane.add(pongFrame.getHostServerConsole());
		this.add(contentPane, BorderLayout.CENTER);
	}

	public void reload() {

		userNameTextField.setText(pongFrame.getHostServer().getName());
		maxUserTextField.setText(pongFrame.getHostServer().getMaxUser());
		stopServer.setEnabled(pongFrame.getHostServer().isShouldRun());

		if (!t.isAlive())
			t.start();
	}

	public void setConsole(boolean visible) {
		if (visible) {
			contentPane.add(pongFrame.getHostServerConsole(), BorderLayout.PAGE_END);
		} else {
			contentPane.remove(pongFrame.getHostServerConsole());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals(stopServer.getText())) {
			// Nachricht "Server geschlossen" an alle Clients senden und Interrupt in allen
			// ServerThreads auslösen
			stopServer.setEnabled(false);
			pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(false);
			pongFrame.getHostServer().sendMessageToAllClientsUsingTCP_IP("SERVER_CLOSED");
			pongFrame.getHostServer().stop();
			pongFrame.getMultiPlayer().getCreateServerPanel().refresh();
			pongFrame.showPane(pongFrame.MULTI_PLAYER);

		} else if (actionCommand.equals(createGame.getText())) {

			/*
			 * Start Button nur aktiviert wenn 1 Spieler Spieler 1 und 1 Spieler Spieler 2
			 * ist.
			 * 
			 * ALT/OLD:
			 * Den aktivierten Clients bescheid sagen, dass das Spiel nun los geht, und
			 * diese in den Live-Game-Modus schalten sollen. Zudem soll konfiguriert werden
			 * welcher client den linken und welcher den rechten slider steuern soll.
			 * 
			 * NEU/NEW:
			 * Den beiden betroffenen Clients(Linker und rechter Spieler) eine Anfrage senden,
			 * ob diese das vom Admin konfigurieerte Spiel antreten wollen.
			 * Wenn diese auf dem ClientControlPanel diese Anfrage akzeptieren, soll auf das
			 * ClientLiveGamePanel umgeschaltet werden(eben per Button-klick). Das Spiel soll
			 * erst losgehen, wenn beide akzeptiert haben, und ein Countdown von 3s abgelaufen ist.
			 * 
			 */
//			if (!spielLäuftBereits) {
//				System.out.println(" SPIEL WIRD GESTARTET: Spiel Stoppen");
//				String playerOneName = playerNames.get(actualPlayerOneID).getText();
//				String playerTwoName = playerNames.get(actualPlayerTwoID).getText();
//
//				System.out.println("CLIENT-LEFT: " + playerOneName + " CLIENT-RIGHT: " + playerTwoName);
//				pongFrame.getHostServer().configureGameForClients(getClientByName(playerOneName),
//						getClientByName(playerTwoName));
//
//				spielLäuftBereits = true;
//				createGame.setText("Spiel Stoppen");
//			} else {
//				System.out.println("SPIEL WIRD GESTOPPT: Spiel Starten");
//				spielLäuftBereits = false;
//				createGame.setText("Spiel Starten");
//
//				pongFrame.getHostServer().stopGameForClients("Das Spiel wurde abgebrochen.");
//			}
//			System.out.println("REFRESHING LISTS");
//			System.out.println("EXPECTING: "+pongFrame.getHostServer().getListClients());
			createGameWasVisible = true;
			createGameAction.getCreateGamePanel().refreshLists(false);
			createGameAction.action();
			
		} else if (actionCommand.equals(skipBack.getText())) {
			pongFrame.showPane(pongFrame.MULTI_PLAYER);

		}
	}

	public void reloadPlayerList() {

		ArrayList<ClientAttributes> playerList = pongFrame.getHostServer().getListClients();
		boolean somethingHasChanged = false;
		int minHeightFromPanel = 299;
		/*
		 * ALT/OLD:
		 * Spieler Liste anzeigen mit jeweils 2 Buttons rechts der linke button soll den
		 * jeweiligen Spieler als Spieler1 (links) festlegen, der rechte Button soll den
		 * jeweiligen Spieler als den Spieler 2 (rechts) festlegen, jeder Spieler darf
		 * nur linker oder rechter Spieler sein, nur ein Spieler darf linker- und nur
		 * ein Spieler rechter Spieler sein.
		 * 
		 * Diese Einstellungen sollen live an alle Clients(ClientControlPanel)
		 * übertragen und angezeigt werden.
		 * 
		 * NEU/NEW:
		 * Spieler Liste soll alle beim Server angemeldeten Clients anzeigen.
		 * Undzwar je Client den Namen, sowie die IP des Clients in MenuTextFields.
		 * Dazu 3 Buttons: mute, kick, ban!
		 * 
		 */

		if (playerList != null) {
			int size = 50;
			for (ClientAttributes ca : playerList) {
				int ID = playerList.indexOf(ca);
				if (playerNames != null) {
					try {
						if (playerNames.get(ID) != null) {
							if (!(playerNames.get(ID).getText().equals(ca.getName()))) { // Wenn das JTextField an der
																							// Stelle *ID* NICHT den
																							// gleichen
																							// Namen hat wie der Spieler
																							// *ca*
																							// Also eine Neuerung
																							// auftritt

								playerNames.get(ID).setText(ca.getName());
								playerIPs.get(ID).setText(ca.getIP());

								if (mutePlayerButtons.get(ID) == null) { //Mute Button an der Stelle *ID* noch
																		// nicht vorhanden

//									JButton tempButtonOne = new JButton("1");
//									tempButtonOne.setToolTipText("Als Spieler 1(links) registrieren");
//									tempButtonOne.setPreferredSize(new Dimension(size, size));
//									tempButtonOne.setLocation(402, ID * size + size);
									
									MenuButton tempMuteButton = new MenuButton(pongFrame, "Mute");
//									tempMuteButton.setToolTipText("");
									tempMuteButton.setPreferredSize(new Dimension(size, size));
									tempMuteButton.setLocation(402, ID * size + size);

									mutePlayerButtons.add(tempMuteButton);
									playerListPanel.add(tempMuteButton);
									somethingHasChanged = true;
								}
								if (kickPlayerButtons.get(ID) == null) {// Kick Button an der Stelle *ID* noch nicht
																		// vorhanden

									MenuButton tempKickButton = new MenuButton(pongFrame, "Kick");
//									tempKickButton.setToolTipText("Als Spieler 2(rechts) registrieren");
									tempKickButton.setPreferredSize(new Dimension(size, size));
									tempKickButton.setLocation(453, ID * size + size);

									kickPlayerButtons.add(tempKickButton);
									playerListPanel.add(tempKickButton);

									somethingHasChanged = true;
								}
								if (banPlayerButtons.get(ID) == null) {
									MenuButton tempBanButton = new MenuButton(pongFrame, "Ban"); // Ban
//									tempBanButton.setIcon(spectatorIcon);
//									tempBanButton.setToolTipText("Als Spectator registrieren");
									tempBanButton.setPreferredSize(new Dimension(size, size));
									tempBanButton.setLocation(453, ID * size + size);
//									banPlayerButtons.add(tempSpectatorButton);
									playerListPanel.add(tempBanButton);

									somethingHasChanged = true;
								}
							}
						} else { // JTextField noch nicht vorhanden: Neuer Spieler/User
//							System.out.println("1111111111111111111111111111111111");
							addUserListLine(ID, size, ca);
							somethingHasChanged = true;
							minHeightFromPanel = (ID + 2) * size + 1;
						}
					} catch (IndexOutOfBoundsException e) {// JTextField noch nicht vorhanden: Neuer Spieler/User
						System.out.println();
//System.out.println("22222222222222222222222222222222222222222");
						addUserListLine(ID, size, ca);
						somethingHasChanged = true;
						minHeightFromPanel = (ID + 2) * size + 1;
					}
				} else {// JTextField noch nicht vorhanden: Neuer Spieler/User ---- arraylist nicht initialisiert
//System.out.println("3333333333333333333333333333333333");
					addUserListLine(ID, size, ca);
					somethingHasChanged = true;
					minHeightFromPanel = (ID + 2) * size + 1;
				}
			}

			/*
			 * Sicherheits-Kontrolle:
			 * 
			 * Falls Spieler in JTextField vorhanden, welcher in der UserListe nicht mehr
			 * vorhanden ist -> Also ein Client der den Server verlassen hat
			 * 
			 */
			try {

				for (MenuTextField menuTextField : playerNames) {
					int ID = playerNames.indexOf(menuTextField);
					try {
						if (playerList.get(ID) != null) {
							// if(!(playerList.get(ID).getName().equals(jt.getName()))) { // Wenn das
							// ClientAttribut-Objekt an der
							// // Stelle *ID* NICHT den gleichen
							// // Namen hat wie das TextFeld *ca*
							// // Also eine Neuerung auftritt

						} else {// Veralteter Eintrag bei JTextField

							playerListPanel.remove(menuTextField);
							playerListPanel.remove(playerIPs.get(ID));
							playerListPanel.remove(mutePlayerButtons.get(ID));
							playerListPanel.remove(kickPlayerButtons.get(ID));
							playerListPanel.remove(banPlayerButtons.get(ID));

							somethingHasChanged = true;
							minHeightFromPanel = menuTextField.getLocation().y - size;

							playerNames.remove(menuTextField);
							playerIPs.remove(ID);
							mutePlayerButtons.remove(ID);
							kickPlayerButtons.remove(ID);
							banPlayerButtons.remove(ID);
						}
					} catch (IndexOutOfBoundsException e) {// Veralteter Eintrag bei JTextField

						playerListPanel.remove(menuTextField);
						playerListPanel.remove(playerIPs.get(ID));
						playerListPanel.remove(mutePlayerButtons.get(ID));
						playerListPanel.remove(kickPlayerButtons.get(ID));
						playerListPanel.remove(banPlayerButtons.get(ID));

						somethingHasChanged = true;
						minHeightFromPanel = menuTextField.getLocation().y - size;

						playerNames.remove(menuTextField);
						playerIPs.remove(ID);
						mutePlayerButtons.remove(ID);
						kickPlayerButtons.remove(ID);
						banPlayerButtons.remove(ID);
					}
				}
			} catch (Exception e) {
				reloadPlayerList();
			}

			if (somethingHasChanged) {
				if (minHeightFromPanel < 299)
					minHeightFromPanel = 299;
				playerListPanel.setPreferredSize(new Dimension(playerListPanel.getPreferredSize().width, minHeightFromPanel));
//	        	System.out.println("minHeight: "+minHeightFromPanel);
				playerListPanel.revalidate();
				playerListPanel.repaint();

//				System.out.println(
//						"PanelSize: " + playerListPanel.getSize() + " : " + playerListPanel.getPreferredSize());
				if(createGameWasVisible)createGameAction.getCreateGamePanel().refreshLists(true);
			}
		}
	}

	private void addUserListLine(int id, int size, ClientAttributes ca) {
//		String ID;
//		if (id < 10)
//			ID = "0" + Integer.toString(id);
//		else
//			ID = Integer.toString(id);

		MenuTextField tempNameTextField = new MenuTextField(pongFrame, ca.getName());
		tempNameTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
		tempNameTextField.setEditable(false);
		tempNameTextField.setSize(new Dimension(220, size));
		
		MenuTextField tempIPTextField = new MenuTextField(pongFrame, ca.getIP());
		tempIPTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
		tempIPTextField.setEditable(false);
		tempIPTextField.setSize(new Dimension(131, size));

		MenuButton tempMuteButton = new MenuButton(pongFrame, "Mute");
		tempMuteButton.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
//		tempMuteButton.setToolTipText("Diesen Spieler Stummschalten");
//		tempMuteButton.setFocusPainted(false);
		tempMuteButton.setSize(new Dimension(size, size));
//		tempMuteButton.setActionCommand("SPIELER_LINKS[" + ID + "]");
//		tempMuteButton.addActionListener(new SomeAction());

		MenuButton tempKickButton = new MenuButton(pongFrame, "Kick");
		tempKickButton.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
//		tempKickButton.setFocusPainted(false);
//		tempKickButton.setToolTipText("Diesen Spieler vom Server-Kicken");
		tempKickButton.setSize(new Dimension(size, size));
//		tempKickButton.setActionCommand("SPIELER_RECHTS[" + ID + "]");
//		tempKickButton.addActionListener(new SomeAction());

		MenuButton tempBanButton = new MenuButton(pongFrame, "Ban"); 
		tempBanButton.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
//		tempBanButton.setIcon(spectatorIcon);
//		tempBanButton.setToolTipText("Diesen Spieler(IP) vom Server bannen");
		tempBanButton.setSize(new Dimension(size, size));
//		tempBanButton.setActionCommand("SPECTATOR[" + ID + "]");
//		tempBanButton.addActionListener(new SomeAction());
//		tempBanButton.setEnabled(false);

		playerNames.add(tempNameTextField);
		playerIPs.add(tempIPTextField);
		mutePlayerButtons.add(tempMuteButton);
		kickPlayerButtons.add(tempKickButton);
		banPlayerButtons.add(tempBanButton);

		playerListPanel.add(tempNameTextField);
		playerListPanel.add(tempIPTextField);
		playerListPanel.add(tempMuteButton);
		playerListPanel.add(tempKickButton);
		playerListPanel.add(tempBanButton);
	}
	
//	
//	public void reloadGameList() {
//
//		ArrayList<ClientAttributes> playerList = pongFrame.getHostServer().getListClients();
//		boolean somethingHasChanged = false;
//		int minHeightFromPanel = 299;
//		/*
//		 * Spieler Liste anzeigen mit jeweils 2 Buttons rechts der linke button soll den
//		 * jeweiligen Spieler als Spieler1 (links) festlegen, der rechte Button soll den
//		 * jeweiligen Spieler als den Spieler 2 (rechts) festlegen, jeder Spieler darf
//		 * nur linker oder rechter Spieler sein, nur ein Spieler darf linker- und nur
//		 * ein Spieler rechter Spieler sein.
//		 * 
//		 * Diese Einstellungen sollen live an alle Clients(ClientControlPanel)
//		 * übertragen und angezeigt werden.
//		 * 
//		 */
//
//		if (playerList != null) {
//			int size = 50;
//			for (ClientAttributes ca : playerList) {
//				int ID = playerList.indexOf(ca);
//				if (playerNames != null) {
//					try {
//						if (playerNames.get(ID) != null) {
//							if (!(playerNames.get(ID).getText().equals(ca.getName()))) { // Wenn das JTextField an der
//																							// Stelle *ID* NICHT den
//																							// gleichen
//																							// Namen hat wie der Spieler
//																							// *ca*
//																							// Also eine Neuerung
//																							// auftritt
//
//								playerNames.get(ID).setText(ca.getName());
//
//								if (mutePlayerButtons.get(ID) == null) { // Spieler 1 Button an der Stelle *ID* noch
//																		// nicht vorhanden
//
//									JButton tempButtonOne = new JButton("1");
//									tempButtonOne.setToolTipText("Als Spieler 1(links) registrieren");
//									tempButtonOne.setPreferredSize(new Dimension(size, size));
//									tempButtonOne.setLocation(402, ID * size + size);
//
//									mutePlayerButtons.add(tempButtonOne);
//									playerListPanel.add(tempButtonOne);
//									somethingHasChanged = true;
//								}
//								if (kickPlayerButtons.get(ID) == null) {// Spieler 2 Button an der Stelle *ID* noch nicht
//																		// vorhanden
//
//									JButton tempButtonTwo = new JButton("2");
//									tempButtonTwo.setToolTipText("Als Spieler 2(rechts) registrieren");
//									tempButtonTwo.setPreferredSize(new Dimension(size, size));
//									tempButtonTwo.setLocation(453, ID * size + size);
//
//									kickPlayerButtons.add(tempButtonTwo);
//									playerListPanel.add(tempButtonTwo);
//
//									somethingHasChanged = true;
//								}
//								if (banPlayerButtons.get(ID) == null) {
//									JButton tempSpectatorButton = new JButton(); // Spectator
//									tempSpectatorButton.setIcon(spectatorIcon);
//									tempSpectatorButton.setToolTipText("Als Spectator registrieren");
//									tempSpectatorButton.setPreferredSize(new Dimension(size, size));
//									tempSpectatorButton.setLocation(453, ID * size + size);
//									banPlayerButtons.add(tempSpectatorButton);
//									playerListPanel.add(tempSpectatorButton);
//
//									somethingHasChanged = true;
//								}
//							}
//						} else { // JTextField noch nicht vorhanden: Neuer Spieler/User
//
//							addUserListLine(ID, size, ca);
//							somethingHasChanged = true;
//							minHeightFromPanel = (ID + 2) * size + 1;
//						}
//					} catch (IndexOutOfBoundsException e) {// JTextField noch nicht vorhanden: Neuer Spieler/User
//
//						addUserListLine(ID, size, ca);
//						somethingHasChanged = true;
//						minHeightFromPanel = (ID + 2) * size + 1;
//					}
//				} else {// JTextField noch nicht vorhanden: Neuer Spieler/User
//
//					addUserListLine(ID, size, ca);
//					somethingHasChanged = true;
//					minHeightFromPanel = (ID + 2) * size + 1;
//				}
//			}
//
//			/*
//			 * Sicherheits-Kontrolle:
//			 * 
//			 * Falls Spieler in JTextField vorhanden, welcher in der UserListe nicht mehr
//			 * vorhanden ist -> Also ein Client der den Server verlassen hat
//			 * 
//			 */
//			try {
//
//				for (JTextField jt : playerNames) {
//					int ID = playerNames.indexOf(jt);
//					try {
//						if (playerList.get(ID) != null) {
//							// if(!(playerList.get(ID).getName().equals(jt.getName()))) { // Wenn das
//							// ClientAttribut-Objekt an der
//							// // Stelle *ID* NICHT den gleichen
//							// // Namen hat wie das TextFeld *ca*
//							// // Also eine Neuerung auftritt
//
//						} else {// Veralteter Eintrag bei JTextField
//
//							playerListPanel.remove(jt);
//							playerListPanel.remove(mutePlayerButtons.get(ID));
//							playerListPanel.remove(kickPlayerButtons.get(ID));
//							playerListPanel.remove(banPlayerButtons.get(ID));
//
//							somethingHasChanged = true;
//							minHeightFromPanel = jt.getLocation().y - size;
//
//							playerNames.remove(jt);
//							mutePlayerButtons.remove(ID);
//							kickPlayerButtons.remove(ID);
//							banPlayerButtons.remove(ID);
//						}
//					} catch (IndexOutOfBoundsException e) {// Veralteter Eintrag bei JTextField
//
//						playerListPanel.remove(jt);
//						playerListPanel.remove(mutePlayerButtons.get(ID));
//						playerListPanel.remove(kickPlayerButtons.get(ID));
//						playerListPanel.remove(banPlayerButtons.get(ID));
//
//						somethingHasChanged = true;
//						minHeightFromPanel = jt.getLocation().y - size;
//
//						playerNames.remove(jt);
//						mutePlayerButtons.remove(ID);
//						kickPlayerButtons.remove(ID);
//						banPlayerButtons.remove(ID);
//					}
//				}
//			} catch (Exception e) {
//				reloadPlayerList();
//			}
//
//			if (somethingHasChanged) {
//				if (minHeightFromPanel < 299)
//					minHeightFromPanel = 299;
//				playerListPanel.setPreferredSize(new Dimension(450, minHeightFromPanel));
////	        	System.out.println("minHeight: "+minHeightFromPanel);
//				playerListPanel.revalidate();
//				playerListPanel.repaint();
//
//				System.out.println(
//						"PanelSize: " + playerListPanel.getSize() + " : " + playerListPanel.getPreferredSize());
//
//			}
//		}
//	}
//	
//	
//	private static void addGameListLine(int id, int size, ClientAttributes ca) {
//		String ID;
//		if (id < 10)
//			ID = "0" + Integer.toString(id);
//		else
//			ID = Integer.toString(id);
//
//		JTextField tempTextField = new JTextField(ca.getName());
//		tempTextField.setEditable(false);
//		tempTextField.setPreferredSize(new Dimension((int) Math.round(size * 7), size));
//
//		JButton tempButtonOne = new JButton("1");
//		tempButtonOne.setToolTipText("Als Spieler 1(links) registrieren");
//		tempButtonOne.setPreferredSize(new Dimension(size, size));
//		tempButtonOne.setActionCommand("SPIELER_LINKS[" + ID + "]");
//		tempButtonOne.addActionListener(new SomeAction());
//
//		JButton tempButtonTwo = new JButton("2");
//		tempButtonTwo.setToolTipText("Als Spieler 2(rechts) registrieren");
//		tempButtonTwo.setPreferredSize(new Dimension(size, size));
//		tempButtonTwo.setActionCommand("SPIELER_RECHTS[" + ID + "]");
//		tempButtonTwo.addActionListener(new SomeAction());
//
//		JButton tempSpectatorButton = new JButton(); // Spectator
//		tempSpectatorButton.setIcon(spectatorIcon);
//		tempSpectatorButton.setToolTipText("Als Spectator registrieren");
//		tempSpectatorButton.setPreferredSize(new Dimension(size, size));
//		tempSpectatorButton.setActionCommand("SPECTATOR[" + ID + "]");
//		tempSpectatorButton.addActionListener(new SomeAction());
//		tempSpectatorButton.setEnabled(false);
//
//		playerNames.add(tempTextField);
//		mutePlayerButtons.add(tempButtonOne);
//		kickPlayerButtons.add(tempButtonTwo);
//		banPlayerButtons.add(tempSpectatorButton);
//
//		playerListPanel.add(tempTextField);
//		playerListPanel.add(tempButtonOne);
//		playerListPanel.add(tempButtonTwo);
//		playerListPanel.add(tempSpectatorButton);
//	}
//	
//	
//	public void reloadGameRequestList() {
//
//		ArrayList<ClientAttributes> playerList = pongFrame.getHostServer().getListClients();
//		boolean somethingHasChanged = false;
//		int minHeightFromPanel = 299;
//		/*
//		 * Spieler Liste anzeigen mit jeweils 2 Buttons rechts der linke button soll den
//		 * jeweiligen Spieler als Spieler1 (links) festlegen, der rechte Button soll den
//		 * jeweiligen Spieler als den Spieler 2 (rechts) festlegen, jeder Spieler darf
//		 * nur linker oder rechter Spieler sein, nur ein Spieler darf linker- und nur
//		 * ein Spieler rechter Spieler sein.
//		 * 
//		 * Diese Einstellungen sollen live an alle Clients(ClientControlPanel)
//		 * übertragen und angezeigt werden.
//		 * 
//		 */
//
//		if (playerList != null) {
//			int size = 50;
//			for (ClientAttributes ca : playerList) {
//				int ID = playerList.indexOf(ca);
//				if (playerNames != null) {
//					try {
//						if (playerNames.get(ID) != null) {
//							if (!(playerNames.get(ID).getText().equals(ca.getName()))) { // Wenn das JTextField an der
//																							// Stelle *ID* NICHT den
//																							// gleichen
//																							// Namen hat wie der Spieler
//																							// *ca*
//																							// Also eine Neuerung
//																							// auftritt
//
//								playerNames.get(ID).setText(ca.getName());
//
//								if (mutePlayerButtons.get(ID) == null) { // Spieler 1 Button an der Stelle *ID* noch
//																		// nicht vorhanden
//
//									JButton tempButtonOne = new JButton("1");
//									tempButtonOne.setToolTipText("Als Spieler 1(links) registrieren");
//									tempButtonOne.setPreferredSize(new Dimension(size, size));
//									tempButtonOne.setLocation(402, ID * size + size);
//
//									mutePlayerButtons.add(tempButtonOne);
//									playerListPanel.add(tempButtonOne);
//									somethingHasChanged = true;
//								}
//								if (kickPlayerButtons.get(ID) == null) {// Spieler 2 Button an der Stelle *ID* noch nicht
//																		// vorhanden
//
//									JButton tempButtonTwo = new JButton("2");
//									tempButtonTwo.setToolTipText("Als Spieler 2(rechts) registrieren");
//									tempButtonTwo.setPreferredSize(new Dimension(size, size));
//									tempButtonTwo.setLocation(453, ID * size + size);
//
//									kickPlayerButtons.add(tempButtonTwo);
//									playerListPanel.add(tempButtonTwo);
//
//									somethingHasChanged = true;
//								}
//								if (banPlayerButtons.get(ID) == null) {
//									JButton tempSpectatorButton = new JButton(); // Spectator
//									tempSpectatorButton.setIcon(spectatorIcon);
//									tempSpectatorButton.setToolTipText("Als Spectator registrieren");
//									tempSpectatorButton.setPreferredSize(new Dimension(size, size));
//									tempSpectatorButton.setLocation(453, ID * size + size);
//									banPlayerButtons.add(tempSpectatorButton);
//									playerListPanel.add(tempSpectatorButton);
//
//									somethingHasChanged = true;
//								}
//							}
//						} else { // JTextField noch nicht vorhanden: Neuer Spieler/User
//
//							addUserListLine(ID, size, ca);
//							somethingHasChanged = true;
//							minHeightFromPanel = (ID + 2) * size + 1;
//						}
//					} catch (IndexOutOfBoundsException e) {// JTextField noch nicht vorhanden: Neuer Spieler/User
//
//						addUserListLine(ID, size, ca);
//						somethingHasChanged = true;
//						minHeightFromPanel = (ID + 2) * size + 1;
//					}
//				} else {// JTextField noch nicht vorhanden: Neuer Spieler/User
//
//					addUserListLine(ID, size, ca);
//					somethingHasChanged = true;
//					minHeightFromPanel = (ID + 2) * size + 1;
//				}
//			}
//
//			/*
//			 * Sicherheits-Kontrolle:
//			 * 
//			 * Falls Spieler in JTextField vorhanden, welcher in der UserListe nicht mehr
//			 * vorhanden ist -> Also ein Client der den Server verlassen hat
//			 * 
//			 */
//			try {
//
//				for (JTextField jt : playerNames) {
//					int ID = playerNames.indexOf(jt);
//					try {
//						if (playerList.get(ID) != null) {
//							// if(!(playerList.get(ID).getName().equals(jt.getName()))) { // Wenn das
//							// ClientAttribut-Objekt an der
//							// // Stelle *ID* NICHT den gleichen
//							// // Namen hat wie das TextFeld *ca*
//							// // Also eine Neuerung auftritt
//
//						} else {// Veralteter Eintrag bei JTextField
//
//							playerListPanel.remove(jt);
//							playerListPanel.remove(mutePlayerButtons.get(ID));
//							playerListPanel.remove(kickPlayerButtons.get(ID));
//							playerListPanel.remove(banPlayerButtons.get(ID));
//
//							somethingHasChanged = true;
//							minHeightFromPanel = jt.getLocation().y - size;
//
//							playerNames.remove(jt);
//							mutePlayerButtons.remove(ID);
//							kickPlayerButtons.remove(ID);
//							banPlayerButtons.remove(ID);
//						}
//					} catch (IndexOutOfBoundsException e) {// Veralteter Eintrag bei JTextField
//
//						playerListPanel.remove(jt);
//						playerListPanel.remove(mutePlayerButtons.get(ID));
//						playerListPanel.remove(kickPlayerButtons.get(ID));
//						playerListPanel.remove(banPlayerButtons.get(ID));
//
//						somethingHasChanged = true;
//						minHeightFromPanel = jt.getLocation().y - size;
//
//						playerNames.remove(jt);
//						mutePlayerButtons.remove(ID);
//						kickPlayerButtons.remove(ID);
//						banPlayerButtons.remove(ID);
//					}
//				}
//			} catch (Exception e) {
//				reloadPlayerList();
//			}
//
//			if (somethingHasChanged) {
//				if (minHeightFromPanel < 299)
//					minHeightFromPanel = 299;
//				playerListPanel.setPreferredSize(new Dimension(450, minHeightFromPanel));
////	        	System.out.println("minHeight: "+minHeightFromPanel);
//				playerListPanel.revalidate();
//				playerListPanel.repaint();
//
//				System.out.println(
//						"PanelSize: " + playerListPanel.getSize() + " : " + playerListPanel.getPreferredSize());
//
//			}
//		}
//	}
//	
	private void addGameRequestListLine(ClientAttributes leftPlayer, ClientAttributes rightPlayer) {
//		MenuButton declineRequestButton = new MenuButton(pongFrame, "");
//			String ID;
//			if (id < 10)
//				ID = "0" + Integer.toString(id);
//			else
//				ID = Integer.toString(id);

		MenuTextField leftPlayerName = new MenuTextField(pongFrame, leftPlayer.getName());
		leftPlayerName.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
		leftPlayerName.setEditable(false);
		leftPlayerName.setSize(new Dimension(Math.round(120*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));
		
		MenuTextField rightPlayerName = new MenuTextField(pongFrame, rightPlayer.getIP());
		rightPlayerName.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
		rightPlayerName.setEditable(false);
		rightPlayerName.setSize(new Dimension(Math.round(120*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));

		MenuButton acceptNStartButton = new MenuButton(pongFrame, "Akzeptieren & starten");
		acceptNStartButton.setFont(pongFrame.getGLOBAL_FONT().deriveFont(8f * pongFrame.getASPECT_RATIO()));
		acceptNStartButton.setSize(new Dimension(Math.round(168f*pongFrame.getASPECT_RATIO()), Math.round(14.9f*pongFrame.getASPECT_RATIO())));
		
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
				requestPlayerLeftNames.remove(leftPlayerName);
				requestPlayerRightNames.remove(rightPlayerName);
				acceptNStartButtons.remove(acceptNStartButton);
				declineRequestButtons.remove(declineRequestButton);

				gameRequestListPanel.remove(leftPlayerName);
				gameRequestListPanel.remove(rightPlayerName);
				gameRequestListPanel.remove(acceptNStartButton);
				gameRequestListPanel.remove(declineRequestButton);
				
				//Nachricht an den Client senden, der diese anfrage geschickt hat, dass der Admin abgelehnt hat.
			}
		});
		acceptNStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Von der Request-Liste löschen
				requestPlayerLeftNames.remove(leftPlayerName);
				requestPlayerRightNames.remove(rightPlayerName);
				acceptNStartButtons.remove(acceptNStartButton);
				declineRequestButtons.remove(declineRequestButton);

				gameRequestListPanel.remove(leftPlayerName);
				gameRequestListPanel.remove(rightPlayerName);
				gameRequestListPanel.remove(declineRequestButton);
				gameRequestListPanel.remove(acceptNStartButton);
				
				//-> Nun eine anfrage an die Clients schicken, und diese anfrage als game in die gameliste mit status "requested" aufnehmen
			}
		});
		
		requestPlayerLeftNames.add(leftPlayerName);
		requestPlayerRightNames.add(rightPlayerName);
		acceptNStartButtons.add(acceptNStartButton);
		declineRequestButtons.add(declineRequestButton);

		gameRequestListPanel.add(leftPlayerName);
		gameRequestListPanel.add(rightPlayerName);
		gameRequestListPanel.add(acceptNStartButton);
		gameRequestListPanel.add(declineRequestButton);
	}

	public void processGameRequestFromClient(ClientAttributes leftPlayer, ClientAttributes rightPlayer) {
//		int id = acceptNStartButtons.size()-1;
//		if(id<0)id=0;
//		addGameRequestListLine(id, leftPlayer, rightPlayer);
		addGameRequestListLine(leftPlayer, rightPlayer);
	}
	
	public class SendPressEnterListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				JTextField comp = (JTextField) e.getComponent();
				comp.setEditable(false);
				requestFocus();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}

	private class ReloadClientNameThread implements Runnable {

		public void run() {
			lock = new Object();
			while (true) {
				if (pongFrame.isUpdateUserListOnServer()) {
					synchronized(lock) {

						reloadPlayerList();
					}
//					reloadGameList(); // Nicht benötigt
//					reloadGameRequestList();
				}
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

//	public static class SomeAction implements ActionListener {
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			String actionCommand = e.getActionCommand();
//			if ("SPIELER_LINKS".equals(new String(actionCommand.substring(0, e.getActionCommand().length() - 4)))) {
//				int ID = Integer
//						.valueOf(actionCommand.substring(actionCommand.indexOf("[") + 1, actionCommand.indexOf("]")));
////				System.out.println("liiinks: " + ID);
//
//				actualPlayerOneID = ID;
//
//				for (JButton button : mutePlayerButtons) {
//					int id = mutePlayerButtons.indexOf(button);
//					button.setEnabled(true);
//					if (kickPlayerButtons.get(id).isEnabled())
//						banPlayerButtons.get(id).setEnabled(false);
//				}
//
//				mutePlayerButtons.get(ID).setEnabled(false);
//				kickPlayerButtons.get(ID).setEnabled(true);
//				banPlayerButtons.get(ID).setEnabled(true);
//
//			} else if ("SPIELER_RECHTS"
//					.equals(new String(actionCommand.substring(0, e.getActionCommand().length() - 4)))) {
//				int ID = Integer
//						.valueOf(actionCommand.substring(actionCommand.indexOf("[") + 1, actionCommand.indexOf("]")));
////				System.out.println("reeechts: " + ID);
//
//				actualPlayerTwoID = ID;
//
//				for (JButton button : kickPlayerButtons) {
//					int id = kickPlayerButtons.indexOf(button);
//					button.setEnabled(true);
//					if (mutePlayerButtons.get(id).isEnabled())
//						banPlayerButtons.get(id).setEnabled(false);
//				}
//
//				mutePlayerButtons.get(ID).setEnabled(true);
//				kickPlayerButtons.get(ID).setEnabled(false);
//				banPlayerButtons.get(ID).setEnabled(true);
//
//			} else if ("SPECTATOR".equals(new String(actionCommand.substring(0, e.getActionCommand().length() - 4)))) {
//				int ID = Integer
//						.valueOf(actionCommand.substring(actionCommand.indexOf("[") + 1, actionCommand.indexOf("]")));
////				System.out.println("Spectaaaator: " + ID);
//
//				mutePlayerButtons.get(ID).setEnabled(true);
//				kickPlayerButtons.get(ID).setEnabled(true);
//				banPlayerButtons.get(ID).setEnabled(false);
//
//			} else if ("".equals(actionCommand)) {
//
//			} else if ("".equals(actionCommand)) {
//
//			}
//
//			createGame.setEnabled(checkForRegularPlayerSettings());
//
//		}
//
//		private boolean checkForRegularPlayerSettings() {
//			int countedOne = 0, countedTwo = 0;
//			for (JButton one : mutePlayerButtons) {
//				if (!one.isEnabled())
//					countedOne++;
//			}
//
//			for (JButton two : kickPlayerButtons) {
//				if (!two.isEnabled())
//					countedTwo++;
//			}
//
//			return (countedOne == 1 && countedTwo == 1);
//
//		}
//	}
}