package hauptmenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.MenuButton;
import gui.MenuLabel;
//import gui.MenuPanel;
import pongtoolkit.ImageLoader;

public class MainMenu extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 3657118563693224777L;
//	private MenuPanel buttonPanel, contentPane;
	private JPanel buttonPanel, contentPane;
	private MenuButton singlePlayer, multiPlayer, credits, leaveGame;
//	private MenuLabel title;
	private JLabel title, backgroundLabel;
	private ImageIcon background = ImageLoader.loadIcon("menu-background-forest.jpg");
	private PongFrame pongFrame;
	
	public MainMenu(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		this.setAlignmentY(SwingConstants.CENTER);
		this.setAlignmentX(SwingConstants.CENTER);
//		this.setSize(new Dimension(500, 600));
//		this.setMaximumSize(new Dimension(500, 600));
//		this.setLayout(new BorderLayout());
//		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(null);
		
		backgroundLabel = new JLabel();
		backgroundLabel.setSize(1920, 1080);
//		backgroundLabel.setLocation(0, 0);
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new FlowLayout());
//		buttonPanel = new MenuPanel(pongFrame);
//		buttonPanel.setLayout(new GridLayout(4, 1));
//		buttonPanel.setAlignmentY(SwingConstants.CENTER);
//		buttonPanel.setSize(800, 400);
//		contentPane = new MenuPanel(pongFrame);
//		contentPane.setAlignmentY(SwingConstants.CENTER);
//		contentPane.setSize(pongFrame.getGraphicResolution());
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));
		buttonPanel.setAlignmentY(SwingConstants.CENTER);
		buttonPanel.setSize(800, 400);
		buttonPanel.setPreferredSize(new Dimension(800,400));
		contentPane = new JPanel();
		contentPane.setAlignmentY(SwingConstants.CENTER);
		contentPane.setAlignmentX(SwingConstants.CENTER);
//		contentPane.setSize(pongFrame.getGraphicResolution());
		contentPane.setSize(1920,1080);
		contentPane.setMaximumSize(new Dimension(1920, 1080));
		contentPane.setPreferredSize(new Dimension(1920, 1080));
		//		contentPane.setLayout(new GridLayout(2, 1));
		
//		buttonPanel.setMaximumSize(new Dimension(500, 600));
//		buttonPanel.setPreferredSize(new Dimension(500, 600));
//		singlePlayer.setMaximumSize(new Dimension(500, 150));
//		singlePlayer.setSize(new Dimension(500, 150));
		singlePlayer = new MenuButton(pongFrame, "Einzelspieler");
		singlePlayer.addActionListener(this);
//		singlePlayer.addMouseMotionListener(f);
		multiPlayer = new MenuButton(pongFrame, "Mehrspieler");
		multiPlayer.addActionListener(this);
//		multiPlayer.addMouseMotionListener(f);
		credits = new MenuButton(pongFrame, "Credits");
		credits.setFont(multiPlayer.getFont().deriveFont(60f));
		credits.addActionListener(this);
//		credits.addMouseMotionListener(f);
		leaveGame = new MenuButton(pongFrame, "Spiel beenden");
		leaveGame.addActionListener(this);
//		leaveGame.addMouseMotionListener(f);
//		title = new MenuLabel(pongFrame,"<html>P  O  N  G    -    E  A  I  T  6<br/><br/><br/></html>");
		title = new JLabel("<html><br/>P  O  N  G    -    E  A  I  T  6<br/><br/><br/></html>");
		title.setSize(1920, 300);
		title.setForeground(Color.white);
		title.setFont(pongFrame.getGLOBAL_FONT().deriveFont(0, 80));
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setOpaque(false);
		
		contentPane.add(title);
//		buttonPanel.setPreferredSize(new Dimension(800, 400));
//		buttonPanel.setMaximumSize(new Dimension(800, 400));
		buttonPanel.setAlignmentY(SwingConstants.CENTER);
		buttonPanel.add(singlePlayer);
		buttonPanel.add(multiPlayer);
		buttonPanel.add(credits);
		buttonPanel.add(leaveGame);
		buttonPanel.setOpaque(false);
		contentPane.add(buttonPanel);
		contentPane.setOpaque(false);
		backgroundLabel.add(contentPane);
		this.add(backgroundLabel);
//		System.out.println("this: "+this.getBounds()+" backg: "+backgroundLabel.getBounds()+" cont: "+contentPane.getBounds());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(singlePlayer)) {
//			System.out.println(getLocationOnScreen());
			pongFrame.showPane(pongFrame.LEVEL_SELECTION);
			
		}else if(e.getSource().equals(multiPlayer)) {
			
			pongFrame.showPane(pongFrame.MULTI_PLAYER);
			
		}else if(e.getSource().equals(credits)) {
			
			pongFrame.showPane(pongFrame.CREDITS);
			
		}else if(e.getSource().equals(leaveGame)) {
//			System.out.println("B2: "+this.getBounds());
//			System.out.println("this: "+this.getBounds()+" backg: "+backgroundLabel.getBounds()+" cont: "+contentPane.getBounds());
			System.exit(0);
		}
	}	
}
