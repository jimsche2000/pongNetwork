package hauptmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.MenuButton;
import pongtoolkit.ImageLoader;

public class LevelSelection extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1554442548630233138L;
	private MenuButton returnToMainMenu, playEasy, playMiddle, playHard, playCustom;
	private JLabel title, downTitle;
	private ImageIcon background;
	private JLabel backgroundLabel;
	private PongFrame pongFrame;
	
	public LevelSelection(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		Dimension preferredSize = pongFrame.getGraphicResolution();
		background = ImageLoader.loadIcon("CPU-Wallpaper2.jpg", preferredSize);
//		this.setSize(preferredSize);
//;		this.setLayout(null);
		this.setLayout(new BorderLayout());
		
		backgroundLabel = new JLabel();
//		backgroundLabel.setSize(new Dimension(1920, 1080));
		backgroundLabel.setPreferredSize(preferredSize);
//		backgroundLabel.setLocation(0, 0);
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new FlowLayout());
		
		title = new JLabel("<html>Einzelspieler</html>");
		title.setForeground(Color.white);
		title.setFont(pongFrame.getGLOBAL_FONT().deriveFont(preferredSize.height/15.0f));
//		title.setFont(pongFrame.getGLOBAL_FONT().deriveFont(0, 70));
//		title.setFont(new Font("Nevada", 0, 70));
//		title.setPreferredSize(new Dimension(1920, 150));
		title.setPreferredSize(new Dimension(preferredSize.width, preferredSize.height/5));
		title.setHorizontalAlignment(SwingConstants.CENTER);
//		title.setHorizontalTextPosition(SwingConstants.CENTER);
//		title.setVerticalTextPosition(SwingConstants.CENTER);
		title.setOpaque(false);
		
		downTitle = new JLabel("<html>Setze dir eigene Herausforderungen<br/>&nbsp&nbsp beim Spiel gegen den Computer &nbsp<br/><br/><br/></html>");
		downTitle.setForeground(Color.white);
		downTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(preferredSize.height/30.0f));
//		downTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(0, 30));
//		downTitle.setPreferredSize(new Dimension(1920, 150));
		downTitle.setPreferredSize(new Dimension(preferredSize.width, preferredSize.height/5));
		downTitle.setHorizontalAlignment(SwingConstants.CENTER);
		downTitle.setHorizontalTextPosition(SwingConstants.CENTER);
		downTitle.setVerticalTextPosition(SwingConstants.CENTER);
		downTitle.setOpaque(false);
		
		backgroundLabel.add(title);
		backgroundLabel.add(downTitle);
		
		JPanel difficultyDecision = new JPanel();
		difficultyDecision.setOpaque(false);
		difficultyDecision.setPreferredSize(new Dimension(Math.round(preferredSize.width/7), Math.round(preferredSize.height/2.0f)));
//		difficultyDecision.setPreferredSize(new Dimension(250, 400));
		playEasy = new MenuButton(pongFrame, "Einfach");
		playEasy.setSize(new Dimension(Math.round(preferredSize.width/7.5f), preferredSize.height/12));
//		playEasy.setSize(new Dimension(200, 75));
		playEasy.addActionListener(this);
//		backgroundLabel.add(playEasy);
		difficultyDecision.add(playEasy);

		playMiddle = new MenuButton(pongFrame, "Mittel");
		playMiddle.setSize(new Dimension(Math.round(preferredSize.width/7.5f), preferredSize.height/12));
		playMiddle.addActionListener(this);
//		backgroundLabel.add(playMiddle);
		difficultyDecision.add(playMiddle);
		
		playHard = new MenuButton(pongFrame, "Schwer");
		playHard.setSize(new Dimension(Math.round(preferredSize.width/7.5f), preferredSize.height/12));
		playHard.addActionListener(this);
//		backgroundLabel.add(playHard);
		difficultyDecision.add(playHard);
		
		playCustom = new MenuButton(pongFrame, "Custom");
		playCustom.setSize(new Dimension(Math.round(preferredSize.width/7.5f), preferredSize.height/12));
		playCustom.addActionListener(this);
//		backgroundLabel.add(playCustom);
		difficultyDecision.add(playCustom);
		
		returnToMainMenu = new MenuButton(pongFrame, "Zurück");
		returnToMainMenu.setSize(new Dimension(Math.round(preferredSize.width/7.5f), preferredSize.height/12));
		returnToMainMenu.addActionListener(this);
//		backgroundLabel.add(returnToMainMenu);
		difficultyDecision.add(returnToMainMenu);
		
		backgroundLabel.add(difficultyDecision);
		this.setBackground(Color.black);
		this.add(backgroundLabel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(returnToMainMenu)) {
			
			pongFrame.showPane(pongFrame.MAIN_MENU);
		}else if(e.getSource().equals(playEasy)) {
			pongFrame.getSinglePlayer().restartGame();
			pongFrame.getSinglePlayer().setDifficulty(pongFrame.getSinglePlayer().EASY_MODE);
			pongFrame.showPane(pongFrame.SINGLEPLAYER);
			
		}else if(e.getSource().equals(playMiddle)) {
			pongFrame.getSinglePlayer().restartGame();
			pongFrame.getSinglePlayer().setDifficulty(pongFrame.getSinglePlayer().MIDDLE_MODE);
			pongFrame.showPane(pongFrame.SINGLEPLAYER);
			
		}else if(e.getSource().equals(playHard)) {
			pongFrame.getSinglePlayer().restartGame();
			pongFrame.getSinglePlayer().setDifficulty(pongFrame.getSinglePlayer().HARD_MODE);
			pongFrame.showPane(pongFrame.SINGLEPLAYER);
			
		}else if(e.getSource().equals(playCustom)) {
			pongFrame.getSinglePlayer().restartGame();
			pongFrame.getSinglePlayer().setDifficulty(pongFrame.getSinglePlayer().CUSTOM_MODE);
			pongFrame.showPane(pongFrame.SINGLEPLAYER);
			
		}
	}
}
