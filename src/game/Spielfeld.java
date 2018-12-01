package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import hauptmenu.PongFrame;
import pongtoolkit.ImageLoader;

public class Spielfeld extends JPanel implements KeyListener {


	private static final long serialVersionUID = 8592435655761040011L;
	private JPanel contentPane;
	private JLabel schlagL, schlagR;
	private JLabel punktestand;
	private Ball ball;

	public final int SPECTATOR = -1;
	public final int PLAYER_RIGHT = 0;
	public final int PLAYER_LEFT = 1;
//	private int size = 50;	
	public boolean richtungx;
	public boolean richtungy;
	public boolean xnull;
	public boolean ynull;
	private boolean up,down,up1,down1;
	private boolean playerRightActivated = false;
	private boolean playerLeftActivated = false;
	private ImageIcon schlägerIcon = ImageLoader.loadIcon("Schläger.png");
	private ImageIcon schlägerGREENIcon = ImageLoader.loadIcon("Schläger_GREEEEEEEEEEEEEEEEEEEEEN.png");
//	private boolean playable = false;
	MyThread t;
	int periodendauer = 2; // Milisekunden
	boolean gestartet = false;
	PongFrame pongFrame;
	
	public Spielfeld(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		Dimension newSize = new Dimension(pongFrame.getSize().width, pongFrame.getSize().height);
		this.setLayout(null);
		this.setSize(newSize);
		this.setLocation(0, 0);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setSize(newSize);

		schlagL = new JLabel();
		schlagR = new JLabel();
		punktestand = new JLabel("0 : 0");
		
		
		schlagL.setBounds(10, (newSize.height-200)/2, 10, 200);
		schlagL.setBackground(Color.BLACK);
		schlagL.setIcon(schlägerIcon);
		
		schlagR.setBounds(newSize.width-40, (newSize.height-200)/2, 10, 200);
		schlagR.setBackground(Color.BLACK);
		schlagR.setIcon(schlägerIcon);
		
		ball = new Ball();
		ball.setSize(50, 50);
		ball.setLocation((pongFrame.getWidth()-ball.getWidth())/2, (pongFrame.getHeight()-ball.getHeight())/2);
		
		punktestand.setBounds((newSize.width-200)/2, 10, 200, 50);
		punktestand.setHorizontalAlignment(SwingConstants.CENTER);
		punktestand.setText("0 : 0");
		punktestand.setFont(new Font("Arial", Font.BOLD, 30));
		
		contentPane.add(schlagL);
		contentPane.add(schlagR);
		contentPane.add(punktestand);
		contentPane.add(ball);
		
		 KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		    manager.addKeyEventDispatcher(new MyDispatcher());
//		this.setBackground(Color.YELLOW);
		contentPane.setBackground(Color.WHITE);
		this.add(contentPane);
	}

	public JLabel getSchlagL() {
		return schlagL;
	}

//	public void setSchlagL(JPanel schlagL) {
//		Spielfeld.schlagL = schlagL;
//	}

	public JLabel getSchlagR() {
		return schlagR;
	}

//	public static void setSchlagR(JPanel schlagR) {
//		Spielfeld.schlagR = schlagR;
//	}

	public JLabel getPunktestand() {
		return punktestand;
	}

	public void setPunktestand(JLabel punktestand) {
		this.punktestand = punktestand;
	}

//	public static void main(String[] args) {
//		JFrame f = new JFrame();
//		f.setLayout(null);
//		f.setSize(800, 800);
//		f.setLocationRelativeTo(null);
//		Spielfeld sF = new Spielfeld();
//		sF.configSF(PLAYER_RIGHT);
//		f.add(sF);
//		f.setVisible(true);
//	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		if(playerLeftActivated) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				up = false;
			}
	
			if (e.getKeyCode() == KeyEvent.VK_S) {
				down = false;
			}
		}
		if(playerRightActivated) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				up1= false;
			}
	
				if (e.getKeyCode() == KeyEvent.VK_S) {
				down1 = false;
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		int x  = schlagL.getX();
//		int y  = schlagL.getY();
//		int x1 = schlagR.getX();
//		int y1 = schlagR.getY();
		if(playerLeftActivated) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				up = true;
				}
	
			if (e.getKeyCode() == KeyEvent.VK_S) {
				down = true;
			}
		}
		if(playerRightActivated) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
			up1= true;
			}
	
			if (e.getKeyCode() == KeyEvent.VK_S) {
			down1 = true;
			}
		}
	}

	public void configSF(int player) {
		
		playerRightActivated = false;
		playerLeftActivated = false;
		schlagL.setIcon(schlägerIcon);
		schlagR.setIcon(schlägerIcon);
		switch (player) {
		case PLAYER_RIGHT:
			playerRightActivated = true;
			schlagR.setIcon(schlägerGREENIcon);
			break;
		case PLAYER_LEFT:
			playerLeftActivated = true;
			schlagL.setIcon(schlägerGREENIcon);
			break;
		default:
			break;
		}
		sendLocationsToServer();
		laufen();
	}
	public void stopInGameSliderControl() {
		
		playerRightActivated = false;
		playerLeftActivated = false;
		
		gestartet = false;
	}
	
	Point lastPoint = new Point(1,1);

	private void sendLocationsToServer() {
//		System.out.println("----------\nSEND_TO_SERVER");
		String orientation = "SPECTATOR";
		Point p = lastPoint;
		if(playerRightActivated) {
			orientation = "RIGHT";
			p = schlagR.getLocation();
//			System.out.println("SCHLAGR: "+schlagR.getLocation());
		}else if(playerLeftActivated) {
			orientation = "LEFT";
			p = schlagL.getLocation();
//			System.out.println("SCHLAGL: "+schlagL.getLocation());
		}
		if(!p.equals(lastPoint))pongFrame.getClientThread().sendMessageToServer(pongFrame.getClientThread().getNO_CHAT_MESSAGE()+pongFrame.getClientThread().getIN_GAME_POSITIONS()+"{ORIENTATION="+orientation+"}{X="+p.x+"}{Y="+p.y+"}");
		p = lastPoint;
//		System.out.println("---------------------");
	}
	
	public void updateLocations(PongLocationData pLD) {
//		System.out.println("--------------------\nupdateLocations: "+pLD);
//		System.out.println("Right? "+playerRightActivated+" Left? "+playerLeftActivated);
		if(!playerRightActivated) {
//			System.out.println("Update playerRight");
			schlagR.setLocation(pLD.getSliderRight());
		}
		if(!playerLeftActivated) {
//			System.out.println("Update PlayerLeft");
			schlagL.setLocation(pLD.getSliderLeft());
		}
//		System.out.println("Update Ball");
		ball.setLocation(pLD.getBall());
//		System.out.println("--------------------");
		punktestand.setText(pLD.getScore());
	}
	
	public void laufen() {
		if (!gestartet) {
			t = new MyThread();
			t.start();
			gestartet = true;
		}
	 }
	
	class MyThread extends Thread {
		public void run() {

			while(true){

				if(gestartet) {
					int xu = schlagL.getX();
					int yu = schlagL.getY();
					int x1u = schlagR.getX();
					int y1u = schlagR.getY();

					int schrittweite = 3;
					
					if(playerLeftActivated) {
						if(up == true && schlagL.getY() != 0){
							yu = yu - schrittweite;
							schlagL.setLocation(xu,yu);
						}
						
						if(down == true && schlagL.getY() <= 850){
							yu = yu + schrittweite;
							schlagL.setLocation(xu,yu);
						} 
					}
					
					if(playerRightActivated) {
						if(up1 == true && schlagR.getY() != 0){
							y1u = y1u - schrittweite;
							schlagR.setLocation(x1u,y1u);
						}
						
						if(down1 == true && schlagR.getY() <= 850){
							y1u = y1u + schrittweite;
							schlagR.setLocation(x1u,y1u);
						}
					}
					sendLocationsToServer();
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

//		private void gehe(int i, int j) {
//			if(playerLeftActivated) {
//				
//				Point p = schlagL.getLocation();
//				schlagL.setLocation(p.x+i, p.y+j);
//			}else if(playerRightActivated) {
//				Point p = schlagR.getLocation();
//				schlagR.setLocation(p.x+i, p.y+j);			
//			}
//		}
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
}
