package hauptmenu;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
	private MenuButton returnToMainMenu, playEasy, playMiddle, playHard, playCustom; // NORMAL SELECTION
	private MenuButton returnToNormalSelection, playNormal, playBotvsBot, playKOOP, startPlaying; // CUSTOM SELECTION
	private MenuTextField leftName, rightName;
	private JLabel title, downTitle;
	private ImageIcon background;
	private JLabel backgroundLabel;
	private MenuToggleSwitchButton toggleSwitch;
	private CardLayout layout = new CardLayout();
	private JPanel changePanel, normalSelectionPanel, customSelectionPanel;
	private PongFrame pongFrame;
	private String RIGHT_BOT = "Rechter Bot", LEFT_BOT = "Linker Bot";
	private final int NORMAL_GAME_MODE = 200;
	private final int BOT_VS_BOT_GAME_MODE = 201;
	private final int KOOP_GAME_MODE = 202;
	private int SELECTED_GAME_MODE;
	public LevelSelection(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		Dimension preferredSize = pongFrame.getGraphicResolution();
		background = ImageLoader.loadIcon("CPU-Wallpaper2.jpg", preferredSize);
		this.setLayout(new BorderLayout());
		
		SELECTED_GAME_MODE = NORMAL_GAME_MODE;
		
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

		leftName = new MenuTextField(pongFrame, "Spieler");
		leftName.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20.0f * pongFrame.getASPECT_RATIO()));
		leftName.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()),
				Math.round(50 * pongFrame.getASPECT_RATIO())));
		leftName.setDocument(new JTextFieldCharLimit(14));
		leftName.setText("Spieler");
		rightName = new MenuTextField(pongFrame, RIGHT_BOT);
		rightName.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20.0f * pongFrame.getASPECT_RATIO()));
		rightName.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()),
				Math.round(50 * pongFrame.getASPECT_RATIO())));
		rightName.setDocument(new JTextFieldCharLimit(14));
		rightName.setText(RIGHT_BOT);
		rightName.setEditable(false);
		toggleSwitch = new MenuToggleSwitchButton(pongFrame, Math.round(100 * pongFrame.getASPECT_RATIO()),
				Math.round(50 * pongFrame.getASPECT_RATIO()));
		leftOrRightPlayer.add(leftName);
		leftOrRightPlayer.add(toggleSwitch);
		leftOrRightPlayer.add(rightName);
		backgroundLabel.add(leftOrRightPlayer);

		changePanel = new JPanel();
		changePanel.setOpaque(false);
		changePanel.setLayout(layout);

		/**
		 * Custom-Selection:
		 */
		customSelectionPanel = new JPanel();
		customSelectionPanel.setOpaque(false);
		customSelectionPanel.setLayout(new GridLayout(0, 3, Math.round(10 * pongFrame.getASPECT_RATIO()),
				Math.round(10 * pongFrame.getASPECT_RATIO())));

		playNormal = new MenuButton(pongFrame, "Spieler vs Bot");
		playNormal.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playNormal.addActionListener(this);
		playNormal.setEnabled(false);
		customSelectionPanel.add(playNormal);

		playBotvsBot = new MenuButton(pongFrame, "Bot vs Bot");
		playBotvsBot.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playBotvsBot.addActionListener(this);
		customSelectionPanel.add(playBotvsBot);

		playKOOP = new MenuButton(pongFrame, "Spieler vs Spieler [KOOP]");
		playKOOP.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playKOOP.addActionListener(this);
		customSelectionPanel.add(playKOOP);

		returnToNormalSelection = new MenuButton(pongFrame, "Zurück");
		returnToNormalSelection
				.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		returnToNormalSelection.addActionListener(this);
		customSelectionPanel.add(returnToNormalSelection);
		
		startPlaying = new MenuButton(pongFrame, "Spiel Starten");
		startPlaying.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		startPlaying.addActionListener(this);
		customSelectionPanel.add(startPlaying);

		/*
		 * \ \ Custom-Selection / Normal-Selection: \
		 */

		normalSelectionPanel = new JPanel();
		normalSelectionPanel.setOpaque(false);
		normalSelectionPanel.setLayout(new GridLayout(0, 3, Math.round(10 * pongFrame.getASPECT_RATIO()),
				Math.round(10 * pongFrame.getASPECT_RATIO())));
		playEasy = new MenuButton(pongFrame, "Einfach");
		playEasy.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playEasy.addActionListener(this);
		normalSelectionPanel.add(playEasy);

		playMiddle = new MenuButton(pongFrame, "Mittel");
		playMiddle.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playMiddle.addActionListener(this);
		normalSelectionPanel.add(playMiddle);

		playHard = new MenuButton(pongFrame, "Schwer");
		playHard.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playHard.addActionListener(this);
		normalSelectionPanel.add(playHard);

		returnToMainMenu = new MenuButton(pongFrame, "Zurück");
		returnToMainMenu
				.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		returnToMainMenu.addActionListener(this);
		normalSelectionPanel.add(returnToMainMenu);

		playCustom = new MenuButton(pongFrame, "Custom");
		playCustom.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playCustom.addActionListener(this);
		normalSelectionPanel.add(playCustom);
		/*
		 * \Normal-Selection
		 */
		changePanel.add(normalSelectionPanel, "normal");
		changePanel.add(customSelectionPanel, "custom");
		backgroundLabel.add(changePanel);
		layout.show(changePanel, "normal");
//		backgroundLabel.add(normalSelectionPanel);
		this.setBackground(Color.black);
		this.add(backgroundLabel, BorderLayout.CENTER);
	}

	// FÜR KOOP: BEIDE EDITABLE, FARBE VOM TOGGLESWITCH IN KOMPLETT GRÜN ÄNDERN,
	// KOOP BUTTON ALS CHECKBOX
	public void changeTextFields() {
		if (rightName.getText().equals(RIGHT_BOT)) {
			rightName.setText(leftName.getText());
			rightName.setEditable(true);
			leftName.setText(LEFT_BOT);
			leftName.setEditable(false);
		} else if (leftName.getText().equals(LEFT_BOT)) {
			leftName.setText(rightName.getText());
			leftName.setEditable(true);
			rightName.setText(RIGHT_BOT);
			rightName.setEditable(false);
		}
	}

	// Last Data from normal Menu:
	private boolean activated = false;
	private String lastLeftName = "Spieler Links", lastRightName = "Spieler Rechts", tempLeftName = " ",
			tempRightName = " "; // FOR CUSTOM

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(returnToMainMenu)) { // SHOW MAIN-MENU
			pongFrame.showPane(pongFrame.MAIN_MENU);

		} else if (e.getSource().equals(playCustom)) { // SHOW CUSTOM SELECTION-MENU
			activated = toggleSwitch.isActivated();
			lastLeftName = leftName.getText();
			lastRightName = rightName.getText();
			layout.show(changePanel, "custom");

		} else if (e.getSource().equals(returnToNormalSelection)) { // SHOW NORMAL SELECTION-MENU
			toggleSwitch.setActivated(activated);
			leftName.setText(lastLeftName);
			rightName.setText(lastRightName);
			if (leftName.getText().contains("Bot")) {
				leftName.setEditable(false);
				rightName.setEditable(true);
				if (!tempRightName.equals(" ")) {
					rightName.setText(tempRightName);
					tempRightName = " ";
				} else {
					rightName.setText("Spieler");
				}
			} else if (rightName.getText().contains("Bot")) {
				leftName.setEditable(true);
				rightName.setEditable(false);
				if (!tempLeftName.equals(" ")) {
					leftName.setText(tempLeftName);
					tempLeftName = " ";
				} else {
					leftName.setText("Spieler");
				}
			}
			layout.show(changePanel, "normal");

		} else if (e.getSource().equals(playNormal)) { // PLAYER VS BOT
			playNormal.setEnabled(false);
			playBotvsBot.setEnabled(true);
			playKOOP.setEnabled(true);
			if (toggleSwitch.isActivated()) {// TRUE=RIGHT
				leftName.setEditable(false);
				rightName.setEditable(true);
				leftName.setText(LEFT_BOT);
				if (!tempRightName.equals(" ")) {
					rightName.setText(tempRightName);
					tempRightName = " ";
				} else {
					rightName.setText("Spieler 2");
				}
			} else {
				leftName.setEditable(true);
				rightName.setEditable(false);
				rightName.setText(RIGHT_BOT);
				if (!tempLeftName.equals(" ")) {
					leftName.setText(tempLeftName);
					tempLeftName = " ";
				} else {
					leftName.setText("Spieler");
				}
			}
			SELECTED_GAME_MODE = NORMAL_GAME_MODE;
		} else if (e.getSource().equals(playBotvsBot)) { // BOT VS BOT
			playNormal.setEnabled(true);
			playBotvsBot.setEnabled(false);
			playKOOP.setEnabled(true);
			if (!toggleSwitch.isActivated()) {
				tempLeftName = leftName.getText();
			} else {
				tempRightName = rightName.getText();
			}
			leftName.setText(LEFT_BOT);
			leftName.setEditable(false);
			rightName.setText(RIGHT_BOT);
			rightName.setEditable(false);
			SELECTED_GAME_MODE = BOT_VS_BOT_GAME_MODE;
		} else if (e.getSource().equals(playKOOP)) { // PLAYER VS PLAYER
			playNormal.setEnabled(true);
			playBotvsBot.setEnabled(true);
			playKOOP.setEnabled(false);
			leftName.setEditable(true);
			rightName.setEditable(true);
			if(!toggleSwitch.isActivated()) {//Linker Spieler
				if (!tempLeftName.equals(" ")) {
					leftName.setText(tempLeftName);
					tempLeftName = " ";
				} else {
					leftName.setText("Spieler");
				}
				rightName.setText("Spieler 2");
			}else {
				if (!tempRightName.equals(" ")) {
					rightName.setText(tempRightName);
					tempRightName = " ";
				} else {
					rightName.setText("Spieler 2");
				}
				leftName.setText("Spieler");
			}
			if(leftName.getText().equals(rightName.getText())) {
				rightName.setText(rightName.getText()+" 2");
			}
			SELECTED_GAME_MODE = KOOP_GAME_MODE;
		}else if(e.getSource().equals(startPlaying)){ //START CUSTOM-GAME
			
			/* TODO: Alle Elemente für Nötige Einstellungen Sinnvoll unterbringen 
			 * TODO: Alle Einstellungen an Singleplayer schicken, und das Customisierte Spiel starten!
			 */
			boolean isLeftPlayerBot = false, isRightPlayerBot = true;
			
			switch(SELECTED_GAME_MODE) {
				case NORMAL_GAME_MODE:
					isLeftPlayerBot = toggleSwitch.isActivated();
					isRightPlayerBot = !toggleSwitch.isActivated();
					break;
				case BOT_VS_BOT_GAME_MODE:
					isLeftPlayerBot = true;
					isRightPlayerBot = true;
					break;
				case KOOP_GAME_MODE:
					isLeftPlayerBot = false;
					isRightPlayerBot = false;
					break;
			}

			pongFrame.getSinglePlayer().setNameLabel(leftName.getText(), rightName.getText());
			pongFrame.getSinglePlayer().restartGame(pongFrame.getSinglePlayer().MIDDLE_MODE, isLeftPlayerBot, isRightPlayerBot);
			pongFrame.showPane(pongFrame.SINGLEPLAYER);
			
		} else { // START GAME
			pongFrame.getSinglePlayer().setPlayerLeftRight(toggleSwitch.isActivated());
			pongFrame.getSinglePlayer().setNameLabel(leftName.getText(), rightName.getText());
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
