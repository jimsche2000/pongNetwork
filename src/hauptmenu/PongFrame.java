package hauptmenu;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import game.SinglePlayer;
import gui.AlphaContainer;
import multiplayer.client.ClientChat;
import multiplayer.client.ClientControlPanel;
import multiplayer.client.ClientLiveGamePanel;
import multiplayer.client.ClientMainThread;
import multiplayer.server.ServerConsole;
import multiplayer.server.ServerControlPanel;
import multiplayer.server.ServerMainThread;
import multiplayer.server.ServerSpectatorPanel;
import pongtoolkit.FontLoader;
import pongtoolkit.ImageLoader;

public class PongFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel componentPanel;
	private Font GLOBAL_FONT;
	private ServerControlPanel serverControlPanel;
	private ServerSpectatorPanel serverLiveGamePanel;
	private ClientControlPanel clientControlPanel;
	private ClientLiveGamePanel clientLiveGamePanel;
	private MainMenu mainMenu;
	private LevelSelection levelSelection;
	private Credits credits;
	private MultiPlayer multiPlayer;
	private SinglePlayer singleplayer; // Offline/SinglePlayer
	private Point MOUSE_LOCATION = new Point(500, 500);
	private Image cursorImage = ImageLoader.loadImage("edit_icon2.png", 50, 50);// giphy.gif
	private FontLoader flo;
	private ServerMainThread hostServer;
	private ServerConsole hostServerConsole = new ServerConsole(this);
	private ClientMainThread clientThread;
	private ClientChat clientChat = new ClientChat(this);
	private boolean showClientNetworkInformation = false;
	private boolean showServerNetworkInformation = false;
	private boolean updateUserListOnServer = false;
	private boolean updateUserListOnClient = false;
	private CardLayout cl = new CardLayout();
	private float ASPECT_RATIO = 1.0f;
	private Dimension graphicResolution;
	private Dimension windowResolution;
	private int ACTIVE_PANEL;

	public final int SERVER_CONTROL_PANEL = 3;
	public final int SERVER_LIVE_GAME_PANEL = 4;
	public final int CLIENT_CONTROL_PANEL = 5;
	public final int CLIENT_LIVE_GAME_PANEL = 6;
	public final int MAIN_MENU = 7;
	public final int SINGLEPLAYER = 8;
	public final int MULTI_PLAYER = 9;
	public final int CREDITS = 10;
	public final int LEVEL_SELECTION = 11;
	public final int TITLE_HEIGHT = 50;

	public PongFrame() {
		super("Pong Projekt 2018 - EAIT6");
		JOptionPane pane = new JOptionPane("Das Spiel lädt...", JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = pane.createDialog(null, "Pong-Network-2018");
		dialog.setModal(false);
		dialog.setVisible(true);

//		Font font;
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		String[] arfonts = ge.getAvailableFontFamilyNames();
		flo = new FontLoader();
		GLOBAL_FONT = flo.loadFont("PressStart2P");

		// hammer wichtig
//		Dimension fullScreenSize = new Dimension(1920, 1080); //for testing on different resolutions
//		Dimension fullScreenSize = new Dimension(1680, 1050); // for testing on different resolutions
		Dimension fullScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		int gcd = gcd(fullScreenSize.width, fullScreenSize.height); //greatest common divisor // größter gemeinsamer teiler
//		int har = fullScreenSize.width / gcd; // horizontal aspect ratio
//		int var = fullScreenSize.height / gcd; //vertical aspect ratio
//		System.out.println("Aspect ratio is "+har+":"+var);

		windowResolution = fullScreenSize;
//		float[] screenSize = {1920.0f,1200.0f};
		float[] screenSize = { (float) fullScreenSize.getWidth(), (float) fullScreenSize.getHeight() };
		float[] programmedSize = { 1920.0f, 1080.0f };
		ASPECT_RATIO = aspect_correct_scale_for_rect(screenSize, programmedSize);
		graphicResolution = new Dimension((int) (1920 * ASPECT_RATIO), (int) (1080 * ASPECT_RATIO));
		System.out.println("PR 1920,1080; ASP_RATIO: " + ASPECT_RATIO + "; Resolution: " + (graphicResolution.width)
				+ "x" + (graphicResolution.height));

		setLayout(new BorderLayout());
		setSize(fullScreenSize);

		componentPanel = new JPanel();
		componentPanel.setLayout(cl);
		componentPanel.setAlignmentY(SwingConstants.CENTER);
		componentPanel.setPreferredSize(graphicResolution);
		componentPanel.setSize(graphicResolution);

		serverControlPanel = new ServerControlPanel(this);
		serverControlPanel.setAlignmentX(SwingConstants.CENTER);
		serverControlPanel.setAlignmentY(SwingConstants.CENTER);

		serverLiveGamePanel = new ServerSpectatorPanel(this);
		serverLiveGamePanel.setAlignmentX(SwingConstants.CENTER);
		serverLiveGamePanel.setAlignmentY(SwingConstants.CENTER);

		clientControlPanel = new ClientControlPanel(this);
		clientControlPanel.setAlignmentX(SwingConstants.CENTER);
		clientControlPanel.setAlignmentY(SwingConstants.CENTER);

		clientLiveGamePanel = new ClientLiveGamePanel(this);
		clientLiveGamePanel.setAlignmentX(SwingConstants.CENTER);
		clientLiveGamePanel.setAlignmentY(SwingConstants.CENTER);

		mainMenu = new MainMenu(this);
		mainMenu.setAlignmentX(SwingConstants.CENTER);
		mainMenu.setAlignmentY(SwingConstants.CENTER);

		levelSelection = new LevelSelection(this);
		levelSelection.setAlignmentX(SwingConstants.CENTER);
		levelSelection.setAlignmentY(SwingConstants.CENTER);
		
		credits = new Credits(this);
		credits.setAlignmentX(SwingConstants.CENTER);
		credits.setAlignmentY(SwingConstants.CENTER);

		multiPlayer = new MultiPlayer(this);
		multiPlayer.setAlignmentX(SwingConstants.CENTER);
		multiPlayer.setAlignmentY(SwingConstants.CENTER);

		singleplayer = new SinglePlayer(this);
		singleplayer.setAlignmentX(SwingConstants.CENTER);
		singleplayer.setAlignmentY(SwingConstants.CENTER);

		componentPanel.setOpaque(false);
//		credits.setOpaque(false); //Deswegen war der Hintergrund nicht Schwarz!

		componentPanel.add(serverControlPanel, "serverControlPanel");
		componentPanel.add(serverLiveGamePanel, "serverLiveGamePanel");
		componentPanel.add(clientControlPanel, "clientControlPanel");
		componentPanel.add(clientLiveGamePanel, "clientLiveGamePanel");
		componentPanel.add(mainMenu, "mainMenu");
		componentPanel.add(new AlphaContainer(levelSelection), "levelSelection");
		componentPanel.add(credits, "credits");
		componentPanel.add(multiPlayer, "multiPlayer");
		componentPanel.add(singleplayer, "singleplayer");
		componentPanel.setBackground(Color.black);

		this.add(componentPanel, BorderLayout.CENTER);
		cl.show(componentPanel, "mainMenu");
		ACTIVE_PANEL = this.MAIN_MENU;

		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(this.getX(), this.getY()),
				"cursor"));
		setBackground(Color.black);
		setIconImage(ImageLoader.loadImage("pongIcon.png"));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		setVisible(true);
//		showOnScreen(1, this);
		dialog.setVisible(false);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(cursorImage, MOUSE_LOCATION.x, MOUSE_LOCATION.y - cursorImage.getHeight(null), null);

	}

	boolean firstTime = true;

	public void showPane(int ID) {

		switch (ID) {
		case SERVER_CONTROL_PANEL:
			ACTIVE_PANEL = this.SERVER_CONTROL_PANEL;
			updateUserListOnServer = true;
			serverControlPanel.reload();
			cl.show(componentPanel, "serverControlPanel");
			break;
		case SERVER_LIVE_GAME_PANEL:
			ACTIVE_PANEL = this.SERVER_LIVE_GAME_PANEL;
			setConsoleToPanel(SERVER_LIVE_GAME_PANEL);
			cl.show(componentPanel, "serverLiveGamePanel");
			break;
		case CLIENT_CONTROL_PANEL:
			ACTIVE_PANEL = this.CLIENT_CONTROL_PANEL;
			cl.show(componentPanel, "clientControlPanel");
			break;
		case CLIENT_LIVE_GAME_PANEL:
			ACTIVE_PANEL = this.CLIENT_LIVE_GAME_PANEL;
			cl.show(componentPanel, "clientLiveGamePanel");
			break;
		case MAIN_MENU:
			ACTIVE_PANEL = this.MAIN_MENU;
			cl.show(componentPanel, "mainMenu");
			break;
		case LEVEL_SELECTION:
			ACTIVE_PANEL = this.LEVEL_SELECTION;
			cl.show(componentPanel, "levelSelection");
//			levelSelection.repaint();
			break;
		case CREDITS:
			ACTIVE_PANEL = this.CREDITS;
			cl.show(componentPanel, "credits");
			break;
		case MULTI_PLAYER:
			ACTIVE_PANEL = this.MULTI_PLAYER;
			cl.show(componentPanel, "multiPlayer");
			if (multiPlayer.isJoinServerPanelActive()) {
				if (firstTime) {
					clientThread = new ClientMainThread(this);
					clientThread.startSearchForServer();
					firstTime = false;
				} else {
					getClientThread().setShouldSearchForServer(true);
				}
			}
			break;
		case SINGLEPLAYER:
			ACTIVE_PANEL = this.SINGLEPLAYER;
			cl.show(componentPanel, "singleplayer");
//			singleplayer.repaint();
			// Der richtige Modus wird von der LevelSelection direkt an Game übergeben
			break;
		default:
			break;
		}
//		repaint();

	}

	private void setConsoleToPanel(int ID) {

		switch (ID) {

		case SERVER_CONTROL_PANEL:

			serverLiveGamePanel.setConsole(false);
			serverControlPanel.setConsole(true);
			break;
		case SERVER_LIVE_GAME_PANEL:

			serverControlPanel.setConsole(false);
			serverLiveGamePanel.setConsole(true);
			break;
		default:
			break;
		}
	}

//	private int gcd (int a, int b) {
//	    if (b == 0)return a;
//	    
//	    return gcd (b, a % b);
//	}

	/*
	 * For a rectangle inside a screen, get the scale factor that permits the
	 * rectangle to be scaled without stretching or squashing.
	 */
	private float aspect_correct_scale_for_rect(float screen[], float rect[]) {
		float screenAspect = screen[0] / screen[1];
		float rectAspect = rect[0] / rect[1];

		float scaleFactor;
		if (screenAspect > rectAspect)
			scaleFactor = screen[1] / rect[1];
		else
			scaleFactor = screen[0] / rect[0];

		return scaleFactor;
	}

	public static void showOnScreen(int screen, JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (screen > -1 && screen < gd.length) {
			frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x,
					gd[0].getDefaultConfiguration().getBounds().y + frame.getY());
		} else if (gd.length > 0) {
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x,
					gd[0].getDefaultConfiguration().getBounds().y + frame.getY());
		} else {
			throw new RuntimeException("No Screens Found");
		}
	}

	public Font getGLOBAL_FONT() {
		return GLOBAL_FONT;
	}

	public void setGLOBAL_FONT(Font gLOBAL_FONT) {
		GLOBAL_FONT = gLOBAL_FONT;
	}

	public ClientLiveGamePanel getClientLiveGamePanel() {
		return clientLiveGamePanel;
	}

	public void setClientLiveGamePanel(ClientLiveGamePanel clientLiveGamePanel) {
		this.clientLiveGamePanel = clientLiveGamePanel;
	}

	public MultiPlayer getMultiPlayer() {
		return multiPlayer;
	}

	public void setMultiPlayer(MultiPlayer multiPlayer) {
		this.multiPlayer = multiPlayer;
	}

	public ServerMainThread getHostServer() {
		return hostServer;
	}

	public void setHostServer(ServerMainThread hostServer) {
		this.hostServer = hostServer;
	}

	public ServerConsole getHostServerConsole() {
		return hostServerConsole;
	}

	public void setHostServerConsole(ServerConsole hostServerConsole) {
		this.hostServerConsole = hostServerConsole;
	}

	public ClientMainThread getClientThread() {
		return clientThread;
	}

	public void setClientThread(ClientMainThread clientThread) {
		this.clientThread = clientThread;
	}

	public ClientChat getClientChat() {
		return clientChat;
	}

	public void setClientChat(ClientChat clientChat) {
		this.clientChat = clientChat;
	}

	public boolean isShowClientNetworkInformation() {
		return showClientNetworkInformation;
	}

	public void setShowClientNetworkInformation(boolean showClientNetworkInformation) {
		this.showClientNetworkInformation = showClientNetworkInformation;
	}

	public boolean isShowServerNetworkInformation() {
		return showServerNetworkInformation;
	}

	public void setShowServerNetworkInformation(boolean showServerNetworkInformation) {
		this.showServerNetworkInformation = showServerNetworkInformation;
	}

	public boolean isUpdateUserListOnServer() {
		return updateUserListOnServer;
	}

	public void setUpdateUserListOnServer(boolean updateUserListOnServer) {
		this.updateUserListOnServer = updateUserListOnServer;
	}

	public boolean isUpdateUserListOnClient() {
		return updateUserListOnClient;
	}

	public void setUpdateUserListOnClient(boolean updateUserListOnClient) {
		this.updateUserListOnClient = updateUserListOnClient;
	}

	public SinglePlayer getSinglePlayer() {
		return singleplayer;
	}

	public void setSinglePlayer(SinglePlayer singleplayer) {
		this.singleplayer = singleplayer;
	}

	public LevelSelection getLevelSelection() {
		return levelSelection;
	}

	public void setLevelSelection(LevelSelection levelSelection) {
		this.levelSelection = levelSelection;
	}

	public float getASPECT_RATIO() {
		return ASPECT_RATIO;
	}

	public void setASPECT_RATIO(float aSPECT_RATIO) {
		ASPECT_RATIO = aSPECT_RATIO;
	}

	public Dimension getGraphicResolution() {
		return graphicResolution;
	}

	public void setGraphicResolution(Dimension graphicResolution) {
		this.graphicResolution = graphicResolution;
	}

	public Dimension getWindowResolution() {
		return windowResolution;
	}

	public void setWindowResolution(Dimension windowResolution) {
		this.windowResolution = windowResolution;
	}

	public int getACTIVE_PANEL() {
		return ACTIVE_PANEL;
	}

	public void setACTIVE_PANEL(int aCTIVE_PANEL) {
		ACTIVE_PANEL = aCTIVE_PANEL;
	}
}