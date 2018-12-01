package multiplayer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;

import game.PongLocationData;
import game.Spielfeld;
import hauptmenu.JoinServerPanel;
import hauptmenu.PongFrame;
import main.PongMain;
import multiplayer.server.ServerAttributes;
import pongtoolkit.ObjectStringCoder;

public class ClientMainThread {

	// SearchForServers
	private DatagramSocket c;
	private volatile ArrayList<ServerAttributes> serverList;
	private boolean shouldSearchForServer = false;

	// connect to server
	private ServerAttributes connectedServer;

	// others
	private Socket client;
	private PrintWriter writer;
	private BufferedReader reader;
	private String userName;
	private final String nameAlreadyTakenError = "NAME_ALREADY_TAKEN!",
			nameTakenQuestion = "IS_NAME_ALREADY_TAKEN?";
	private final String NO_CHAT_MESSAGE = "rHBvyWvqbR0JVs6x6g24";
	private final String IP_ALREADY_IN_USE_ERROR = "IP_ALREADY_IN_USE_ERROR";
	private final String GAME_START = "GAME_START";
	private final String GAME_STOP = "GAME_STOP";
	private final String SPECTATOR_MODE = "SPECTATOR_MODE";
	private final String PLAYER_LEFT_MODE = "PLAYER_LEFT_MODE";
	private final String PLAYER_RIGHT_MODE = "PLAYER_RIGHT_MODE";
	private final String IN_GAME_POSITIONS = "IN_GAME_POSITIONS";
	


	//	public static ArrayList<String> bingoSentences = new ArrayList<String>();
	private PongFrame pongFrame;
	
	public ClientMainThread(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		serverList = new ArrayList<ServerAttributes>();

	}
	
	public void startSearchForServer()
	{
		Thread serverSearcher = new Thread(new ServerSearcherThread());
		serverSearcher.start();
		setShouldSearchForServer(true);
	}
	
	public boolean connectToServer() { // if true wait for server-response
		try {

			pongFrame.getClientChat().clear();
			System.out.println(getConnectedServer()+"");
			client = new Socket(getConnectedServer().getIP(), 5555);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream());

			Thread t = new Thread(new MessagesFromServerListener());
			t.start();

			sendMessageToServer(NO_CHAT_MESSAGE + "JOINING" + getUserName());

			return true; //yolo
		} catch (Exception e) {
			appendTextMessages("Netzwerkverbindung konnte nicht hergestellt werden", pongFrame.getClientChat().LEVEL_ERROR);
			System.out.println("\n\nI BIMS 1 EXCEPTION\n\n");
			e.printStackTrace();

			return false;
		}
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
		System.out.println("SETTING SHOULD SERCH FOR SERVER "+shouldSearchForServer);
	}

	public boolean disconnectFromServer(boolean WasOnServerChat) {

		System.out.println("iBims1leavenderdude");
		
		try {
			if (WasOnServerChat)
				sendMessageToServer(NO_CHAT_MESSAGE + "LEAVING" + getUserName());

			client.close();
			reader.close();
			writer.close();

			setConnect_Server(null);
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public void sendMessageToServer(String message) {
		if (writer != null) {
			if (!message.contains(NO_CHAT_MESSAGE)) {

				message = getUserName() + ":" + message;
			}
			writer.println(message);
			writer.flush();
		}
		ClientChat.last_timestamp = System.currentTimeMillis();
	}

	public void appendTextMessages(String message, int level) {
		pongFrame.getClientChat().appendTextToChat(message, level);
	}

	public void executeCommand(String msg) {
		if (msg.substring(0, 1).equals("/")) { // Command

		} else { // Message
			sendMessageToServer(msg);
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

	public void setConnect_Server(ServerAttributes connectedServer) {
		this.connectedServer = connectedServer;
	}

	public class MessagesFromServerListener implements Runnable {

		@Override
		public void run() {

			String message;
			try {

				while ((message = reader.readLine()) != null) {
//					System.out.println("Hi, its \""+userName+"\". I got this message: \""+message+"\". Go, check if it works!");
					if (message.contains(NO_CHAT_MESSAGE)) {
						if (message.contains(nameTakenQuestion)) {
							if (message.contains(nameAlreadyTakenError)) {

								disconnectFromServer(false);
								pongFrame.getMultiPlayer().getJoinServerPanel().clientAccepted(pongFrame.getMultiPlayer().getJoinServerPanel().NAME_ALREADY_IN_USE);
							} else if(message.contains(IP_ALREADY_IN_USE_ERROR)){
								
								disconnectFromServer(false);
								pongFrame.getMultiPlayer().getJoinServerPanel().clientAccepted(pongFrame.getMultiPlayer().getJoinServerPanel().IP_ALREADY_IN_USE);
								
							}else {
								pongFrame.getMultiPlayer().getJoinServerPanel().clientAccepted(pongFrame.getMultiPlayer().getJoinServerPanel().CLIENT_ACCEPTED);
							}
						}else if(message.contains(GAME_START)) {
							
							if(message.contains(SPECTATOR_MODE)) {
								pongFrame.showPane(pongFrame.CLIENT_LIVE_GAME_PANEL);
								pongFrame.getClientLiveGamePanel().getSpielfeld().configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().SPECTATOR);
							}else if(message.contains(PLAYER_LEFT_MODE)) {
								pongFrame.showPane(pongFrame.CLIENT_LIVE_GAME_PANEL);
								pongFrame.getClientLiveGamePanel().getSpielfeld().configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().PLAYER_LEFT);
							}else if(message.contains(PLAYER_RIGHT_MODE)) {
								pongFrame.showPane(pongFrame.CLIENT_LIVE_GAME_PANEL);
								pongFrame.getClientLiveGamePanel().getSpielfeld().configSF(pongFrame.getClientLiveGamePanel().getSpielfeld().PLAYER_RIGHT);
							}
						}else if(message.contains(IN_GAME_POSITIONS)) {
							
							
							
							safeInGameLocations(message.substring(message.indexOf("{")));
							
							
						}else if(message.contains(GAME_STOP)) {
							pongFrame.getClientLiveGamePanel().getSpielfeld().stopInGameSliderControl();
							pongFrame.showPane(pongFrame.CLIENT_CONTROL_PANEL);
						}
					} else {
						appendTextMessages(message, pongFrame.getClientChat().LEVEL_INFO);
					}
				}
			} catch (Exception e) {
				appendTextMessages("Verbindung zum Server verloren. Vielleicht ist dieser Offline?",
						pongFrame.getClientChat().LEVEL_ERROR);
				e.printStackTrace();

				System.out.println("Da gabs wohl son error, würd ich sagen");
				connectToServer();
			}
		}

		private void safeInGameLocations(String locationData) {
			//"{PLD="+ObjectStringCoder.objectToString(pLD)+"}"
			
			String objectString = locationData.substring(locationData.indexOf("{PLD=")+5, locationData.lastIndexOf("}"));
			PongLocationData pLD = null;
			try {
				pLD = (PongLocationData)ObjectStringCoder.stringToObject(objectString);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			pongFrame.getClientLiveGamePanel().getSpielfeld().updateLocations(pLD);
//			System.out.println("PONG-LOCATION-DATA="+pLD);
		}
		
//		public MessagesFromServerListener getMessagesFromServerListenerInstance() {
//
//			return MessagesFromServerListenerThreadHolder.INSTANCE;
//		}
//
//		private  class MessagesFromServerListenerThreadHolder {
//
//			private final MessagesFromServerListener INSTANCE = new MessagesFromServerListener();
//		}
	}

	private class ServerSearcherThread implements Runnable {

		@Override
		public void run() {

			while (true) {

				if (shouldSearchForServer) {

					// Search for Servers

					// Find the server using UDP broadcast

					try {

						// Open a random port to send the package

						c = new DatagramSocket();

						c.setBroadcast(true);

						byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

						// Try the 255.255.255.255 first

						try {

							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
									InetAddress.getByName("255.255.255.255"), 8888);

							c.send(sendPacket);
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

									c.send(sendPacket);

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
									getUserName() + ">>> Done looping over all network interfaces. Now waiting for a reply!",
									pongFrame.getClientChat().LEVEL_INFO);

						// START-SCHLEIFE;

						int timeout_waiting_response = 1000; // ms
						serverList.clear();
						c.setSoTimeout(timeout_waiting_response);
						long nowtime = System.currentTimeMillis(); // Timestamp from now in MS

						while ((System.currentTimeMillis() - nowtime) < timeout_waiting_response - 100) {

							// Wait for a response
							byte[] recvBuf = new byte[15000];
							DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);

							c.receive(receivePacket);

							// We have a response
							String IP = receivePacket.getAddress().getHostAddress();
							int lastByteIndex = IP.lastIndexOf(".");
							String lastByte = IP.substring(lastByteIndex + 1, IP.length());

							if (lastByte.equals("1"))
								continue; // Skip normal Gateway-Responses

							if (pongFrame.isShowClientNetworkInformation())
								appendTextMessages(getUserName() + ">>> Broadcast response from server: "
										+ receivePacket.getAddress().getHostAddress(), pongFrame.getClientChat().LEVEL_INFO);

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
//							System.out.println("FOUND_SERVER: "+found_actual__server);
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
//								System.out.println("ADDING SERVER: "+serverList);
							}
							pongFrame.getMultiPlayer().getJoinServerPanel().setServerList(serverList);
						} // ENDE-SCHLEIFE

						c.close();

					} catch (SocketTimeoutException e) {

						if (pongFrame.isShowClientNetworkInformation())
							appendTextMessages(
									getClass().getName() + ">>> Got no reply - No Server found!",
									pongFrame.getClientChat().LEVEL_INFO);

					} catch (IOException ex) {

						appendTextMessages("exception: \n" + ex.getMessage(), pongFrame.getClientChat().LEVEL_ERROR);

					}
				}
				try {
					Thread.sleep(10); //TODO: Sind 10ms die effizienteste Wartezeit?
				} catch (InterruptedException e) {
					appendTextMessages(e.getMessage(), pongFrame.getClientChat().LEVEL_ERROR);
				}
			}
		}

//		public static ServerSearcherThread getServerSearcherThreadInstance() {
//
//			return ServerSearcherThreadHolder.INSTANCE;
//		}
//
//		private static class ServerSearcherThreadHolder {
//
//			private static final ServerSearcherThread INSTANCE = new ServerSearcherThread();
//		}
	}
}