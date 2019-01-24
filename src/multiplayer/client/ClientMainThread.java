package multiplayer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import hauptmenu.PongFrame;
import multiplayer.datapacks.GameCountdownData;
import multiplayer.datapacks.GameRequestData;
import multiplayer.datapacks.PongLocationData;
import multiplayer.datapacks.PongSliderData;
import multiplayer.datapacks.ServerAttributes;
import pongtoolkit.ObjectStringCoder;

public class ClientMainThread {

	// SearchForServers
	private DatagramSocket searchSocket;
	private volatile ArrayList<ServerAttributes> serverList;
	private boolean shouldSearchForServer = false;

	// UDP-Live-Game-Koordinaten
	private DatagramSocket udpSocket;

	// connect to server
	private ServerAttributes connectedServer;

	// others
	private Socket client;
	private PrintWriter writer;
	private BufferedReader reader;
	private String userName;
	private final String nameAlreadyTakenError = "NAME_ALREADY_TAKEN!", nameTakenQuestion = "IS_NAME_ALREADY_TAKEN?";
	private final String NO_CHAT_MESSAGE = "rHBvyWvqbR0JVs6x6g24";
	private final String IP_ALREADY_IN_USE_ERROR = "IP_ALREADY_IN_USE_ERROR";
	private final String GAME_START = "GAME_START";
	private final String GAME_STOP = "GAME_STOP";
	private final String SPECTATOR_MODE = "SPECTATOR_MODE";
	private final String PLAYER_LEFT_MODE = "PLAYER_LEFT_MODE";
	private final String PLAYER_RIGHT_MODE = "PLAYER_RIGHT_MODE";
	private final String IN_GAME_POSITIONS = "IN_GAME_POSITIONS";
	private final String GAME_REQUEST_DATA = "GAME_REQUEST_DATA";
	private final String GAME_COUNTDOWN_DATA = "GAME_COUNTDOWN_DATA";
//	private final String GAME_REQUEST_SERVER_TO_CLIENT = "GAME_REQUEST_SERVER_TO_CLIENT";
//	private final String GAME_REQUEST_CLIENT_TO_SERVER = "GAME_REQUEST_CLIENT_TO_SERVER";
//	private final String GAME_REQUEST_REPLY_SERVER_TO_CLIENT = "GAME_REQUEST_REPLY_SERVER_TO_CLIENT";
//	private final String GAME_REQUEST_REPLY_CLIENT_TO_SERVER = "GAME_REQUEST_REPLY_CLIENT_TO_SERVER";

	private PongFrame pongFrame;

	public ClientMainThread(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		serverList = new ArrayList<ServerAttributes>();

	}

	public void startSearchForServer() {
		Thread serverSearcher = new Thread(new ServerSearcherThread());
		serverSearcher.start();
		setShouldSearchForServer(true);
	}

	public boolean connectToServer() { // if true wait for server-response
		try {

			pongFrame.getClientChat().clear();
			System.out.println(getConnectedServer() + "");
			client = new Socket(getConnectedServer().getIP(), 5555);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream());

			Thread t = new Thread(new MessagesFromServerListenerTCP_IP());
			t.start();

			Thread t2 = new Thread(new MessagesFromServerListenerUDP());
			t2.start();

			sendMessageToServerUsingTCP_IP(NO_CHAT_MESSAGE + "JOINING" + getUserName());

			return true; // yolo
		} catch (Exception e) {
			appendTextMessages("Netzwerkverbindung konnte nicht hergestellt werden",
					pongFrame.getClientChat().LEVEL_ERROR);
//			System.out.println("\n\nI BIMS 1 EXCEPTION\n\n");
			e.printStackTrace();

			return false;
		}
	}

	public boolean disconnectFromServer(boolean WasOnServerChat) {

//		System.out.println("iBims1leavenderdude");

		try {
			if (WasOnServerChat)
				sendMessageToServerUsingTCP_IP(NO_CHAT_MESSAGE + "LEAVING" + getUserName());

			client.close();
			reader.close();
			writer.close();

			setConnectServer(null);
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public void sendMessageToServerUsingTCP_IP(String message) {
		if (writer != null) {
			if (!message.contains(NO_CHAT_MESSAGE)) {

				message = getUserName() + ":" + message;
				ClientChat.last_timestamp = System.currentTimeMillis();
			}
			writer.println(message);
			writer.flush();
		}
	}

	public void appendTextMessages(String message, int level) {
		pongFrame.getClientChat().appendTextToChat(message, level);
	}

	public void executeCommand(String msg) {
//		System.out.println("MSG: \""+msg+"\"");
		if (msg != null) {
			if (!msg.equals("")) {
				if (msg.substring(0, 1).equals("/")) { // Command

				} else { // Message
					sendMessageToServerUsingTCP_IP(msg);
				}
			}
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ServerAttributes getConnectedServer() {
		return connectedServer;
	}

	public void setConnectServer(ServerAttributes connectedServer) {
		this.connectedServer = connectedServer;
	}

	public ArrayList<ServerAttributes> getServerlist() {
		return serverList;
	}

	public String getNO_CHAT_MESSAGE() {
		return NO_CHAT_MESSAGE;
	}

	public String getIP_ALREADY_IN_USE_ERROR() {
		return IP_ALREADY_IN_USE_ERROR;
	}

	public String getGAME_START() {
		return GAME_START;
	}

	public String getGAME_STOP() {
		return GAME_STOP;
	}

	public String getSPECTATOR_MODE() {
		return SPECTATOR_MODE;
	}

	public String getPLAYER_LEFT_MODE() {
		return PLAYER_LEFT_MODE;
	}

	public String getPLAYER_RIGHT_MODE() {
		return PLAYER_RIGHT_MODE;
	}

	public String getIN_GAME_POSITIONS() {
		return IN_GAME_POSITIONS;
	}

	public void setServerlist(ArrayList<ServerAttributes> serverlist) {
		this.serverList = serverlist;
	}

	public boolean isShouldSearchForServer() {
		return shouldSearchForServer;
	}

	public void setShouldSearchForServer(boolean shouldSearchForServer) {
		this.shouldSearchForServer = shouldSearchForServer;
//		System.out.println("SETTING SHOULD SERCH FOR SERVER " + shouldSearchForServer);
	}

	/*
	 * Send reply-message to server
	 */
	public void sendAnswerToServersGameRequest(GameRequestData gRD, boolean accepted) {

		if (getUserName().equals(gRD.getPlayerLeftName())) {
			gRD.setPlayerLeftAccepted(accepted);
			System.out.println("Client: Linker Spieler: " + accepted);
		} else if (getUserName().equals(gRD.getPlayerRightName())) {
			gRD.setPlayerRightAccepted(accepted);
			System.out.println("Client: Rechter Spieler: " + accepted);
		}

		String objectString = null;
		try {
			objectString = ObjectStringCoder.objectToString(gRD);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = NO_CHAT_MESSAGE + GAME_REQUEST_DATA + "{PRD=" + objectString + "}";
		System.out.println("Client: Sending message to Server: \"" + message + "\"");
		sendMessageToServerUsingTCP_IP(message);
		System.out.println("Client: Erfolgreich Nachricht an Server gesendet");
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
			udpSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class MessagesFromServerListenerUDP implements Runnable {
		// Nur für live-game-koordinaten!
		private int port;
		private boolean firsttime;
		public MessagesFromServerListenerUDP() {
			this.port = 8887;
			firsttime = true;
		}
		@Override
		public void run() {
			
			byte[] recvBuf = new byte[15000];
			DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
			
			if(firsttime) {
				try {
					udpSocket = new DatagramSocket(port, InetAddress.getByName(connectedServer.getIP()));
				} catch (SocketException | UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			while (true) {

				try {
//					udpSocket.setSoTimeout(1000);
					udpSocket.receive(receivePacket);

//					String message = receivePacket.getData().toString();
					String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

					System.out.println("UDP RECEIVED PAKET: \"" + message + "\"");
					if (message.contains(GAME_COUNTDOWN_DATA)) {
//						System.out.println("Client>>Received message countdown-time");
						showCountdown(message.substring(message.indexOf("{")));
					} else if (message.contains(IN_GAME_POSITIONS)) {
//						System.out.println("Client>>Received message ingame-positions");

						safeInGameLocations(message.substring(message.indexOf("{")));
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void showCountdown(String countdownData) {
			String objectString = countdownData.substring(countdownData.indexOf("{GCD=") + 5,
					countdownData.lastIndexOf("}"));
			GameCountdownData gCD = null;
			try {
				gCD = (GameCountdownData) ObjectStringCoder.stringToObject(objectString);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			pongFrame.getClientLiveGamePanel().getSpielfeld().updateCountdown(gCD);
		}

		private void safeInGameLocations(String locationData) {

			String objectString = locationData.substring(locationData.indexOf("{PLD=") + 5,
					locationData.lastIndexOf("}"));
			PongLocationData pLD = null;
			try {
				pLD = (PongLocationData) ObjectStringCoder.stringToObject(objectString);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			pongFrame.getClientLiveGamePanel().getSpielfeld().updateLocations(pLD);
		}
	}

	public class MessagesFromServerListenerTCP_IP implements Runnable {

		@Override
		public void run() {

			String message;
			try {

				while ((message = reader.readLine()) != null) {
					if (message.contains(NO_CHAT_MESSAGE)) {
						if (message.contains(nameTakenQuestion)) {
							if (message.contains(nameAlreadyTakenError)) {

								disconnectFromServer(false);
								pongFrame.getMultiPlayer().getJoinServerPanel().clientAccepted(
										pongFrame.getMultiPlayer().getJoinServerPanel().NAME_ALREADY_IN_USE);
							} else if (message.contains(IP_ALREADY_IN_USE_ERROR)) {

								disconnectFromServer(false);
								pongFrame.getMultiPlayer().getJoinServerPanel().clientAccepted(
										pongFrame.getMultiPlayer().getJoinServerPanel().IP_ALREADY_IN_USE);

							} else {
								pongFrame.getMultiPlayer().getJoinServerPanel().clientAccepted(
										pongFrame.getMultiPlayer().getJoinServerPanel().CLIENT_ACCEPTED);
							}
						} else if (message.contains(GAME_START)) {
							System.out.println("Client>>Received message Game-start");
							if (message.contains(SPECTATOR_MODE)) {
								pongFrame.showPane(pongFrame.CLIENT_LIVE_GAME_PANEL);
								pongFrame.getClientLiveGamePanel().getSpielfeld()
										.configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().SPECTATOR);
							} else if (message.contains(PLAYER_LEFT_MODE)) {
								pongFrame.showPane(pongFrame.CLIENT_LIVE_GAME_PANEL);
								pongFrame.getClientLiveGamePanel().getSpielfeld()
										.configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().PLAYER_LEFT);
							} else if (message.contains(PLAYER_RIGHT_MODE)) {
								pongFrame.showPane(pongFrame.CLIENT_LIVE_GAME_PANEL);
								pongFrame.getClientLiveGamePanel().getSpielfeld()
										.configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().PLAYER_RIGHT);
							}
						} else if (message.contains(IN_GAME_POSITIONS)) {
							if (message.contains(GAME_COUNTDOWN_DATA)) {
//								System.out.println("Client>>Received message countdown-time");
//								showCountdown(message.substring(message.indexOf("{")));
							} else {
//								System.out.println("Client>>Received message ingame-positions");

//								safeInGameLocations(message.substring(message.indexOf("{")));
							}
						} else if (message.contains(GAME_STOP)) {
							String resultMessage = message.substring(message.indexOf("{") + 1,
									message.lastIndexOf("}"));
							pongFrame.getClientLiveGamePanel().getSpielfeld().stopGameAndShowResult(resultMessage);
//							pongFrame.showPane(pongFrame.CLIENT_CONTROL_PANEL);
						} else if (message.contains(GAME_REQUEST_DATA)) {

							String msg = message.substring(message.indexOf("{"));

							String objectString = msg.substring(msg.indexOf("{GRD=") + "{GRD=".length() + 1,
									msg.lastIndexOf("}"));

							System.out.println("REQUEST: NORMAL:\"" + message + "\" Modified:\"" + msg
									+ "\" objectString:\"" + objectString + "\"");
							GameRequestData gRD = null;
							try {
								gRD = (GameRequestData) ObjectStringCoder.stringToObject(objectString);
							} catch (ClassNotFoundException | IOException e) {
								e.printStackTrace();
							}
							/*
							 * TODO: Dieses GameRequestData-Objekt muss irgendwie in einer liste gespeichert
							 * werden, sodass es wieder zurückgesendet werden kann. Wichtig dabei ist, dass
							 * es irgendwie mit dem Request-Eintrag im ClientControlpanel in verbindugn
							 * steht
							 * 
							 */
							String leftPlayerName = gRD.getPlayerLeftName(), rightPlayerName = gRD.getPlayerRightName();

							if (leftPlayerName.equals(getUserName())) {
								pongFrame.getClientLiveGamePanel().getSpielfeld()
										.configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().PLAYER_LEFT);

							} else if (rightPlayerName.equals(getUserName())) {
								pongFrame.getClientLiveGamePanel().getSpielfeld()
										.configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().PLAYER_RIGHT);
							}

							pongFrame.getClientControlPanel().processGameRequestFromServer(gRD);

						}
//						else if (message.contains(GAME_REQUEST_SERVER_TO_CLIENT)) {// REQUEST FROM SERVER
//							// Chat-Nachricht-Info anzeigen, in der request-liste des client-control-panels
//							// anzeigen
//							String leftPlayerName = null, rightPlayerName = null;
//
//							String msg = message.substring(message.indexOf(GAME_REQUEST_SERVER_TO_CLIENT)
//									+ GAME_REQUEST_SERVER_TO_CLIENT.length());
//							
//							
//							
////							String objectString = msg.substring(msg.indexOf("{PLD=") + 5,
////									msg.lastIndexOf("}"));
//
////							pongFrame.getClientLiveGamePanel().getSpielfeld().updateLocations(pLD);
//							
//							
//							//msg-example: "{NAME_LEFT=Anonymous}{NAME_RIGHT=Anonymous1}"
////							System.out.println("-------------------------------");
////							System.out.println(getClass().getName()+": Request vom Server erkannt!");
//							
////							System.out.println("MESSAGE: {"+message+"} MSG: {"+msg+"}");
//							
//							// Namen aus der Nachricht fischen
//							// TODO: Vielleicht noch nicht die richtigen Positionen
//							int index = msg.indexOf("{NAME_LEFT=") + "{NAME_LEFT=".length();
//							int index2 = msg.indexOf("}{");
//							int index3 = msg.indexOf("{NAME_RIGHT=") + "{NAME_RIGHT=".length();
//							int index4 = msg.lastIndexOf("}");
//
////							System.out.println("index1: "+index+" index2: "+index2+" index3: "+index3+" index4: "+index4);
//							
//							leftPlayerName = msg.substring(index, index2);
//							rightPlayerName = msg.substring(index3, index4);
//							
//							if(leftPlayerName.equals(getUserName())) {
//								pongFrame.getClientLiveGamePanel().getSpielfeld()
//								.configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().PLAYER_LEFT);
//								
//							}else if(rightPlayerName.equals(getUserName())) {
//								pongFrame.getClientLiveGamePanel().getSpielfeld()
//								.configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().PLAYER_RIGHT);
//								
//							}
//							
////							leftPlayerName = "Spieler1";
////							rightPlayerName = "Spieler2";
//
////							System.out.println("leftName: "+leftPlayerName+" rightName: "+rightPlayerName);
////							System.out.println("-------------------------------");
//							pongFrame.getClientControlPanel().processGameRequestFromServer(leftPlayerName,
//									rightPlayerName);
//
//						} 
//						else if (message.contains(GAME_REQUEST_REPLY_SERVER_TO_CLIENT)) { // ANSWER FROM SERVER TO
//																							// REQUEST FROM THIS CLIENT
//							
//							
//						}
					} else {
						appendTextMessages(message, pongFrame.getClientChat().LEVEL_INFO);
					}
				}
			} catch (Exception e) {
				appendTextMessages("Verbindung zum Server verloren. Vielleicht ist dieser Offline?",
						pongFrame.getClientChat().LEVEL_ERROR);
				e.printStackTrace();

//				System.out.println("Da gabs wohl son error, würd ich sagen");
//				connectToServer();
			}
		}

		private void showCountdown(String countdownData) {
			String objectString = countdownData.substring(countdownData.indexOf("{GCD=") + 5,
					countdownData.lastIndexOf("}"));
			GameCountdownData gCD = null;
			try {
				gCD = (GameCountdownData) ObjectStringCoder.stringToObject(objectString);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			pongFrame.getClientLiveGamePanel().getSpielfeld().updateCountdown(gCD);

		}

		private void safeInGameLocations(String locationData) {

			String objectString = locationData.substring(locationData.indexOf("{PLD=") + 5,
					locationData.lastIndexOf("}"));
			PongLocationData pLD = null;
			try {
				pLD = (PongLocationData) ObjectStringCoder.stringToObject(objectString);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			pongFrame.getClientLiveGamePanel().getSpielfeld().updateLocations(pLD);
		}
	}

	public void sendMultiPlayerPositionsToServer(PongSliderData sliderData) {
//		System.out.println("Client--> send Multiplayer positions");
		String objectString = null;
		try {
			objectString = ObjectStringCoder.objectToString(sliderData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = NO_CHAT_MESSAGE + IN_GAME_POSITIONS + "{PSD=" + objectString + "}";

		sendPacketUDP(connectedServer.getIP(), message);
//		sendMessageToServerUsingTCP_IP(message);
	}

//	public void sendMultiPlayerPositionsToServerTCP_IP(String message) {
//
//		
//		sendMessageToServerUsingTCP_IP(message); //TODO: UDP
//	}

	private class ServerSearcherThread implements Runnable {

		@Override
		public void run() {

			while (true) {

				if (shouldSearchForServer) {

					// Search for Servers

					// Find the server using UDP broadcast

					try {

						// Open a random port to send the package

						searchSocket = new DatagramSocket();

						searchSocket.setBroadcast(true);

						byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

						// Try the 255.255.255.255 first

						try {

							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
									InetAddress.getByName("255.255.255.255"), 8888);

							searchSocket.send(sendPacket);
							if (pongFrame.isShowClientNetworkInformation())
								appendTextMessages(
										getUserName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)",
										pongFrame.getClientChat().LEVEL_INFO);

						} catch (Exception e) {
						}

						// Broadcast the message over all the network interfaces

						@SuppressWarnings("rawtypes")
						Enumeration interfaces = NetworkInterface.getNetworkInterfaces();

						while (interfaces.hasMoreElements()) {

							NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

							if (networkInterface.isLoopback() || !networkInterface.isUp()) {

								// continue; // Don't want to broadcast to the loopback interface

							}

							for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {

								InetAddress broadcast = interfaceAddress.getBroadcast(); // Broadcast-Adresse des Netzes
																							// (z.B. 192.168.2.255)

								if (broadcast == null) {

									continue;

								}
								// Send the broadcast package!

								try {

									DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast,
											8888);

									searchSocket.send(sendPacket);

								} catch (Exception e) {
								}

								if (pongFrame.isShowClientNetworkInformation())
									appendTextMessages(
											getUserName() + ">>> Request packet sent to: " + broadcast.getHostAddress()
													+ "; Interface: " + networkInterface.getDisplayName(),
											pongFrame.getClientChat().LEVEL_INFO);
							}
						}

						if (pongFrame.isShowClientNetworkInformation())
							appendTextMessages(
									getUserName()
											+ ">>> Done looping over all network interfaces. Now waiting for a reply!",
									pongFrame.getClientChat().LEVEL_INFO);

						// START-SCHLEIFE;

						int timeout_waiting_response = 1000; // ms
						serverList.clear();
						searchSocket.setSoTimeout(timeout_waiting_response);
						long nowtime = System.currentTimeMillis(); // Timestamp from now in MS

						while ((System.currentTimeMillis() - nowtime) < timeout_waiting_response - 100) {

							// Wait for a response
							byte[] recvBuf = new byte[15000];
							DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);

							searchSocket.receive(receivePacket);

							// We have a response
							String IP = receivePacket.getAddress().getHostAddress();
							int lastByteIndex = IP.lastIndexOf(".");
							String lastByte = IP.substring(lastByteIndex + 1, IP.length());

							if (lastByte.equals("1"))
								continue; // Skip normal Gateway-Responses

							if (pongFrame.isShowClientNetworkInformation())
								appendTextMessages(
										getUserName() + ">>> Broadcast response from server: "
												+ receivePacket.getAddress().getHostAddress(),
										pongFrame.getClientChat().LEVEL_INFO);

							// Informationen des Servers auslesen
							String text = new String(receivePacket.getData(), "UTF-8");
							String server_name = "";
							String server_user_count = "";
							int index;

							// Geschweifte Klammer-auf finden
							while ((index = text.indexOf("{")) != -1) {// Geschweifte Klammer-auf gefunden

								text = text.substring(index); // Inhalt nach der Klammer
								int index2;

								// Gleichaltszeichen finden
								if ((index2 = text.indexOf("=")) != -1) {// Gleichhaltszeichen gefunden
									index = text.indexOf("{");

									String dataType = text.substring(index + 1, index2);
									if (dataType.equals("NAME")) {// If the dataType is the server-name

										int index3 = text.indexOf("}");
										String data = text.substring(index2 + 1, index3);
										server_name = data;

									} else if (dataType.equals("USER_COUNT")) {// If the dataType is the
																				// server-user-count
										int index3 = text.indexOf("}");
										String data = text.substring(index2 + 1, index3);
										server_user_count = data;
									}
									text = text.substring(text.indexOf("}") + 1);
								}
							}

							ServerAttributes found_actual__server = new ServerAttributes(
									receivePacket.getAddress().getHostAddress(), server_name, server_user_count);
							boolean new_server = true;
							for (int i = 0; i < serverList.size(); i++) {
								if (found_actual__server.getIP().equals(serverList.get(i).getIP())) {
									if (found_actual__server.getName().equals(serverList.get(i).getName())) {
										new_server = false;
									}
								}
							}
							if (new_server) {
								serverList.add(found_actual__server);
								setServerlist(serverList);
							}
							pongFrame.getMultiPlayer().getJoinServerPanel().setServerList(serverList);
						} // ENDE-SCHLEIFE

						searchSocket.close();

					} catch (SocketTimeoutException e) {

						if (pongFrame.isShowClientNetworkInformation())
							appendTextMessages(getClass().getName() + ">>> Got no reply - No Server found!",
									pongFrame.getClientChat().LEVEL_INFO);

					} catch (IOException ex) {

						appendTextMessages("exception: \n" + ex.getMessage(), pongFrame.getClientChat().LEVEL_ERROR);

					}
				}
				try {
					Thread.sleep(10); // TODO: Sind 10ms die effizienteste Wartezeit?
				} catch (InterruptedException e) {
					appendTextMessages(e.getMessage(), pongFrame.getClientChat().LEVEL_ERROR);
				}
			}
		}
	}
}