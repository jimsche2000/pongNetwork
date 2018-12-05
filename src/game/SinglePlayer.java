package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.MemoryImageSource;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.MenuLabel;
import hauptmenu.PongFrame;
import pause.PauseAction;

@SuppressWarnings("serial")
public class SinglePlayer extends JPanel implements KeyListener {

	// Objects
	private PongFrame pongFrame;
	private PauseAction pauseAction;
	private JPanel contentPane;
	private PhysicData physicData;
	private Ball ball; // Panel, welches statt einem Quadrat ein gefülltes Oval Zeichnet
	private Schlag sLinks; // JLabel
	private Schlag sRechts; // JLabel
	private MenuLabel winner;
	private MenuLabel scoreLabel;
	private MenuLabel countdown;
	private JLabel playerLeftLabel, playerRightLabel;
	private Timer timer = new Timer();

	// Variables (Normal Datatypes)
	private boolean countdownActive = false;
	private boolean firstThreadStart;
	private boolean spielGestartet;
	private boolean pauseMenu;
	private volatile boolean shouldCountdown;
	public int scoreLinks; // Score des linken Spielers
	public int scoreRechts; // Score des rechten Spielers
	private int maxPunkte; // Hat einer der beiden Spieler soviele Punkte wie maxPunkte besagt, hat dieser
							// das Spiel gewonnen

	private int erfassungsbereichRechterBot;// Horizontaler Pixelfester-Wert, ab dem der Bot "zuguckt" und seine
											// Position verändert
	private int erfassungsbereichLinkerBot; // Horizontaler Pixelfester-Wert, ab dem der Bot "zuguckt" und seine
											// Position verändert
	private float botSpeed; // Geschwindigkeit des Bots

	private float leftPlayerSpeed, rightPlayerSpeed;
	// private int tempCountCollision; // Variable wird bei jeder Ball-Schläger
	// Kollision um einen erhöht, und bei Tor
	// zurückgesetzt
	private boolean upW, downS, upArrow, downArrow; // Variablen für die Funktion der Schläger und der Tastatur. (up,
													// down für W
	// und S; up1 und down1 für Pfeil-Hoch und Pfeil-Runter)
	private boolean x1, y1; // (Start)-Richtung des Balls.
	private float weitey, weitex; // Schrittweite des Balls jeweils in X- und in Y- Richtung
	private float boostSpeed; // Zusatzschnelligkeit des Balls, wird auf weitex und weitey addiert; erhöht
								// sich bei Kollisionen des Balls mit den Schlägern/Spielern, und wird bei einem
								// Punkt/Tor zurückgesetzt

	private double botFailFactor; // die schwierigkeit des bots (Letztendlich wie oft er stehenbleibt. von 0.0 -
									// 1.0; 0.1 ist er sehr schwer, 0.5 ist er leicht)
									// Nicht zu empfehlen die Schwierigkeit so einzustellen. Entweder überarbeiten,
									// oder nur den erfassungsbereich verändern
	private boolean winkel, // Spezial-Modus mit verschiedenen Abprallwinkeln/geschwindigkeiten
			kl, kr, // Kollision Links/Rechts?!?!
			isRightPlayerBot, isLeftPlayerBot, impossibleMode; // Bot Einstellungen
	private float weitex1, weitey1, weitex2, weitey2, weitex3, weitey3; // Hardgecodete Werte für die verschiedenen
																		// Winkel
	private GameThread t;
	private int sleepTime = 5; // ms
	public final int EASY_MODE = 0;
	public final int MIDDLE_MODE = 1;
	public final int HARD_MODE = 2;
	public final int CUSTOM_MODE = 3;
	public int MODE = -1;
	private float boostStep = 0.1f;

	public SinglePlayer(PongFrame frame) {
		this.pongFrame = frame;
		Dimension preferredSize = pongFrame.getGraphicResolution();
		physicData = new PhysicData(50, 10, 200);
		this.pauseAction = new PauseAction(frame);
		this.setLayout(null);
		ball = new Ball();
		ball.setBounds(Math.round(physicData.getBallX() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getBallY() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getBallSize() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getBallSize() * pongFrame.getASPECT_RATIO())); // Sets all Bounds of the Ball from
																						// physicsData times Aspect
																						// Ratio to fit the screens
																						// Resolution
		centerPhysicObjects(false);
		// JLabel positions from physicsData times Aspect Ratio to fit the screens
		// Resolution
		sLinks = new Schlag(Math.round(physicData.getBallSize() * pongFrame.getASPECT_RATIO()), // Set Y from physics *
																								// aspectratio, as in
				// mythread down there
				Math.round((preferredSize.height
						- Math.round(physicData.getPlayerOneHeight() * pongFrame.getASPECT_RATIO())) / 2),
				Math.round(physicData.getPlayerOneWidth() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getPlayerOneHeight() * pongFrame.getASPECT_RATIO())); // Set
		// JLabel positions from physicsData times Aspect Ratio to fit the screens
		// Resolution
		sRechts = new Schlag(
				Math.round((1920 - 50 - Math.round(physicData.getPlayerTwoWidth() * pongFrame.getASPECT_RATIO()))
						* pongFrame.getASPECT_RATIO()),
				Math.round((preferredSize.height
						- Math.round(physicData.getPlayerTwoHeight() * pongFrame.getASPECT_RATIO())) / 2),
				Math.round(physicData.getPlayerTwoWidth() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getPlayerTwoHeight() * pongFrame.getASPECT_RATIO())); // Set

		winner = new MenuLabel(frame, "");
		scoreLabel = new MenuLabel(frame, "");
		countdown = new MenuLabel(frame, "");
		playerLeftLabel = new JLabel("Spieler Links");
		playerRightLabel = new JLabel("Spieler Rechts");
		// SetBounds(X,Y,WIDTH,HEIGHT);
		winner.setBounds(Math.round(260 * pongFrame.getASPECT_RATIO()), Math.round(900 * pongFrame.getASPECT_RATIO()),
				Math.round(1400 * pongFrame.getASPECT_RATIO()), Math.round(100 * pongFrame.getASPECT_RATIO()));
		winner.setFont(pongFrame.getGLOBAL_FONT().deriveFont(40 * pongFrame.getASPECT_RATIO()));
		winner.setOpaque(false);
		scoreLabel.setBounds(0, Math.round(10 * pongFrame.getASPECT_RATIO()), preferredSize.width,
				Math.round(50 * pongFrame.getASPECT_RATIO()));
		scoreLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(50 * pongFrame.getASPECT_RATIO()));
		scoreLabel.setOpaque(false);
		countdown.setBounds(Math.round(600 * pongFrame.getASPECT_RATIO()), // X
				Math.round(100 * pongFrame.getASPECT_RATIO()), // Y
				Math.round(750 * pongFrame.getASPECT_RATIO()), // WIDTH
				Math.round(300 * pongFrame.getASPECT_RATIO()));// HEIGHT
		countdown.setFont(pongFrame.getGLOBAL_FONT().deriveFont(100 * pongFrame.getASPECT_RATIO()));
		countdown.setHorizontalAlignment(SwingConstants.CENTER);
		countdown.setOpaque(false);
		
		playerLeftLabel.setBounds(Math.round(75*pongFrame.getASPECT_RATIO()), 0, Math.round(500*pongFrame.getASPECT_RATIO()), Math.round(100*pongFrame.getASPECT_RATIO()));
		playerLeftLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(25*pongFrame.getASPECT_RATIO()));
		playerLeftLabel.setHorizontalAlignment(SwingConstants.LEFT);
		playerLeftLabel.setVerticalAlignment(SwingConstants.TOP);
		playerLeftLabel.setBorder(BorderFactory.createEmptyBorder(Math.round(10*pongFrame.getASPECT_RATIO()), 0,0,0));
		playerLeftLabel.setForeground(Color.black);
		playerLeftLabel.setOpaque(false);
		playerRightLabel.setBounds(Math.round((1920 - 575)*pongFrame.getASPECT_RATIO()), 0, Math.round(500*pongFrame.getASPECT_RATIO()), Math.round(100*pongFrame.getASPECT_RATIO()));
		playerRightLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(25*pongFrame.getASPECT_RATIO()));
		playerRightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		playerRightLabel.setVerticalAlignment(SwingConstants.TOP);
		playerRightLabel.setBorder(BorderFactory.createEmptyBorder(Math.round(10*pongFrame.getASPECT_RATIO()), 0,0,0));
		playerRightLabel.setForeground(Color.black);
		playerRightLabel.setOpaque(false);
		
		contentPane = new JPanel();
		contentPane.setLocation(0, (pongFrame.getWindowResolution().height - preferredSize.height) / 2);
		contentPane.setSize(preferredSize);
		contentPane.setPreferredSize(preferredSize);
		contentPane.setLayout(null); // Muss so sein....
		contentPane.setBackground(Color.white);
		contentPane.add(winner);
		contentPane.add(scoreLabel);
		contentPane.add(countdown);
		contentPane.add(playerLeftLabel);
		contentPane.add(playerRightLabel);
		contentPane.add(ball);
		contentPane.add(sLinks);
		contentPane.add(sRechts);
		this.setBackground(Color.black);
		this.add(contentPane);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());
		// Configure Game
		configureGame(MIDDLE_MODE);
		firstThreadStart = true;
		shouldCountdown = false;

		// unsichtbarkeit des courses
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0),
				"invisibleCursor");
		this.setCursor(transparentCursor);
	}

	public void setNameLabel(String leftName, String rightName) {
		playerLeftLabel.setText(leftName);
		playerRightLabel.setText(rightName);		
	}
	private void centerPhysicObjects(boolean graphics) { // Center Ball and 2 Player
		centerBall();
		physicData.setPlayerOneLocation(50, (1080 - physicData.getPlayerOneHeight()) / 2);
		physicData.setPlayerTwoLocation((1920 - 50 - 10), (1080 - physicData.getPlayerTwoHeight()) / 2);
		if (graphics) { // reLocate graphical objects
			Dimension preferredSize = pongFrame.getGraphicResolution();
			sLinks.setLocation(sLinks.getLocation().x, Math.round(
					(preferredSize.height - Math.round(physicData.getPlayerOneHeight() * pongFrame.getASPECT_RATIO()))
							/ 2));
			sRechts.setLocation(sRechts.getLocation().x, Math.round(
					(preferredSize.height - Math.round(physicData.getPlayerTwoHeight() * pongFrame.getASPECT_RATIO()))
							/ 2));
		}
	}

	private void centerBall() {
		physicData.setBallLocation((1920 - physicData.getBallSize()) / 2, (1080 - physicData.getBallSize()) / 2); // Ball
																													// Zentrieren
		ball.setLocation(Math.round(physicData.getBallX() * pongFrame.getASPECT_RATIO()),
				Math.round(physicData.getBallY() * pongFrame.getASPECT_RATIO()));
	}

	private void setBallSpeed(float factor) {

		// startwerte für die winkel
		weitey = 5 * factor; // schrittweite des balls in y richtung
		weitex = 5 * factor; // schrittweite des balls in x richtung
		// Es gibt 3 verschiedene Abrallwinkel
		// weitey1 - 3 und weitex1 - 3 sind die Winkeleinstellungen umso höher man diese
		// einstellt umso flacher der winkel.
		// weitex/y1: am flachesten; weitex/y2: flacher; weitex/y3: normal, 45°
		weitey1 = 6 * factor;
		weitex1 = 10 * factor; //
		weitey2 = 5 * factor; //
		weitex2 = 7 * factor; //// TODO: EINE MENGE SPIELRAUM FÜR SCHWIERIGKEITSGRADE, SCHMETTERBÄLLE ETC
		weitey3 = 6 * factor; //
		weitex3 = 6 * factor; //
		boostStep = 0.1f * factor;
	}

	private void configureGame(int difficulty) {
		MODE = difficulty;
		scoreLabel.setText("0 : 0");
		impossibleMode = false; // SPECIAL SPECIAL, NICHT SINNVOLL FÜR DEN SCHWIERIGKEITSGRAD
		winkel = true; // Verschiedene Abprallwinkel, würde auf false momentan gar nicht funktionieren.

		if (difficulty == this.EASY_MODE) {
			this.botFailFactor = 0.3f;
			botSpeed = 5; // geschwindigkeit des bots
			erfassungsbereichRechterBot = 1200; // umso höher umso kleiner der bereich
			erfassungsbereichLinkerBot = 600;
			setBallSpeed(0.7f);
			leftPlayerSpeed = 7 * 1.25f;
			rightPlayerSpeed = 7 * 1.25f;

		} else if (difficulty == this.HARD_MODE) {
			this.botFailFactor = 0.1; //
			botSpeed = 12; // geschwindigkeit des bots
			erfassungsbereichRechterBot = 600; // umso höher umso kleiner der bereich
			erfassungsbereichLinkerBot = 1200;
			setBallSpeed(1.25f);
			leftPlayerSpeed = 7 * 0.8f;
			rightPlayerSpeed = 7 * 0.8f;

		} else {// if(difficulty == this.MIDDLE_MODE) {
			this.botFailFactor = 0.2; //
			botSpeed = 10; // geschwindigkeit des bots
			erfassungsbereichRechterBot = 800; // umso höher umso kleiner der bereich
			erfassungsbereichLinkerBot = 800;
			setBallSpeed(1.0f);
			leftPlayerSpeed = 7;
			rightPlayerSpeed = 7;
		}
		centerPhysicObjects(true);

		maxPunkte = 10;
		scoreLinks = 0;
		scoreRechts = 0;
		// änderung der Startrichtung des balles
		x1 = false;
		y1 = false;
	}
	

	// stoppt den thread und setzt den ball zurück und wartet 3 Sekunden bis er
	// wieder startet
	private void stoppen() {
		scoreLabel.setText(scoreLinks + " : " + scoreRechts);
		if (spielGestartet) {
			spielGestartet = false;
			// Ball in die Mitte
			centerBall();

			// Hat jemand gewonnen?
			if (scoreLinks == maxPunkte) {
				countdown(5);
				timer.schedule(new TimerTask() {
					public void run() {
						stopGame();
						winner.setText("");
						pongFrame.showPane(pongFrame.LEVEL_SELECTION);
					}
				}, 5000);
				
				winner.setText(playerLeftLabel.getText()+" hat mit " + scoreLinks + ":" + scoreRechts + " Gewonnen");
			} else if (scoreRechts == maxPunkte) {
				countdown(5);
				timer.schedule(new TimerTask() {
					public void run() {
						stopGame();
						winner.setText("");
						pongFrame.showPane(pongFrame.LEVEL_SELECTION);
					}
				}, 5000);
				winner.setText(playerRightLabel.getText()+" hat mit " + scoreRechts + ":" + scoreLinks + " Gewonnen");
			}

			// Hat noch keiner gewonnen?
			if (scoreLinks < maxPunkte && scoreRechts < maxPunkte) {
				timer.schedule(new TimerTask() {
					public void run() {
						boostSpeed = 0;
						kl = false;
						kr = false;
						if(MODE==EASY_MODE) {
							weitex = 6 * 0.75f;
							weitey = 6 * 0.75f;
							
						}else if(MODE == HARD_MODE) {
							weitex = 6 * 1.25f;
							weitey = 6 * 1.25f;
							
						}else {
							weitex = 6;
							weitey = 6;
						}
						centerBall();
						startGame();
					}
				}, 3000);
				countdown(3);
			}
		}
	}

	private void countdown(int seconds) {
		if (!countdownActive) {
			countdownActive = true;
			shouldCountdown = true;

			if (spielGestartet) {
				spielGestartet = false;
			}
			RunWrapper run = new RunWrapper(seconds * 1000);
			Thread t = new Thread(run);
			t.start();
		}
	}

	// startet den thread
	public void startGame() {
		if (firstThreadStart) {
			firstThreadStart = false;
//			System.out.println("FIRST THREAD START");
			t = new GameThread();
			t.start();
			spielGestartet = true;
		} else if (!spielGestartet) {
			spielGestartet = true;
		}
	}

	private void pauseGame() {
		pauseMenu = true;
		shouldCountdown = false;
	}

	public void continueGame() {
		countdown(3);
		pauseMenu = false;
	}

	public void restartGame(int difficulty) {
		configureGame(difficulty);
		countdown(3);
		pauseMenu = false;
		startGame();
		spielGestartet = false;
	}
	public void restartGame(int difficulty, boolean leftPlayerBot, boolean rightPlayerBot) {
		configureGame(difficulty);
		countdown(3);
		pauseMenu = false;
		startGame();
		spielGestartet = false;
		this.isLeftPlayerBot = leftPlayerBot;
		this.isRightPlayerBot = rightPlayerBot;
	}

	public void stopGame() {
		configureGame(MODE);
		pauseMenu = true;
		spielGestartet = false;
	}

	public void setPlayerLeftRight(boolean activated) {
		if (activated) {// left = player
			isLeftPlayerBot = true;
			isRightPlayerBot = false;
		} else {
			isLeftPlayerBot = false;
			isRightPlayerBot = true;
		}
	}

	private void ballBewegung(float x, float y) {
		float ballX = physicData.getBallX();
		float ballY = physicData.getBallY();
		ballX -= x;
		ballY += y;
		// Objekt Kollision

		Dimension ds = new Dimension(1920, 1080); // Spielfeld größe
		int radius = physicData.getBallSize() / 2;

		// Kollision mit schläger 1

		if (Collision.circleToRect(ballX + radius, ballY + radius, radius, physicData.getPlayerOneX(),
				physicData.getPlayerOneY(), physicData.getPlayerOneWidth(), physicData.getPlayerOneHeight())) {
			boostSpeed += 0.1;
			x1 = true; // diese abfrage ändert die x richtung des balls nach der kollision so dass der
						// Ball reflektiert wird

			float ballHeight = ballY - physicData.getPlayerOneY() + physicData.getBallSize();
			// kr gegen doppelkollision

			if (kr == false) {
				kr = true;
				kl = false;
				// winkel true gleich winkel

				if (winkel == true) {
					// wenn der ball eine kollision mit einem wert von unter 40 hatt ist der
					// abprallwinkel am flacheseten
					if (ballHeight <= 40) {
						weitex = weitex1 + boostSpeed;
						weitey = weitey1 + boostSpeed;
					}

					// Wenn der ball eine kollision mit einem wert zwischen 40 und 90 hat ist der
					// Winkel leicht flacher als der normale 45° Winkel
					if (ballHeight > 40 && ballHeight <= 90) {
						weitex = weitex2 + boostSpeed;
						weitey = weitey2 + boostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 90 und 160 hatt ist der
					// winkel des balls 45°
					if (ballHeight > 90 && ballHeight <= 160) {
						weitex = weitex3 + boostSpeed;
						weitey = weitey3 + boostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 160 und 210 hatt ist der
					// winkel leicht flacher als der normale winkel der 45° beträgt

					if (ballHeight > 160 && ballHeight <= 210) {
						weitey = weitey2 + boostSpeed;
						weitex = weitex2 + boostSpeed;
					}

					// wenn der ball eine kollision mit einem wert von über 210 hatt ist der
					// abprallwinkel am flacheseten

					if (ballHeight > 210) {
						weitex = weitex1 + boostSpeed;
						weitey = weitey1 + boostSpeed;
					}
				}
			}
		}

		// Kollision mit schläger 2

		if (Collision.circleToRect(ballX + radius, ballY + radius, radius, physicData.getPlayerTwoX(),
				physicData.getPlayerTwoY(), physicData.getPlayerTwoWidth(), physicData.getPlayerTwoHeight())) {
			boostSpeed += boostStep;
			float ballHeight = physicData.getBallY() - physicData.getPlayerTwoY() + physicData.getBallSize();

			x1 = false; // diese abfrage ändert die x richtung des balls nach der kollision so das der
						// ball reflektiert wird

			if (kl == false) {
				kl = true;
				kr = false;

				if (winkel == true) {

					// wenn der ball eine kollision mit einem wert von unter 40 hatt ist der
					// abprallwinkel am flacheseten
					if (ballHeight <= 40) {
						weitex = weitex1 + boostSpeed;
						weitey = weitey1 + boostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 40 und 90 hatt ist der
					// winkel leicht flacher als der normale winkel der 45° beträgt
					if (ballHeight > 40 && ballHeight <= 90) {
						weitex = weitex2 + boostSpeed;
						weitey = weitey2 + boostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 90 und 160 hatt ist der
					// winkel des balls 45°
					if (ballHeight > 90 && ballHeight <= 160) {
						weitex = weitex3 + boostSpeed;
						weitey = weitey3 + boostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 160 und 210 hatt ist der
					// winkel leicht flacher als der normale winkel der 45° beträgt

					if (ballHeight > 160 && ballHeight <= 210) {
						weitex = weitex2 + boostSpeed;
						weitey = weitey2 + boostSpeed;
					}

					// wenn der ball eine kollision mit einem wert von über 210 hatt ist der
					// abprallwinkel am flacheseten

					if (ballHeight > 210) {
						weitex = weitex1 + boostSpeed;
						weitey = weitey1 + boostSpeed;
					}
				}
			}
		}
		// Seiten Kollision

		if (ballX < 0) { // kollision links hinten
			scoreRechts++;
			boostSpeed = 0;
			this.stoppen();
		}

		if (ballY < 0) { // kollision oben
			y1 = true;
		}

		if (ds.width <= ballX + physicData.getBallSize()) { // kollision rechts hinten
			scoreLinks++;
			boostSpeed = 0;
			this.stoppen();
		}

		if (ds.height <= ballY + physicData.getBallSize()) { // kollision unten
			y1 = false;
		}

		if (spielGestartet) {
			physicData.setBallLocation(ballX, ballY);
			ball.setLocation(Math.round(ballX * pongFrame.getASPECT_RATIO()),
					Math.round(ballY * pongFrame.getASPECT_RATIO()));
		}
	}

//  der bot
	private void botBewegungKomplexSchlägerRechts(float bally) {
		float schlagY = physicData.getPlayerTwoY(); // PlayerTwoY
		float schlagX = physicData.getPlayerTwoX();

		// wenn schwer auf fals ist wird der bot benutzt der sich in die richtung des
		// balls bewegt

		if (impossibleMode == false) {
			boolean dumm = false;

			// rand kollision oben der schläger wird leicht zurückgesetzt da er sich sonst
			// nichtmehr bewegen kann
			if (physicData.getPlayerTwoY() + physicData.getPlayerTwoHeight() >= 1080) {
				physicData.setPlayerTwoLocation(schlagX, 1080 - physicData.getPlayerTwoHeight());
				sRechts.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
						Math.round((1080 - physicData.getPlayerTwoHeight()) * pongFrame.getASPECT_RATIO()));
			}

			// rand kollision unten der schläger wird leicht zurückgesetzt da er sich sonst
			// nichtmehr bewegen kann
			if (physicData.getPlayerTwoY() <= 0) {
				physicData.setPlayerTwoLocation(schlagX, 0);
				sRechts.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()), 0);
			}

			// bewegt den schläger ind die mitte wenn ein punkt geffallen ist oder der ball
			// auserhalb des zu erfassenen bereichs ist
			if (spielGestartet == false || physicData.getBallX() < erfassungsbereichRechterBot) {

				if (physicData.getPlayerTwoY() < 400) {
					schlagY = physicData.getPlayerTwoY() + botSpeed;
					physicData.setPlayerTwoLocation(schlagX, schlagY);
					sRechts.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
							Math.round(schlagY * pongFrame.getASPECT_RATIO()));
				}

				if (physicData.getPlayerTwoY() + physicData.getPlayerTwoHeight() > 600) {
					schlagY = physicData.getPlayerTwoY() - botSpeed;
					physicData.setPlayerTwoLocation(schlagX, schlagY);
					sRechts.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
							Math.round(schlagY * pongFrame.getASPECT_RATIO()));
				}
			}
			// Math.random: Wert zwischen 0.0 und 0.99
			// Wenn der random Wert kleiner als der eingestellte difficulty Wert ist, bleibt
			// der Bot einfach stehen.
			// Vorsicht bei zu hohen difficulty Werten(höchstens 0.4 empfohlen). Sieht sonst
			// nur verbuggt aus.
			dumm = Math.random() < botFailFactor;
			// bewegt den schläger in die richtung in die der ball sich bewegt

			if (spielGestartet == true && physicData.getBallX() > erfassungsbereichRechterBot) {
				if (!dumm) {

					// bewegt den schläger nach unten wenn der ball sich nach unten bewegt und der
					// ball über dem schläger ist

					if (y1 == true && bally > physicData.getPlayerTwoY() && physicData.getPlayerTwoY() >= 0
							&& physicData.getPlayerTwoY() + physicData.getPlayerTwoHeight() <= 1080) {

						schlagY = physicData.getPlayerTwoY() + botSpeed;
						physicData.setPlayerTwoLocation(schlagX, schlagY);
						sRechts.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
								Math.round(schlagY * pongFrame.getASPECT_RATIO()));
					}

					// bewegt den schläger nach oben wenn der ball sich nach unten bewegt und der
					// ball unter dem schläger ist

					else if (y1 == true && bally < physicData.getPlayerTwoY() + physicData.getPlayerTwoHeight()
							&& physicData.getPlayerTwoY() >= 0
							&& physicData.getPlayerTwoY() + physicData.getPlayerTwoHeight() <= 1080) {

						schlagY = physicData.getPlayerTwoY() - botSpeed;
						physicData.setPlayerTwoLocation(schlagX, schlagY);
						sRechts.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
								Math.round(schlagY * pongFrame.getASPECT_RATIO()));
					}

					// bewegt den schläger nach unten wenn der ball sich nach oben bewegt und der
					// ball über dem schläger ist

					else if (y1 == false && bally > physicData.getPlayerTwoY() && physicData.getPlayerTwoY() >= 0
							&& physicData.getPlayerTwoY() + physicData.getPlayerTwoHeight() <= 1080) {

						schlagY = physicData.getPlayerTwoY() + botSpeed;
						physicData.setPlayerTwoLocation(schlagX, schlagY);
						sRechts.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
								Math.round(schlagY * pongFrame.getASPECT_RATIO()));
					}

					// bewegt den schläger nach unten wenn der ball sich nach oben bewegt und der
					// ball unter dem schläger ist

					else if (y1 == false && bally < physicData.getPlayerTwoY() + physicData.getPlayerTwoHeight()
							&& physicData.getPlayerTwoY() >= 0
							&& physicData.getPlayerTwoY() + physicData.getPlayerTwoHeight() <= 1080) {

						schlagY = physicData.getPlayerTwoY() - botSpeed;
						physicData.setPlayerTwoLocation(schlagX, schlagY);
						sRechts.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
								Math.round(schlagY * pongFrame.getASPECT_RATIO()));
					}
				}
			}
		}
		// bewgt den schläger auf die selbe höhe wie die des balles
		if (impossibleMode == true) {
			schlagY = bally;

			physicData.setPlayerTwoLocation(schlagX, schlagY);
			sRechts.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
					Math.round(schlagY * pongFrame.getASPECT_RATIO()));

		}
	}

//  der bot
	private void botBewegungKomplexSchlägerLinks(float bally) {
		float schlagY = physicData.getPlayerOneY(); // PlayerOneY
		float schlagX = physicData.getPlayerOneX();

		// wenn schwer auf fals ist wird der bot benutzt der sich in die richtung des
		// balls bewegt

		if (impossibleMode == false) {
			boolean dumm = false;

			// rand kollision oben der schläger wird leicht zurückgesetzt da er sich sonst
			// nichtmehr bewegen kann
			if (physicData.getPlayerOneY() + physicData.getPlayerOneHeight() >= 1080) {
				physicData.setPlayerOneLocation(schlagX, 1080 - physicData.getPlayerOneHeight());
				sLinks.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
						Math.round((1080 - physicData.getPlayerOneHeight()) * pongFrame.getASPECT_RATIO()));
			}

			// rand kollision unten der schläger wird leicht zurückgesetzt da er sich sonst
			// nichtmehr bewegen kann
			if (physicData.getPlayerOneY() <= 0) {
				physicData.setPlayerOneLocation(schlagX, 0);
				sLinks.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()), 0);
			}

			// bewegt den schläger ind die mitte wenn ein punkt geffallen ist oder der ball
			// auserhalb des zu erfassenen bereichs ist
			if (spielGestartet == false || physicData.getBallX() > erfassungsbereichLinkerBot) {

				if (physicData.getPlayerOneY() < 400) {
					schlagY = physicData.getPlayerOneY() + botSpeed;
					physicData.setPlayerOneLocation(schlagX, schlagY);
					sLinks.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
							Math.round(schlagY * pongFrame.getASPECT_RATIO()));
				}

				if (physicData.getPlayerOneY() + physicData.getPlayerOneHeight() > 600) {
					schlagY = physicData.getPlayerOneY() - botSpeed;
					physicData.setPlayerOneLocation(schlagX, schlagY);
					sLinks.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
							Math.round(schlagY * pongFrame.getASPECT_RATIO()));
				}
			}
			// Math.random: Wert zwischen 0.0 und 0.99
			// Wenn der random Wert kleiner als der eingestellte difficulty Wert ist, bleibt
			// der Bot einfach stehen.
			// Vorsicht bei zu hohen difficulty Werten(höchstens 0.4 empfohlen). Sieht sonst
			// nur verbuggt aus.
			dumm = Math.random() < botFailFactor;
			// bewegt den schläger in die richtung in die der ball sich bewegt

			if (spielGestartet == true && physicData.getBallX() < erfassungsbereichLinkerBot) {
				if (!dumm) {

					// bewegt den schläger nach unten wenn der ball sich nach unten bewegt und der
					// ball über dem schläger ist

					if (y1 == true && bally > physicData.getPlayerOneY() && physicData.getPlayerOneY() >= 0
							&& physicData.getPlayerOneY() + physicData.getPlayerOneHeight() <= 1080) {

						schlagY = physicData.getPlayerOneY() + botSpeed;
						physicData.setPlayerOneLocation(schlagX, schlagY);
						sLinks.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
								Math.round(schlagY * pongFrame.getASPECT_RATIO()));
					}

					// bewegt den schläger nach oben wenn der ball sich nach unten bewegt und der
					// ball unter dem schläger ist

					else if (y1 == true && bally < physicData.getPlayerOneY() + physicData.getPlayerOneHeight()
							&& physicData.getPlayerOneY() >= 0
							&& physicData.getPlayerOneY() + physicData.getPlayerOneHeight() <= 1080) {

						schlagY = physicData.getPlayerOneY() - botSpeed;
						physicData.setPlayerOneLocation(schlagX, schlagY);
						sLinks.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
								Math.round(schlagY * pongFrame.getASPECT_RATIO()));
					}

					// bewegt den schläger nach unten wenn der ball sich nach oben bewegt und der
					// ball über dem schläger ist

					else if (y1 == false && bally > physicData.getPlayerOneY() && physicData.getPlayerOneY() >= 0
							&& physicData.getPlayerOneY() + physicData.getPlayerOneHeight() <= 1080) {

						schlagY = physicData.getPlayerOneY() + botSpeed;
						physicData.setPlayerOneLocation(schlagX, schlagY);
						sLinks.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
								Math.round(schlagY * pongFrame.getASPECT_RATIO()));
					}

					// bewegt den schläger nach unten wenn der ball sich nach oben bewegt und der
					// ball unter dem schläger ist

					else if (y1 == false && bally < physicData.getPlayerOneY() + physicData.getPlayerOneHeight()
							&& physicData.getPlayerOneY() >= 0
							&& physicData.getPlayerOneY() + physicData.getPlayerOneHeight() <= 1080) {

						schlagY = physicData.getPlayerOneY() - botSpeed;
						physicData.setPlayerOneLocation(schlagX, schlagY);
						sLinks.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
								Math.round(schlagY * pongFrame.getASPECT_RATIO()));
					}
				}
			}
		}
		// bewgt den schläger auf die selbe höhe wie die des balles
		if (impossibleMode == true) {
			schlagY = bally;

			physicData.setPlayerOneLocation(schlagX, schlagY);
			sLinks.setLocation(Math.round(schlagX * pongFrame.getASPECT_RATIO()),
					Math.round(schlagY * pongFrame.getASPECT_RATIO()));

		}
	}

	class GameThread extends Thread {
		public void run() {

			while (true) {
				// während gestartet
				while (spielGestartet && !pauseMenu) {
					float playerOneX = physicData.getPlayerOneX();
					float playerOneY = physicData.getPlayerOneY();
					float playerTwoX = physicData.getPlayerTwoX();
					float playerTwoY = physicData.getPlayerTwoY();

//					System.out.println("LEFTBOT: " + isLeftPlayerBot + " RIGHTBOT: " + isRightPlayerBot);

					// Wenn der rechte Spieler ein Bot ist, wird hiermit seine Bewegung ausgeführt
					if (isRightPlayerBot) {
						botBewegungKomplexSchlägerRechts(physicData.getBallY());
					}
					// Wenn der linke Spieler ein Bot ist, wird hiermit seine Bewegung ausgeführt
					if (isLeftPlayerBot) {
						botBewegungKomplexSchlägerLinks(physicData.getBallY()); // -80 und einfacheBewegung, eh nie
																				// benutzt
					}

					float difX = 0, difY = 0;
					if (x1) { // Ball nach links
						difX = -weitex;
					} else if (!x1) { // ball nach rechts
						difX = weitex;
					}
					if (y1) {// ball nach unten
						difY = weitey;
					} else if (!y1) {// ball nach unten
						difY = -weitey;
					}
					ballBewegung(difX, difY);

					boolean twoPlayer = !isLeftPlayerBot && !isRightPlayerBot;
					if (!isLeftPlayerBot) {
						if (upW || (!twoPlayer && upArrow)) {
							if (playerOneY - leftPlayerSpeed < 0) {
								playerOneY = 0;
								physicData.setPlayerOneLocation(playerOneX, playerOneY);
								sLinks.setLocation(Math.round(playerOneX * pongFrame.getASPECT_RATIO()),
										Math.round(playerOneY * pongFrame.getASPECT_RATIO()));
							} else {
								playerOneY = playerOneY - leftPlayerSpeed;
								physicData.setPlayerOneLocation(playerOneX, playerOneY);
								sLinks.setLocation(Math.round(playerOneX * pongFrame.getASPECT_RATIO()),
										Math.round(playerOneY * pongFrame.getASPECT_RATIO()));
							}
						}
						if (downS || (!twoPlayer && downArrow)) {
							if ((playerOneY + leftPlayerSpeed + physicData.getPlayerOneHeight()) > 1080) {
								playerOneY = 1080 - physicData.getPlayerOneHeight();
								physicData.setPlayerOneLocation(playerOneX, playerOneY);
								sLinks.setLocation(Math.round(playerOneX * pongFrame.getASPECT_RATIO()),
										Math.round(playerOneY * pongFrame.getASPECT_RATIO()));
							} else {
								playerOneY = playerOneY + leftPlayerSpeed;
								physicData.setPlayerOneLocation(playerOneX, playerOneY);
								sLinks.setLocation(Math.round(playerOneX * pongFrame.getASPECT_RATIO()),
										Math.round(playerOneY * pongFrame.getASPECT_RATIO()));
							}
						}
					}

					if (!isRightPlayerBot) {
						if (upArrow || (!twoPlayer && upW)) {
							if (playerTwoY - rightPlayerSpeed < 0) {
								playerTwoY = 0;
								physicData.setPlayerTwoLocation(playerTwoX, playerTwoY);
								sRechts.setLocation(Math.round(playerTwoX * pongFrame.getASPECT_RATIO()),
										Math.round(playerTwoY * pongFrame.getASPECT_RATIO()));

							} else {
								playerTwoY = playerTwoY - rightPlayerSpeed;
								physicData.setPlayerTwoLocation(playerTwoX, playerTwoY);
								sRechts.setLocation(Math.round(playerTwoX * pongFrame.getASPECT_RATIO()),
										Math.round(playerTwoY * pongFrame.getASPECT_RATIO()));
							}
						}
						if (downArrow || (!twoPlayer && downS)) {
							if (playerTwoY + rightPlayerSpeed + physicData.getPlayerTwoHeight() > 1080) {
								playerTwoY = 1080 - physicData.getPlayerTwoHeight();
								physicData.setPlayerTwoLocation(playerTwoX, playerTwoY);
								sRechts.setLocation(Math.round(playerTwoX * pongFrame.getASPECT_RATIO()),
										Math.round(playerTwoY * pongFrame.getASPECT_RATIO()));
							} else {
								playerTwoY = playerTwoY + rightPlayerSpeed;
								physicData.setPlayerTwoLocation(playerTwoX, playerTwoY);
								sRechts.setLocation(Math.round(playerTwoX * pongFrame.getASPECT_RATIO()),
										Math.round(playerTwoY * pongFrame.getASPECT_RATIO()));
							}
						}
					}

					try {

						sleep(sleepTime);
					} catch (IllegalArgumentException | InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
				if (!pauseMenu) {
					// Damit man die Schläger auch bewegen kann, wenn gerade ein Punkt erziehlt
					// wurde, und der Countdown läuft
					float playerOneX = physicData.getPlayerOneX();
					float playerOneY = physicData.getPlayerOneY();
					float playerTwoX = physicData.getPlayerTwoX();
					float playerTwoY = physicData.getPlayerTwoY();

					// wenn der rechte bot true ist wird hierrüber die bewegungen des bots afgerufen
					if (isRightPlayerBot) {
						botBewegungKomplexSchlägerRechts(physicData.getBallY());
					}
					// wenn der linke bot true ist wird hierrüber die bewegungen des bots aufgerufen
					if (isLeftPlayerBot) {
						botBewegungKomplexSchlägerLinks(physicData.getBallY());
					}
					boolean twoPlayer = !isLeftPlayerBot && !isRightPlayerBot;
					if (!isLeftPlayerBot) {
						if (upW || (!twoPlayer && upArrow)) {
							if (playerOneY - leftPlayerSpeed < 0) {
								playerOneY = 0;
								physicData.setPlayerOneLocation(playerOneX, playerOneY);
								sLinks.setLocation(Math.round(playerOneX * pongFrame.getASPECT_RATIO()),
										Math.round(playerOneY * pongFrame.getASPECT_RATIO()));
							} else {
								playerOneY = playerOneY - leftPlayerSpeed;
								physicData.setPlayerOneLocation(playerOneX, playerOneY);
								sLinks.setLocation(Math.round(playerOneX * pongFrame.getASPECT_RATIO()),
										Math.round(playerOneY * pongFrame.getASPECT_RATIO()));
							}
						}
						if (downS || (!twoPlayer && downArrow)) {
							if ((playerOneY + leftPlayerSpeed + physicData.getPlayerOneHeight()) > 1080) {
								playerOneY = 1080 - physicData.getPlayerOneHeight();
								physicData.setPlayerOneLocation(playerOneX, playerOneY);
								sLinks.setLocation(Math.round(playerOneX * pongFrame.getASPECT_RATIO()),
										Math.round(playerOneY * pongFrame.getASPECT_RATIO()));
							} else {
								playerOneY = playerOneY + leftPlayerSpeed;
								physicData.setPlayerOneLocation(playerOneX, playerOneY);
								sLinks.setLocation(Math.round(playerOneX * pongFrame.getASPECT_RATIO()),
										Math.round(playerOneY * pongFrame.getASPECT_RATIO()));
							}
						}
					}

					if (!isRightPlayerBot) {
						if (upArrow || (!twoPlayer && upW)) {
							if (playerTwoY - rightPlayerSpeed < 0) {
								playerTwoY = 0;
								physicData.setPlayerTwoLocation(playerTwoX, playerTwoY);
								sRechts.setLocation(Math.round(playerTwoX * pongFrame.getASPECT_RATIO()),
										Math.round(playerTwoY * pongFrame.getASPECT_RATIO()));

							} else {
								playerTwoY = playerTwoY - rightPlayerSpeed;
								physicData.setPlayerTwoLocation(playerTwoX, playerTwoY);
								sRechts.setLocation(Math.round(playerTwoX * pongFrame.getASPECT_RATIO()),
										Math.round(playerTwoY * pongFrame.getASPECT_RATIO()));
							}
						}
						if (downArrow || (!twoPlayer && downS)) {
							if (playerTwoY + rightPlayerSpeed + physicData.getPlayerTwoHeight() > 1080) {

								playerTwoY = 1080 - physicData.getPlayerTwoHeight();
								physicData.setPlayerTwoLocation(playerTwoX, playerTwoY);
								sRechts.setLocation(Math.round(playerTwoX * pongFrame.getASPECT_RATIO()),
										Math.round(playerTwoY * pongFrame.getASPECT_RATIO()));
							} else {
								playerTwoY = playerTwoY + rightPlayerSpeed;
								physicData.setPlayerTwoLocation(playerTwoX, playerTwoY);
								sRechts.setLocation(Math.round(playerTwoX * pongFrame.getASPECT_RATIO()),
										Math.round(playerTwoY * pongFrame.getASPECT_RATIO()));
							}
						}
					}
				}
				try {
					sleep(sleepTime);
				} catch (IllegalArgumentException | InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}// Not-Used

	// Wenn die Taste gedrückt wird bewegt sich der Spieler solange in die Richtung
	// der Taste, bis sie losgelassen wird

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_W) {
			upW = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			downS = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upArrow = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downArrow = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (pongFrame.getACTIVE_PANEL() == pongFrame.SINGLEPLAYER) {
				if (pauseMenu) {
					pauseAction.getPausePanel().resume();
					continueGame();
				} else {
					if(winner.getText().equals("")) {//Damit die Pause Taste nicht gedrückt werden kann, während das Spiel vorbei ist
						pauseGame();
						pauseAction.action();
					}
				}
			}
		}
	}

	// Stoppen der Spieler-Bewegung wenn die Taste losgelassen wird
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_W) {
			upW = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			downS = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upArrow = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downArrow = false;
		}
	}

	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				keyPressed(e);
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				keyReleased(e);
			} else if (e.getID() == KeyEvent.KEY_TYPED) {
				keyTyped(e);
			}
			return false;
		}
	}

	private class RunWrapper implements Runnable {
		private int milliSeconds;

		public RunWrapper(int milliSeconds) {
			this.milliSeconds = milliSeconds;
		}

		@Override
		public void run() {
			synchronized (this) {
				while (milliSeconds > 0 && shouldCountdown) {
					try {
						int seconds = milliSeconds / 1000;
						int milli = milliSeconds - seconds * 1000;
						milli = Math.round(milli / 10); // TODO:
						countdown.setText("0" + (seconds) + ":" + milli);
						milliSeconds -= 10;

						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			countdown.setText("");
			startGame();
			countdownActive = false;
		}
	}
}