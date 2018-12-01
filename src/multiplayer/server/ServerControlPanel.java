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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gui.ShadowLabel;
import hauptmenu.CreateServerPanel;
import hauptmenu.PongFrame;
import main.PongMain;
import multiplayer.client.ClientAttributes;
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
	static  JButton stopServer, startGame, skipBack;
	static int actualPlayerOneID = 0, actualPlayerTwoID;
	private static JPanel contentPane;
	private JTextField userNameTextField, maxUserTextField;
	private ShadowLabel maxUserLabel, title, labelServerName, consoleTitle;

	//Components for Setting up the 1st and 2nd player
	private static JPanel playerOneTwoPanel, playerOneTwoTitlePanel;
	private ShadowLabel playerNameTitle, playerOneTitle, playerTwoTitle, spectatorTitle;
	private static ArrayList<JTextField> playerNames;
	private static ArrayList<JButton> playerOneButtons;
	private static ArrayList<JButton> playerTwoButtons;
	private static ArrayList<JButton> spectatorButtons;
	private static JScrollPane scrollPane;
	private Thread t = new Thread(new ReloadClientNameThread());
	private static ImageIcon spectatorIcon = ImageLoader.loadIcon("user_glasses.png", 50, 50);

	private boolean spielLäuftBereits = false;
	private PongFrame pongFrame;
	
	public ServerControlPanel(PongFrame pongFrame) {

		this.pongFrame = pongFrame;
		this.setLayout(new BorderLayout());
		contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setBackground(Color.black);
		this.setSize(pongFrame.getSize());
		contentPane.setPreferredSize(new Dimension((int) (pongFrame.getWidth() * 0.5), (int) (pongFrame.getHeight() * 0.5)));
//		contentPane.setMaximumSize(new Dimension((int) (pongFrame.getWidth() * 0.5), (int) (pongFrame.getHeight() * 0.5)));
		contentPane.setLayout(null);

		title = new ShadowLabel("Spiel konfigurieren", 30, 300, 49);
		title.setBounds(25, 0, 400, 60);
		contentPane.add(title);

		contentPane.setAlignmentX(SwingConstants.CENTER);
		contentPane.setAlignmentY(SwingConstants.CENTER);
		this.setAlignmentX(SwingConstants.CENTER);
		this.setAlignmentY(SwingConstants.CENTER);

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

		startGame = new JButton("Spiel Starten"); 
		startGame.setBounds(180, 200, 150, 50);
		startGame.setEnabled(false);
		startGame.addActionListener(this);
		contentPane.add(startGame);

		skipBack = new JButton("Zurück zum Hauptmenü");
		skipBack.setBounds(25, 255, 305, 50);
		skipBack.addActionListener(this);
		contentPane.add(skipBack);
		
		playerNames = new ArrayList<JTextField>();
		playerOneButtons = new ArrayList<JButton>();
		playerTwoButtons = new ArrayList<JButton>();
		spectatorButtons = new ArrayList<JButton>();
		
		playerNameTitle = new ShadowLabel("Spieler-Name", 25, 166, 35);
		playerNameTitle.setPreferredSize(new Dimension(348, 37));
		
		playerOneTitle = new ShadowLabel("Spieler 1", 12, 50, 35);
		playerOneTitle.setPreferredSize(new Dimension(50, 37));
		
		playerTwoTitle = new ShadowLabel("Spieler 2", 12, 50, 35);
		playerTwoTitle.setPreferredSize(new Dimension(50, 37));
		
		spectatorTitle = new ShadowLabel("Spectator", 10, 50, 35);
		spectatorTitle.setPreferredSize(new Dimension(50, 37));
		
		playerOneTwoPanel = new JPanel();
		playerOneTwoPanel.setBounds(0, 0, 505, 299);//505,299
		playerOneTwoPanel.setPreferredSize(new Dimension(450, 299));
		playerOneTwoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));

		playerOneTwoTitlePanel = new JPanel();
		playerOneTwoTitlePanel.setPreferredSize(new Dimension(505, 40));
		playerOneTwoTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
		playerOneTwoTitlePanel.add(playerNameTitle);
		playerOneTwoTitlePanel.add(playerOneTitle);
		playerOneTwoTitlePanel.add(playerTwoTitle);
		playerOneTwoTitlePanel.add(spectatorTitle);
		
		playerOneTwoPanel.add(playerOneTwoTitlePanel);
		playerOneTwoPanel.setOpaque(false);
		scrollPane = new JScrollPane(playerOneTwoPanel);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        scrollPane.setBounds(355, 100, 547, 299);//522,299
        scrollPane.setLocation(355, 100);
        scrollPane.setOpaque(false);

        contentPane.add(scrollPane);
		
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
		
		if(!t.isAlive())t.start();
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
//		System.out.println(new String(actionCommand.substring(0, e.getActionCommand().length() - 3)));
		if (actionCommand.equals(stopServer.getText())) {
			// Nachricht "Server geschlossen" an alle Clients senden und Interrupt in allen
			// ServerThreads auslösen
			stopServer.setEnabled(false);
//			pongFrame.getMultiPlayer().getCreateServerPanel().setServerIsRunning(false);
			pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(false);
			pongFrame.getHostServer().sendToAllClients("SERVER_CLOSED");
			pongFrame.getHostServer().stop();
			pongFrame.getMultiPlayer().getCreateServerPanel().refresh();
			pongFrame.showPane(pongFrame.MULTI_PLAYER);
			
		} else if (actionCommand.equals(startGame.getText())) {


			/*
			 * Start Button nur aktiviert wenn 1 Spieler Spieler 1 und 1 Spieler Spieler 2 ist.
			 * 
			 * Den aktivierten Clients bescheid sagen, dass das Spiel nun los geht,
			 * und diese in den Live-Game-Modus schalten sollen.
			 * Zudem soll konfiguriert werden welcher client den linken und welcher den rechten slider steuern soll.
			 * 
			 */
			if(!spielLäuftBereits) {
				System.out.println(" SPIEL WIRD GESTARTET: Spiel Stoppen");
				String playerOneName = playerNames.get(actualPlayerOneID).getText();
				String playerTwoName = playerNames.get(actualPlayerTwoID).getText();
				
				System.out.println("CLIENT-LEFT: "+playerOneName+" CLIENT-RIGHT: "+playerTwoName);
				pongFrame.getHostServer().configureGameForClients(getClientByName(playerOneName), getClientByName(playerTwoName));
				
				spielLäuftBereits = true;
				startGame.setText("Spiel Stoppen");
			}else {
				System.out.println("SPIEL WIRD GESTOPPT: Spiel Starten");
				spielLäuftBereits = false;
				startGame.setText("Spiel Starten");
				
				pongFrame.getHostServer().stopGameForClients("Das Spiel wurde abgebrochen.");
			}
//			pongFrame.getHostServer().sendToAllClients(pongFrame.getHostServer().NO_CHAT_MESSAGE+pongFrame.getHostServer().GAME_START);
			
//			PongFrame.showPane(PongFrame.SERVER_LIVE_GAME_PANEL);

		} else if (actionCommand.equals(skipBack.getText())) {
			pongFrame.showPane(pongFrame.MULTI_PLAYER);

		} else if ("EDIT_SENTENCE".equals(new String(actionCommand.substring(0, e.getActionCommand().length() - 1)))) {

		} else if ("DELETE_SENTENCE"
				.equals(new String(actionCommand.substring(0, e.getActionCommand().length() - 1)))) {

		}
	}

	private ClientAttributes getClientByName(String name) {
		ArrayList<ClientAttributes> playerList = pongFrame.getHostServer().getListClients();
		
		for(ClientAttributes cA : playerList) {
			if(cA.getName().equals(name))return cA;
		}
		
		return null;
	}
	
	public void reloadPlayerList() {
		
		ArrayList<ClientAttributes> playerList = pongFrame.getHostServer().getListClients();
		boolean somethingHasChanged = false;
		int minHeightFromPanel = 299;
		/*
		 * Spieler Liste anzeigen mit jeweils 2 Buttons rechts
		 * der linke button soll den jeweiligen Spieler als Spieler1 (links) festlegen,
		 * der rechte Button soll den jeweiligen Spieler als den Spieler 2 (rechts) festlegen,
		 * jeder Spieler darf nur linker oder rechter Spieler sein,
		 * nur ein Spieler darf linker- und nur ein Spieler rechter Spieler sein.
		 * 
		 * Diese Einstellungen sollen live an alle Clients(ClientControlPanel) übertragen und angezeigt werden.
		 * 
		 */

		if(playerList != null) {
			int size = 50;
			for(ClientAttributes ca : playerList) {
				int ID = playerList.indexOf(ca);
				if(playerNames!=null) {
					try {
						if(playerNames.get(ID)!=null) {
							if(!(playerNames.get(ID).getText().equals(ca.getName()))) { // Wenn das JTextField an der 
																						// Stelle *ID* NICHT den gleichen 
																						// Namen hat wie der Spieler *ca*
																						// Also eine Neuerung auftritt
								
								playerNames.get(ID).setText(ca.getName());
								
								if(playerOneButtons.get(ID)==null) { // Spieler 1 Button an der Stelle *ID* noch nicht vorhanden
									
									JButton tempButtonOne = new JButton("1");
									tempButtonOne.setToolTipText("Als Spieler 1(links) registrieren");
									tempButtonOne.setPreferredSize(new Dimension(size, size));
									tempButtonOne.setLocation(402, ID*size+size);

									playerOneButtons.add(tempButtonOne);
									playerOneTwoPanel.add(tempButtonOne);
									somethingHasChanged = true;
								}
								if(playerTwoButtons.get(ID)==null) {// Spieler 2 Button an der Stelle *ID* noch nicht vorhanden
									
									JButton tempButtonTwo = new JButton("2");
									tempButtonTwo.setToolTipText("Als Spieler 2(rechts) registrieren");
									tempButtonTwo.setPreferredSize(new Dimension(size, size));
									tempButtonTwo.setLocation(453, ID*size +size);

									playerTwoButtons.add(tempButtonTwo);
									playerOneTwoPanel.add(tempButtonTwo);
									 
									somethingHasChanged = true;
								}
								if(spectatorButtons.get(ID)==null) {
									JButton tempSpectatorButton = new JButton(); //Spectator
									tempSpectatorButton.setIcon(spectatorIcon);
									tempSpectatorButton.setToolTipText("Als Spectator registrieren");
									tempSpectatorButton.setPreferredSize(new Dimension(size, size));
									tempSpectatorButton.setLocation(453, ID*size +size);
									spectatorButtons.add(tempSpectatorButton);
									playerOneTwoPanel.add(tempSpectatorButton);
									
									somethingHasChanged = true;
								}
							}
						}else { //JTextField noch nicht vorhanden: Neuer Spieler/User
							
							addLine(ID, size, ca);
							somethingHasChanged = true;
							minHeightFromPanel = (ID+2)*size +1;
						}
					} catch ( IndexOutOfBoundsException e ) {//JTextField noch nicht vorhanden: Neuer Spieler/User
						
						addLine(ID, size, ca);
						somethingHasChanged = true;
						minHeightFromPanel = (ID+2)*size +1;
					}
				}else {//JTextField noch nicht vorhanden: Neuer Spieler/User
					
					addLine(ID, size, ca);
					somethingHasChanged = true;
					minHeightFromPanel = (ID+2)*size +1;
				}
			}

			/*
			 * Sicherheits-Kontrolle:
			 * 
			 * Falls Spieler in JTextField vorhanden, 
			 * welcher in der UserListe nicht mehr vorhanden ist
			 * -> Also ein Client der den Server verlassen hat
			 * 
			 */
			try {
				
				for(JTextField jt : playerNames) {
					int ID = playerNames.indexOf(jt);
					try {
						if(playerList.get(ID)!=null) {
		//					if(!(playerList.get(ID).getName().equals(jt.getName()))) { // Wenn das ClientAttribut-Objekt an der 
		//																				// Stelle *ID* NICHT den gleichen 
		//																				// Namen hat wie das TextFeld *ca*
		//																				// Also eine Neuerung auftritt

						}else {//Veralteter Eintrag bei JTextField
							
							playerOneTwoPanel.remove(jt);
							playerOneTwoPanel.remove(playerOneButtons.get(ID));
							playerOneTwoPanel.remove(playerTwoButtons.get(ID));
							playerOneTwoPanel.remove(spectatorButtons.get(ID));
							
							somethingHasChanged = true;
							minHeightFromPanel = jt.getLocation().y-size;
							
							playerNames.remove(jt);
							playerOneButtons.remove(ID);
							playerTwoButtons.remove(ID);
							spectatorButtons.remove(ID);
						}
					}catch(IndexOutOfBoundsException e) {//Veralteter Eintrag bei JTextField
					
						playerOneTwoPanel.remove(jt);
						playerOneTwoPanel.remove(playerOneButtons.get(ID));
						playerOneTwoPanel.remove(playerTwoButtons.get(ID));
						playerOneTwoPanel.remove(spectatorButtons.get(ID));
						
						somethingHasChanged = true;
						minHeightFromPanel = jt.getLocation().y-size;	
						
						playerNames.remove(jt);
						playerOneButtons.remove(ID);
						playerTwoButtons.remove(ID);
						spectatorButtons.remove(ID);
					}
				}
			}catch(Exception e) {
				reloadPlayerList();
			}

	        if(somethingHasChanged) {
	        	if(minHeightFromPanel<299)minHeightFromPanel=299;
	    		playerOneTwoPanel.setPreferredSize(new Dimension(450, minHeightFromPanel));
//	        	System.out.println("minHeight: "+minHeightFromPanel);
		        playerOneTwoPanel.revalidate();
		        playerOneTwoPanel.repaint();
		        
		        System.out.println("PanelSize: "+playerOneTwoPanel.getSize()+" : "+playerOneTwoPanel.getPreferredSize());

	        }
		}
	}
	
	private static void addLine(int id, int size, ClientAttributes ca) {
		String ID;
		if(id<10)ID = "0"+Integer.toString(id);
		else ID = Integer.toString(id);
		
		JTextField tempTextField = new JTextField(ca.getName());
		tempTextField.setEditable(false);
		tempTextField.setPreferredSize(new Dimension((int)Math.round(size*7), size));

		
		JButton tempButtonOne = new JButton("1");
		tempButtonOne.setToolTipText("Als Spieler 1(links) registrieren"); 
		tempButtonOne.setPreferredSize(new Dimension(size, size));
		tempButtonOne.setActionCommand("SPIELER_LINKS["+ID+"]");
		tempButtonOne.addActionListener(new SomeAction());
		
		JButton tempButtonTwo = new JButton("2");
		tempButtonTwo.setToolTipText("Als Spieler 2(rechts) registrieren");
		tempButtonTwo.setPreferredSize(new Dimension(size, size));
		tempButtonTwo.setActionCommand("SPIELER_RECHTS["+ID+"]");
		tempButtonTwo.addActionListener(new SomeAction());
		
		JButton tempSpectatorButton = new JButton(); //Spectator
		tempSpectatorButton.setIcon(spectatorIcon);
		tempSpectatorButton.setToolTipText("Als Spectator registrieren");
		tempSpectatorButton.setPreferredSize(new Dimension(size, size));
		tempSpectatorButton.setActionCommand("SPECTATOR["+ID+"]");
		tempSpectatorButton.addActionListener(new SomeAction());
		tempSpectatorButton.setEnabled(false);
		
		playerNames.add(tempTextField);		
		playerOneButtons.add(tempButtonOne);
		playerTwoButtons.add(tempButtonTwo);
		spectatorButtons.add(tempSpectatorButton);
		
		playerOneTwoPanel.add(tempTextField);
		playerOneTwoPanel.add(tempButtonOne);
		playerOneTwoPanel.add(tempButtonTwo);
		playerOneTwoPanel.add(tempSpectatorButton);
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
			while(true) {
				if(pongFrame.isUpdateUserListOnServer()) {
					reloadPlayerList();
				}
				try {
					Thread.sleep(10);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static class SomeAction implements ActionListener{


		@Override
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
//			System.out.println(new String(actionCommand.substring(0, e.getActionCommand().length() - 4)));
			
			
			
			if("SPIELER_LINKS".equals(new String(actionCommand.substring(0, e.getActionCommand().length() - 4)))) {
				int ID = Integer.valueOf(actionCommand.substring(actionCommand.indexOf("[")+1, actionCommand.indexOf("]")));
				System.out.println("liiinks: "+ID);
				
				actualPlayerOneID = ID;
				
				for(JButton button : playerOneButtons) {
					int id = playerOneButtons.indexOf(button);
					button.setEnabled(true);
					if(playerTwoButtons.get(id).isEnabled())spectatorButtons.get(id).setEnabled(false);
				}
				
				playerOneButtons.get(ID).setEnabled(false);
				playerTwoButtons.get(ID).setEnabled(true);
				spectatorButtons.get(ID).setEnabled(true);
				
				
				
			}else if("SPIELER_RECHTS".equals(new String(actionCommand.substring(0, e.getActionCommand().length() - 4)))) {
				int ID = Integer.valueOf(actionCommand.substring(actionCommand.indexOf("[")+1, actionCommand.indexOf("]")));
				System.out.println("reeechts: "+ID);
				
				actualPlayerTwoID = ID;
				
				for(JButton button : playerTwoButtons) {
					int id = playerTwoButtons.indexOf(button);
					button.setEnabled(true);
					if(playerOneButtons.get(id).isEnabled())spectatorButtons.get(id).setEnabled(false);
				}
				
				playerOneButtons.get(ID).setEnabled(true);
				playerTwoButtons.get(ID).setEnabled(false);
				spectatorButtons.get(ID).setEnabled(true);
				
			}else if("SPECTATOR".equals(new String(actionCommand.substring(0, e.getActionCommand().length() - 4)))) {
				int ID = Integer.valueOf(actionCommand.substring(actionCommand.indexOf("[")+1, actionCommand.indexOf("]")));
				System.out.println("Spectaaaator: "+ID);
				
				playerOneButtons.get(ID).setEnabled(true);
				playerTwoButtons.get(ID).setEnabled(true);
				spectatorButtons.get(ID).setEnabled(false);
				
			}else if("".equals(actionCommand)) {
				
				
			}else if("".equals(actionCommand)) {
				
				
			}
			
			startGame.setEnabled(checkForRegularPlayerSettings());
			
		}
		
		private boolean checkForRegularPlayerSettings() {
			int countedOne = 0, countedTwo = 0;
			for(JButton one : playerOneButtons) {
				if(!one.isEnabled())countedOne++;
			}
			
			for(JButton two : playerTwoButtons) {
				if(!two.isEnabled())countedTwo++;
			}			
			
			return (countedOne == 1 && countedTwo == 1);
			
		}
	}
}