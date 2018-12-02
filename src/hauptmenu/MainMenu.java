package hauptmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import gui.MenuButton;
//import gui.MenuPanel;
import pongtoolkit.ImageLoader;

public class MainMenu extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 3657118563693224777L;
	private JPanel buttonPanel;
	private MenuButton singlePlayer, multiPlayer, credits, leaveGame;
	private JLabel title, backgroundLabel;
	private ImageIcon background;
	private PongFrame pongFrame;
	private GridBagConstraints c;
	
	public MainMenu(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		Dimension preferredSize = pongFrame.getGraphicResolution();
		background = ImageLoader.loadIcon("menu-background-forest.jpg",preferredSize);
		this.setLayout(new BorderLayout());
		
		backgroundLabel = new JLabel();
		backgroundLabel.setPreferredSize(preferredSize);
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		singlePlayer = new MenuButton(pongFrame, "Einzelspieler");
		singlePlayer.addActionListener(this);
		singlePlayer.setSize(new Dimension(preferredSize.width/2, preferredSize.height/11));

		multiPlayer = new MenuButton(pongFrame, "Mehrspieler");
		multiPlayer.addActionListener(this);
		multiPlayer.setSize(new Dimension(preferredSize.width/2, preferredSize.height/11));
		
		credits = new MenuButton(pongFrame, "Credits");
		credits.addActionListener(this);
		credits.setSize(new Dimension(preferredSize.width/2, preferredSize.height/11));
		
		leaveGame = new MenuButton(pongFrame, "Spiel beenden");
		leaveGame.addActionListener(this);
		leaveGame.setSize(new Dimension(preferredSize.width/2, preferredSize.height/11));
		
		title = new JLabel("<html>P  O  N  G    -    E  A  I  T  6</html>");
		title.setForeground(Color.white);
		title.setFont(pongFrame.getGLOBAL_FONT().deriveFont(preferredSize.height/17.5f));
		title.setOpaque(false);
		
		buttonPanel.add(singlePlayer);
		buttonPanel.add(multiPlayer);
		buttonPanel.add(credits);
		buttonPanel.add(leaveGame);
		buttonPanel.setOpaque(false);
		
			c.weightx = 0;
			c.weighty = 0.3;
			c.gridy = 0; //Reihe
			c.gridx = 0;//Spalte
			c.insets = new Insets(Math.round(getPreferredSize().height/1500), 0, 0, 0); //Genug Abstand zwischen oben und titel
//			c.anchor = GridBagConstraints.PAGE_START;
//			c.fill = GridBagConstraints.HORIZONTAL;
		backgroundLabel.add(title, c);
		
			c.weightx = 0;
			c.weighty = 1;
			c.gridy = GridBagConstraints.RELATIVE; //Unter dem ersten Component
			c.gridx = 0;
			c.anchor = GridBagConstraints.CENTER;
			c.insets = new Insets(0, 0, Math.round(getPreferredSize().height/10), 0); //Genug Abstand zwischen boden und button-panel
			c.ipady = (int) Math.round(getPreferredSize().height/3.5);
			c.ipadx = Math.round(getPreferredSize().width/3);
	//		c.fill = GridBagConstraints.HORIZONTAL;
		backgroundLabel.add(buttonPanel, c);
		this.setBackground(Color.black);
		this.add(backgroundLabel, BorderLayout.CENTER);
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
