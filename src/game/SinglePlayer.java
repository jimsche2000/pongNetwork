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
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.MenuLabel;
import pause.*;
import hauptmenu.PongFrame;

@SuppressWarnings("serial")
public class SinglePlayer extends JPanel implements KeyListener {

	// Objects
	private PongFrame pongFrame;
	private PauseAction pauseAction;
	private JPanel contentPane;
	private Ball ball = new Ball();
	private Schlag sLinks = new Schlag(100, 540);
	private Schlag sRechts = new Schlag(1920 - 100, 540);
	private MenuLabel winner;
	private MenuLabel scoreLabel;
	private MenuLabel countdown;
//	private JLabel winner = new JLabel();
//	private JLabel scoreLabel = new JLabel();
//	private JLabel countdown = new JLabel();
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

	private int erfassungsbereichDummerBot; // Horizontaler Pixelfester-Wert, ab dem der Bot "zuguckt" und seine
											// Position verändert
	private int botSpeed; // Geschwindigkeit des Bots

	private int leftPlayerSpeed, rightPlayerSpeed;
	//private int tempCountCollision; // Variable wird bei jeder Ball-Schläger Kollision um einen erhöht, und bei Tor
									// zurückgesetzt
	private boolean up, down, up1, down1; // Variablen für die Funktion der Schläger und der Tastatur. (up, down für W
											// und S; up1 und down1 für Pfeil-Hoch und Pfeil-Runter)
	private boolean x1, y1; // (Start)-Richtung des Balls.
	private int weitey, weitex; // Schrittweite des Balls jeweils in X- und in Y- Richtung
	private double boostSpeed, // Zusatzschnelligkeit des Balls, wird auf weitex und weitey addiert; erhöht
								// sich bei Kollisionen des Balls mit den Schlägern/Spielern
			temphalfPixelSpeed; // Übriggebliebene Pixel (zB. 0,3 Pixel). boostSpeed ist ein double Wert. Pixel
								// sind ints. Bei jedem durchlauf bleiben kommazahl-Pixel liegen. Diese werden
								// hier gesammelt.
								// Immer wenn ein pixel gesammelt wurde, wird dieser auch genutzt. Dadurch kann
								// die !!!Geschwindigkeit des Balls!!! letztendlich viel genauer verändert
								// werden

	private double difficulty; // die schwierigkeit des bots (Letztendlich wie oft er stehenbleibt. von 0.0 -
								// 1.0; 0.1 ist er sehr schwer, 0.5 ist er leicht)
								// Nicht zu empfehlen die Schwierigkeit so einzustellen. Entweder überarbeiten,
								// oder nur den erfassungsbereich verändern
	private boolean winkel, // Spezial-Modus mit verschiedenen Abprallwinkeln/geschwindigkeiten
			kl, kr, // Kollision Links/Rechts?!?!
			isRightPlayerBot, isLeftPlayerBot, impossibleMode; // Bot Einstellungen
	private int weitex1, weitey1, weitex2, weitey2, weitex3, weitey3; // Hardgecodete Werte für die verschiedenen Winkel
	private MyThread t;
	private int sleepTime = 5; //ms
	public final int EASY_MODE = 0;
	public final int MIDDLE_MODE = 1;
	public final int HARD_MODE = 2;
	public final int CUSTOM_MODE = 3;
	public int MODE = -1;
	
	public SinglePlayer(PongFrame frame) {
		this.pongFrame = frame;
//		float AR = frame.getASPECT_RATIO(); //Math.round(x*AR)
		this.pauseAction = new PauseAction(frame);
		this.setLayout(null);
		this.setSize(1920, 1080);
		this.setPreferredSize(new Dimension(1920,1080));
		//		this.setSize(frame.getSize());
//		this.setPreferredSize(pongFrame.getSize());
		this.setLocation(0, 0);
//		this.setTitle("Pong");
//		this.setDefaultCloseOperation(3);
		ball.setBounds(940, 500, 50, 50);
		ball.setBackground(Color.BLACK);
//		ball.reLocate(AR);
		

		winner = new MenuLabel(frame, "");
		scoreLabel = new MenuLabel(frame, "");
		countdown = new MenuLabel(frame, "");
		
		winner.setBounds(450, 900, 1000, 100);
		winner.setFont(new Font("Arial", Font.BOLD, 50));
		scoreLabel.setBounds(1920 / 2 - 50, 10, 400, 50);
		scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
		
		sLinks.setBackground(Color.BLACK);
//		sLinks.reLocate(AR);
		sRechts.setBackground(Color.BLACK);
//		sRechts.reLocate(AR);
		countdown.setBounds(660, 100, 600, 300);
		countdown.setFont(new Font("Arial", Font.BOLD, 150));
		countdown.setHorizontalAlignment(SwingConstants.CENTER);
		
		contentPane = new JPanel();
		contentPane.setSize(1920, 1080);
		contentPane.setPreferredSize(new Dimension(1920,1080));
		contentPane.setLayout(null);
//		contentPane.setSize(frame.getGraphicResolution());
//		contentPane.setPreferredSize(frame.getGraphicResolution());
		contentPane.setBackground(Color.white);
		contentPane.add(winner);
		contentPane.add(ball);
		contentPane.add(sLinks);
		contentPane.add(sRechts);
		contentPane.add(scoreLabel);
		contentPane.add(countdown);
		this.add(contentPane);
//		this.setUndecorated(true);
//		this.addKeyListener(this);
//		this.setVisible(true);
//		this.laufen();
		 KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		    manager.addKeyEventDispatcher(new MyDispatcher());
		// Configure Game
		standardConfig();
		firstThreadStart = true;
		shouldCountdown = false;

		// unsichtbarkeit des courses
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0),
				"invisibleCursor");
		this.setCursor(transparentCursor);
	}

	public void setDifficulty(int difficulty) {
		MODE = difficulty;
		switch(difficulty) {
			
			case EASY_MODE:
				this.difficulty = 0.35;
				botSpeed = 7; // geschwindigkeit des bots 
				erfassungsbereichDummerBot = 1200; // umso höher umso kleiner der bereich
				
				break;
			case MIDDLE_MODE:
				this.difficulty = 0.2; // 
				botSpeed = 10; // geschwindigkeit des bots 
				erfassungsbereichDummerBot = 800; // umso höher umso kleiner der bereich
				
				break;
			case HARD_MODE:
				this.difficulty = 0.1; //
				botSpeed = 14; // geschwindigkeit des bots 
				erfassungsbereichDummerBot = 600; // umso höher umso kleiner der bereich 
				
				break;
			case CUSTOM_MODE:
				break;
			default:
				break;
		}
	}
	private void standardConfig() {

		scoreLabel.setText("0 : 0");
//		newGame = false;
//		tempCountCollision = 0;
		ball.setLocation(940, 500);
		// einstellungesn des bots
		isRightPlayerBot = true; // sp = true = rechter spieler ist ein bot
		isLeftPlayerBot = false;// sp2 = true = linker spieler ist ein bot
		impossibleMode = false; //SPECIAL SPECIAL, NICHT SINNVOLL FÜR DEN SCHWIERIGKEITSGRAD
		difficulty = 0.2; 
		botSpeed = 14; // geschwindigkeit des bots
		erfassungsbereichDummerBot = 800; // umso höher umso kleiner der bereich 
		// einstellung der restlichen optionen im spiel
		winkel = true;
		// startwerte für die winkel
		weitey = 5; // schrittweite des balls in y richtung
		weitex = 5; // schrittweite des balls in x richtung
		// Es gibt 3 verschiedene Abrallwinkel
		weitey1 = 4; 	// weitey1 - 3 und weitex1 - 3 sind die Winkeleinstellungen umso höher man diese
						// einstellt umso flacher der winkel.
						//weitex/y1: am flachesten; weitex/y2: flacher; weitex/y3: normal, 45°
		weitex1 = 8; 	//
		weitey2 = 5;	//
		weitex2 = 7; 	////TODO: EINE MENGE SPIELRAUM FÜR SCHWIERIGKEITSGRADE, SCHMETTERBÄLLE ETC	
		weitey3 = 6;	//
		weitex3 = 6;	//

		leftPlayerSpeed = 7;
		rightPlayerSpeed = 7;
		maxPunkte = 15;
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
			
			//Ball in die Mitte
			ball.setLocation(940, 500);
			//System.out.println("BALL WIRD ZURÜCKGESETZT AUF X940 Y500 timestamp: "+System.currentTimeMillis());

			//Hat jemand gewonnen?
			if (scoreLinks == maxPunkte) {
				timer.schedule(new TimerTask() {
					public void run() {
						stopGame();
						winner.setText("");
						pongFrame.showPane(pongFrame.LEVEL_SELECTION);
					}
				}, 5000);
				winner.setText("Linker Spieler hat mit " + scoreLinks + ":" + scoreRechts + " Gewonnen");
			}else if (scoreRechts == maxPunkte) {
				timer.schedule(new TimerTask() {
					public void run() {
						stopGame();
						winner.setText("");
						pongFrame.showPane(pongFrame.LEVEL_SELECTION);
					}
				}, 5000);
				winner.setText("Rechter Spieler hat mit " + scoreRechts + ":" + scoreLinks + " Gewonnen");
			}

			//Hat noch keiner gewonnen?
			if (scoreLinks < maxPunkte && scoreRechts < maxPunkte) {
				timer.schedule(new TimerTask() {
					public void run() {
//						tempCountCollision = 0;

						//System.out.println("ZURÜCKSETZEN, timestamp: "+System.currentTimeMillis());
						
						boostSpeed = 0;
						temphalfPixelSpeed = 0;
						kl = false;
						kr = false;
						weitex = 5;
						weitey = 5;
						ball.setLocation(940, 500);
						startGame();
					}
				}, 3000);
				countdown(3);
			}
		}
	}
	private void countdown(int seconds) {
		if(!countdownActive) {
			countdownActive = true;
			shouldCountdown = true;
			
			if(spielGestartet) {
				spielGestartet = false;
			}		
//			timer.schedule(new TimerTask() {
//				
//				@Override
//				public void run() {
////					System.out.println("halo");
//
//				}
//			}, seconds*1000);
			RunWrapper run = new RunWrapper(seconds*1000);
//			run.run();
			Thread t = new Thread(run);
			t.start();
		}
	}
	// startet den thread
	public void startGame() {
		if (firstThreadStart) {
			firstThreadStart = false;
			System.out.println("FIRST THREAD START");
			t = new MyThread();
			t.start();
			spielGestartet = true;
			//System.out.println("VARIANTE1");
		}else if(!spielGestartet) {
			spielGestartet = true;
			//System.out.println("VARIANTE2");
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
	public void restartGame() {
		standardConfig();
		countdown(3);
		pauseMenu = false;
		startGame();
		spielGestartet = false;
//		startGame();
//		stoppen();
	}
	public void stopGame() {
		standardConfig();
		pauseMenu = true;
		spielGestartet = false;
	}
	
//	private void startPhysics() {
//		if (!spielGestartet) {
//			spielGestartet = true;
//		}
//	}
int i234 = 0;
	private void ballBewegung(int x, int y) {
		//System.out.println("BEWEGE DEN BALL(("+i234+"));  PARAMETER: X:"+x+" Y:"+y);
		i234++;
		Point p = ball.getLocation(); //TODO: RES

		p.x = p.x - x;
		p.y = p.y + y;

		//System.out.println("Ball position: "+p+" timestamp: "+System.currentTimeMillis());
		// Objekt Kollision

		Dimension ds = contentPane.getSize();
		int radius = ball.getWidth() / 2;

		// Kollision mit schläger 1

		if (Collision.circleToRect(p.x + radius, p.y + radius, radius, sLinks.getLocation().x, sLinks.getLocation().y,
				10, 200)) {

			x1 = true; // diese abfrage ändert die x richtung des balls nach der kollision so dass der
						// Ball reflektiert wird

			int ballY = ball.getLocation().y - sLinks.getLocation().y + ball.getSize().height;

			// kr gegen doppelkollision

			if (kr == false) {

				// winkel false gleich keine winkel
				if (winkel == false) {

					int tempBoostSpeed = calcBoostSpeed();

					weitey = weitey + tempBoostSpeed;
					weitex = weitex + tempBoostSpeed;
				}

				kr = true;
				kl = false;
				// winkel true gleich winkel

				if (winkel == true) {

					// wenn der ball eine kollision mit einem wert von unter 40 hatt ist der
					// abprallwinkel am flacheseten
					if (ballY <= 40) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey1 + tempBoostSpeed;
						weitex = weitex1 + tempBoostSpeed;
					}

					// Wenn der ball eine kollision mit einem wert zwischen 40 und 90 hat ist der
					// Winkel leicht flacher als der normale 45° Winkel
					if (ballY > 40 && ballY <= 90) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey2 + tempBoostSpeed;
						weitex = weitex2 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 90 und 160 hatt ist der
					// winkel des balls 45°
					if (ballY > 90 && ballY <= 160) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey3 + tempBoostSpeed;
						weitex = weitex3 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 160 und 210 hatt ist der
					// winkel leicht flacher als der normale winkel der 45° beträgt

					if (ballY > 160 && ballY <= 210) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey2 + tempBoostSpeed;
						weitex = weitex2 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert von über 210 hatt ist der
					// abprallwinkel am flacheseten

					if (ballY > 210) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey1 + tempBoostSpeed;
						weitex = weitex1 + tempBoostSpeed;
					}
				}
			}
		}

		// Kollision mit schläger 2

		if (Collision.circleToRect(p.x + radius, p.y + radius, radius, sRechts.getLocation().x, sRechts.getLocation().y,
				10, 200)) {

			int ballY = ball.getLocation().y - sRechts.getLocation().y + ball.getSize().height;

			x1 = false; // diese abfrage ändert die x richtung des balls nach der kollision so das der
						// ball reflektiert wird

			if (kl == false) {

				if (winkel == false) {
					int tempBoostSpeed = calcBoostSpeed();

					weitey = weitey + tempBoostSpeed;
					weitex = weitex + tempBoostSpeed;
				}

				kl = true;
				kr = false;

				if (winkel == true) {

					// wenn der ball eine kollision mit einem wert von unter 40 hatt ist der
					// abprallwinkel am flacheseten
					if (ballY <= 40) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey1 + tempBoostSpeed;
						weitex = weitex1 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 40 und 90 hatt ist der
					// winkel leicht flacher als der normale winkel der 45° beträgt
					if (ballY > 40 && ballY <= 90) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey2 + tempBoostSpeed;
						weitex = weitex2 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 90 und 160 hatt ist der
					// winkel des balls 45°
					if (ballY > 90 && ballY <= 160) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey3 + tempBoostSpeed;
						weitex = weitex3 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 160 und 210 hatt ist der
					// winkel leicht flacher als der normale winkel der 45° beträgt

					if (ballY > 160 && ballY <= 210) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey2 + tempBoostSpeed;
						weitex = weitex2 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert von über 210 hatt ist der
					// abprallwinkel am flacheseten

					if (ballY > 210) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey1 + tempBoostSpeed;
						weitex = weitex1 + tempBoostSpeed;
					}
				}
			}
		}
		//System.out.println("Ball position2: "+p);
		// Seiten Kollision

		if (p.x < 0) { // kollision links hinten
			//System.out.println("PUNKT RECHTS");
			scoreRechts++;
			this.stoppen();
		}

		if (p.y < 0) { // kollision oben
			y1 = true;
		}

		if (ds.width <= p.x + ball.getSize().width) { // kollision rechts hinten
			//System.out.println("PUNKT LINKS");
			scoreLinks++;
			this.stoppen();
		}

		if (ds.height <= p.y + ball.getSize().height) { // kollision unten
			y1 = false;
		}

//		if (spielGestartet == true && firstThreadStart == true) {
//			firstThreadStart = false;
//			//System.out.println("METHOD1, SETZE IHN AUF: "+p+" timestamp: "+System.currentTimeMillis());
//			ball.setLocation(p);
//		}

		if (spielGestartet) {
			//System.out.println("METHOD2, SETZE IHN AUF: "+p+" timestamp: "+System.currentTimeMillis());
			ball.setLocation(p);
		}
	}
//  der bot

	private void botBewegungKomplex(int bally) {

		int schlagy;

		// wenn schwer auf fals ist wird der bot benutzt der sich in die richtung des
		// balls bewegt

		if (impossibleMode == false) {
			boolean dumm = false;

			// rand kollision oben der schläger wird leicht zurückgesetzt da er sich sonst
			// nichtmehr bewegen kann
			if (sRechts.getY() >= 880) {
				sRechts.setLocation(sRechts.getX(), 875);
			}

			// rand kollision unten der schläger wird leicht zurückgesetzt da er sich sonst
			// nichtmehr bewegen kann
			if (sRechts.getY() <= 5) {
				sRechts.setLocation(sRechts.getX(), 10);
			}

			// bewegt den schläger ind die mitte wenn ein punkt geffallen ist oder der ball
			// auserhalb des zu erfassenen bereichs ist
			if (spielGestartet == false || ball.getLocation().x < erfassungsbereichDummerBot) {

				if (sRechts.getY() < 400) {
					schlagy = sRechts.getY() + botSpeed;
					sRechts.setLocation(sRechts.getX(), schlagy);
				}

				if (sRechts.getY() + 200 > 600) {
					schlagy = sRechts.getY() - botSpeed;
					sRechts.setLocation(sRechts.getX(), schlagy);
				}

			}
			//Math.random: Wert zwischen 0.0 und 0.99
			//Wenn der random Wert kleiner als der eingestellte difficulty Wert ist, bleibt der Bot einfach stehen.
			//Vorsicht bei zu hohen difficulty Werten(höchstens 0.4 empfohlen). Sieht sonst nur verbuggt aus.
			dumm = Math.random() < difficulty; 

			
			// bewegt den schläger in die richtung in die der ball sich bewegt

			if (spielGestartet == true && ball.getLocation().x > erfassungsbereichDummerBot) {

				if (!dumm) {

					// bewegt den schläger nach unten wenn der ball sich nach unten bewegt und der
					// ball über dem schläger ist

					if (y1 == true && bally > sRechts.getY() && sRechts.getY() >= 0 && sRechts.getY() <= 880) {

						schlagy = sRechts.getY() + botSpeed;
						sRechts.setLocation(sRechts.getX(), schlagy);

					}

					// bewegt den schläger nach oben wenn der ball sich nach unten bewegt und der
					// ball unter dem schläger ist

					else if (y1 == true && bally < sRechts.getY() + 200 && sRechts.getY() >= 0
							&& sRechts.getY() <= 880) {

						schlagy = sRechts.getY() - botSpeed;
						sRechts.setLocation(sRechts.getX(), schlagy);
					}

					// bewegt den schläger nach unten wenn der ball sich nach oben bewegt und der
					// ball über dem schläger ist

					else if (y1 == false && bally > sRechts.getY() && sRechts.getY() >= 0 && sRechts.getY() <= 880) {

						schlagy = sRechts.getY() + botSpeed;
						sRechts.setLocation(sRechts.getX(), schlagy);

					}

					// bewegt den schläger nach unten wenn der ball sich nach oben bewegt und der
					// ball unter dem schläger ist

					else if (y1 == false && bally < sRechts.getY() + 200 && sRechts.getY() >= 0
							&& sRechts.getY() <= 880) {

						schlagy = sRechts.getY() - botSpeed;
						sRechts.setLocation(sRechts.getX(), schlagy);
					}

				}
			}
		}
		// bewgt den schläger auf die selbe höhe wie die des balles
		if (impossibleMode == true) {
			schlagy = bally;
			sRechts.setLocation(sRechts.getX(), schlagy);

		}

	}

	// der bot für den linken schläger bewgt den schläger auf die selbe höhe wie die
	// des balles

	private void botBewegungEinfach(int bally) {
		int schlagy;
		schlagy = bally;
		sLinks.setLocation(sLinks.getX(), schlagy);

	}

	// der thread über den der ball der bot und die bewegung der schläger läuft

	private int calcBoostSpeed() {
		/*
		 * Variablen Hilfe:
		 * 
		 * boostSpeed ist die Extra-Geschwindigkeit, welche sich bei jeder Ball-Schläger Kollision erhöht.
		 * temphalfPixelSpeed sammelt die Dezimal-Werte(Nach dem Komma) von boostSpeed, da es nunmal nur ganze Pixel gibt.
		 * Sobald ein ganzer Pixel in temphalfPixelSpeed gesammelt wurde, wird dieser der tempBoostSpeed hinzugefügt,
		 * und von temphalfPixelSpeed abgezogen.
		 * tempBoostSpeed ist die Geschwindigkeit die im temporären durchlauf wirklich hinzugefügt wird
		 * tempExtraSpeed ist nur eine weitere temporäre Variable, die henötigt wird, um den Ganzzahl-Wert von temphalfPixelSpeed herauszufiltern
		 */
		boostSpeed += 0.1; //Verschnellert den Ball bei jeder Ball-Schläger-Kollision
		boostSpeed = Math.round(boostSpeed * 100000.0) / 100000.0; //Auf die 5. Dezimalstelle aufrunden	
		
		temphalfPixelSpeed += (boostSpeed - (int) boostSpeed); // Der Wert nach dem Dezimalpunkt
		temphalfPixelSpeed = Math.round(temphalfPixelSpeed * 100000.0) / 100000.0; //Auf die 5. Dezimalstelle aufrunden	
		int tempBoostSpeed = (int) boostSpeed; // Der Wert vor dem Dezimalpunkt
		
		if (temphalfPixelSpeed > 1) { // Wenn genug Dezimalwerte gesammelt wurden, dass wieder ein ganzer Pixel
										// zusammen ist
			int tempExtraSpeed = (int) temphalfPixelSpeed; // Der Wert vor dem Dezimalpunkt
			tempBoostSpeed += tempExtraSpeed; // Den gesammelten Pixel der Geschwindigkeit hinzufügen
			temphalfPixelSpeed -= tempExtraSpeed; // Den gesammelten Pixel wieder von der sammel-variable abziehen
			temphalfPixelSpeed = Math.round(temphalfPixelSpeed * 100000.0) / 100000.0; //Auf die 5. Dezimalstelle aufrunden	
			//System.out.println("I GOT A PIXEL");
		}		
		//System.out.println("tempBoostSpeed: "+tempBoostSpeed+" boostSpeed: "+boostSpeed+" temphalfPixelSpeed2: "+temphalfPixelSpeed);
		return tempBoostSpeed;
	}
	
	class MyThread extends Thread {
		public void run() {

			while (true) {
				//System.out.println("DIES IST DIE WHILE-SCHLEIFE DES GRAUENS");
				// während gestartet
				while (spielGestartet && !pauseMenu) {
					//System.out.println("ICH BIN GESTARTET! UND SPIELE!");
					int xu = sLinks.getX();
					int yu = sLinks.getY();
					int x1u = sRechts.getX();
					int y1u = sRechts.getY();
					
					// wenn der rechte bot true ist wird hierrüber die bewegungen des bots afgerufen
					if (isRightPlayerBot == true) {
						botBewegungKomplex(ball.getLocation().y);
					}
					// wenn der linke bot true ist wird hierrüber die bewegungen des bots aufgerufen
					if (isLeftPlayerBot == true) {
						botBewegungEinfach(ball.getY() - 80);
					}
					int difX = 0, difY = 0;
					if (x1) {
						difX = -weitex;
//						ballBewegung(-weitex, 0);
//						//System.out.println("X: "+(-weitex)+" Y:"+0);
					}else if(!x1) {
						difX = weitex;
					}

//					if (x1 == false) {
//						ballBewegung(weitex, 0);
//						//System.out.println("X: "+weitex+" Y:"+0);
//					}

					if (y1) {
						difY = weitey;
//						ballBewegung(0, weitey);
//						//System.out.println("X: "+0+" Y:"+weitey);
					}else if (!y1) {
						difY = -weitey;
//						ballBewegung(0, -weitey);
//						//System.out.println("X: "+0+" Y:"+(-weitey));
					}
					ballBewegung(difX, difY);

					if (isLeftPlayerBot == false) {

						if (up == true && sLinks.getY() > 0) {
							yu = yu - leftPlayerSpeed;
							sLinks.setLocation(xu, yu);
						}

						if (down == true && sLinks.getY() <= 900) {
							yu = yu + leftPlayerSpeed;
							sLinks.setLocation(xu, yu);
						}
					}

					if (isRightPlayerBot == false) {

						if (up1 == true && sRechts.getY() > 0) {
							y1u = y1u - rightPlayerSpeed;
							sRechts.setLocation(x1u, y1u);
						}

						if (down1 == true && sRechts.getY() <= 900) {
							y1u = y1u + rightPlayerSpeed;
							sRechts.setLocation(x1u, y1u);
						}

					}

					try {
						
						sleep(sleepTime);
					} catch (IllegalArgumentException e) {
						//System.out.println(e.getMessage());
					} catch (InterruptedException e) {
						//System.out.println(e.getMessage());
					}
				}
//				System.out.println("HUHU; DU BIST NICHT IM SPIEL!");
				//System.out.println("PAUSE? "+pauseMenu);
				if(!pauseMenu) {
					//Damit man die Schläger auch bewegen kann, wenn gerade ein Punkt erziehlt wurde, und der Countdown läuft
					int xu = sLinks.getX();
					int yu = sLinks.getY();
					int x1u = sRechts.getX();
					int y1u = sRechts.getY();
	
					if (isRightPlayerBot == true) {
						botBewegungKomplex(ball.getLocation().y);
					}
	
					if (isLeftPlayerBot == false) {
						if (up == true && sLinks.getY() > 0) {
							yu = yu - leftPlayerSpeed;
							sLinks.setLocation(xu, yu);
						}
	
						if (down == true && sLinks.getY() <= 900) {
							yu = yu + leftPlayerSpeed;
							sLinks.setLocation(xu, yu);
						}
					}
	
					if (isRightPlayerBot == false) {
						if (up1 == true && sRechts.getY() > 0) {
							y1u = y1u - rightPlayerSpeed;
							sRechts.setLocation(x1u, y1u);
						}
	
						if (down1 == true && sRechts.getY() <= 900) {
							y1u = y1u + rightPlayerSpeed;
							sRechts.setLocation(x1u, y1u);
						}
					}
				}
				try {
					sleep(sleepTime);
				} catch (IllegalArgumentException e) {
					//System.out.println(e.getMessage());
				} catch (InterruptedException e) {
					//System.out.println(e.getMessage());
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	// Wenn die Taste gedrückt wird bewegt sich der Spieler solange in die Richtung
	// der Taste, bis sie losgelassen wird

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_W) {
			up = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			down = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up1 = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down1 = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if(pauseMenu) {
				pauseAction.getPausePanel().resume();
				continueGame();
			}else {
				pauseGame();
				pauseAction.action();
			}
		}
	}

	// Stoppen der Spieler-Bewegung wenn die Taste losgelassen wird

	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_W) {
			up = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_S) {
			down = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up1 = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down1 = false;
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
	private class RunWrapper implements Runnable{
		private int milliSeconds;
		public RunWrapper(int milliSeconds) {
			this.milliSeconds = milliSeconds;
		}
		@Override
		public void run() {
			synchronized(this) {
				while(milliSeconds>0 && shouldCountdown) {
					try {
//						//System.out.println("setText: "+seconds);
						int seconds =milliSeconds/1000;
//						System.out.println("milliSeconds: "+milliSeconds);
						int milli = milliSeconds-seconds*1000;
//						System.out.println("milli: "+milli);
						milli = Math.round(milli / 10) ; //TODO:
						countdown.setText((seconds)+":"+milli);
						milliSeconds-=10;
						
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

//				//System.out.println("END");
				countdown.setText("");
				startGame();
				countdownActive = false;
		}
	}
	//Little Helper Class for Timer, Countdown
//	private class Task extends TimerTask{
//		
//		int seconds;
//		public Task(int seconds) {
//			this.seconds = seconds;
//		}
//		@Override
//		public void run() {
//			countdown.setText(Integer.toString(seconds));
//		}
//	}
}
