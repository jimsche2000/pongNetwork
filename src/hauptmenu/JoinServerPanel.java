package hauptmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gui.MenuButton;
import gui.MenuLabel;
import gui.MenuTextField;
import multiplayer.server.ServerAttributes;

public class JoinServerPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1809224404389227045L;
	private JPanel contentPane, namePanel;
	private MenuTextField textField;
	private MenuLabel nameLabel;
	private MenuLabel nameAlreadyTaken;
	private JPanel reloadPanel;
	private MenuButton reloadButton;
	private JPanel serverListPanel, serverListTitlePanel;
	private MenuLabel serverNameTitle, serverIpAdressTitle, serverUserCountTitle, serverJoinTitle;
	private ArrayList<MenuButton> serverJoinButtons;
	private JScrollPane scrollPane;

	public final int CLIENT_ACCEPTED = -1;
	public final int NAME_ALREADY_IN_USE = 0;
	public final int IP_ALREADY_IN_USE = 1;
	private volatile ArrayList<ServerAttributes> serverList;

	private PongFrame pongFrame;

	public JoinServerPanel(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		this.setSize(pongFrame.getSize());
		this.setLayout(new BorderLayout());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		namePanel = new JPanel();
		namePanel.setPreferredSize(new Dimension(1920, 110));
		namePanel.setOpaque(false);
		nameLabel = new MenuLabel(pongFrame, "Dein Name:");
		nameLabel.setSize(new Dimension(250, 50));
		nameLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(24f));
		namePanel.add(nameLabel);

		textField = new MenuTextField(pongFrame, "Anonymous");
		textField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
		textField.setSize(new Dimension(300, 50));
		textField.setBorder(new EmptyBorder(0, 0, 0, 0));

		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				clearNameAlreadyTakenLabel();
			}

			public void removeUpdate(DocumentEvent e) {
				clearNameAlreadyTakenLabel();
			}

			public void insertUpdate(DocumentEvent e) {
				clearNameAlreadyTakenLabel();
			}

			public void clearNameAlreadyTakenLabel() {
				String text = "Gebe dir einen Namen und joine auf den Server deiner Wahl - oder erstelle einen Eigenen.";
				if (!nameAlreadyTaken.getText().equals(text))
					nameAlreadyTaken.setText(
							"Gebe dir einen Namen und joine auf den Server deiner Wahl - oder erstelle einen Eigenen.");
			}
		});
		namePanel.add(textField);
		contentPane.add(namePanel);

		nameAlreadyTaken = new MenuLabel(pongFrame,
				"Gebe dir einen Namen und joine auf den Server deiner Wahl - oder erstelle einen Eigenen.");
		nameAlreadyTaken.setFont(pongFrame.getGLOBAL_FONT().deriveFont(15f));
		nameAlreadyTaken.setSize(new Dimension(1920, 50));
		nameAlreadyTaken.setBorder(new EmptyBorder(1, 700, 1, 700));
		nameAlreadyTaken.setForeground(Color.red);
		namePanel.add(nameAlreadyTaken);

		serverList = new ArrayList<ServerAttributes>();
		serverJoinButtons = new ArrayList<MenuButton>();

		reloadPanel = new JPanel();
		reloadPanel.setPreferredSize(new Dimension(1920, 50));
		reloadButton = new MenuButton(pongFrame, "RELOAD");
		reloadButton.setSize(new Dimension(200, 50));
		reloadButton.addActionListener(this);
		reloadPanel.setOpaque(false);
		reloadPanel.add(reloadButton);
		contentPane.add(reloadPanel);
		
		
		serverNameTitle = new MenuLabel(pongFrame, "Server-Name");
		serverNameTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12f));
		serverNameTitle.setHorizontalTextPosition(SwingConstants.LEFT);
		serverNameTitle.setDrawBottomLine(true);
		serverNameTitle.setSize(new Dimension(400, 50));

		serverIpAdressTitle = new MenuLabel(pongFrame, "IP-Adresse");
		serverIpAdressTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12f));
		serverIpAdressTitle.setHorizontalTextPosition(SwingConstants.LEFT);
		serverIpAdressTitle.setDrawBottomLine(true);
		serverIpAdressTitle.setSize(new Dimension(300, 50));

		serverUserCountTitle = new MenuLabel(pongFrame, "Spieler");
		serverUserCountTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12f));
		serverUserCountTitle.setHorizontalTextPosition(SwingConstants.LEFT);
		serverUserCountTitle.setDrawBottomLine(true);
		serverUserCountTitle.setSize(new Dimension(150, 50));

		serverJoinTitle = new MenuLabel(pongFrame, "Verbinden");
		serverJoinTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12f));
		serverJoinTitle.setHorizontalTextPosition(SwingConstants.RIGHT);
		serverJoinTitle.setDrawBottomLine(true);
		serverJoinTitle.setSize(new Dimension(140, 50));

		serverListPanel = new JPanel();
		serverListPanel.setPreferredSize(new Dimension(1000, 410));
		serverListPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));

		serverListTitlePanel = new JPanel();
		serverListTitlePanel.setPreferredSize(new Dimension(1000, 50));
		serverListTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		serverListTitlePanel.add(serverNameTitle);
		serverListTitlePanel.add(serverIpAdressTitle);
		serverListTitlePanel.add(serverUserCountTitle);
		serverListTitlePanel.add(serverJoinTitle);

		contentPane.add(serverListTitlePanel);
		scrollPane = new JScrollPane(serverListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setPreferredSize(new Dimension(serverListPanel.getPreferredSize().width + 15,
		serverListPanel.getPreferredSize().height + 5));

		contentPane.add(scrollPane);

		serverListTitlePanel.setOpaque(false);
		serverListPanel.setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		contentPane.setOpaque(false);
		this.setOpaque(false);
		this.add(contentPane, BorderLayout.CENTER);
	}

	public synchronized ArrayList<ServerAttributes> getServerList() {
		return serverList;
	}

	public synchronized void setServerList(ArrayList<ServerAttributes> serverList) {
		this.serverList = serverList;
	}

//	public void startAutoReloadServerTableList() {
//
//		if (!t.isAlive())
//			t.start();
//
//	}

//	@SuppressWarnings("unlikely-arg-type")
//	private void reloadServerTableList() {
//
//		boolean somethingHasChanged = false;
//		int minHeightFromPanel = 410; //510
//		/*
//		 * Server Liste anzeigen mit 3 disabledten TextFelder: Server-Name, iP-Adresse,
//		 * User-Count MenuButton zum beitreten/verbinden
//		 */
//
//		/*
//		 * Name Breite Höhe Name 400 50 Ip 300 50 UserC 150 50 JoinB 140 50
//		 * 
//		 * Summe: 990px
//		 * 
//		 */
//
//		int nameTextFieldWidth = 400, ipTextFieldWidth = 300, userCountTextFieldWidth = 150, joinButtonWidth = 140, size = 50;
//		System.out.println("SERVER_LIST VONG JOIN HER: " + serverList + "\nNAME_LIST VONG HER: " + serverNameTextFields);
//		if (someThingNew(serverList)) {
//			if (serverList != null) {
//				
//				synchronized (this) {
//					server : for (ServerAttributes ca : serverList) {
//						int ID = serverList.indexOf(ca);
//
//						if (serverIpAdressTextFields.size() > 0 && serverNameTextFields.size() > 0) {
////								System.out.println("TRYING FIND A MISSING MENUFIELD: \""
////										+ serverNameTextFields.get(ID).getText() + "\" : \"" + ca.getName() + "\""
////										+ serverNameTextFields.get(ID).getText().equals(ca.getName()));
//								boolean serverAlreadyThere = false;
//								components : for (MenuTextField mt : serverIpAdressTextFields) {		
//									if ((mt.getText().equals(ca.getIP()))) {
//
//										//Wenn Der Server(Name, IP) bereits verfügbar ist
//										serverAlreadyThere = true;
//									}else {
//										System.out.println("IP: "+mt.getText()+" : "
//												+ ""+ca.getIP()+" NAME: "
////														+ ""+serverNameTextFields.get(ID).getText()+" : "
//																+ ""+ca.getName());
//									}
//								}
//								if(!serverAlreadyThere) {//Server noch nicht vorhanden -> ADD NEW LINE
//									System.out.println("add1");
//									addLine(ID, size, ca, serverList, nameTextFieldWidth, ipTextFieldWidth,
//											userCountTextFieldWidth, joinButtonWidth);
//									somethingHasChanged = true;
//									minHeightFromPanel = (ID + 2) * size + 1;
//								}
//						} else {// Erster Server Eintrag -> ADD NEW LINE
//							System.out.println("add2");
//							addLine(ID, size, ca, serverList, nameTextFieldWidth, ipTextFieldWidth, userCountTextFieldWidth,
//									joinButtonWidth);
//							somethingHasChanged = true;
//							minHeightFromPanel = (ID + 2) * size + 1;
//						}
//					}
//				}
//				/*
//				 * Sicherheits-Kontrolle:
//				 * 
//				 * Falls Server in MenuTextField vorhanden, welcher in der ServerListe nicht
//				 * mehr vorhanden ist -> Also ein Server geschlossen/beendet wurde
//				 * 
//				 */
//				try {
//					// System.out.println("serverNameTextFields: "+serverNameTextFields);
//					synchronized(this) {
//						ArrayList<Integer> idList = new ArrayList<Integer>();
//						for (MenuTextField jt : serverIpAdressTextFields) {
//							int ID = serverIpAdressTextFields.indexOf(jt);
//							// System.out.println("ID: "+ID+" sizeList: "+serverList.size());
////							try {
//								if (serverList.size() > 0) {
//									MenuTextField name = serverNameTextFields.get(ID);
//									//User-Count kann ja anders sein
//									boolean serverFound = false;
//									for(ServerAttributes server : serverList) {
//										if(server.getIP().equals(jt.getText())&&server.getName().equals(name.getText())) { //Name und IP Gleich = Server ist noch in der Liste und muss nicht gelöscht werden
//											//delete textfields etc. der Position ID
//											serverFound = true;
//										}
//										if(!serverFound) {
//											idList.add(ID);
//										}
//									}
//								}else {//Server-Eintrag nicht mehr vorhanden -> TextFields etc. löschen
//									//Jegliche Server Einträge löschen
//									idList.add(ID);
//								}
////							} catch (IndexOutOfBoundsException e) {// Veralteter Eintrag bei MenuTextField
////								System.out.println("remove2");
////								e.printStackTrace();
////								
////								reloadServerTableList();
////								//DELETE?!?!?!
////							}
//						}
//						
//						if(idList.size()>0) {
//							
//							for(Integer ID : idList) {
//								
//								serverListPanel.remove(serverNameTextFields.get(ID));
//								serverListPanel.remove(serverIpAdressTextFields.get(ID));
//								serverListPanel.remove(ServerUserCountTextFields.get(ID));
//								serverListPanel.remove(serverJoinButtons.get(ID));
//
//								serverNameTextFields.remove(ID);
//								serverIpAdressTextFields.remove(ID);
//								ServerUserCountTextFields.remove(ID);
//								serverJoinButtons.remove(ID);
//								
//								somethingHasChanged = true;
//								minHeightFromPanel = serverNameTextFields.get(serverNameTextFields.size()-1).getLocation().y - size;
//							}
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				if (somethingHasChanged) {
//					System.out.println("\n\n\n\nCHANGING_SOMETHING: Nicht null -> Veränderung\n\n\n\n");
//					if (minHeightFromPanel < 410)
//						minHeightFromPanel = 410;
//					serverListPanel.setPreferredSize(new Dimension(1000, minHeightFromPanel));
//					serverListPanel.revalidate();
//					serverListPanel.repaint();
//					scrollPane.getViewport().revalidate();
//					scrollPane.getViewport().repaint();
//				}
//			}
//		}
//		System.out.println("NAMES: "+serverNameTextFields+"\nIP: "+serverIpAdressTextFields);
//	}
//
//	private boolean someThingNew(ArrayList<ServerAttributes> serverList) {
////		System.out.println("------checkIfSomethingIsNew------");
//		boolean notFound = false;
//		try {
//			if(serverList.size()<1 && (serverNameTextFields.size()>0 && serverIpAdressTextFields.size()>0&&ServerUserCountTextFields.size()>0)) {
//				notFound = true;
//			}else {
//				for (ServerAttributes sa : serverList) {
//					System.out.println("Server: "+sa);
//					if(serverNameTextFields.size()<1 && serverIpAdressTextFields.size()<1&&ServerUserCountTextFields.size()<1) {
//						
//						notFound = true;
//					}
//					for (MenuTextField mt : serverNameTextFields) {
////						System.out.println("Server_name: "+sa.getName()+" ");
//						if (!(sa.getName().equals(mt.getText()))) {
//							notFound = true;
//						}
//					}
//					for (MenuTextField mt : serverIpAdressTextFields) {
//						if (!(sa.getIP().equals(mt.getText()))) {
//							notFound = true;
//						}
//					}
//					for (MenuTextField mt : ServerUserCountTextFields) {
//						if (!(sa.getUser_count().equals(mt.getText()))) {
//							notFound = true;
//						}
//					}
//				}
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//			/*
//			 * 
//			 * WARNING: STACK_OVER_FLOW COULD THROW
//			 */
//			if(serverList!=null)return someThingNew(serverList);
//			else return true;//Vielleicht ist ein Server nicht mehr erreichbar
//		}
//
//
//
////		System.out.println("\n\n\nNOT FOUND? " + notFound);
//		return notFound;
//	}
//
//	private void addLine(int ID, int size, ServerAttributes sa, ArrayList<ServerAttributes> serverList,
//			int nameTextFieldWidth, int ipTextFieldWidth, int userCountTextFieldWidth, int joinButtonWidth) {
//		// System.out.println(sa);
//		System.out.println("\n\n\nADDING LINE{" + sa.getName() + "}\n\n\n");
//		MenuTextField tempTextField = new MenuTextField(pongFrame, sa.getName());
//		tempTextField.setEditable(false);
//		tempTextField.setSize(new Dimension(nameTextFieldWidth, size));
//		// System.out.print("1");
//		MenuTextField tempIpAdressTextField = new MenuTextField(pongFrame, sa.getIP());
//		tempIpAdressTextField.setToolTipText("Server-IP-Adresse");
//		tempIpAdressTextField.setSize(new Dimension(ipTextFieldWidth, size));
//		// tempIpAdressTextField.setLocation(402, ID*size+size);
//		tempIpAdressTextField.setEditable(false);
//		// System.out.print("2");
//		MenuTextField tempUserCountTextField = new MenuTextField(pongFrame, sa.getUser_count());
//		tempUserCountTextField.setToolTipText("Anzahl der Spieler auf dem Server");
//		tempUserCountTextField.setSize(new Dimension(userCountTextFieldWidth, size));
//		// tempUserCountTextField.setLocation(453, ID*size +size);
//		tempUserCountTextField.setEditable(false);
//		// System.out.print("3");
//		MenuButton tempJoinServerButton = new MenuButton(pongFrame, "Verbinden"); // Server join Button noch nicht
//																					// vorhanden
//		// tempJoinServerButton.setIcon(lanIcon);
//		tempJoinServerButton.setToolTipText("Mit dem Server verbinden");
//		tempJoinServerButton.setSize(new Dimension(joinButtonWidth, size));
//		// tempJoinServerButton.setLocation(453, ID*size +size);
//		tempJoinServerButton.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//
//				int ID = serverJoinButtons.indexOf(tempJoinServerButton);
//
//				pongFrame.getClientThread().setUserName(textField.getText());
//				pongFrame.getClientThread().setConnect_Server(serverList.get(ID));
//
//				if (pongFrame.getClientThread().connectToServer()) {
//					System.out.println(">>>VERBINDUNG MIT SERVER HERGESTELLT");
//					pongFrame.getClientThread().setShouldSearchForServer(false);
//
//				} else { // Verbindung nicht hergestellt -> Error
//					System.out.println(">>>ERROR BEIM VERBINDEN");
//				}
//			}
//		});
//		// System.out.print("4\n\n\n");
//		// System.out.println("COMPONENTS CREATED\n\n");
//
//		serverListPanel.add(tempTextField);
//		serverListPanel.add(tempIpAdressTextField);
//		serverListPanel.add(tempUserCountTextField);
//		serverListPanel.add(tempJoinServerButton);
//		
//		serverIpAdressTextFields.add(tempIpAdressTextField);
//		ServerUserCountTextFields.add(tempUserCountTextField);
//		serverJoinButtons.add(tempJoinServerButton);
//		serverNameTextFields.add(tempTextField);
//		// System.out.println("COMPONENTS ADDED TO LIST\n\n");
//
//		// System.out.println(Arrays.asList(serverListPanel.getComponents())+"
//		// changed\n\n\n");
//	}

	// @SuppressWarnings("unused")
	// private class Action implements ActionListener {
	//
	// @Override
	// public void actionPerformed(ActionEvent e) {
	//
	// if (e.getActionCommand().equals("SKIP_BACK")) {
	//
	// pongFrame.showPane(pongFrame.MAIN_MENU);
	// }
	// }
	// }

//	private class ReloadServerListThread implements Runnable {
//
//		public void run() {
//			// int i = 0;
//			while (true) {
//
//				try {
////					System.out.println("\n\n\nSHOULD SEARCHS FOR SERVER??? "+pongFrame.getClientThread().isShouldSearchForServer()+"\n\n");
//					if (pongFrame.getClientThread().isShouldSearchForServer()) {
//						// i++;
//						// System.out.println("I'm reloading!!!!! Actually "+i+" times");
//						reloadServerTableList();
//					}
//					Thread.sleep(10);
//
//				} catch (InterruptedException e) {
//					pongFrame.getClientThread().appendTextMessages(e.getMessage(),
//							pongFrame.getClientChat().LEVEL_ERROR);
//				}
//			}
//		}
//	}

	public void clientAccepted(int error) {

		switch (error) {
		case CLIENT_ACCEPTED:

			pongFrame.showPane(pongFrame.CLIENT_CONTROL_PANEL);
			break;
		case NAME_ALREADY_IN_USE:

			pongFrame.getClientThread().setShouldSearchForServer(true);
			nameAlreadyTaken.setText("Dieser Name ist bereits vergeben.");
			break;
		case IP_ALREADY_IN_USE:

			pongFrame.getClientThread().setShouldSearchForServer(true);
			nameAlreadyTaken.setText("IP-Adresse wird bereits benutzt.");
			break;
		default:
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(reloadButton)) {
			
			Component[] comps = serverListPanel.getComponents();
			for(int i = 0; i < comps.length; i++) {
					serverListPanel.remove(comps[i]);
			}
			
			ArrayList<ServerAttributes> serverList = pongFrame.getClientThread().getServerlist();
			int nameTextFieldWidth = 400, ipTextFieldWidth = 300, userCountTextFieldWidth = 150, joinButtonWidth = 140, size = 50;
			
			for(ServerAttributes sa : serverList) {
				addLine(serverList.indexOf(sa), size, sa, serverList, nameTextFieldWidth, ipTextFieldWidth, userCountTextFieldWidth, joinButtonWidth);
			}
			serverListPanel.revalidate();
			serverListPanel.repaint();
		}
	}
	
	private void addLine(int ID, int size, ServerAttributes sa, ArrayList<ServerAttributes> serverList,
			int nameTextFieldWidth, int ipTextFieldWidth, int userCountTextFieldWidth, int joinButtonWidth) {
		System.out.println("\n\n\nADDING LINE{" + sa.getName() + "}\n\n\n");
		MenuTextField tempTextField = new MenuTextField(pongFrame, sa.getName());
		tempTextField.setEditable(false);
		tempTextField.setSize(new Dimension(nameTextFieldWidth, size));
		MenuTextField tempIpAdressTextField = new MenuTextField(pongFrame, sa.getIP());
		tempIpAdressTextField.setToolTipText("Server-IP-Adresse");
		tempIpAdressTextField.setSize(new Dimension(ipTextFieldWidth, size));
		tempIpAdressTextField.setEditable(false);
		MenuTextField tempUserCountTextField = new MenuTextField(pongFrame, sa.getUser_count());
		tempUserCountTextField.setToolTipText("Anzahl der Spieler auf dem Server");
		tempUserCountTextField.setSize(new Dimension(userCountTextFieldWidth, size));
		tempUserCountTextField.setEditable(false);
		MenuButton tempJoinServerButton = new MenuButton(pongFrame, "Verbinden"); // Server join Button noch nicht
																					// vorhanden
		tempJoinServerButton.setToolTipText("Mit dem Server verbinden");
		tempJoinServerButton.setSize(new Dimension(joinButtonWidth, size));
		tempJoinServerButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int ID = serverJoinButtons.indexOf(tempJoinServerButton);

				pongFrame.getClientThread().setUserName(textField.getText());
				pongFrame.getClientThread().setConnect_Server(serverList.get(ID));

				if (pongFrame.getClientThread().connectToServer()) {
					System.out.println(">>>VERBINDUNG MIT SERVER HERGESTELLT");
					pongFrame.getClientThread().setShouldSearchForServer(false);

				} else { // Verbindung nicht hergestellt -> Error
					System.out.println(">>>ERROR BEIM VERBINDEN");
				}
			}
		});
		serverListPanel.add(tempTextField);
		serverListPanel.add(tempIpAdressTextField);
		serverListPanel.add(tempUserCountTextField);
		serverListPanel.add(tempJoinServerButton);

		serverJoinButtons.add(tempJoinServerButton);
	}
}