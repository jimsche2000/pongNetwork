package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.MemoryImageSource;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class offlineGame extends JFrame implements KeyListener {

	// Objects
	private JPanel contentPane;
	private Ball ball = new Ball();
	private Schlag sLinks = new Schlag(100, 540);
	private Schlag sRechts = new Schlag(1920 - 100, 540);
	private JLabel winner = new JLabel();
	private JLabel scoreLabel = new JLabel();
	private JLabel countdown = new JLabel();
	private Timer timer = new Timer(), timer1 = new Timer(), timer2 = new Timer(), timer3 = new Timer(),
			timer4 = new Timer();

	// Variables (Normal Datatypes)
	private boolean newGame;
	private boolean spielGestartet;
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
	private int periodendauer = 5;
	
	public offlineGame() {
		this.setLayout(null);
		this.setSize(1920, 1080);
		this.setLocation(0, 0);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setSize(1920, 1080);
		this.setTitle("Pong");
		this.setDefaultCloseOperation(3);
		ball.setBounds(940, 500, 50, 50);
		ball.setBackground(Color.BLACK);
		winner.setBounds(450, 900, 1000, 100);
		winner.setFont(new Font("Arial", Font.BOLD, 50));
		contentPane.add(winner);
		scoreLabel.setBounds(1920 / 2 - 50, 10, 400, 50);
		scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
		contentPane.add(ball);
		contentPane.setBackground(Color.white);
		sLinks.setBackground(Color.BLACK);
		sRechts.setBackground(Color.BLACK);
		contentPane.add(sLinks);
		contentPane.add(sRechts);
		contentPane.add(scoreLabel);
		countdown.setBounds(800, 100, 300, 300);
		countdown.setFont(new Font("Arial", Font.BOLD, 250));
		countdown.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(countdown);
		this.add(contentPane);
		this.setUndecorated(true);
		this.addKeyListener(this);
		this.setVisible(true);
		this.laufen();

		// Configure Game
		standardConfig();

		// unsichtbarkeit des courses
		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0),
				"invisibleCursor");
		this.setCursor(transparentCursor);
	}

	private void standardConfig() {

		scoreLabel.setText("0 : 0");
		newGame = false;
//		tempCountCollision = 0;
		// einstellungesn des bots
		isRightPlayerBot = true; // sp = true = rechter spieler ist ein bot
		isLeftPlayerBot = false;// sp2 = true = linker spieler ist ein bot
		impossibleMode = false;
		difficulty = 0.2; // TODO: SCHWIERIGKEITSGRAD
		botSpeed = 14; // geschwindigkeit des bots TODO: SCHWIERIGKEITSGRAD
		erfassungsbereichDummerBot = 800; // umso höher umso kleiner der bereich TODO: SCHWIERIGKEITSGRAD
		// einstellung der restlichen optionen im spiel
		winkel = true;
		// startwerte für die winkel
		weitey = 5; // schrittweite des balls in y richtung
		weitex = 5; // schrittweite des balls in x richtung
		// Es gibt 3 verschiedene Abrallwinkel
		weitey1 = 4; // weitey1 - 3 und weitex1 - 3 sind die Winkeleinstellungen umso höher man diese
						// einstellt umso flacher der winkel
		weitex1 = 8;
		weitey2 = 5;
		weitex2 = 7;
		weitey3 = 6;
		weitex3 = 6;

		leftPlayerSpeed = 7;
		rightPlayerSpeed = 7;
		maxPunkte = 15;
		scoreLinks = 0;
		scoreRechts = 0;
		// änderung der Startrichtung des balles
		x1 = false;
		y1 = false;
	}

	public static void main(String[] args) {
		new offlineGame(); 
	}



	// startet den thread

	public void laufen() {
		if (!spielGestartet) {
			t = new MyThread();
			t.start();
			spielGestartet = true;

		}

	}

	// stoppt den thread und setzt den ball zurück und wartet 3 Sekunden bis er
	// wieder startet

	public void stoppen() {
		if (spielGestartet) {
			spielGestartet = false;
			ball.setLocation(940, 500);

			if (scoreLinks == maxPunkte) {
				timer1.schedule(new TimerTask() {
					public void run() {
						System.exit(0);
					}
				}, 5000);
				winner.setText("Linker Spieler hat mit " + scoreLinks + ":" + scoreRechts + " Gewonnen");
			}

			if (scoreRechts == maxPunkte) {
				timer1.schedule(new TimerTask() {
					public void run() {
						System.exit(0);
					}
				}, 5000);
				winner.setText("Rechter Spieler hat mit " + scoreRechts + ":" + scoreLinks + " Gewonnen");
			}

			if (scoreLinks < maxPunkte && scoreRechts < maxPunkte) {
				timer.schedule(new TimerTask() {
					public void run() {
						starten();
//						tempCountCollision = 0;

						System.out.println("ZURÜCKSETZEN");

						boostSpeed = 0;
						temphalfPixelSpeed = 0;
						kl = false;
						kr = false;
						weitex = 5;
						weitey = 5;
					}
				}, 3000);

				timer1.schedule(new TimerTask() {
					public void run() {
						countdown.setText("3");
					}
				}, 5);

				timer2.schedule(new TimerTask() {
					public void run() {
						countdown.setText("2");
					}
				}, 1000);

				timer3.schedule(new TimerTask() {
					public void run() {
						countdown.setText("1");
					}
				}, 2000);

				timer4.schedule(new TimerTask() {
					public void run() {
						countdown.setText("");
					}
				}, 3000);

			}
		}
	}

	// Startet den thread nachdem er gestoppt wurde

	public void starten() {
		if (!spielGestartet) {
			spielGestartet = true;
		}
	}

	public void ballBewegung(int x, int y) {
		Point p = ball.getLocation();

		p.x = p.x - x;
		p.y = p.y + y;

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
					if (ballY >= 40 && ballY <= 90) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey2 + tempBoostSpeed;
						weitex = weitex2 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 90 und 160 hatt ist der
					// winkel des balls 45°
					if (ballY >= 90 && ballY <= 160) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey3 + tempBoostSpeed;
						weitex = weitex3 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 160 und 210 hatt ist der
					// winkel leicht flacher als der normale winkel der 45° beträgt

					if (ballY >= 160 && ballY <= 210) {
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
					if (ballY >= 40 && ballY <= 90) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey2 + tempBoostSpeed;
						weitex = weitex2 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 90 und 160 hatt ist der
					// winkel des balls 45°
					if (ballY >= 90 && ballY <= 160) {
						int tempBoostSpeed = calcBoostSpeed();

						weitey = weitey3 + tempBoostSpeed;
						weitex = weitex3 + tempBoostSpeed;
					}

					// wenn der ball eine kollision mit einem wert zwischen 160 und 210 hatt ist der
					// winkel leicht flacher als der normale winkel der 45° beträgt

					if (ballY >= 160 && ballY <= 210) {
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

		// Seiten Kollision

		if (p.x < 0) { // kollision links hinten
			scoreRechts++;
			scoreLabel.setText(scoreLinks + " : " + scoreRechts);
			this.stoppen();
		}

		if (p.y < 0) { // kollision oben
			y1 = true;
		}

		if (ds.width <= p.x + ball.getSize().width) { // kollision rechts hinten
			scoreLinks++;
			scoreLabel.setText(scoreLinks + " : " + scoreRechts);
			this.stoppen();
		}

		if (ds.height <= p.y + ball.getSize().height) { // kollision unten
			y1 = false;
		}

		if (spielGestartet == true && newGame == true) {
			newGame = false;
			ball.setLocation(p);
		}

		if (spielGestartet == true && newGame == false) {
			ball.setLocation(p);
		}
	}
//  der bot

	public void botBewegungKomplex(int bally) {

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

			dumm = Math.random() * 100 < difficulty * 100;

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

			// bewegt den schläger in die richtung in die der ball sich bewegt

			if (spielGestartet == true && ball.getLocation().x > erfassungsbereichDummerBot) {

				if (dumm == false) {

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

	public void botBewegungEinfach(int bally) {
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
			System.out.println("I GOT A PIXEL");
		}		
		System.out.println("tempBoostSpeed: "+tempBoostSpeed+" boostSpeed: "+boostSpeed+" temphalfPixelSpeed2: "+temphalfPixelSpeed);
		return tempBoostSpeed;
	}
	
	class MyThread extends Thread {
		public void run() {

			while (true) {
				// während gestartet
				while (spielGestartet) {

					int xu = sLinks.getX();
					int yu = sLinks.getY();
					int x1u = sRechts.getX();
					int y1u = sRechts.getY();
					// wenn der linke bot true ist wird hierrüber die bewegungen des bots aufgerufen

					if (isRightPlayerBot == true) {
						botBewegungKomplex(ball.getLocation().y);

					}

					// wenn der rechte bot true ist wird hierrüber die bewegungen des bots
					// aufgerufen

					if (isLeftPlayerBot == true) {
						botBewegungEinfach(ball.getY() - 80);
					}

					if (x1 == true) {
						ballBewegung(-weitex, 0);
					}

					if (x1 == false) {
						ballBewegung(weitex, 0);
					}

					if (y1 == true) {
						ballBewegung(0, weitey);

					}

					if (y1 == false) {
						ballBewegung(0, -weitey);

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

					try {
						sleep(periodendauer);
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
				//Damit man die Schläger auch bewegen kann, wenn das Spiel nicht gestartet ist
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

				try {
					sleep(periodendauer);
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
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
			System.exit(0);
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

}
