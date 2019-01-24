package multiplayer.server;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.Collision;
import game.PhysicData;
import game.Richtung;
import hauptmenu.PongFrame;
import multiplayer.datapacks.ClientAttributes;
import multiplayer.datapacks.GameCountdownData;
import multiplayer.datapacks.GameRequestData;
import multiplayer.datapacks.PongLocationData;
import multiplayer.datapacks.PongSliderData;
import pongtoolkit.ObjectStringCoder;

public class ServerMainThread implements Runnable {

	ServerSocket server;
	private volatile ArrayList<ClientAttributes> listClients;
	private boolean shouldRun = false;
	private String name = "Server", maxUser = "0/100";
	private final String nameTakenQuestion = "IS_NAME_ALREADY_TAKEN?", nameAlreadyTakenError = "NAME_ALREADY_TAKEN!",
			IP_ALREADY_IN_USE_ERROR = "IP_ALREADY_IN_USE_ERROR", NO_CHAT_MESSAGE = "rHBvyWvqbR0JVs6x6g24",
			GAME_START = "GAME_START", SPECTATOR_MODE = "SPECTATOR_MODE", PLAYER_LEFT_MODE = "PLAYER_LEFT_MODE",
			GAME_STOP = "GAME_STOP", PLAYER_RIGHT_MODE = "PLAYER_RIGHT_MODE", IN_GAME_POSITIONS = "IN_GAME_POSITIONS",
			GAME_REQUEST_DATA = "GAME_REQUEST_DATA", GAME_COUNTDOWN_DATA = "GAME_COUNTDOWN_DATA";
	private ArrayList<String> leavedPlayerNames = new ArrayList<String>();
	// Game-Threads
	private ArrayList<Thread> gameThreads;
	private ArrayList<GameThread> gameThreadRunnableClasses;
	private ArrayList<ClientAttributes> playersInGame;
	private ArrayList<GameRequestData> gameRequests;
	private int GAME_REQUEST_ID = 0;
	private DatagramSocket socketUDP; // UDP-SOCKET
	private DiscoveryThread discoveryThreadInstance;

	private Thread discoveryThread;
	private PongFrame pongFrame;
	private boolean isAnotherServerRunningOnThisPC = false;

	public synchronized boolean isShouldRun() {
		return shouldRun;
	}

	public synchronized boolean setShouldRun(boolean shouldRun) {
		this.shouldRun = shouldRun;
		try {
			Thread serverThread = new Thread(pongFrame.getHostServer());
			serverThread.start();
//			System.out.println("RUNNING SERVER WAS SUCCESSFUL");
		} catch (Exception e) {
//			System.out.println("RUNNING SERVER WASN'T SUCCESSFUL");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		System.out.println(getClass().getName() + ":");
		shouldRun = true;
		while (true) {

			if (shouldRun && !isAnotherServerRunningOnThisPC) {

				if (runServer()) {
					listenToClients();
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Thread getDiscoveryThread() {
		return discoveryThread;
	}

	public void setDiscoveryThread(Thread discoveryThread) {
		this.discoveryThread = discoveryThread;
	}

	boolean firsttime = true;

	public ServerMainThread(String name, Long maxUser, PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		this.gameThreads = new ArrayList<Thread>();
		this.gameThreadRunnableClasses = new ArrayList<GameThread>();
		this.playersInGame = new ArrayList<ClientAttributes>();
		this.gameRequests = new ArrayList<GameRequestData>();
		this.setName(name);
		this.setMaxUser(Long.toString(maxUser));
		this.shouldRun = true;

		if (firsttime) {
			setDiscoveryThreadInstance(new DiscoveryThread());
			discoveryThread = new Thread(getDiscoveryThreadInstance());
			firsttime = false;
		}
	}

	public void stop() {
		isAnotherServerRunningOnThisPC = false;
		shouldRun = false;
		// DiscoveryThread soll nicht weiter nach Clients suchen!
		discoveryThreadInstance.setShouldDiscover(false);
		try {
			if (server != null)
				server.close();
			shouldRun = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		pongFrame.getHostServer().appendTextToConsole("Server wurde gestoppt!",
				pongFrame.getHostServerConsole().LEVEL_INFO);
	}

	/*
	 * Neuen GameThread erstellen (Runnable sowie Thread), zu den Listen hinzufügen
	 * und starten. Dies wird erst ausgeführt, sobald beide Spieler akzeptiert haben
	 * 
	 */
	public void configureNewGameAndStart(ClientAttributes leftPlayer, ClientAttributes rightPlayer) {

		playersInGame.add(leftPlayer);
		playersInGame.add(rightPlayer);

		int ID = gameThreads.size();
		GameThread gameThreadRunnable = new GameThread(ID, leftPlayer, rightPlayer);
		Thread gameThread = new Thread(gameThreadRunnable);

		gameThreadRunnableClasses.add(gameThreadRunnable);
		gameThreads.add(gameThread);
		System.out.println("Server>>starting game!");
		gameThread.start();
//		gameThreadRunnable.starten();
	}

//	public void stopAllGames(String msg) {
//
////		gameThreadRunnable.reset();
//		for (GameThread gt : gameThreadRunnableClasses) {
//			gt.reset();
//		}
//		String stopMSG = this.NO_CHAT_MESSAGE + this.GAME_STOP;
//
//		pongFrame.getHostServer().sendMessageToAllClientsUsingTCP_IP(stopMSG);
//		pongFrame.getHostServer().sendMessageToAllClientsUsingTCP_IP(msg);
//		pongFrame.getHostServerConsole().appendTextToConsole(msg, pongFrame.getHostServerConsole().LEVEL_INFO);
//	}

	public void listenToClients() {
		
		Thread clientThreadUDP = new Thread(new ClientHandlerUDP());
		clientThreadUDP.start();
		while (shouldRun) {
			try {
				if (!server.isClosed()) {

					Socket client = null;
					try {

						client = server.accept();
					} catch (Exception e) {
						continue;
					}
					PrintWriter writer = new PrintWriter(client.getOutputStream());
					Thread clientThreadTCP_IP = new Thread(new ClientHandlerTCP_IP(client));
					clientThreadTCP_IP.start();
					
					ClientAttributes cA = new ClientAttributes(writer, client);
					getListClients().add(cA);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean runServer() {
		try {

			this.server = new ServerSocket(5555);
			appendTextToConsole("Server wurde gestartet!", pongFrame.getHostServerConsole().LEVEL_INFO);

			setListClients(new ArrayList<ClientAttributes>());
			return true;
		} catch (IOException e) {
			appendTextToConsole("Server konnte nicht gestartet werden! Läuft bereits ein Server an diesem PC?",
					pongFrame.getHostServerConsole().LEVEL_ERROR);
			pongFrame.getMultiPlayer().getCreateServerPanel().setFirstTimeBecauseThereIsAnotherServerRunning(true);
			isAnotherServerRunningOnThisPC = true;
			e.printStackTrace();
			return false;
		}
	}

	public void executeCommand(String cmd) {
		if (cmd.substring(0, 1).equals("/")) { // Command

		} else { // Message
			appendTextToConsole("Admin: " + cmd, pongFrame.getHostServerConsole().LEVEL_NORMAL);
			sendMessageToAllClientsUsingTCP_IP("Admin: " + cmd);
		}
	}

	public void appendTextToConsole(String message, int level) {
		pongFrame.getHostServerConsole().appendTextToConsole(message, level);

	}

	public void sendMessageToAllClientsUsingTCP_IP(String message) {
		if (getListClients() != null) {

			@SuppressWarnings("rawtypes")
			Iterator it = getListClients().iterator();
			while (it.hasNext()) {
				PrintWriter writer = ((ClientAttributes) it.next()).getWriter();

				writer.println(message);
				writer.flush();

			}
		}
	}

	public void sendMessageToClientUsingTCP_IP(Socket client, String msg) {

		PrintWriter writer;
		try {
			writer = new PrintWriter(client.getOutputStream());
			writer.println(msg);
			writer.flush();

//			System.out.println("Write: \"" + msg + "\"");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void removeClientFromList(String ip, String name) {
		lala: for (int i = 0; i < getListClients().size(); i++) {

			String ipAddr = getListClients().get(i).getIP();
			String ClName = getListClients().get(i).getName();

			if (ipAddr.equals(ip) && ClName.equals(name)) {

				getListClients().remove(getListClients().get(i));

				break lala;
			}
		}
	}

	public boolean nameAlreadyTaken(String name) {
		if (getListClients().size() > 0) {
			for (ClientAttributes client : getListClients()) {
				if (client.getName().equals(name)) {

					return true;
				}
			}
			return false;
		}
		return false;
	}

	public boolean ipAlreadyTaken(String iP) {
		if (getListClients().size() > 0) {
			int count = 0;
			for (ClientAttributes client : getListClients()) {
				if (client.getIP().equals(iP)) {
					count++;
				}
			}
			if (count >= 2)
				return true;
			return false;
		}
		return false;
	}

	/*
	 * couldThesePlayerPlay: Check if these 2 players are online & not already in a
	 * game.
	 * 
	 */
	public boolean couldThesePlayerPlay(String leftPlayerName, String rightPlayerName) {
		ClientAttributes playerLeft = getClientByName(leftPlayerName);
		ClientAttributes playerRight = getClientByName(rightPlayerName);
		if (isClientInGame(playerLeft)) {// Left Player is ingame
			if (isClientInGame(playerRight)) {// Right Player is ingame
				return false;
			}
			return false;
		}
		return true;
	}

	private boolean isClientInGame(ClientAttributes client) {
		return playersInGame.contains(client);
	}

	public boolean sendPlayersPlayRequest(String leftPlayerName, String rightPlayerName) {
		ClientAttributes leftPlayer, rightPlayer;
		try {
			GameRequestData gRD = new GameRequestData(leftPlayerName, rightPlayerName, GAME_REQUEST_ID);
			gameRequests.add(gRD);

			GAME_REQUEST_ID++;

			String objectString = ObjectStringCoder.objectToString(gRD);

			leftPlayer = getClientByName(leftPlayerName);
			rightPlayer = getClientByName(rightPlayerName);

			String message = NO_CHAT_MESSAGE + GAME_REQUEST_DATA + "{PRD=" + objectString + "}";

			sendMessageToClientUsingTCP_IP(leftPlayer.getClient(), message);
			sendMessageToClientUsingTCP_IP(rightPlayer.getClient(), message);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public ClientAttributes getClientByName(String name) {
		ArrayList<ClientAttributes> playerList = getListClients();

		for (ClientAttributes cA : playerList) {
			if (cA.getName().equals(name))
				return cA;
		}

		return null;
	}

	/*
	 * 
	 * Die ID des Eintrages der gameRequests-Liste zurückgeben, damit der Eintrag
	 * direkt verändert oder überprüft werden kann
	 * 
	 */
	public int getListIDByGameRequestObject(GameRequestData gRD) {
		ArrayList<GameRequestData> gRDList = gameRequests;
		System.out.println("IS REQUEST VORHANDEN?");
		for (GameRequestData gRD_list_item : gRDList) {
			System.out.print("GAME-REQUEST: " + gRD_list_item + " <- ");
			if (gRD_list_item.getGAME_REQUEST_ID() == gRD.getGAME_REQUEST_ID()) {
				System.out.println("ist richtig!");
				return gameRequests.indexOf(gRD_list_item);
			}
			System.out.println("ist leider falsch");
		}
		return -1;
	}

	public int getListIDByGameID(int gameID) {
		for (GameThread gt : gameThreadRunnableClasses) {
			if (gt.getGameID() == gameID) {
				return gameThreadRunnableClasses.indexOf(gt);
			}
		}
		return -1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaxUser() {
		return maxUser;
	}

	public void setMaxUser(String maxUser) {
		this.maxUser = maxUser;
	}

	public synchronized ArrayList<ClientAttributes> getListClients() {
		return listClients;
	}

	public synchronized void setListClients(ArrayList<ClientAttributes> listClients) {
		this.listClients = listClients;
	}

	public synchronized DiscoveryThread getDiscoveryThreadInstance() {
		return discoveryThreadInstance;
	}

	public synchronized void setDiscoveryThreadInstance(DiscoveryThread discoveryThreadInstance) {
		this.discoveryThreadInstance = discoveryThreadInstance;
	}

	// TODO: Kann ein DatagramSocket Pakete senden, während es auf empfangende
	// Pakete wartet?
	private void sendPacketUDP(String IP, String message) {
		// Get InetAddress
		InetAddress IPAddress = null;
		try {
			IPAddress = InetAddress.getByName(IP);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		// Get Message Data
		byte[] sendData = null;
		sendData = message.getBytes();

		// Send Paket
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8887);
		try {
			socketUDP.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * We just need one UDP-ClientHandle for all Clients, because it listens on THE
	 * defined Port we will use for UDP-Live-Game-Coords-Communication
	 * 
	 */
	private class ClientHandlerUDP implements Runnable {
		private int port;
		// serversocket
		private boolean firsttime;

		public ClientHandlerUDP() {
			this.port = 8887;
			firsttime = true;
		}
//		public DatagramSocket getSocket() {
//			return socket;
//		}

//		public void setSocket(DatagramSocket socket) {
//			this.socket = socket;
//		}

		@Override
		public void run() {

			// DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[8000];
			// String sendString = "polo";
			// byte[] sendData = sendString.getBytes("UTF-8");

			// System.out.printf("Listening on udp:%s:%d%n",
			// InetAddress.getLocalHost().getHostAddress(), port);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			if (firsttime) {
				try {
//					socketUDP = new DatagramSocket(port, InetAddress.getLocalHost());//TODO: Muss an die Adresse des anderen Clients gebunden sein! Entweder Broadcast-zuhören, oder wie bei tcp/ip für jeden client einen clienthandler-thread
					socketUDP = new DatagramSocket(port, InetAddress.getByName("0.0.0.0")); //Um allen zuzuhören 0.0.0.0
				} catch (SocketException | UnknownHostException e) {
					e.printStackTrace();
				}

				firsttime = false;
				System.out.println("Client-Handler-UDP-THREAD STARTET");
			}

			while (true) {
				try {

					socketUDP.receive(receivePacket);
					String data = new String(receivePacket.getData(), 0, receivePacket.getLength());
					System.out.println("RECEIVED: " + data);
					// now send acknowledgement packet back to sender
//					InetAddress IPAddress = receivePacket.getAddress();

					// TODO: Paket auswerten und an entsprechenden gamethread übermitteln
					safePlayersLocationToGameThread(data.substring(data.indexOf("{")));

					// DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					// IPAddress, receivePacket.getPort());
					// socket.send(sendPacket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/*
		 * GAME-ID mitsenden, diese hier herausfiltern, und die positionen von genau dem
		 * GameThread aktualisieren
		 * 
		 */
		private void safePlayersLocationToGameThread(String locationData) {
//			System.out.println("[SERVER]SAFE_INGAME_LOCATIONS: \"" + locationData + "\"");

			String objectString = locationData.substring(locationData.indexOf("{PSD=") + "{PSD=".length(),
					locationData.lastIndexOf("}"));

//			System.out.println("Server<< this is objectString: \""+objectString+"\"");

			PongSliderData pSD = null;
			try {
				pSD = (PongSliderData) ObjectStringCoder.stringToObject(objectString);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

			int listID = getListIDByGameID(pSD.getGameID());

			if (listID != -1) {
				if (pSD.getOrientation() == pSD.LEFT) {

					gameThreadRunnableClasses.get(listID).refreshSliderData(GameThread.LEFT, pSD.getSlider());

				} else if (pSD.getOrientation() == pSD.RIGHT) {

					gameThreadRunnableClasses.get(listID).refreshSliderData(GameThread.RIGHT, pSD.getSlider());

				}
			} else {
				// Spiel existiert noch nicht!
			}
		}
	}

	private class ClientHandlerTCP_IP implements Runnable {

		Socket client;
		BufferedReader reader;

		public ClientHandlerTCP_IP(Socket client) {
			try {
				this.client = client;
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			if (!client.isClosed() && shouldRun) {
				String message;
				String IP = client.getInetAddress().getHostAddress();
				String name = "";
				try {
					// if connection reset exception -> user leaved game
					while ((message = reader.readLine()) != null) {
//						System.out.println("-----------------------------\n<Message>\n-----------------------------");
//						System.out.println("(client-count: " + getListClients().size() + ")");
//						System.out.println("message=\""+message+"\"");
						if (message.contains(NO_CHAT_MESSAGE)) {

							if (message.contains("JOINING")) {// "JOINING".equals(nachricht.substring(0,
																// nachricht.indexOf("G", 0)+1))

								IP = client.getInetAddress().getHostAddress();
								name = message.substring(message.indexOf("JOINING", 0) + "JOINING".length());

								System.out.println("CLIENT \"" + name + "\" wants to join!");

								if (!ipAlreadyTaken(IP)) {
									if (!nameAlreadyTaken(name)) {
										if (leavedPlayerNames.contains(name))
											leavedPlayerNames.remove(name);
										/*
										 * Anhand der IP den Client-Namen im ClientAttributes Objekt hinzufügen setzen
										 * 
										 */
										for (ClientAttributes client : getListClients()) {
											if (client.getIP().equals(IP)) {
												client.setName(name);
											}
										}
										pongFrame.getHostServer().appendTextToConsole(
												name + " ist dem Server beigetreten",
												pongFrame.getHostServerConsole().LEVEL_INFO);
										sendMessageToClientUsingTCP_IP(client,
												pongFrame.getHostServer().NO_CHAT_MESSAGE + nameTakenQuestion);
										pongFrame.getHostServer().sendMessageToAllClientsUsingTCP_IP(
												name + " ist dem Server beigetreten");
									} else {
										sendMessageToClientUsingTCP_IP(client, pongFrame.getHostServer().NO_CHAT_MESSAGE
												+ nameTakenQuestion + nameAlreadyTakenError);

										for (ClientAttributes client : getListClients()) {
											if (client.getIP().equals(IP)) {
												client.setName(name);
												System.out.println("CLIENT: " + client);
											}
										}

										removeClientFromList(IP, name);
									}

								} else { // IP wird bereits von einem Client benutzt.

									sendMessageToClientUsingTCP_IP(client,
											NO_CHAT_MESSAGE + nameTakenQuestion + IP_ALREADY_IN_USE_ERROR);

									for (ClientAttributes client : getListClients()) {
										if (client.getIP().equals(IP)) {
											client.setName(name);
											System.out.println("CLIENT: " + client);
										}
									}
									removeClientFromList(IP, name);
								}
							}
							// LEAVINGname
							else if (message.contains("LEAVING")) {// "LEAVING".equals(nachricht.substring(0,
																	// nachricht.indexOf("G", 0)+1))

								IP = client.getInetAddress().getHostAddress();
								name = message.substring(message.indexOf("LEAVING", 0) + 7);
								leavedPlayerNames.add(name);
								removeClientFromList(IP, name);

								pongFrame.getHostServer().appendTextToConsole(name + " hat den Server verlassen",
										pongFrame.getHostServerConsole().LEVEL_INFO);
								pongFrame.getHostServer()
										.sendMessageToAllClientsUsingTCP_IP(name + " hat den Server verlassen");

							} else if (message.contains(IN_GAME_POSITIONS)) {

								// SHOULD GO WITH UDP
//								System.out.println("[SERVER]IN_GAME_POSITIONS_BY_CLIENTS");

//								System.out.println("Server<<<Received location-message: \""+message+"\"");
//								safePlayersLocationToGameThread(message.substring(message.indexOf("{")));
							} else if (message.contains(GAME_REQUEST_DATA)) {
								boolean removeRequest = false;
								String msg = message.substring(message.indexOf("{"));

								String objectString = msg.substring(msg.indexOf("{GRD=") + "{GRD=".length() + 1,
										msg.lastIndexOf("}"));

								System.out.println("REQUEST>> message:\"" + message + "\"\nmsg:\"" + msg
										+ "\"\nobjString:\"" + objectString + "\"");
								GameRequestData gRD = null;
								try {
									gRD = (GameRequestData) ObjectStringCoder.stringToObject(objectString);
								} catch (ClassNotFoundException | IOException e) {
									e.printStackTrace();
								}
								int list_id = getListIDByGameRequestObject(gRD);

								if (name.equals(gRD.getPlayerLeftName())) {
									gameRequests.get(list_id).setPlayerLeftAccepted(gRD.isPlayerLeftAccepted());
									if (!gRD.isPlayerLeftAccepted()) {// Der Linke Spieler hat die Anfrage soeben
																		// abgelehnt
										// TODO: Den 2. Spieler darüber benachrichtigen, die Request zurückziehen und
										// den client auf das clientcontrolpanel zurückholen?
										// Mittels eines Dialoges drauf aufmerksam machen, bei klick auf OK zurück auf
										// das ClientControlPanel schieben
										removeRequest = true;
									}
								} else if (name.equals(gRD.getPlayerRightName())) {
									gameRequests.get(list_id).setPlayerRightAccepted(gRD.isPlayerRightAccepted());
									if (!gRD.isPlayerRightAccepted()) {// Der Rechte Spieler hat die Anfrage soeben
																		// abgelehnt
										// TODO: Den 2. Spieler darüber benachrichtigen, die Request zurückziehen und
										// den client auf das clientcontrolpanel zurückholen?
										// Mittels eines Dialoges drauf aufmerksam machen, bei klick auf OK zurück auf
										// das ClientControlPanel schieben
										removeRequest = true;
									}
								}

								/*
								 * Erst ausführen, wenn beide Spieler die Anfrage akzeptiert haben!
								 * 
								 */
								System.out
										.println("REQUEST>>> left? " + gameRequests.get(list_id).isPlayerLeftAccepted()
												+ " right? " + gameRequests.get(list_id).isPlayerRightAccepted());
								if (gameRequests.get(list_id).isPlayerLeftAccepted()
										&& gameRequests.get(list_id).isPlayerRightAccepted()) {

									configureNewGameAndStart(
											getClientByName(gameRequests.get(list_id).getPlayerLeftName()),
											getClientByName(gameRequests.get(list_id).getPlayerRightName()));
								}
								if (removeRequest)
									gameRequests.remove(gameRequests.get(list_id));
							}
						} else {
							int pos = message.indexOf(":");
							String name2 = message.substring(0, pos);
							message = message.substring(pos + 1, message.length());
							String sendMSG = name2 + ": " + message;
							appendTextToConsole(sendMSG, pongFrame.getHostServerConsole().LEVEL_NORMAL);
							sendMessageToAllClientsUsingTCP_IP(sendMSG);
						}
//						System.out.println("-----------------------------\n</Message>-----------------------------");
					}
				} catch (IOException e) {
					if (!leavedPlayerNames.contains(name)) {
						removeClientFromList(IP, name);

						pongFrame.getHostServer().appendTextToConsole(name + " hat den Server verlassen",
								pongFrame.getHostServerConsole().LEVEL_INFO);
						pongFrame.getHostServer()
								.sendMessageToAllClientsUsingTCP_IP(name + " hat den Server verlassen");

						e.printStackTrace();
					}
				}
			} else if (client.isClosed()) {// Client geschlossen
				try {

					for (ClientAttributes cA : getListClients()) {

						if (cA.getClient().equals(client)) {
							String name = cA.getName();

							pongFrame.getHostServer().appendTextToConsole(name + " hat den Server verlassen",
									pongFrame.getHostServerConsole().LEVEL_INFO);
							pongFrame.getHostServer()
									.sendMessageToAllClientsUsingTCP_IP(name + " hat den Server verlassen");
						}
					}
				} catch (Exception e2) {

					pongFrame.getHostServer().appendTextToConsole("Unbekannt hat den Server verlassen",
							pongFrame.getHostServerConsole().LEVEL_INFO);
					pongFrame.getHostServer().sendMessageToAllClientsUsingTCP_IP("Unbekannt hat den Server verlassen");
				}
			}
		}

		/*
		 * GAME-ID mitsenden, diese hier herausfiltern, und die positionen von genau dem
		 * GameThread aktualisieren
		 * 
		 */
		private void safePlayersLocationToGameThread(String locationData) {
//			System.out.println("[SERVER]SAFE_INGAME_LOCATIONS: \"" + locationData + "\"");

			String objectString = locationData.substring(locationData.indexOf("{PSD=") + "{PSD=".length(),
					locationData.lastIndexOf("}"));

//			System.out.println("Server<< this is objectString: \""+objectString+"\"");

			PongSliderData pSD = null;
			try {
				pSD = (PongSliderData) ObjectStringCoder.stringToObject(objectString);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

			int listID = getListIDByGameID(pSD.getGameID());

			if (listID != -1) {
				if (pSD.getOrientation() == pSD.LEFT) {

					gameThreadRunnableClasses.get(listID).refreshSliderData(GameThread.LEFT, pSD.getSlider());

				} else if (pSD.getOrientation() == pSD.RIGHT) {

					gameThreadRunnableClasses.get(listID).refreshSliderData(GameThread.RIGHT, pSD.getSlider());

				}
			} else {
				// Spiel existiert noch nicht!
			}
		}
	}

	public class DiscoveryThread implements Runnable {
		private DatagramSocket socket;
		private boolean shouldDiscover = true;
		private int port = 8888;

		public DatagramSocket getSocket() {
			return socket;
		}

		public void setSocket(DatagramSocket socket) {
			this.socket = socket;
		}

		boolean firsttime = true;

		public boolean isFirstTime() {
			return firsttime;
		}

		@Override
		public void run() {
			try {
				// Keep a socket open to listen to all the UDP trafic that is destined for this
				// port
				if (firsttime) {
					socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"));

					socket.setBroadcast(true);
					firsttime = false;
					System.out.println("SERVER DISCOVERY-THREAD STARTET");
				}
				while (true) {
//					System.out.println("SERVER DISCOVERYTHREAD SOCKET CLOSED?: " + socket.isClosed() + " shouldDiscover: "
//							+ shouldDiscover);
					if (!socket.isClosed() && shouldDiscover) {
						if (pongFrame.isShowServerNetworkInformation())
							pongFrame.getHostServer().appendTextToConsole(
									getClass().getName() + ">>>Ready to receive broadcast packets!",
									pongFrame.getHostServerConsole().LEVEL_INFO);
						// Receive a packet

						byte[] recvBuf = new byte[15000];

						DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);

						try {
							socket.receive(packet);
						} catch (Exception e) {
							continue;
						}
						// Packet received
						if (pongFrame.isShowServerNetworkInformation())
							pongFrame.getHostServer().appendTextToConsole(
									getClass().getName() + ">>>Discovery packet received from: "
											+ packet.getAddress().getHostAddress(),
									pongFrame.getHostServerConsole().LEVEL_INFO);

						if (pongFrame.isShowServerNetworkInformation())
							pongFrame.getHostServer().appendTextToConsole(
									getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()),
									pongFrame.getHostServerConsole().LEVEL_INFO);
						// See if the packet holds the right command (message)

						String message = new String(packet.getData()).trim();

						int clients;
						if(getListClients() != null) {
							clients = getListClients().size();
						}else {
							 clients = 0;
						}
						if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
							String text = "DISCOVER_FUIFSERVER_RESPONSE{NAME=" + getName() + "}{USER_COUNT="
									+ clients + "/" + getMaxUser() + "}";
							byte[] sendData = text.getBytes("UTF-8");

							// Send a response

							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
									packet.getAddress(), packet.getPort());

							socket.send(sendPacket);
							if (pongFrame.isShowServerNetworkInformation())
								pongFrame.getHostServer().appendTextToConsole(
										getClass().getName() + ">>>Sent packet to: "
												+ sendPacket.getAddress().getHostAddress(),
										pongFrame.getHostServerConsole().LEVEL_INFO);
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} catch (IOException ex) {

				Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		public boolean isShouldDiscover() {
			return shouldDiscover;
		}

		public void setShouldDiscover(boolean shouldDiscover) {

			if (isFirstTime() && !isAnotherServerRunningOnThisPC && shouldDiscover) {
				discoveryThread.start();
			}
			this.shouldDiscover = shouldDiscover;
		}
	}

	public class GameThread implements Runnable {

		public boolean richtungx;
		public boolean richtungy;
		private boolean neu = false;
		public boolean xnull;
		public boolean ynull;
		public int scoreLeft = 0;
		public int scoreRight = 0;
		private int MAX_POINTS = 15;
		private int oft;
		private String score = "0 : 0";
		Timer timer = new Timer();
		int periodendauer = 5; // (oldValue=2) Milisekunden
		volatile boolean gestartet = false, beendet = false;
		Dimension ballSize = new Dimension(50, 50);
		Dimension sliderSize = new Dimension(10, 200);
		Dimension frameSize = new Dimension(1920, 1080);
//		int wy = 2, wx = 2, px, py;
		int wy = 5, wx = 5, px, py;
		boolean winkel, kl, kr;
		private int weitex, weitey, weitex1, weitey1, weitey2, weitey3;

		private volatile PongLocationData pLD;
		private int gameID;
		private ClientAttributes leftPlayer;
		private ClientAttributes rightPlayer;
		private ArrayList<ClientAttributes> spectator;

		public static final int RIGHT = 0;
		public static final int LEFT = 1;

		private PhysicData physicData;

		/*
		 * TODO Eigenes Datagram-Socket eröffnen, welches die aktuellen Locations per
		 * UDP an die beiden Spieler und die Spectator schickt. Zusätzlich noch eine Art
		 * UDP Listener um die Positionen der Spieler zu erhalten. Das ganze natürlich
		 * auch anders herum für die Clients.....
		 */

		public GameThread(int gameID) {
			this.gameID = gameID;
			physicData = new PhysicData(50, 10, 200);
		}

		public GameThread(int gameID, ClientAttributes playerLeft, ClientAttributes rightPlayer) {
			this.gameID = gameID;
			physicData = new PhysicData(50, 10, 200);
			this.leftPlayer = playerLeft;
			this.rightPlayer = rightPlayer;
			spectator = new ArrayList<ClientAttributes>();
			pLD = new PongLocationData();
//			pLD.setBall(new Point((1920 - 50) / 2, (1080 - 50 - 50) / 2));
//			pLD.setSliderLeft(new Point(10, (1030 - 200) / 2));
//			pLD.setSliderRight(new Point(1920 - 40, (1030 - 50) / 2));
			pLD.setBall(new Point((1920 - physicData.getBallSize()) / 2, (1080 / 2) - physicData.getBallSize()));
			pLD.setSliderLeft(new Point(10, (1080 - physicData.getPlayerOneHeight()) / 2));
			pLD.setSliderRight(new Point(1920 - 10 - physicData.getPlayerTwoWidth(),
					(1080 - physicData.getPlayerTwoHeight()) / 2));
			pLD.setGAME_ID(gameID);
		}

		public int getGameID() {
			return gameID;
		}

		public void setGameID(int gameID) {
			this.gameID = gameID;
		}

		public void addSpectator(ClientAttributes cA) {
			spectator.add(cA);
		}

		public void removeSpectator(ClientAttributes cA) {
			spectator.remove(cA);
		}

		public void sendToPlayersAndSpectatorsUDP(String message) {

			sendPacketUDP(leftPlayer.getIP(), message);
			sendPacketUDP(rightPlayer.getIP(), message);

			for (ClientAttributes cA : spectator) {
				sendPacketUDP(cA.getIP(), message);
			}
		}

		public void sendToPlayersAndSpectatorsTCP_IP(String message) {
			sendMessageToClientUsingTCP_IP(leftPlayer.getClient(), message);
			sendMessageToClientUsingTCP_IP(rightPlayer.getClient(), message);

			for (ClientAttributes cA : spectator) {
				sendMessageToClientUsingTCP_IP(cA.getClient(), message);
			}
		}

		public void refreshSliderData(int orientation, Point sliderData) {
			if (orientation == RIGHT) {
				pLD.setSliderRight(sliderData);
			} else if (orientation == LEFT) {
				pLD.setSliderLeft(sliderData);
			}
		}

		@Override
		public void run() {
			// Start-Richtung des Balls
			Richtung.setRichtungX(false);
			Richtung.setRichtungY(true);

			weitex = wx;
			weitey = wy;
//			weitey1 = 2;
//			weitex1 = 4;
//			weitey2 = 2;
//			weitey3 = 3;
			weitey1 = 6;
			weitex1 = 10;
			weitey2 = 5;
//			weitex2 = 7;
			weitey3 = 6;
//			weitex3 = 6;
			oft = 0;
			winkel = true;

			starten();
//			timer.schedule(new TimerTask() {// start after 3 seconds
//
//				@Override
//				public void run() {
//					if (!gestartet) {
//						gestartet = true;
//					}
//
//				}
//			}, 3000);
//			countdown(3); // 3 SECONDS COUNTDOWN

			// GAME-PHYSICS
			while (true) {
				// GAME
				try {
					if (gestartet) {

						boolean x1 = Richtung.isRichtungX();
						boolean y1 = Richtung.isRichtungY();

//						Richtung.isxNichts();
//						Richtung.isyNichts();

						if (x1 == true) {
							gehe(-weitex, 0);
						}

						if (x1 == false) {
							gehe(weitex, 0);
						}

						if (y1 == true) {
							gehe(0, weitey);
						}

						if (y1 == false) {
							gehe(0, -weitey);
						}
						// Nur an die beiden spieler, und die spectator senden
//						pongFrame.getHostServer()
//								.sendToAllClients(pongFrame.getHostServer().NO_CHAT_MESSAGE
//										+ pongFrame.getHostServer().IN_GAME_POSITIONS + "{PLD="
//										+ ObjectStringCoder.objectToString(pLD) + "}");
						sendToPlayersAndSpectatorsUDP(NO_CHAT_MESSAGE + IN_GAME_POSITIONS + "{PLD="
								+ ObjectStringCoder.objectToString(pLD) + "}");
					} else if (!beendet) {
						sendToPlayersAndSpectatorsUDP(NO_CHAT_MESSAGE + IN_GAME_POSITIONS + "{PLD="
								+ ObjectStringCoder.objectToString(pLD) + "}");
					} else {
						periodendauer = 1000; // TODO: Vorsichtig!
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// WAIT
				try {
					Thread.sleep(periodendauer);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void reset() {
			oft = 0;
			px = 0;
			kl = false;
			kr = false;
			weitex = wx;
			weitey = wy;
			scoreLeft = 0;
			scoreRight = 0;

			pLD = new PongLocationData();
			pLD.setBall(new Point((1920 - 50) / 2, (1080 - 50 - 50) / 2));
			pLD.setSliderLeft(new Point(10, (1030 - 200) / 2));
			pLD.setSliderRight(new Point(1920 - 40, (1030 - 50) / 2));

			gestartet = false;
		}

		public void stoppen() {
			if (gestartet) {
				gestartet = false;
				if (scoreLeft >= MAX_POINTS || scoreRight >= MAX_POINTS) {
					String msg = "";
					beendet = true;
					if (scoreLeft > scoreRight) {
						msg = leftPlayer.getName() + " hat mit " + scoreLeft + " zu " + scoreRight + " gegen "
								+ rightPlayer.getName() + " gewonnen.";
					} else if (scoreRight > scoreLeft) {
						msg = rightPlayer.getName() + " hat mit " + scoreRight + " zu " + scoreLeft + " gegen "
								+ leftPlayer.getName() + " gewonnen.";
					}
					scoreLeft = 0;
					scoreRight = 0;
//					pongFrame.getHostServer().stopAllGames(msg);

					reset();
//						for (GameThread gt : gameThreadRunnableClasses) {
//							gt.reset();
//						}
					String stopMSG = NO_CHAT_MESSAGE + GAME_STOP + "{" + msg + "}";

					pongFrame.getHostServer().sendMessageToAllClientsUsingTCP_IP(stopMSG);
					pongFrame.getHostServer().sendMessageToAllClientsUsingTCP_IP(msg);
					pongFrame.getHostServerConsole().appendTextToConsole(msg,
							pongFrame.getHostServerConsole().LEVEL_INFO);

					playersInGame.remove(leftPlayer);
					playersInGame.remove(rightPlayer);
				} else {
					pLD.getBall().setLocation((frameSize.width - ballSize.width) / 2,
							(frameSize.height - ballSize.height) / 2);
					////////////////// \\
					countdown(3f); // 1.5f
					// \\\\\\\\\\\\\\\\\\
					timer.schedule(new TimerTask() {
						public void run() {
							starten();
							oft = 0;
							px = 0;
							kl = false;
							kr = false;
							weitex = wx;
							weitey = wy;
						}
					}, 3000); // 1500
				}
			}
		}

		public void starten() {

			if (!gestartet) {
				gestartet = true;
			}
		}

		public void schneller() {
			oft++;
			px = oft / 10;
		}

		int ID = 0;
		boolean tempCollision1 = false, tempCollision2 = false;

		public void gehe(int x, int y) {
			Point p = pLD.getBall(); // ballLocation:p

			p.x = p.x - x;
			p.y = p.y + y;

			// Objekt Kollision

			Dimension ds = new Dimension(1920, 1080);
			int radius = ballSize.width / 2;

			if (Collision.circleToRect(p.x + radius, p.y + radius, radius, pLD.getSliderLeft().getLocation().x,
					pLD.getSliderLeft().getLocation().y, sliderSize.width, sliderSize.height)) {

				int ballY = pLD.getBall().getLocation().y - pLD.getSliderLeft().getLocation().y + sliderSize.height;

				if (!kr) {

					if (!winkel) {
						weitey = wy + px;
						weitex = wx + px;
						schneller();
					}

					kr = true;
					kl = false;

					if (winkel) {
						if (ballY <= 40) {
							weitey = weitey1 + px;
							weitex = weitex1 + px;
							schneller();
						}

						else if (ballY >= 40 && ballY <= 90) {
							weitey = weitey2 + px;
							weitex = weitey2 + px;
							schneller();
						}

						else if (ballY >= 90 && ballY <= 160) {
							weitey = weitey3 + px;
							weitex = weitey3 + px;
							schneller();
						}

						else if (ballY >= 160 && ballY <= 210) {
							weitey = weitey2 + px;
							weitex = weitey2 + px;
							schneller();
						}

						else if (ballY > 210) {
							weitey = weitey1 + px;
							weitex = weitex1 + px;
							schneller();
						}
					}

					if (Richtung.isRichtungY() == false) {
						randtestx(true);
						randtesty(false);
					}

					if (Richtung.isRichtungY() == true) {
						randtestx(true);
						randtesty(true);
					}
				}
			}

			if (Collision.circleToRect(p.x + radius, p.y + radius, radius, pLD.getSliderRight().getLocation().x,
					pLD.getSliderRight().getLocation().y, sliderSize.width, sliderSize.height)) {

				int ballY = pLD.getBall().getLocation().y - pLD.getSliderRight().getLocation().y + ballSize.height;

				if (kl == false) {

					if (winkel == false) {
						weitey = wy + px;
						weitex = wx + px;
						schneller();
					}

					kl = true;
					kr = false;

					if (winkel == true) {

						if (ballY <= 40) {
							weitey = weitey1 + px;
							weitex = weitex1 + px;
							schneller();
						}

						else if (ballY >= 40 && ballY <= 90) {
							weitey = weitey2 + px;
							weitex = weitey2 + px;
							schneller();
						}

						else if (ballY >= 90 && ballY <= 160) {
							weitey = weitey3 + px;
							weitex = weitey3 + px;
							schneller();
						}

						else if (ballY >= 160 && ballY <= 210) {
							weitey = weitey2 + px;
							weitex = weitey2 + px;
							schneller();
						}

						else if (ballY > 210) {
							weitey = weitey1 + px;
							weitex = weitex1 + px;
							schneller();
						}
					}

					if (Richtung.isRichtungY() == true) {
						randtestx(false);
						randtesty(true);

					}

					if (Richtung.isRichtungY() == false) {
						randtestx(false);
						randtesty(false);

					}
				}
			}

			// Seiten Kollision

			if (p.x < 0 && !tempCollision1)

			{
				scoreRight++;
				score = scoreLeft + " : " + scoreRight;
				pLD.setScore(score);
				tempCollision1 = true;
				this.stoppen();
			} else {
				tempCollision1 = false;
			}
			if (p.y < 0) {
				randtesty(true);
			}

			if (ds.width <= p.x + ballSize.width && !tempCollision2) {
				scoreLeft++;
				score = scoreLeft + " : " + scoreRight;
				pLD.setScore(score);
				tempCollision2 = true;
				this.stoppen();
			} else {
				tempCollision2 = false;
			}
			if (ds.height <= p.y + ballSize.getSize().height) {
				randtesty(false);
			}

			if (gestartet && neu) {
				pLD.getBall().setLocation(400, 400);
				neu = false;
				pLD.getBall().setLocation(p);
			}
			if (gestartet && neu) {
				pLD.setBall(p);
			}
		}

		private void randtestx(boolean x) {
			if (x == true) {
				Richtung.setRichtungX(true);
			}
			if (x == false) {
				Richtung.setRichtungX(false);
			}
		}

		private void randtesty(boolean y) {
			if (y == true) {
				Richtung.setRichtungY(true);
			}
			if (y == false) {
				Richtung.setRichtungY(false);
			}
		}

		private void countdown(float seconds) {
			if (!countdownActive) {
				countdownActive = true;
				shouldCountdown = true;

				if (gestartet) {
					gestartet = false;
				}
				RunWrapper run = new RunWrapper(Math.round(seconds * 1000), gameID);
				Thread t = new Thread(run);
				t.start();
			}
		}
	}

	private boolean shouldCountdown = false, countdownActive = false;

	private class RunWrapper implements Runnable {
		private int milliSeconds, listID;
		private GameCountdownData gCD;

		public RunWrapper(int milliSeconds, int GAME_ID) {
			this.milliSeconds = milliSeconds;
			this.listID = getListIDByGameID(GAME_ID);
			gCD = new GameCountdownData();
		}

		@Override
		public void run() {
			synchronized (this) {
				while (milliSeconds > 0 && shouldCountdown) {
					try {
						int seconds = milliSeconds / 1000;
						int milli = milliSeconds - seconds * 1000;
						milli = Math.round(milli / 10);

						gCD.setNowtime("0" + (seconds) + ":" + milli);
						sendCountdown(gCD);

						milliSeconds -= 10;

						Thread.sleep(10);

						if (!(milliSeconds > 0 && shouldCountdown)) { // Letzter durchlauf
							gCD.setNowtime("STOP");
							sendCountdown(gCD);
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
//			countdown.setText("");
//			startGame();
			countdownActive = false;
		}

		private void sendCountdown(GameCountdownData gCD) {
			String message;
			try {
				message = NO_CHAT_MESSAGE + IN_GAME_POSITIONS + GAME_COUNTDOWN_DATA + "{GCD="
						+ ObjectStringCoder.objectToString(gCD) + "}";

				gameThreadRunnableClasses.get(listID).sendToPlayersAndSpectatorsUDP(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}