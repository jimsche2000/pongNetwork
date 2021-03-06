package hauptmenu;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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
	private CardLayout cl = new CardLayout();
	private ServerControlPanel serverControlPanel;
	private ServerSpectatorPanel serverLiveGamePanel;
	private ClientControlPanel clientControlPanel;
	private ClientLiveGamePanel clientLiveGamePanel;
	private MainMenu mainMenu;
	private LevelSelection levelSelection;
	private Credits credits;
	private MultiPlayer multiPlayer;
	private SinglePlayer singleplayer; // Offline/SinglePlayer
//	private Point MOUSE_LOCATION = new Point(500, 500);
	private Image cursorImage = ImageLoader.loadImage("edit_icon3.png", 50, 50);// giphy.gif
	private Image frameIcon = ImageLoader.loadImage("PongIcon.png", 64, 64);
	private FontLoader flo;
	private ServerMainThread hostServer;
	private ServerConsole hostServerConsole = new ServerConsole(this);
	private ClientMainThread clientThread;
	private ClientChat clientChat;
	private boolean showClientNetworkInformation = false;
	private boolean showServerNetworkInformation = false;
	private boolean updateUserListOnServer = false;
	private boolean updateUserListOnClient = false;
	private float ASPECT_RATIO = 1.0f;
	private Dimension graphicResolution;
	private Dimension windowResolution;
	private Insets graphicInsets;
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

//    private BufferedImage cursorImage;
//    private Toolkit kit;

	public PongFrame() {
		super("Pong Projekt 2018 - EAIT6");
		JOptionPane pane = new JOptionPane("Das Spiel l�dt...", JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = pane.createDialog(null, "Pong-Network-2019");
		dialog.setModal(false);
		dialog.setVisible(true);
		/*
		 * Getting installed Fonts, which load fast, and show first Menu/Screen while
		 * special- font loads slowly from file
		 * 
		 * Font font; GraphicsEnvironment ge =
		 * GraphicsEnvironment.getLocalGraphicsEnvironment(); String[] arfonts =
		 * ge.getAvailableFontFamilyNames();
		 */
		flo = new FontLoader();
		GLOBAL_FONT = flo.loadFont("PressStart2P");

		Dimension fullScreenSize = new Dimension(1920, 1080); // for testing on different resolutions
//		Dimension fullScreenSize = new Dimension(1680, 1050); // for testing on different resolutions
//		Dimension fullScreenSize = new Dimension(1366, 768); // for testing on different resolutions
//		Dimension fullScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

		windowResolution = fullScreenSize;
		float[] screenSize = { (float) fullScreenSize.getWidth(), (float) fullScreenSize.getHeight() };
		float[] programmedSize = { 1920.0f, 1080.0f };
		ASPECT_RATIO = aspect_correct_scale_for_rect(screenSize, programmedSize);
		graphicResolution = new Dimension((int) (1920 * ASPECT_RATIO), (int) (1080 * ASPECT_RATIO));
		System.out.println("PR 1920,1080; ASP_RATIO: " + ASPECT_RATIO + "; Resolution: " + (graphicResolution.width)
				+ "x" + (graphicResolution.height));

		graphicInsets = setInsets(fullScreenSize, graphicResolution);

		clientChat = new ClientChat(this);

		setLayout(new BorderLayout());
		setSize(fullScreenSize);

		componentPanel = new JPanel();
//		{
//			
//			public void paint(Graphics g) {
//				super.paint(g);
////				super.paintComponents(g);
//				System.out.println("REPAINTING CURSOR: "+cursorImage+" POS: "+MOUSE_LOCATION);
//				g.drawImage(cursorImage, MOUSE_LOCATION.x, MOUSE_LOCATION.y, null);
//				repaint();
//			}
//		};
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
//		cl.show(componentPanel, "clientLiveGamePanel");
		ACTIVE_PANEL = this.MAIN_MENU;

		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(this.getX(), this.getY()),
				"cursor"));
//		setCursor(null);
		// unsichtbarkeit des courses
//		int[] pixels = new int[16 * 16];
//		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
//		Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0),
//				"invisibleCursor");
//		this.setCursor(transparentCursor);

//        try {
//            kit = Toolkit.getDefaultToolkit();
//            cursorImage = ImageLoader.loadBufferedImage("edit_icon3.png");
//            for (int i = 0; i < cursorImage.getHeight(); i++) {
//                int[] rgb = cursorImage.getRGB(0, i, cursorImage.getWidth(), 1, null, 0, cursorImage.getWidth() * 4);
//                for (int j = 0; j < rgb.length; j++) {
//                    int alpha = (rgb[j] >> 24) & 255;
//                    if (alpha < 128) {
//                        alpha = 0;
//                    } else {
//                        alpha = 255;
//                    }
//                    rgb[j] &= 0x00ffffff;
//                    rgb[j] = (alpha << 24) | rgb[j];
//                }
//                cursorImage.setRGB(0, i, cursorImage.getWidth(), 1, rgb, 0,
//                        cursorImage.getWidth() * 4);
//            }
//            Cursor cursor = kit.createCustomCursor(
//                    cursorImage, new Point(0, 0), "CustomCursor");
//
//            setCursor(cursor);
//
//        } catch (Exception exp) {
//            exp.printStackTrace();
//        }

		setBackground(Color.black);
		setIconImage(frameIcon);
		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		setVisible(true);
//		showOnScreen(1, this);
		dialog.setVisible(false);

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());

	}

	private Insets setInsets(Dimension fullScreenSize, Dimension graphicResolution) {
		int left = 0, top = 0, bottom = 0, right = 0;

		if (fullScreenSize.width > graphicResolution.width) { // Wenn links und rechts r�nder am Bildschirm sind
			int leftToRight = fullScreenSize.width - graphicResolution.width;
			left = leftToRight / 2;
			right = leftToRight / 2;
		}
		if (fullScreenSize.height > graphicResolution.height) { // Wenn oben und unten R�nder am Bildschirm sind
			int topToDown = fullScreenSize.height - graphicResolution.height;
			top = topToDown / 2;
			bottom = topToDown / 2;
		}
		return new Insets(top, left, bottom, right);
	}

//	public void paintComponent(Graphics g) {
//		super.paintComponents(g);
//		g.drawImage(cursorImage, MOUSE_LOCATION.x, MOUSE_LOCATION.y - cursorImage.getHeight(null), null);
//
////		System.out.println("Frame double Buffered? "+isDoubleBuffered());
//	}

	boolean firstTime = true;

	public void showPane(int ID) {
		PongFrame frame = this;
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {		
				switch (ID) {
			case SERVER_CONTROL_PANEL:
				ACTIVE_PANEL = SERVER_CONTROL_PANEL;
				updateUserListOnServer = true;
				serverControlPanel.reload();
				cl.show(componentPanel, "serverControlPanel");
				break;
			case SERVER_LIVE_GAME_PANEL:
				ACTIVE_PANEL = SERVER_LIVE_GAME_PANEL;
				setConsoleToPanel(SERVER_LIVE_GAME_PANEL);
				cl.show(componentPanel, "serverLiveGamePanel");
				break;
			case CLIENT_CONTROL_PANEL:
				ACTIVE_PANEL = CLIENT_CONTROL_PANEL;
				cl.show(componentPanel, "clientControlPanel");
				break;
			case CLIENT_LIVE_GAME_PANEL:
				ACTIVE_PANEL = CLIENT_LIVE_GAME_PANEL;
				cl.show(componentPanel, "clientLiveGamePanel");
//				clientLiveGamePanel.repaint();
				clientLiveGamePanel.wakeUp();
				break;
			case MAIN_MENU:
				ACTIVE_PANEL = MAIN_MENU;
				cl.show(componentPanel, "mainMenu");
				break;
			case LEVEL_SELECTION:
				ACTIVE_PANEL = LEVEL_SELECTION;
				cl.show(componentPanel, "levelSelection");
				break;
			case CREDITS:
				ACTIVE_PANEL = CREDITS;
				cl.show(componentPanel, "credits");
				break;
			case MULTI_PLAYER:
				ACTIVE_PANEL = MULTI_PLAYER;
				cl.show(componentPanel, "multiPlayer");
				if (multiPlayer.isJoinServerPanelActive()) {
					if (firstTime) {
						clientThread = new ClientMainThread(frame);
						clientThread.startSearchForServer();
						firstTime = false;
					} else {
						getClientThread().setShouldSearchForServer(true);
					}
				}
				break;
			case SINGLEPLAYER:
				ACTIVE_PANEL = SINGLEPLAYER;
				cl.show(componentPanel, "singleplayer");
				break;
			default:
				break;
			}
				
			}
		});
		

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

//	public ServerControlPanel getServerControlPanel() {
//		return serverControlPanel;
//	}
//
//	public void setServerControlPanel(ServerControlPanel serverControlPanel) {
//		this.serverControlPanel = serverControlPanel;
//	}

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

	public Insets getGraphicInsets() {
		return graphicInsets;
	}

	public void setGraphicInsets(Insets graphicInsets) {
		this.graphicInsets = graphicInsets;
	}

	public ClientControlPanel getClientControlPanel() {
		return clientControlPanel;
	}

	public void setClientControlPanel(ClientControlPanel clientControlPanel) {
		this.clientControlPanel = clientControlPanel;
	}

	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {

			if (getACTIVE_PANEL() == CLIENT_LIVE_GAME_PANEL) {

				if (e.getID() == KeyEvent.KEY_PRESSED) {
					getClientLiveGamePanel().getSpielfeld().keyPressed(e);
				} else if (e.getID() == KeyEvent.KEY_RELEASED) {
					getClientLiveGamePanel().getSpielfeld().keyReleased(e);
				} else if (e.getID() == KeyEvent.KEY_TYPED) {
					getClientLiveGamePanel().getSpielfeld().keyTyped(e);
				}
			} else if (getACTIVE_PANEL() == SINGLEPLAYER) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					getSinglePlayer().keyPressed(e);
				} else if (e.getID() == KeyEvent.KEY_RELEASED) {
					getSinglePlayer().keyReleased(e);
				} else if (e.getID() == KeyEvent.KEY_TYPED) {
					getSinglePlayer().keyTyped(e);
				}
			}
			return false;
		}
	}
}