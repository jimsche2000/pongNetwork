package multiplayer.server;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.Collision;
import game.PongLocationData;
import game.Richtung;
import hauptmenu.PongFrame;
import multiplayer.client.ClientAttributes;
import pongtoolkit.ObjectStringCoder;

public class ServerMainThread implements Runnable {

	ServerSocket server;
//	static ArrayList<PrintWriter> listClientWriter;
//	static ArrayList<Socket> listSockets;
	private volatile ArrayList<ClientAttributes> listClients;
	private volatile PongLocationData pLD;
	private boolean shouldRun = false;
	private String name = "Server", maxUser = "0/100";
	private final String nameTakenQuestion = "IS_NAME_ALREADY_TAKEN?",
			nameAlreadyTakenError = "NAME_ALREADY_TAKEN!", IP_ALREADY_IN_USE_ERROR = "IP_ALREADY_IN_USE_ERROR", NO_CHAT_MESSAGE = "rHBvyWvqbR0JVs6x6g24",
			GAME_START = "GAME_START", SPECTATOR_MODE = "SPECTATOR_MODE", PLAYER_LEFT_MODE = "PLAYER_LEFT_MODE",
			GAME_STOP = "GAME_STOP", PLAYER_RIGHT_MODE = "PLAYER_RIGHT_MODE", IN_GAME_POSITIONS = "IN_GAME_POSITIONS";
	private ArrayList<String> leavedPlayerNames = new ArrayList<String>();
//	private static int indexID = 0;
	private GameThread runnable0 = new GameThread();
	private Thread gameThread = new Thread(runnable0);
	private DiscoveryThread discoveryThreadInstance;


	private Thread discoveryThread;
	private ClientAttributes PLAYER_ONE;
	private ClientAttributes PLAYER_TWO;
	private PongFrame pongFrame;
	
	public synchronized boolean isShouldRun() {
		return shouldRun;
	}

	public synchronized void setShouldRun(boolean shouldRun) {
		this.shouldRun = shouldRun;
	}

	@Override
	public void run() {
		System.out.println(getClass().getName() + ":");
		shouldRun = true;
		while (true) {

			if (shouldRun) {

				if (runServer()) {
					listenToClients();
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
	public ServerMainThread(String name, Long long1, PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		this.setName(name);
		this.setMaxUser(Long.toString(long1));
		this.shouldRun = true;
		pLD = new PongLocationData();
		pLD.setBall(new Point((1920-50)/2, (1080-50-50)/2));
		pLD.setSliderLeft(new Point(10, (1030-200)/2));
		pLD.setSliderRight(new Point(1920-40, (1030-50)/2));
		
		if(firsttime) {
			setDiscoveryThreadInstance(new DiscoveryThread());
			discoveryThread = new Thread(getDiscoveryThreadInstance());
			firsttime = false;
		}
//		schlagL.setBounds(10, (newSize.height-300)/2, 30, 300);
//		schlagL.setBackground(Color.WHITE);
//		
//		schlagR.setBounds(newSize.width-40, (newSize.height-300)/2, 30, 300);
//		schlagR.setBackground(Color.WHITE);
	}

	public void stop() {
		shouldRun = false;
//		pongFrame.getMultiPlayer().getCreateServerPanel().setServerIsRunning(false);
		try {

			pongFrame.getHostServer().server.close();
			shouldRun = false;
//			if(discoveryThreadInstance!=null)if(discoveryThreadInstance.getSocket()!=null)discoveryThreadInstance.getSocket().close();
//			getDiscoveryThread().interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pongFrame.getHostServer().appendTextToConsole("Server wurde gestoppt!", pongFrame.getHostServerConsole().LEVEL_ERROR);
	}

	/*
	 * playerOne bescheid geben dass er den linken slider zu steuern hat playerTwo
	 * bescheid gebeen dass er den rechten slider zu steuern hat
	 * 
	 * allen anderen clients bescheid geben, dass sie spectator sind.
	 * 
	 * listClients -> socket anhand von den IPs der beiden ClientAttributes
	 * ermitteln
	 * 
	 */
	public void configureGameForClients(ClientAttributes playerOne, ClientAttributes playerTwo) {
		// ClientAttributes list:

		String spectatorMessage = NO_CHAT_MESSAGE + GAME_START + SPECTATOR_MODE,
				playerOneMessage = NO_CHAT_MESSAGE + GAME_START + PLAYER_LEFT_MODE,
				playerTwoMessage = NO_CHAT_MESSAGE + GAME_START + PLAYER_RIGHT_MODE;

//		System.out.println("CLIENTS: " + listClients.size() + " WRITER: " + listClientWriter.size() + " SOCKETS: "
//				+ listSockets.size());
		for (ClientAttributes cA : getListClients()) {
//			int ID = listClients.indexOf(cA);
			PrintWriter pW = cA.getWriter();
			
			if (cA.equals(playerOne)) { // Spieler Links
				System.out.println("SEND \"" + playerOneMessage + "\" to " + cA.getName());
				pW.println(playerOneMessage);
				pW.flush();
				this.PLAYER_ONE = playerOne;
			} else if (cA.equals(playerTwo)) { // Spieler Rechts
				System.out.println("SEND \"" + playerTwoMessage + "\" to " + cA.getName());
				pW.println(playerTwoMessage);
				pW.flush();
				this.PLAYER_TWO = playerTwo;
			} else { // Spectator
				System.out.println("SEND \"" + spectatorMessage + "\" to " + cA.getName());
				pW.println(spectatorMessage);
				pW.flush();
			}
		}
		

		if(!gameThread.isAlive()) {
			gameThread.start();
		}else {
			runnable0.starten();
		}
	}
	
	public void stopGameForClients(String msg) {
		
		runnable0.reset();
		String stopMSG = this.NO_CHAT_MESSAGE+this.GAME_STOP;
		
		pongFrame.getHostServer().sendToAllClients(stopMSG);
		pongFrame.getHostServer().sendToAllClients(msg);
		pongFrame.getHostServerConsole().appendTextToConsole(msg, pongFrame.getHostServerConsole().LEVEL_INFO);
	}

	public void listenToClients() {
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
//					if(listClientWriter.size()<=indexID) {
//						listClientWriter.add(writer);
//					}else {
//						listClientWriter.set(indexID, writer);
//					}

					Thread clientThread = new Thread(new ClientHandler(client));
					clientThread.start();
					
//					if(listSockets.size()<=indexID) {
//						listSockets.add(client);
//					}else {
//						listSockets.set(indexID, client);
//					}
//					indexID++;
					
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

//			listClientWriter = new ArrayList<PrintWriter>();
			setListClients(new ArrayList<ClientAttributes>());
//			listSockets = new ArrayList<Socket>();
			return true;
		} catch (IOException e) {
			appendTextToConsole("Server konnte nicht gestartet werden! Läuft bereits ein Server an diesem PC?",
					pongFrame.getHostServerConsole().LEVEL_INFO);
			e.printStackTrace();
			return false;
		}
	}

	public void executeCommand(String cmd) {
		if (cmd.substring(0, 1).equals("/")) { // Command

		} else { // Message
			appendTextToConsole("Admin: " + cmd, pongFrame.getHostServerConsole().LEVEL_NORMAL);
			sendToAllClients("Admin: " + cmd);
		}
	}

	public void appendTextToConsole(String message, int level) {
		pongFrame.getHostServerConsole().appendTextToConsole(message, level);

	}

	public void sendToAllClients(String message) {
		if (getListClients() != null) {

			@SuppressWarnings("rawtypes")
			Iterator it = getListClients().iterator();

//			System.out.println("sendToAll: \"" + message + "\"");

			while (it.hasNext()) {
				PrintWriter writer = ((ClientAttributes)it.next()).getWriter();

				writer.println(message);
				writer.flush();

			}
		}
	}

	public static void sendToClient(Socket client, String msg) {

		PrintWriter writer;
		try {
			writer = new PrintWriter(client.getOutputStream());
			writer.println(msg);
			writer.flush();

			System.out.println("Write: \"" + msg + "\"");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void removeClientFromList(String ip, String name) {
		// System.out.println("----------------------------------\nREMOVE CLIENT FROM
		// LIST\n--------------------------------");
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
		// System.out.println("JOIN-Name: "+name);
		if (getListClients().size() > 0) {
			for (ClientAttributes client : getListClients()) {
				// System.out.println("Client-Name: "+client.getName());
				if (client.getName().equals(name)) {

					return true;
				}
			}
			return false;
		}
		return false;
	}

	public boolean ipAlreadyTaken(String iP) {
//		System.out.println("CLIENT_LIST: "+listClients);
		if (getListClients().size() > 0) {
			int count = 0;
			for (ClientAttributes client : getListClients()) {
				// System.out.println("Client-Name: "+client.getName());
//				System.out.println("Client \""+client.getName()+"\" der iP-Adresse "+client.getIP()+"");
				if (client.getIP().equals(iP)) {
//					System.out.println("Die iP \""+iP+"\" ist bereits vergeben.");
					count++;
				}
			}
			if(count>=2)return true;
			return false;
		}
		return false;
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

	public class ClientHandler implements Runnable {

		Socket client;
		BufferedReader reader;

		public ClientHandler(Socket client) {
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
				String nachricht;
				String IP = client.getInetAddress().getHostAddress();
				String name = "";
				try {
					// if connection reset exception -> user leaved game
					while ((nachricht = reader.readLine()) != null) {
						System.out.println("-----------------------------\n<Message>\n-----------------------------");
						System.out.println("(client-count: "+getListClients().size()+")");
						if (nachricht.contains(NO_CHAT_MESSAGE)) {

							if (nachricht.contains("JOINING")) {// "JOINING".equals(nachricht.substring(0,
																// nachricht.indexOf("G", 0)+1))

								IP = client.getInetAddress().getHostAddress();
								name = nachricht.substring(nachricht.indexOf("JOINING", 0) + 7);
								
								if(!ipAlreadyTaken(IP)) {
									if (!nameAlreadyTaken(name)) {
										if (leavedPlayerNames.contains(name))
											leavedPlayerNames.remove(name);
										/*
										 * Anhand der IP den Client-Namen im ClientAttributes Objekt hinzufügen setzen
										 * 
										 */
										for(ClientAttributes client : getListClients()) {
											if(client.getIP().equals(IP)) {
												client.setName(name);
//												System.out.println("CLIENT: "+client);
											}
										}
										
										
//										listClients.add(new ClientAttributes(IP, name));
										pongFrame.getHostServer().appendTextToConsole(name + " ist dem Server beigetreten",
												pongFrame.getHostServerConsole().LEVEL_INFO);
										sendToClient(client, pongFrame.getHostServer().NO_CHAT_MESSAGE + nameTakenQuestion);
										pongFrame.getHostServer().sendToAllClients(name + " ist dem Server beigetreten");
									} else {
										sendToClient(client, pongFrame.getHostServer().NO_CHAT_MESSAGE + nameTakenQuestion
												+ nameAlreadyTakenError);

										for(ClientAttributes client : getListClients()) {
											if(client.getIP().equals(IP)) {
												client.setName(name);
												System.out.println("CLIENT: "+client);
											}
										}
										
										removeClientFromList(IP, name);
									}
									
									
								}else { //IP wird bereits von einem Client benutzt.
									
									
									sendToClient(client, pongFrame.getHostServer().NO_CHAT_MESSAGE + nameTakenQuestion
											+ IP_ALREADY_IN_USE_ERROR);

									for(ClientAttributes client : getListClients()) {
										if(client.getIP().equals(IP)) {
											client.setName(name);
											System.out.println("CLIENT: "+client);
										}
									}
									removeClientFromList(IP, name);
								}
							}
							// LEAVINGname
							else if (nachricht.contains("LEAVING")) {// "LEAVING".equals(nachricht.substring(0,
																		// nachricht.indexOf("G", 0)+1))

								IP = client.getInetAddress().getHostAddress();
								name = nachricht.substring(nachricht.indexOf("LEAVING", 0) + 7);
								leavedPlayerNames.add(name);
								removeClientFromList(IP, name);

								pongFrame.getHostServer().appendTextToConsole(name + " hat den Server verlassen",
										pongFrame.getHostServerConsole().LEVEL_INFO);
								pongFrame.getHostServer().sendToAllClients(name + " hat den Server verlassen");

							}else if(nachricht.contains(pongFrame.getHostServer().IN_GAME_POSITIONS)) {
								
								System.out.println("[SERVER]IN_GAME_POSITIONS_BY_CLIENTS");
								
								safeInGameLocations(nachricht.substring(nachricht.indexOf("{")));
							}
							// else if (nachricht.contains("VORSCHLAG")) {
							// String message = nachricht.substring(nachricht.indexOf("VORSCHLAG") + 10);
							//
							// pongFrame.getHostServer().appendTextToConsole(message, ServerConsole.LEVEL_INFO);
							// } else if (nachricht.contains(PATTERN_REACHED)) {
							// int startPos = nachricht.indexOf(PATTERN_REACHED) + PATTERN_REACHED.length();
							//
							// String message = nachricht.substring(startPos) + " hat diese Bingo-Runde
							// gewonnen!";
							// for (int i = 0; i < 10; i++) {
							// appendTextToConsole(message, ServerConsole.LEVEL_INFO);
							// pongFrame.getHostServer().sendToAllClients(message);
							// }
							// }
						} else {
							int pos = nachricht.indexOf(":");
							String name2 = nachricht.substring(0, pos);
							nachricht = nachricht.substring(pos + 1, nachricht.length());
							String sendMSG = name2 + ": " + nachricht;
							appendTextToConsole(sendMSG, pongFrame.getHostServerConsole().LEVEL_NORMAL);
							sendToAllClients(sendMSG);
						}
						System.out.println("-----------------------------\n</Message>-----------------------------");
					}
				} catch (IOException e) {
					if (!leavedPlayerNames.contains(name)) {
						removeClientFromList(IP, name);

						pongFrame.getHostServer().appendTextToConsole(name + " hat den Server verlassen",
								pongFrame.getHostServerConsole().LEVEL_INFO);
						pongFrame.getHostServer().sendToAllClients(name + " hat den Server verlassen");

						e.printStackTrace();
					}
				}
			} else if (client.isClosed()) {// Client geschlossen
				try {

					for(ClientAttributes cA : getListClients()) {
						
						if(cA.getClient().equals(client)) {
							String name = cA.getName();

							pongFrame.getHostServer().appendTextToConsole(name + " hat den Server verlassen", pongFrame.getHostServerConsole().LEVEL_INFO);
							pongFrame.getHostServer().sendToAllClients(name + " hat den Server verlassen");
						}
					}
				} catch (Exception e2) {

					pongFrame.getHostServer().appendTextToConsole("Unbekannt hat den Server verlassen",
							pongFrame.getHostServerConsole().LEVEL_INFO);
					pongFrame.getHostServer().sendToAllClients("Unbekannt hat den Server verlassen");
				}
			}
		}
		
		
		private void safeInGameLocations(String locationData) {
			//"{ORIENTATION="+orientation+"}{X="+p.x+"}{Y="+p.y+"}"
			
			System.out.println("[SERVER]SAFE_INGAME_LOCATIONS: \""+locationData+"\"");
			
			String orientation = locationData.substring(locationData.indexOf("=")+1, locationData.indexOf("}"));
			locationData = locationData.substring(locationData.indexOf("}")+1);
			
			int x = Integer.valueOf(locationData.substring(locationData.indexOf("X=")+2, locationData.indexOf("}")));
			int y = Integer.valueOf(locationData.substring(locationData.indexOf("Y=")+2, locationData.lastIndexOf("}")));
			
			Point p = new Point(x, y);
			System.out.println("[SERVER]ORIENTATION="+orientation+" X="+x+" Y="+y);
			if(orientation.equals("RIGHT")) {
				
				pLD.setSliderRight(p);
				
			}else if(orientation.equals("LEFT")) {
				
				pLD.setSliderLeft(p);
			}
		}
	}
//	public class GameCommunicationThread implements Runnable{
//		private DatagramSocket socket;
//		int port = 5555;
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			try {
//				socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
//			} catch (SocketException | UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		} //UDP PORT 7777
//		
//		private void sendMessage(String msg, InetAddress Ip) {
//			byte[] sendData = null;
//			try {
//				sendData = msg.getBytes("UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			DatagramPacket sendPacket = new DatagramPacket(sendData, msg.length(),
//					Ip, port);
//
//			try {
//				socket.send(sendPacket);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
	public class DiscoveryThread implements Runnable {
		private DatagramSocket socket;
		private boolean shouldDiscover = true;
		public DatagramSocket getSocket() {
			return socket;
		}

		public void setSocket(DatagramSocket socket) {
			this.socket = socket;
		}
		boolean firsttime = true;
		@Override
		public void run() {
			try {
				// Keep a socket open to listen to all the UDP trafic that is destined for this
				// port
				if(firsttime) {
					socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
	
					socket.setBroadcast(true);
					firsttime = false;
					
				}
				while (true) {
					System.out.println("DISCOVERYTHREAD SOCKET CLOSED: "+socket.isClosed()+" shouldDiscover: "+shouldDiscover);
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
							pongFrame.getHostServer().appendTextToConsole(getClass().getName()
									+ ">>>Discovery packet received from: " + packet.getAddress().getHostAddress(),
									pongFrame.getHostServerConsole().LEVEL_INFO);

						if (pongFrame.isShowServerNetworkInformation())
							pongFrame.getHostServer().appendTextToConsole(
									getClass().getName() + ">>>Packet received; data: " + new String(packet.getData()),
									pongFrame.getHostServerConsole().LEVEL_INFO);
						// See if the packet holds the right command (message)

						String message = new String(packet.getData()).trim();

						if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
							String text = "DISCOVER_FUIFSERVER_RESPONSE{NAME=" + getName() + "}{USER_COUNT="
									+ getListClients().size() + "/" + getMaxUser() + "}";
							byte[] sendData = text.getBytes("UTF-8");

							// Send a response

							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
									packet.getAddress(), packet.getPort());

							socket.send(sendPacket);
							if (pongFrame.isShowServerNetworkInformation())
								pongFrame.getHostServer().appendTextToConsole(getClass().getName() + ">>>Sent packet to: "
										+ sendPacket.getAddress().getHostAddress(), pongFrame.getHostServerConsole().LEVEL_INFO);
						}
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
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
			System.out.println("\n\nShouldDiscover: "+shouldDiscover);
			if(!discoveryThread.isAlive())discoveryThread.start();
			this.shouldDiscover = shouldDiscover;
		}

//		public static DiscoveryThread getInstance() {
//
//			return DiscoveryThreadHolder.INSTANCE;
//		}
//
//		private static class DiscoveryThreadHolder {
//
//			private static final DiscoveryThread INSTANCE = new DiscoveryThread();
//		}
	}
	
	public class GameThread implements Runnable {

		public boolean richtungx;
		public boolean richtungy;
		private boolean neu = false;
		public boolean xnull;
		public boolean ynull;
		public int score1 = 0;
		public int score2 = 0;
		private int MAX_POINTS = 15;
		private int oft;
		// private JLabel scoreLabel = new JLabel("0 : 0");
		private String score = "0 : 0";
		// private double speed = 1;
//		private boolean up, down, up1, down1;
		Timer timer = new Timer();
		int periodendauer = 2; // Milisekunden
		boolean gestartet = false;
		Dimension ballSize = new Dimension(50, 50);
		Dimension sliderSize = new Dimension(10, 200);
		Dimension frameSize = new Dimension(1920, 1080);
//		int speed = 2;
		int wy = 2, wx = 2, px, py;
		boolean winkel, kl, kr;
		private int weitex, weitey, weitex1, weitey1, weitey2, weitey3;

		/* TODO
		 * Eigenes Datagram-Socket eröffnen, welches die aktuellen Locations
		 * per UDP an die beiden Spieler und die Spectator schickt.
		 * Zusätzlich noch eine Art UDP Listener um die Positionen der Spieler zu erhalten.
		 * Das ganze natürlich auch anders herum für die Clients.....
		 */
		// double speadup = 1.0;
		@Override
		public void run() {
			// PongLocationData pLD
			Richtung.setRichtungX(false);
			Richtung.setRichtungY(true);

			weitex = wx;
			weitey = wy;
			weitey1 = 2;
			weitex1 = 4;
			weitey2 = 2;
			weitey3 = 3;
			oft = 0;
			winkel = true;

			starten();

//			PongLocationData old = null;
			// GAME-PHYSICS
			while (true) {
				// GAME
				try {
					if (gestartet) {
						
						
						
						boolean x1 = Richtung.isRichtungX();
						boolean y1 = Richtung.isRichtungY();

						Richtung.isxNichts();
						Richtung.isyNichts();
						
						if (x1 == true) {
							gehe(-weitex, 0);
							// System.out.println("x true");
						}

						if (x1 == false) {
							gehe(weitex, 0);
							// System.out.println("x false");
						}

						if (y1 == true) {
							gehe(0, weitey);
							// System.out.println("y true");
						}

						if (y1 == false) {
							gehe(0, -weitey);
							// System.out.println("y false");
						}
						pongFrame.getHostServer()
						.sendToAllClients(pongFrame.getHostServer().NO_CHAT_MESSAGE + pongFrame.getHostServer().IN_GAME_POSITIONS
								+ "{PLD=" + ObjectStringCoder.objectToString(pLD) + "}");
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
				// AUSGABE
//				System.out.println("[GAME-THREAD]SliderLeft: " + pLD.getSliderLeft() + " SliderRight: "
//						+ pLD.getSliderRight() + " Ball: " + pLD.getBall() + " Punkte: " + pLD.getScore());
			}
		}

		public  void reset() {
			oft = 0;
			px = 0;
			kl = false;
			kr = false;
			weitex = 2;
			weitey = 2;
			score1 = 0;
			score2 = 0;
			
			pLD = new PongLocationData();
			pLD.setBall(new Point((1920-50)/2, (1080-50-50)/2));
			pLD.setSliderLeft(new Point(10, (1030-200)/2));
			pLD.setSliderRight(new Point(1920-40, (1030-50)/2));
			
			gestartet = false;
		}
		
		public void stoppen() {
			if (gestartet) {
				gestartet = false;
				if(score1 >=MAX_POINTS || score2 >=MAX_POINTS) {
					String msg = "";
					if(score1 > score2) {
						msg = pongFrame.getHostServer().PLAYER_ONE.getName()+" hat mit "+score1+" zu "+score2+" gegen "+pongFrame.getHostServer().PLAYER_TWO.getName()+" gewonnen.";
					}else if(score2 > score1) {
						msg = pongFrame.getHostServer().PLAYER_TWO.getName()+" hat mit "+score2+" zu "+score1+" gegen "+pongFrame.getHostServer().PLAYER_ONE.getName()+" gewonnen.";
					}
					score1 = 0;
					score2 = 0;
					pongFrame.getHostServer().stopGameForClients(msg);
					
				}else {
					pLD.getBall().setLocation((frameSize.width - ballSize.width) / 2,
							(frameSize.height - ballSize.height) / 2);

					timer.schedule(new TimerTask() {
						public void run() {
							starten();
							oft = 0;
							px = 0;
							kl = false;
							kr = false;
							weitex = 2;
							weitey = 2;
						}
					}, 1500);
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
			Point p = pLD.getBall(); //ballLocation:p
//			Dimension d = ballSize; //ballgröße:d

			p.x = p.x - x;
			p.y = p.y + y;
				
			//Objekt Kollision

//			Dimension ds = pongFrame.getGraphicResolution(); //TODO:
			Dimension ds = new Dimension(1920,1080); //TODO:
			int radius = ballSize.width / 2;
		
			
			if (Collision.circleToRect(p.x + radius, p.y + radius, radius,
					pLD.getSliderLeft().getLocation().x, pLD.getSliderLeft().getLocation().y, sliderSize.width,
					sliderSize.height)) { 

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

			if (Collision.circleToRect(p.x + radius, p.y + radius, radius,
					pLD.getSliderRight().getLocation().x, pLD.getSliderRight().getLocation().y, sliderSize.width,
					sliderSize.height)) {

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
				// speed = 1;
				// speadup = 1.0;
				score2++;
				score = score1 + " : " + score2;
				pLD.setScore(score);
				tempCollision1 = true;
				this.stoppen();
			}else{
				tempCollision1 = false;
			}
	
			if(p.y<0)
			{
				randtesty(true);
			}

			if (ds.width <= p.x + ballSize.width && !tempCollision2) {
				score1++;
				score = score1 + " : " + score2;
				pLD.setScore(score);
				tempCollision2 = true;
				this.stoppen();
			}else
			{
				tempCollision2 = false;
			}
	
			if(ds.height<=p.y+ballSize.getSize().height)
			{
				randtesty(false);
			}
	
			if(gestartet && neu)
			{
				pLD.getBall().setLocation(400, 400);
				neu = false;
				pLD.getBall().setLocation(p);
			}
	
			if(gestartet && neu)
			{
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
	}
}


