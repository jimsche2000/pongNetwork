package hauptmenu;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import multiplayer.client.ClientChat;
import multiplayer.client.ClientControlPanel;
import multiplayer.client.ClientLiveGamePanel;
import multiplayer.client.ClientMainThread;
import multiplayer.server.ServerConsole;
import multiplayer.server.ServerControlPanel;
import multiplayer.server.ServerMainThread;
import multiplayer.server.ServerSpectatorPanel;
import game.SinglePlayer;
import pongtoolkit.FontLoader;
import pongtoolkit.ImageLoader;

public class PongFrame extends JFrame{

	private static final long serialVersionUID = 1L;
//	private int width = 1366, height = 768;// width: 1165
	private int width = 1920, height = 1080;


	private JPanel componentPanel, midButtonPanel;
//	private JLabel title;
	private Font GLOBAL_FONT;
	private ServerControlPanel serverControlPanel;
	private ServerSpectatorPanel serverLiveGamePanel;
	private ClientControlPanel clientControlPanel;
	private ClientLiveGamePanel clientLiveGamePanel;
	private MainMenu mainMenu;
//	private SinglePlayer singlePlayer;
	private LevelSelection levelSelection;
	private Credits credits;
	private MultiPlayer multiPlayer;
	private SinglePlayer singleplayer; //Offline/SinglePlayer
	private Point MOUSE_LOCATION = new Point(500,500);
	private Image cursorImage = ImageLoader.loadImage("edit_icon2.png", 50, 50);//giphy.gif
	private FontLoader flo;
	
	//AUS PONGMAIN:
	private ServerMainThread hostServer;
	private ServerConsole hostServerConsole = new ServerConsole(this);
	private ClientMainThread clientThread;
	private ClientChat clientChat = new ClientChat(this);
	private boolean showClientNetworkInformation = false;
	private boolean showServerNetworkInformation = false;
	private boolean updateUserListOnServer = false;
	private boolean updateUserListOnClient = false;
	private CardLayout cl = new CardLayout();

//	public final int MID_BUTTON_PANEL = 0;
//	public final int CREATE_SERVER_PANEL = 1;
//	public final int JOIN_SERVER_PANEL = 2;
	public final int SERVER_CONTROL_PANEL = 3;
	public final int SERVER_LIVE_GAME_PANEL = 4;
	public final int CLIENT_CONTROL_PANEL = 5;
	public final int CLIENT_LIVE_GAME_PANEL = 6;
	public final int MAIN_MENU = 7;
	public final int SINGLEPLAYER = 8;
	public final int MULTI_PLAYER = 9; //Mid-Button-Panel
	public final int CREDITS = 10;
	public final int LEVEL_SELECTION = 11;
	public final int TITLE_HEIGHT = 50;
	private float ASPECT_RATIO = 1.0f;
	private Dimension graphicResolution;
	
	public PongFrame() {
		super("Pong Projekt 2018 - EAIT6");
        JOptionPane pane = new JOptionPane("Das Spiel lädt...", JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, "Pong-Network-2018");
        dialog.setModal(false);
        dialog.setVisible(true);
        
		flo = new FontLoader();
		GLOBAL_FONT = flo.loadFont("PressStart2P");
		//hammer wichtig
		Dimension fullScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		int gcd = gcd(fullScreenSize.width, fullScreenSize.height); //greatest common divisor // größter gemeinsamer teiler
//		int har = fullScreenSize.width / gcd; // horizontal aspect ratio
//		int var = fullScreenSize.height / gcd; //vertical aspect ratio
//		System.out.println("Aspect ratio is "+har+":"+var);
		
		
//		float[] screenSize = {1920.0f,1200.0f};
		float[] screenSize = {(float) fullScreenSize.getWidth(),(float) fullScreenSize.getHeight()};
		float[] programmedSize = {1920.0f,1080.0f};
		ASPECT_RATIO = aspect_correct_scale_for_rect(screenSize, programmedSize);
		graphicResolution = new Dimension((int)(1920*ASPECT_RATIO), (int)(1080*ASPECT_RATIO));
		System.out.println("PR 1920,1080; ASP_RATIO: "+ASPECT_RATIO+"; Resolution: "+(graphicResolution.width)+"x"+(graphicResolution.height));
		
		setSize(fullScreenSize);
//		setSize(width, height);
		//CENTER IT: 
				// int y = (screenHeight - newHeight)/2;
				// int x = (screenWidth - newWidth)/2;
		componentPanel = new JPanel();
		componentPanel.setLayout(cl);
//		componentPanel.setSize(graphicResolution);
//		componentPanel.setLocation((fullScreenSize.width - graphicResolution.width) /2, (fullScreenSize.height - graphicResolution.height) /2); //Center ComponentPanel
//		System.out.println("X: "+((fullScreenSize.width - graphicResolution.width) /2)+"; Y: "+((fullScreenSize.height - graphicResolution.height) /2));
		componentPanel.setSize(1920, 1080);
		componentPanel.setLocation(0, 0);
		// Buttons with Action
		midButtonPanel = new JPanel();
		midButtonPanel.setAlignmentX(SwingConstants.CENTER);
		midButtonPanel.setAlignmentY(SwingConstants.CENTER);
		
//		title = new JLabel("<html>PONG  -  EAIT6</html>");
//		title.setPreferredSize(new Dimension(width, TITLE_HEIGHT));
//		title.setHorizontalAlignment(SwingConstants.CENTER);
//		title.setFont(new Font("Dialog", 1, 50));
//		title.setBackground(Color.black);

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

//		singlePlayer = new SinglePlayer(this);
//		singlePlayer.setAlignmentX(SwingConstants.CENTER);
//		singlePlayer.setAlignmentY(SwingConstants.CENTER);
		
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
		credits.setOpaque(false);

		componentPanel.add(midButtonPanel, "midButtonPanel");
		componentPanel.add(serverControlPanel, "serverControlPanel");
		componentPanel.add(serverLiveGamePanel, "serverLiveGamePanel");
		componentPanel.add(clientControlPanel, "clientControlPanel");
		componentPanel.add(clientLiveGamePanel, "clientLiveGamePanel");
		componentPanel.add(mainMenu, "mainMenu");
//		componentPanel.add(singlePlayer, "singlePlayer");
		componentPanel.add(levelSelection, "levelSelection");
		componentPanel.add(credits, "credits");
		componentPanel.add(multiPlayer, "multiPlayer");
		componentPanel.add(singleplayer, "singleplayer");

		this.add(componentPanel);
		cl.show(componentPanel, "mainMenu");
		this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(this.getX(),  this.getY()), "cursor"));
		setLayout(null);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(3);
		setUndecorated(true);
		setResizable(false);
		setVisible(true);
//		showOnScreen(1, this);

		dialog.setVisible(false);
	}
	
//	public void paint(Graphics g) {
//		Graphics2D g2d = (Graphics2D)g.create();
//		   g2d.scale(2.5, 2.5);
//		super.paintComponents(g2d);
//		g2d.drawImage(cursorImage, MOUSE_LOCATION.x, MOUSE_LOCATION.y-cursorImage.getHeight(null), null);
//	
//	}
	public void paintComponent(Graphics g) {
		g.drawImage(cursorImage, MOUSE_LOCATION.x, MOUSE_LOCATION.y-cursorImage.getHeight(null), null);

	}
	
	boolean firstTime = true;

//	public void setTitleBackgroundTransparent(boolean transparent) {
//		
//		title.setOpaque(!transparent);
//		title.repaint();
//	}
	
	public void showPane(int ID) {

//		setTitleBackgroundTransparent(true);
		
		switch (ID) {
			case SERVER_CONTROL_PANEL:
	//			System.out.println("Server" + PongMain.updateUserListOnServer);
	//			setConsoleToPanel(SERVER_CONTROL_PANEL);
				updateUserListOnServer = true;
				serverControlPanel.reload();
				cl.show(componentPanel, "serverControlPanel");
	//			System.out.println("Server" + PongMain.updateUserListOnServer);
				break;
			case SERVER_LIVE_GAME_PANEL:
				setConsoleToPanel(SERVER_LIVE_GAME_PANEL);
				cl.show(componentPanel, "serverLiveGamePanel");
				break;
			case CLIENT_CONTROL_PANEL:
	//			setChatToPanel(CLIENT_CONTROL_PANEL);
				cl.show(componentPanel, "clientControlPanel");
	//			clientControlPanel.reload();
				break;
			case CLIENT_LIVE_GAME_PANEL:
	//			setTitleBackgroundTransparent(false);
	//			setChatToPanel(CLIENT_LIVE_GAME_PANEL);
				cl.show(componentPanel, "clientLiveGamePanel");
				break;
			case MAIN_MENU:
				cl.show(componentPanel, "mainMenu");
				break;
			case LEVEL_SELECTION:
				cl.show(componentPanel, "levelSelection");
				break;
			case CREDITS:
				cl.show(componentPanel, "credits");
				break;
			case MULTI_PLAYER:
				cl.show(componentPanel, "multiPlayer");
				if(multiPlayer.isJoinServerPanelActive()) {
					if (firstTime) {
						clientThread = new ClientMainThread(this);
						clientThread.startSearchForServer();
	//					getMultiPlayer().getJoinServerPanel().startAutoReloadServerTableList();
						firstTime = false;
					}else{
						getClientThread().setShouldSearchForServer(true);
					}
				}
				break;
			case SINGLEPLAYER:
					cl.show(componentPanel, "singleplayer");
//					singleplayer.startGame();
					//Der richtige Modus wird von der LevelSelection direkt an Game übergeben
				break;
			default:
				break;
		}
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
	
	/* For a rectangle inside a screen, get the scale factor that permits the rectangle
	   to be scaled without stretching or squashing. */
	private float aspect_correct_scale_for_rect( float screen[],  float rect[])
	{
	    float screenAspect = screen[0] / screen[1];
	    float rectAspect = rect[0] / rect[1];

	    float scaleFactor;
	    if (screenAspect > rectAspect)
	        scaleFactor = screen[1] / rect[1];
	    else
	        scaleFactor = screen[0] / rect[0];

	    return scaleFactor;
	}
	
	public static void showOnScreen( int screen, JFrame frame ) {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] gd = ge.getScreenDevices();
	    if( screen > -1 && screen < gd.length ) {
	        frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, gd[0].getDefaultConfiguration().getBounds().y + frame.getY());
	    } else if( gd.length > 0 ) {
	        frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, gd[0].getDefaultConfiguration().getBounds().y + frame.getY());
	    } else {
	        throw new RuntimeException( "No Screens Found" );
	    }
	}

//	public Dimension getFrameSize() {
//		return super.getSize();
//	}
//	public int getWidth() {
//		return width;
//	}
//	public void setWidth(int width) {
//		this.width = width;
//	}
//	public int getHeight() {
//		return height;
//	}
//	public void setHeight(int height) {
//		this.height = height;
//	}
	public Font getGLOBAL_FONT() {
		return GLOBAL_FONT;
	}
	public void setGLOBAL_FONT(Font gLOBAL_FONT) {
		GLOBAL_FONT = gLOBAL_FONT;
	}
//	public Dimension getSize() {
//		return getFrameSize();
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
//	public float getASPECT_RATIO() {
//		return ASPECT_RATIO;
//	}
//
//	public void setASPECT_RATIO(float aSPECT_RATIO) {
//		ASPECT_RATIO = aSPECT_RATIO;
//	}
//
//	public Dimension getGraphicResolution() {
//		return graphicResolution;
//	}
//
//	public void setGraphicResolution(Dimension graphicResolution) {
//		this.graphicResolution = graphicResolution;
//	}
}