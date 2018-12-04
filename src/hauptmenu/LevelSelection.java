package hauptmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.JTextFieldCharLimit;
import gui.MenuButton;
import gui.MenuTextField;
import gui.MenuToggleSwitchButton;
import pongtoolkit.ImageLoader;

public class LevelSelection extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1554442548630233138L;
	private MenuButton returnToMainMenu, playEasy, playMiddle, playHard, playCustom, playKOOP;
	private MenuTextField leftName, rightName;
	private JLabel title, downTitle;
	private ImageIcon background;
	private JLabel backgroundLabel;
	private MenuToggleSwitchButton toggleSwitch;
	private PongFrame pongFrame;
	private String RIGHT_BOT = "Rechter Bot", LEFT_BOT = "Linker Bot";

	public LevelSelection(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		Dimension preferredSize = pongFrame.getGraphicResolution();
		background = ImageLoader.loadIcon("CPU-Wallpaper2.jpg", preferredSize);
		this.setLayout(new BorderLayout());

		backgroundLabel = new JLabel();
		backgroundLabel.setPreferredSize(preferredSize);
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new FlowLayout());

		title = new JLabel("<html>Einzelspieler</html>");
		title.setForeground(Color.white);
		title.setFont(pongFrame.getGLOBAL_FONT().deriveFont(preferredSize.height / 15.0f));
		title.setPreferredSize(new Dimension(preferredSize.width, preferredSize.height / 5));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setOpaque(false);

		downTitle = new JLabel(
				"<html>Setze dir eigene Herausforderungen<br/>&nbsp&nbsp beim Spiel gegen den Computer &nbsp<br/><br/><br/></html>");
		downTitle.setForeground(Color.white);
		downTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(preferredSize.height / 30.0f));
		downTitle.setPreferredSize(new Dimension(preferredSize.width, preferredSize.height / 5));
		downTitle.setHorizontalAlignment(SwingConstants.CENTER);
		downTitle.setHorizontalTextPosition(SwingConstants.CENTER);
		downTitle.setVerticalTextPosition(SwingConstants.CENTER);
		downTitle.setOpaque(false);

		backgroundLabel.add(title);
		backgroundLabel.add(downTitle);

		JPanel leftOrRightPlayer = new JPanel();
		leftOrRightPlayer.setOpaque(false);
		leftOrRightPlayer.setBorder(BorderFactory.createEmptyBorder(1, 300, 1, 300));
		leftOrRightPlayer.setPreferredSize(new Dimension(Math.round(1920 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));

		leftName = new MenuTextField(pongFrame,"Spieler");
		leftName.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20.0f * pongFrame.getASPECT_RATIO()));
		leftName.setSize(new Dimension(Math.round(300*pongFrame.getASPECT_RATIO()), Math.round(50*pongFrame.getASPECT_RATIO())));
		leftName.setDocument(new JTextFieldCharLimit(14));
		leftName.setText("Spieler");
		rightName = new MenuTextField(pongFrame,RIGHT_BOT);
		rightName.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20.0f * pongFrame.getASPECT_RATIO()));
		rightName.setSize(new Dimension(Math.round(300*pongFrame.getASPECT_RATIO()), Math.round(50*pongFrame.getASPECT_RATIO())));
		rightName.setDocument(new JTextFieldCharLimit(14));
		rightName.setText(RIGHT_BOT);
		rightName.setEditable(false);
//		JLabel left = new JLabel("Linker Spieler"), right = new JLabel("Rechter Spieler");
//		left.setForeground(Color.black);
//		left.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20.0f * pongFrame.getASPECT_RATIO()));
//		right.setForeground(Color.black);
//		right.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20.0f * pongFrame.getASPECT_RATIO()));

		toggleSwitch = new MenuToggleSwitchButton(pongFrame, Math.round(100 * pongFrame.getASPECT_RATIO()),
				Math.round(50 * pongFrame.getASPECT_RATIO()));
		leftOrRightPlayer.add(leftName);
		leftOrRightPlayer.add(toggleSwitch);
		leftOrRightPlayer.add(rightName);

		backgroundLabel.add(leftOrRightPlayer);

		JPanel difficultyDecision = new JPanel();
		difficultyDecision.setOpaque(false);
		difficultyDecision.setLayout(new GridLayout(0, 3, Math.round(10 * pongFrame.getASPECT_RATIO()),
				Math.round(10 * pongFrame.getASPECT_RATIO())));
		playEasy = new MenuButton(pongFrame, "Einfach");
		playEasy.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playEasy.addActionListener(this);
		difficultyDecision.add(playEasy);

		playMiddle = new MenuButton(pongFrame, "Mittel");
		playMiddle.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playMiddle.addActionListener(this);
		difficultyDecision.add(playMiddle);

		playHard = new MenuButton(pongFrame, "Schwer");
		playHard.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playHard.addActionListener(this);
		difficultyDecision.add(playHard);

		returnToMainMenu = new MenuButton(pongFrame, "Zurück");
		returnToMainMenu
				.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		returnToMainMenu.addActionListener(this);
		difficultyDecision.add(returnToMainMenu);

		playCustom = new MenuButton(pongFrame, "Custom");
		playCustom.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playCustom.addActionListener(this);
		difficultyDecision.add(playCustom);

		playKOOP = new MenuButton(pongFrame, "KOOP");
		playKOOP.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playKOOP.addActionListener(this);
		difficultyDecision.add(playKOOP);

		backgroundLabel.add(difficultyDecision);
		this.setBackground(Color.black);
		this.add(backgroundLabel, BorderLayout.CENTER);
	}
	//FÜR KOOP: BEIDE EDITABLE, FARBE VOM TOGGLESWITCH IN KOMPLETT GRÜN ÄNDERN, KOOP BUTTON ALS CHECKBOX
	public void changeTextFields() {
		if(rightName.getText().equals(RIGHT_BOT)) {
			rightName.setText(leftName.getText());
			rightName.setEditable(true);
			leftName.setText(LEFT_BOT);
			leftName.setEditable(false);
		}else if(leftName.getText().equals(LEFT_BOT)) {
			leftName.setText(rightName.getText());
			leftName.setEditable(true);
			rightName.setText(RIGHT_BOT);
			rightName.setEditable(false);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(returnToMainMenu)) {

			pongFrame.showPane(pongFrame.MAIN_MENU);
		} else {
			pongFrame.getSinglePlayer().setPlayerLeftRight(toggleSwitch.isActivated());
			pongFrame.getSinglePlayer().setNameLabel(leftName.getText(),rightName.getText());
			if (e.getSource().equals(playEasy)) {
				pongFrame.getSinglePlayer().restartGame(pongFrame.getSinglePlayer().EASY_MODE);
				pongFrame.showPane(pongFrame.SINGLEPLAYER);

			} else if (e.getSource().equals(playMiddle)) {
				pongFrame.getSinglePlayer().restartGame(pongFrame.getSinglePlayer().MIDDLE_MODE);
				pongFrame.showPane(pongFrame.SINGLEPLAYER);

			} else if (e.getSource().equals(playHard)) {
				pongFrame.getSinglePlayer().restartGame(pongFrame.getSinglePlayer().HARD_MODE);
				pongFrame.showPane(pongFrame.SINGLEPLAYER);

			}
		}
	}
}
