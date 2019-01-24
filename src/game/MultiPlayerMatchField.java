package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.MemoryImageSource;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingConstants;

import gui.MenuLabel;
import hauptmenu.PongFrame;
import multiplayer.datapacks.GameCountdownData;
import multiplayer.datapacks.PongLocationData;
import multiplayer.datapacks.PongSliderData;
import pause.PauseAction;
import pause.PausePanelMultiPlayer;
import pongtoolkit.ImageLoader;

public class MultiPlayerMatchField extends JPanel implements KeyListener {

	private static final long serialVersionUID = 8592435655761040011L;
	private JPanel contentPane;
	private PauseAction pauseAction;
//	private JLabel sLinks, sRechts;
	private Schlag sLinks; // JLabel
	private Schlag sRechts; // JLabel
//	private MenuLabel winner;
	private MenuLabel scoreLabel;
	private MenuLabel countdown; //And Status?
	private JLabel playerLeftLabel, playerRightLabel;
	private Ball ball;
//	private PhysicData physicData;
	private boolean pauseMenu, escapeReleased;
	private PongLocationData startData;

	public final int SPECTATOR = -1;
	public final int PLAYER_RIGHT = 0;
	public final int PLAYER_LEFT = 1;
	public boolean richtungx;
	public boolean richtungy;
	public boolean xnull;
	public boolean ynull;
	private float PLAYER_Y_POS = 0f;
	private boolean upLeft, downLeft, upRight, downRight;
	private boolean playerRightActivated = false;
	private boolean playerLeftActivated = false;
	private ImageIcon schlägerIcon;
	private ImageIcon schlägerGREENIcon;
//	private Dimension sliderSize;
	private MyThread t;
	private int periodendauer = 5; // Milisekunden
	private boolean gestartet = false, firsttime = true;
	private PongFrame pongFrame;

	private int serversGameID = -1;

	private PhysicData physicData;
	
	public MultiPlayerMatchField(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
//		Dimension newSize = new Dimension(pongFrame.getSize().width, pongFrame.getSize().height);
		Dimension preferredSize = pongFrame.getGraphicResolution();
		physicData = new PhysicData(50, 10, 200);
//		physicData = new PhysicData(50, 10, 200);

//		int ballSize = 50;
//		sliderSize = new Dimension(10, 200);
//		Dimension frameSize = new Dimension(1920, 1080);

		startData = new PongLocationData();
		startData.setBall(new Point((1920 - physicData.getBallSize()) / 2, (1080 /2) - physicData.getBallSize()));
		startData.setSliderLeft(new Point(10, (1080 - physicData.getPlayerOneHeight()) / 2));
		startData.setSliderRight(new Point(1920 - 10 - physicData.getPlayerTwoWidth(), (1080 - physicData.getPlayerTwoHeight()) / 2));

		this.pauseAction = new PauseAction(pongFrame);

		pauseMenu = false;
		escapeReleased = true;

		this.setLayout(null);
		this.setSize(preferredSize);
		this.setLocation(0, 0);

		ball = new Ball();
		ball.setBounds(Math.round(startData.getBall().x * pongFrame.getASPECT_RATIO()),
				Math.round(startData.getBall().y * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getBallSize() * pongFrame.getASPECT_RATIO()), Math.round(physicData.getBallSize() * pongFrame.getASPECT_RATIO()));

//		centerPhysicObjects(false);

//		schlagL = new JLabel();
//		schlagR = new JLabel();
//		punktestand = new JLabel("0 : 0");

		sLinks = new Schlag(Math.round(physicData.getBallSize() * pongFrame.getASPECT_RATIO()), // Set Y from physics *
				// aspectratio, as in
				// mythread down there
				Math.round((preferredSize.height - Math.round(physicData.getPlayerOneHeight() * pongFrame.getASPECT_RATIO())) / 2),
				Math.round(physicData.getPlayerOneWidth() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getPlayerOneHeight() * pongFrame.getASPECT_RATIO())); // Set
		// JLabel positions from physicsData times Aspect Ratio to fit the screens
		// Resolution
		sRechts = new Schlag(
				Math.round((1920 - physicData.getBallSize() - Math.round(physicData.getPlayerTwoWidth() * pongFrame.getASPECT_RATIO()))
						* pongFrame.getASPECT_RATIO()),
				Math.round((preferredSize.height - Math.round(physicData.getPlayerTwoHeight() * pongFrame.getASPECT_RATIO())) / 2),
				Math.round(physicData.getPlayerTwoWidth() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getPlayerTwoHeight() * pongFrame.getASPECT_RATIO())); // Set

		schlägerIcon = ImageLoader.loadIcon("Schläger.png", Math.round(physicData.getPlayerTwoWidth() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getPlayerTwoHeight() * pongFrame.getASPECT_RATIO()));
		schlägerGREENIcon = ImageLoader.loadIcon("Schläger_GREEEEEEEEEEEEEEEEEEEEEN.png",
				Math.round(physicData.getPlayerTwoWidth() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getPlayerTwoHeight() * pongFrame.getASPECT_RATIO()));

//		winner = new MenuLabel(pongFrame, " ");
		scoreLabel = new MenuLabel(pongFrame, "0 : 0");
		countdown = new MenuLabel(pongFrame, " ");

		playerLeftLabel = new JLabel("Spieler Links");
		playerRightLabel = new JLabel("Spieler Rechts");

		// ---------------

//		winner.setBounds(Math.round(260 * pongFrame.getASPECT_RATIO()), Math.round(900 * pongFrame.getASPECT_RATIO()),
//				Math.round(1400 * pongFrame.getASPECT_RATIO()), Math.round(100 * pongFrame.getASPECT_RATIO()));
//		winner.setFont(pongFrame.getGLOBAL_FONT().deriveFont(40 * pongFrame.getASPECT_RATIO()));
//		winner.setOpaque(false);
//		winner.setAlignment(winner.ALIGN_MID);
//		winner.setDrawBackground(false);
//		scoreLabel.setBounds(0, Math.round(10 * pongFrame.getASPECT_RATIO()), preferredSize.width,
//				Math.round(50 * pongFrame.getASPECT_RATIO()));
		scoreLabel.setLocation(0, Math.round(10 * pongFrame.getASPECT_RATIO()));
		scoreLabel.setSize(preferredSize.width, Math.round(50 * pongFrame.getASPECT_RATIO()));
		scoreLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(50 * pongFrame.getASPECT_RATIO()));
		scoreLabel.setOpaque(false);
		scoreLabel.setAlignment(scoreLabel.ALIGN_MID);
		scoreLabel.setDrawBackground(false);
		scoreLabel.setText("0 : 0");
//		countdown.setBounds(Math.round(600 * pongFrame.getASPECT_RATIO()), // X
//				Math.round(100 * pongFrame.getASPECT_RATIO()), // Y
//				Math.round(750 * pongFrame.getASPECT_RATIO()), // WIDTH
//				Math.round(300 * pongFrame.getASPECT_RATIO()));// HEIGHT
		countdown.setLocation(Math.round(600 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO()));
		countdown.setSize(Math.round(750 * pongFrame.getASPECT_RATIO()),
				Math.round(300 * pongFrame.getASPECT_RATIO()));
//		countdown.setText("Warten auf deinen Mitspieler..");
//		statusOrCountdown.setFont(pongFrame.getGLOBAL_FONT().deriveFont(40 * pongFrame.getASPECT_RATIO()));
		countdown.setFont(pongFrame.getGLOBAL_FONT().deriveFont(100 * pongFrame.getASPECT_RATIO()));
		countdown.setHorizontalAlignment(SwingConstants.CENTER);
		countdown.setAlignment(countdown.ALIGN_MID);
		countdown.setOpaque(false);
		countdown.setDrawBackground(false);

//		playerLeftLabel.setBounds(Math.round(75 * pongFrame.getASPECT_RATIO()), 0,
//				Math.round(500 * pongFrame.getASPECT_RATIO()), Math.round(100 * pongFrame.getASPECT_RATIO()));
		playerLeftLabel.setLocation(Math.round(75 * pongFrame.getASPECT_RATIO()), 0);
		playerLeftLabel.setSize(Math.round(500 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO()));
		playerLeftLabel.setPreferredSize(new Dimension(Math.round(500 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));
		playerLeftLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(25 * pongFrame.getASPECT_RATIO()));
		playerLeftLabel.setHorizontalAlignment(SwingConstants.LEFT);
		playerLeftLabel.setVerticalAlignment(SwingConstants.TOP);
		playerLeftLabel
				.setBorder(BorderFactory.createEmptyBorder(Math.round(10 * pongFrame.getASPECT_RATIO()), 0, 0, 0));
		playerLeftLabel.setForeground(Color.black);
		playerLeftLabel.setOpaque(false);

		playerRightLabel.setBounds(Math.round((1920 - 575) * pongFrame.getASPECT_RATIO()), 0,
				Math.round(500 * pongFrame.getASPECT_RATIO()), Math.round(100 * pongFrame.getASPECT_RATIO()));
		playerRightLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(25 * pongFrame.getASPECT_RATIO()));
		playerRightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		playerRightLabel.setVerticalAlignment(SwingConstants.TOP);
		playerRightLabel
				.setBorder(BorderFactory.createEmptyBorder(Math.round(10 * pongFrame.getASPECT_RATIO()), 0, 0, 0));
		playerRightLabel.setForeground(Color.black);
		playerRightLabel.setOpaque(false);

		// ---------------
//		schlagL.setBounds(10, (newSize.height - 200) / 2, 10, 200);
//		schlagL.setBackground(Color.BLACK);
//		schlagL.setIcon(schlägerIcon);
//
//		schlagR.setBounds(newSize.width - 40, (newSize.height - 200) / 2, 10, 200);
//		schlagR.setBackground(Color.BLACK);
//		schlagR.setIcon(schlägerIcon);

//		ball.setSize(50, 50);
//		ball.setLocation((pongFrame.getWidth() - ball.getWidth()) / 2, (pongFrame.getHeight() - ball.getHeight()) / 2);

//		punktestand.setBounds((preferredSize.width - 200) / 2, 10, 200, 50);
//		punktestand.setHorizontalAlignment(SwingConstants.CENTER);
//		punktestand.setText("0 : 0");
//		punktestand.setFont(new Font("Arial", Font.BOLD, 30));

		contentPane = new JPanel();
//		contentPane.setLocation(0, (pongFrame.getWindowResolution().height - preferredSize.height) / 2);
		contentPane.setLocation(0, 0);
		contentPane.setSize(preferredSize);
		contentPane.setPreferredSize(preferredSize);
		contentPane.setLayout(null); // Muss so sein....
//		contentPane.add(winner);
		contentPane.add(scoreLabel);
		contentPane.add(countdown);
		contentPane.add(playerLeftLabel);
		contentPane.add(playerRightLabel);
		contentPane.add(ball);
		contentPane.add(sLinks);
		contentPane.add(sRechts);
		contentPane.setBackground(Color.white);
		this.setBackground(Color.black);
		this.add(contentPane);
//		
//		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
//		manager.addKeyEventDispatcher(new MyDispatcher());

		// unsichtbarkeit des courses
//		int[] pixels = new int[16 * 16];
//		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
//		Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0),
//				"invisibleCursor");
//		this.setCursor(transparentCursor);
//		
	}

	public JLabel getSchlagL() {
		return sLinks;
	}

	public JLabel getSchlagR() {
		return sRechts;
	}

//	public JLabel getPunktestand() {
//		return punktestand;
//	}
//
//	public void setPunktestand(JLabel punktestand) {
//		this.punktestand = punktestand;
//	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		if (playerLeftActivated) {
			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				upLeft = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				downLeft = false;
			}
		}
		if (playerRightActivated) {
			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				upRight = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				downRight = false;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			escapeReleased = true;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println("Client>< Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()) + " pauseMenu: " + pauseMenu);
		if (playerLeftActivated) {
			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				upLeft = true;
			}

			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				downLeft = true;
			}
		}
		if (playerRightActivated) {
			if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
				upRight = true;
			}

			if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
				downRight = true;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (escapeReleased) {
				escapeReleased = false;
				if (pauseMenu) {
					pauseMenu = false;
					PausePanelMultiPlayer panel = (PausePanelMultiPlayer) pauseAction
							.getPausePanel(pauseAction.ID_MULTIPLAYER_PANEL);
					panel.resume();
					this.repaint();
				} else {
					pauseMenu = true;
					pauseAction.action(pauseAction.ID_MULTIPLAYER_PANEL);
				}
			}
		}
	}

	public void configSF(int player) {

		playerRightActivated = false;
		playerLeftActivated = false;
		sLinks.setIcon(schlägerIcon);
		sRechts.setIcon(schlägerIcon);
		switch (player) {
		case PLAYER_RIGHT:
			playerRightActivated = true;
			sRechts.setIcon(schlägerGREENIcon);
			break;
		case PLAYER_LEFT:
			playerLeftActivated = true;
			sLinks.setIcon(schlägerGREENIcon);
			break;
		default:
			break;
		}
//		sendLocationsToServer();
//		laufen();
	}

	public void stopGameAndShowResult(String resultMessage) {

		playerRightActivated = false;
		playerLeftActivated = false;

		gestartet = false;
		
//		JOptionPane pane = new JOptionPane(resultMessage, JOptionPane.INFORMATION_MESSAGE);
//		JDialog dialog = pane.createDialog(null, "Pong-Network-2019");
		JOptionPane.showConfirmDialog(
                pongFrame
        , resultMessage
        , "Pong-Network-2019"
        , JOptionPane.DEFAULT_OPTION
        , JOptionPane.INFORMATION_MESSAGE);
		
		pongFrame.showPane(pongFrame.CLIENT_CONTROL_PANEL);
		
	}
	
	private void sendLocationsToServer() {
		PongSliderData sliderData = new PongSliderData();
		sliderData.setGameID(serversGameID);
		sliderData.setOrientation(sliderData.SPECTATOR);
//		Point p = lastPoint;
		if (playerRightActivated) {
			sliderData.setOrientation(sliderData.RIGHT);
			sliderData.setSlider(
					new Point(Math.round(sRechts.getX() / pongFrame.getASPECT_RATIO()), Math.round(PLAYER_Y_POS)));
		} else if (playerLeftActivated) {
			sliderData.setOrientation(sliderData.LEFT);
			sliderData.setSlider(
					new Point(Math.round(sLinks.getX() / pongFrame.getASPECT_RATIO()), Math.round(PLAYER_Y_POS)));
		}
//		if (!p.equals(lastPoint)) //Nur bei TCP/IP notwendig, um spam zu verhindern
		if (sliderData.getOrientation() != sliderData.SPECTATOR) {
			pongFrame.getClientThread().sendMultiPlayerPositionsToServer(sliderData);
		}
//			pongFrame.getClientThread()
//					.sendMessageToServer(pongFrame.getClientThread().getNO_CHAT_MESSAGE()
//							+ pongFrame.getClientThread().getIN_GAME_POSITIONS() + "{ORIENTATION=" + orientation
//							+ "}{X=" + p.x + "}{Y=" + p.y + "}");
//		p = lastPoint;
	}

	public void updateLocations(PongLocationData pLD) {
//		countdown.setText("");
		serversGameID = pLD.getGAME_ID();
		if (!playerRightActivated) {
			sRechts.setLocation(Math.round(pLD.getSliderRight().x * pongFrame.getASPECT_RATIO()),
					Math.round(pLD.getSliderRight().y * pongFrame.getASPECT_RATIO()));
		}
		if (!playerLeftActivated) {
			sLinks.setLocation(Math.round(pLD.getSliderLeft().x * pongFrame.getASPECT_RATIO()),
					Math.round(pLD.getSliderLeft().y * pongFrame.getASPECT_RATIO()));
//			sLinks.setLocation(pLD.getSliderLeft());
		}
		ball.setLocation(Math.round(pLD.getBall().x * pongFrame.getASPECT_RATIO()),
				Math.round(pLD.getBall().y * pongFrame.getASPECT_RATIO()));
		scoreLabel.setText(pLD.getScore());
	}

	public void laufen() {
		if (!gestartet) {
			if (firsttime) {
				firsttime = false;
				t = new MyThread();
				t.start();
			}
			gestartet = true;
		}
		this.repaint();
	}

	class MyThread extends Thread {
		public void run() {

			while (true) {

				if (gestartet) {
//					int xLeft = sLinks.getX();
//					int yLeft = sLinks.getY();
//					int xRight = sRechts.getX();
//					int yRight = sRechts.getY();

					int schrittweite = 7;

					if (playerLeftActivated) {
//						int xLeft = Math.round(sLinks.getX() / pongFrame.getASPECT_RATIO());
//						double yLeft = PLAYER_Y_POS;
						if (upLeft) {
							if (PLAYER_Y_POS - schrittweite <= 0) {
								PLAYER_Y_POS = 0;
//								sLinks.setLocation(xLeft, yLeft);

							} else {
								PLAYER_Y_POS = PLAYER_Y_POS - schrittweite;
//								sLinks.setLocation(xLeft, yLeft);
							}
						}

						if (downLeft) {
							if (PLAYER_Y_POS + schrittweite + physicData.getPlayerOneHeight() >= 1080) {
								PLAYER_Y_POS = 1080 - physicData.getPlayerOneHeight();
//								sLinks.setLocation(xLeft, yLeft);

							} else {
								PLAYER_Y_POS = PLAYER_Y_POS + schrittweite;
//								sLinks.setLocation(xLeft, yLeft);
							}
						}
						sLinks.setLocation(
								new Point(sLinks.getX(), Math.round(PLAYER_Y_POS * pongFrame.getASPECT_RATIO())));
					}

					if (playerRightActivated) {

//						double yRight = PLAYER_Y_POS;
						if (upRight) {
							if (PLAYER_Y_POS - schrittweite <= 0) {
								PLAYER_Y_POS = 0;
//								sLinks.setLocation(xRight, yRight);

							} else {
								PLAYER_Y_POS = PLAYER_Y_POS - schrittweite;
//								sLinks.setLocation(xRight, yRight);
							}
						}

						if (downRight) {
							if (PLAYER_Y_POS + schrittweite + physicData.getPlayerTwoHeight() >= 1080) {
								PLAYER_Y_POS = 1080 - physicData.getPlayerTwoHeight();
//								sLinks.setLocation(xRight, yRight);

							} else {
								PLAYER_Y_POS = PLAYER_Y_POS + schrittweite;
//								sLinks.setLocation(xRight, yRight);
							}
						}
						sRechts.setLocation(
								new Point(sRechts.getX(), Math.round(PLAYER_Y_POS * pongFrame.getASPECT_RATIO())));
					}
					sendLocationsToServer();
				}

				try {
					sleep(periodendauer);
//					repaint();
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
//	long timestamp_lastPressed = System.currentTimeMillis();
//	private class MyDispatcher implements KeyEventDispatcher {
//		@Override
//		public boolean dispatchKeyEvent(KeyEvent e) {
//
//			if (pongFrame.getACTIVE_PANEL() == pongFrame.CLIENT_LIVE_GAME_PANEL) {
////					if(System.currentTimeMillis() - timestamp_lastPressed > 500) { //500ms break between pressing
////						timestamp_lastPressed = System.currentTimeMillis();
//						if (e.getID() == KeyEvent.KEY_PRESSED) {
//							keyPressed(e);
//						} else if (e.getID() == KeyEvent.KEY_RELEASED) {
//							keyReleased(e);
//						} else if (e.getID() == KeyEvent.KEY_TYPED) {
//							keyTyped(e);
//						}
////					}
//			}
//			return false;
//		}
//	}
	public void updateCountdown(GameCountdownData gCD) {
		String time = gCD.getNowtime();
		if(gCD.getNowtime().equals("STOP")) {
			countdown.setText("");
		}else {
			countdown.setText(time);
		}
	}

	public void refreshNames(String playerLeftName, String playerRightName) {
		this.playerLeftLabel.setText(playerLeftName);
		this.playerRightLabel.setText(playerRightName);
	}
}
