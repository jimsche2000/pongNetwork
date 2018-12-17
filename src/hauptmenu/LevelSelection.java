package hauptmenu;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import gui.MenuLabel;
import gui.MenuSlider;
import gui.MenuTextField;
import gui.MenuToggleSwitchButton;
import pongtoolkit.ImageLoader;

public class LevelSelection extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1554442548630233138L;
	private MenuButton returnToMainMenu, playEasy, playMiddle, playHard, playCustom; // NORMAL SELECTION
	private MenuButton playNormal, playBotvsBot, playKOOP, startPlaying, editSpecial; // CUSTOM SELECTION
	private MenuButton returnToNormalSelection; // SPECIAL SELECTION
	private MenuSlider botFailFactorSlider, botSpeedSlider, erfassungsbereichBotLinksSlider,
			erfassungsbereichBotRechtsSlider, ballSpeedSlider, playerSpeedLeftSlider, playerSpeedRightSlider;
	private MenuLabel botFailFactorLabel, botSpeedLabel, erfassungsbereichBotLinksLabel,
			erfassungsBereichBotRechtsLabel, ballSpeedLabel, playerSpeedLeftLabel, playerSpeedRightLabel;
	private MenuTextField leftName, rightName;
	private JLabel title, downTitle;
	private ImageIcon background;
	private JLabel backgroundLabel;
	private MenuToggleSwitchButton toggleSwitch;
	private CardLayout layout = new CardLayout();
	private JPanel changePanel, normalSelectionPanel, specialSelectionPanel, sliderPanel;
	private PongFrame pongFrame;
	private GridBagLayout gbl;
	private String RIGHT_BOT = "Rechter Bot", LEFT_BOT = "Linker Bot";
	private final int NORMAL_GAME_MODE = 200;
	private final int BOT_VS_BOT_GAME_MODE = 201;
	private final int KOOP_GAME_MODE = 202;
	private int SELECTED_GAME_MODE;
	private final int EASY_MODE = 0;
	private final int MIDDLE_MODE = 1;
	private final int HARD_MODE = 2;

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
				"<html> <head></head> <body> <div style=text-align: center;>Setze dir eigene Herausforderungen<br>beim Spiel gegen den Computer</div></body> </html>");
		downTitle.setForeground(Color.white);
		downTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(preferredSize.height / 30.0f));
		downTitle.setPreferredSize(new Dimension(preferredSize.width, preferredSize.height / 5));
		downTitle.setHorizontalAlignment(SwingConstants.CENTER);
		downTitle.setHorizontalTextPosition(SwingConstants.CENTER);
		downTitle.setVerticalTextPosition(SwingConstants.CENTER);
		downTitle.setOpaque(false);

		backgroundLabel.add(title);
		backgroundLabel.add(downTitle);

		changePanel = new JPanel();
		changePanel.setOpaque(false);
		changePanel.setPreferredSize(new Dimension(Math.round(1800 * pongFrame.getASPECT_RATIO()),
				Math.round(500 * pongFrame.getASPECT_RATIO())));
		changePanel.setLayout(layout);

		/*
		 * Special-Selection
		 */
		specialSelectionPanel = new JPanel();
		specialSelectionPanel.setOpaque(false);

		JPanel buttonPanel3 = new JPanel();
		buttonPanel3.setPreferredSize(new Dimension(Math.round(1920 * pongFrame.getASPECT_RATIO()),
				Math.round(90 * pongFrame.getASPECT_RATIO())));
		buttonPanel3.setOpaque(false);

		playEasy = new MenuButton(pongFrame, "Einfach");
		playEasy.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()),
				Math.round(90 * pongFrame.getASPECT_RATIO())));
		playEasy.addActionListener(this);
		buttonPanel3.add(playEasy);

		playMiddle = new MenuButton(pongFrame, "Mittel");
		playMiddle.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()),
				Math.round(90 * pongFrame.getASPECT_RATIO())));
		playMiddle.addActionListener(this);
		buttonPanel3.add(playMiddle);

		playHard = new MenuButton(pongFrame, "Schwer");
		playHard.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()),
				Math.round(90 * pongFrame.getASPECT_RATIO())));
		playHard.addActionListener(this);
		buttonPanel3.add(playHard);

		specialSelectionPanel.add(buttonPanel3);

		sliderPanel = new JPanel();
		sliderPanel.setPreferredSize(new Dimension(Math.round(750 * pongFrame.getASPECT_RATIO()),
				Math.round(300 * pongFrame.getASPECT_RATIO())));
		gbl = new GridBagLayout();
		sliderPanel.setLayout(gbl);
		sliderPanel.setOpaque(false);

		botFailFactorSlider = new MenuSlider(pongFrame);
		botSpeedSlider = new MenuSlider(pongFrame);
		erfassungsbereichBotLinksSlider = new MenuSlider(pongFrame);
		erfassungsbereichBotRechtsSlider = new MenuSlider(pongFrame);
		ballSpeedSlider = new MenuSlider(pongFrame);
		playerSpeedLeftSlider = new MenuSlider(pongFrame);
		playerSpeedRightSlider = new MenuSlider(pongFrame);

		/*
		 * TODO::::::::::::::::::::::::::::::::::::::::::::::::::: Nicht unbedingt:
		 * Grafische Direkt-Auswertung für das Bewegen an den Slidern anzeigen: Am
		 * einfachsten wäre der Punkt mit den Erfassungsbereichen(Bereich auf einer
		 * Mini-Map markieren, in welchem Bereich der jeweilige Bot zusehen könnte)
		 * 
		 */

		ballSpeedLabel = new MenuLabel(pongFrame, "Ball-Geschwindigkeit:");
		ballSpeedLabel.setSize(new Dimension(Math.round(85 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));
		ballSpeedLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12 * pongFrame.getASPECT_RATIO()));
		ballSpeedLabel.setAlignment(ballSpeedLabel.ALIGN_RIGHT);

		playerSpeedLeftLabel = new MenuLabel(pongFrame, "Spieler Links Geschwindigkeit:");
		playerSpeedLeftLabel.setSize(new Dimension(Math.round(85 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));
		playerSpeedLeftLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12 * pongFrame.getASPECT_RATIO()));
		playerSpeedLeftLabel.setAlignment(playerSpeedLeftLabel.ALIGN_RIGHT);

		playerSpeedRightLabel = new MenuLabel(pongFrame, "Spieler Rechts Geschwindigkeit:");
		playerSpeedRightLabel.setSize(new Dimension(Math.round(50 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));
		playerSpeedRightLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12 * pongFrame.getASPECT_RATIO()));
		playerSpeedRightLabel.setAlignment(playerSpeedRightLabel.ALIGN_RIGHT);

		botSpeedLabel = new MenuLabel(pongFrame, "Bot-Geschwindigkeit:");
		botSpeedLabel.setSize(new Dimension(Math.round(85 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));
		botSpeedLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12 * pongFrame.getASPECT_RATIO()));
		botSpeedLabel.setAlignment(botSpeedLabel.ALIGN_RIGHT);

		botFailFactorLabel = new MenuLabel(pongFrame, "Bot-Fail-Faktor:");
		botFailFactorLabel.setSize(new Dimension(Math.round(85 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));
		botFailFactorLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12 * pongFrame.getASPECT_RATIO()));
		botFailFactorLabel.setAlignment(botFailFactorLabel.ALIGN_RIGHT);

		erfassungsbereichBotLinksLabel = new MenuLabel(pongFrame, "Erf.Bereich Linker Bot:");
		erfassungsbereichBotLinksLabel.setSize(new Dimension(Math.round(85 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));
		erfassungsbereichBotLinksLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12 * pongFrame.getASPECT_RATIO()));
		erfassungsbereichBotLinksLabel.setAlignment(erfassungsbereichBotLinksLabel.ALIGN_RIGHT);

		erfassungsBereichBotRechtsLabel = new MenuLabel(pongFrame, "Erf.Bereich Rechter Bot:");
		erfassungsBereichBotRechtsLabel.setSize(new Dimension(Math.round(85 * pongFrame.getASPECT_RATIO()),
				Math.round(100 * pongFrame.getASPECT_RATIO())));
		erfassungsBereichBotRechtsLabel
				.setFont(pongFrame.getGLOBAL_FONT().deriveFont(12 * pongFrame.getASPECT_RATIO()));
		erfassungsBereichBotRechtsLabel.setAlignment(erfassungsBereichBotRechtsLabel.ALIGN_RIGHT);

		// x y w h wx wy

		addComponent(sliderPanel, gbl, ballSpeedLabel, 0, 0, 1, 1, 0.5, 1.0);
		addComponent(sliderPanel, gbl, ballSpeedSlider, 2, 0, 1, 1, 0.5, 1.0);

		addComponent(sliderPanel, gbl, playerSpeedLeftLabel, 0, 1, 1, 1, 0.5, 1.0);
		addComponent(sliderPanel, gbl, playerSpeedLeftSlider, 2, 1, 1, 1, 0.5, 1.0);
		addComponent(sliderPanel, gbl, playerSpeedRightLabel, 0, 2, 1, 1, 0.5, 1.0);
		addComponent(sliderPanel, gbl, playerSpeedRightSlider, 2, 2, 1, 1, 0.5, 1.0);

		addComponent(sliderPanel, gbl, botSpeedLabel, 0, 3, 1, 1, 0.5, 1.0);
		addComponent(sliderPanel, gbl, botSpeedSlider, 2, 3, 1, 1, 0.5, 1.0);

		addComponent(sliderPanel, gbl, botFailFactorLabel, 0, 4, 1, 1, 0.5, 1.0);
		addComponent(sliderPanel, gbl, botFailFactorSlider, 2, 4, 1, 1, 0.5, 1.0);

		addComponent(sliderPanel, gbl, erfassungsbereichBotLinksLabel, 0, 5, 1, 1, 0.5, 1.0);
		addComponent(sliderPanel, gbl, erfassungsbereichBotLinksSlider, 2, 5, 1, 1, 0.5, 1.0);

		addComponent(sliderPanel, gbl, erfassungsBereichBotRechtsLabel, 0, 6, 1, 1, 0.5, 1.0);
		addComponent(sliderPanel, gbl, erfassungsbereichBotRechtsSlider, 2, 6, 1, 1, 0.5, 1.0);

		specialSelectionPanel.add(sliderPanel);

		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.setPreferredSize(
				new Dimension(pongFrame.getGraphicResolution().width, Math.round(150 * pongFrame.getASPECT_RATIO())));
		buttonPanel2.setOpaque(false);
		returnToNormalSelection = new MenuButton(pongFrame, "Zurück");
		returnToNormalSelection
				.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		returnToNormalSelection.addActionListener(this);
		buttonPanel2.add(returnToNormalSelection);
		specialSelectionPanel.add(buttonPanel2);

		/*
		 * \ \ Special-Selection / Normal-Selection: \
		 */

		normalSelectionPanel = new JPanel();
		normalSelectionPanel.setOpaque(false);
		JPanel buttonPanel = new JPanel();

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
		normalSelectionPanel.add(leftOrRightPlayer);

		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new GridLayout(0, 3, Math.round(10 * pongFrame.getASPECT_RATIO()),
				Math.round(10 * pongFrame.getASPECT_RATIO())));

		playNormal = new MenuButton(pongFrame, "Spieler vs Bot");
		playNormal.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playNormal.addActionListener(this);
		playNormal.setEnabled(false);
		buttonPanel.add(playNormal);

		playBotvsBot = new MenuButton(pongFrame, "Bot vs Bot");
		playBotvsBot.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playBotvsBot.addActionListener(this);
		buttonPanel.add(playBotvsBot);

		playKOOP = new MenuButton(pongFrame, "Spieler vs Spieler [KOOP]");
		playKOOP.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		playKOOP.addActionListener(this);
		buttonPanel.add(playKOOP);

		returnToMainMenu = new MenuButton(pongFrame, "Zurück");
		returnToMainMenu
				.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		returnToMainMenu.addActionListener(this);
		buttonPanel.add(returnToMainMenu);

		startPlaying = new MenuButton(pongFrame, "Spiel Starten");
		startPlaying.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		startPlaying.addActionListener(this);
		buttonPanel.add(startPlaying);

		editSpecial = new MenuButton(pongFrame, "Einstellungen");
		editSpecial.setSize(new Dimension(Math.round(300 * pongFrame.getASPECT_RATIO()), preferredSize.height / 12));
		editSpecial.addActionListener(this);
		buttonPanel.add(editSpecial);

		normalSelectionPanel.add(buttonPanel);
		/*
		 * \Normal-Selection
		 */
		changePanel.add(normalSelectionPanel, "normal");
		changePanel.add(specialSelectionPanel, "special");
		backgroundLabel.add(changePanel);
		layout.show(changePanel, "normal");
		this.setBackground(Color.black);
		this.add(backgroundLabel, BorderLayout.CENTER);
	}

	static void addComponent(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height,
			double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}

	/*
	 * 
	 * Diese Methode dirigiert das Preset-Werte-Setzen der Slider, durch die
	 * Einfach, Mittel, Schwer Preset-Buttons
	 */
	private void setCustomSliderValue(int difficulty) {
		switch (difficulty) {
		case EASY_MODE:
			setSliderValue(25);
			break;
		case MIDDLE_MODE:
			setSliderValue(50);
			break;
		case HARD_MODE:
			setSliderValue(75);
			break;
		}
	}

	/*
	 * Diese Methode setzt den Übergebenen Wert auf alle Slider
	 */
	private void setSliderValue(int percent) {
		botFailFactorSlider.setValue(percent);
		botSpeedSlider.setValue(percent);
		erfassungsbereichBotLinksSlider.setValue(percent);
		erfassungsbereichBotRechtsSlider.setValue(percent);
		ballSpeedSlider.setValue(percent);
		playerSpeedLeftSlider.setValue(percent);
		playerSpeedRightSlider.setValue(percent);
	}

	/*
	 * Diese Methode dient der Funktion, dass immer nur die Slider in den
	 * Einstellungen sichtbar sind, die auch benötigt werden.
	 */
	private void setSlidersVisibility() {
		/*
		 * botFailFactorSlider, botSpeedSlider, erfassungsbereichBotLinksSlider,
		 * erfassungsbereichBotRechtsSlider, ballSpeedSlider, playerSpeedLeftSlider,
		 * playerSpeedRightSlider;
		 * 
		 * botFailFactorLabel, botSpeedLabel, erfassungsbereichBotLinksLabel,
		 * erfassungsBereichBotRechtsLabel, ballSpeedLabel, playerSpeedLeftLabel,
		 * playerSpeedRightLabel;
		 * 
		 */
		sliderPanel.remove(ballSpeedLabel);
		sliderPanel.remove(ballSpeedSlider);
		sliderPanel.remove(botSpeedLabel);
		sliderPanel.remove(botSpeedSlider);
		sliderPanel.remove(botFailFactorLabel);
		sliderPanel.remove(botFailFactorSlider);
		sliderPanel.remove(erfassungsbereichBotLinksLabel);
		sliderPanel.remove(erfassungsbereichBotLinksSlider);
		sliderPanel.remove(erfassungsBereichBotRechtsLabel);
		sliderPanel.remove(erfassungsbereichBotRechtsSlider);
		sliderPanel.remove(playerSpeedRightLabel);
		sliderPanel.remove(playerSpeedRightSlider);
		sliderPanel.remove(playerSpeedLeftLabel);
		sliderPanel.remove(playerSpeedLeftSlider);

		switch (SELECTED_GAME_MODE) {

		case NORMAL_GAME_MODE:
			if (ballSpeedLabel.getParent() == null) {
				addComponent(sliderPanel, gbl, ballSpeedLabel, 0, 0, 1, 1, 0.5, 1.0);
				addComponent(sliderPanel, gbl, ballSpeedSlider, 2, 0, 1, 1, 0.5, 1.0);
			}
			if (!toggleSwitch.isActivated()) { // Linker Spieler
				if (playerSpeedRightLabel.getParent() != null) {
					sliderPanel.remove(playerSpeedRightLabel);
					sliderPanel.remove(playerSpeedRightSlider);
				}
				if (erfassungsbereichBotLinksLabel.getParent() != null) {
					sliderPanel.remove(erfassungsbereichBotLinksLabel);
					sliderPanel.remove(erfassungsbereichBotLinksSlider);
				}
				if (playerSpeedLeftLabel.getParent() == null) {
					addComponent(sliderPanel, gbl, playerSpeedLeftLabel, 0, 1, 1, 1, 0.5, 1.0);
					addComponent(sliderPanel, gbl, playerSpeedLeftSlider, 2, 1, 1, 1, 0.5, 1.0);
				}
				if (erfassungsBereichBotRechtsLabel.getParent() == null) {
					addComponent(sliderPanel, gbl, erfassungsBereichBotRechtsLabel, 0, 2, 1, 1, 0.5, 1.0);
					addComponent(sliderPanel, gbl, erfassungsbereichBotRechtsSlider, 2, 2, 1, 1, 0.5, 1.0);
				}

			} else { // Rechter Spieler
				if (playerSpeedLeftLabel.getParent() != null) {
					sliderPanel.remove(playerSpeedLeftLabel);
					sliderPanel.remove(playerSpeedLeftSlider);
				}
				if (erfassungsBereichBotRechtsLabel.getParent() != null) {
					sliderPanel.remove(erfassungsBereichBotRechtsLabel);
					sliderPanel.remove(erfassungsbereichBotRechtsSlider);
				}
				if (playerSpeedRightLabel.getParent() == null) {
					addComponent(sliderPanel, gbl, playerSpeedRightLabel, 0, 1, 1, 1, 0.5, 1.0);
					addComponent(sliderPanel, gbl, playerSpeedRightSlider, 2, 1, 1, 1, 0.5, 1.0);
				}
				if (erfassungsbereichBotLinksLabel.getParent() == null) {
					addComponent(sliderPanel, gbl, erfassungsbereichBotLinksLabel, 0, 2, 1, 1, 0.5, 1.0);
					addComponent(sliderPanel, gbl, erfassungsbereichBotLinksSlider, 2, 2, 1, 1, 0.5, 1.0);
				}
			}

			if (botSpeedLabel.getParent() == null) {

				addComponent(sliderPanel, gbl, botSpeedLabel, 0, 3, 1, 1, 0.5, 1.0);
				addComponent(sliderPanel, gbl, botSpeedSlider, 2, 3, 1, 1, 0.5, 1.0);
			}
			if (botFailFactorLabel.getParent() == null) {

				addComponent(sliderPanel, gbl, botFailFactorLabel, 0, 4, 1, 1, 0.5, 1.0);
				addComponent(sliderPanel, gbl, botFailFactorSlider, 2, 4, 1, 1, 0.5, 1.0);
			}
			break;
//-----------------------------------------------------------------------------------------------------------------------------------------
		case BOT_VS_BOT_GAME_MODE:

			if (ballSpeedLabel.getParent() == null) {
				addComponent(sliderPanel, gbl, ballSpeedLabel, 0, 0, 1, 1, 0.5, 1.0);
				addComponent(sliderPanel, gbl, ballSpeedSlider, 2, 0, 1, 1, 0.5, 1.0);
			}
			if (botSpeedLabel.getParent() == null) {
				addComponent(sliderPanel, gbl, botSpeedLabel, 0, 1, 1, 1, 0.5, 1.0);
				addComponent(sliderPanel, gbl, botSpeedSlider, 2, 1, 1, 1, 0.5, 1.0);
			}
			if (botFailFactorLabel.getParent() == null) {
				addComponent(sliderPanel, gbl, botFailFactorLabel, 0, 2, 1, 1, 0.5, 1.0);
				addComponent(sliderPanel, gbl, botFailFactorSlider, 2, 2, 1, 1, 0.5, 1.0);
			}
			if (erfassungsbereichBotLinksLabel.getParent() == null) {
				addComponent(sliderPanel, gbl, erfassungsbereichBotLinksLabel, 0, 3, 1, 1, 0.5, 1.0);
				addComponent(sliderPanel, gbl, erfassungsbereichBotLinksSlider, 2, 3, 1, 1, 0.5, 1.0);
			}
			if (erfassungsBereichBotRechtsLabel.getParent() == null) {
				addComponent(sliderPanel, gbl, erfassungsBereichBotRechtsLabel, 0, 4, 1, 1, 0.5, 1.0);
				addComponent(sliderPanel, gbl, erfassungsbereichBotRechtsSlider, 2, 4, 1, 1, 0.5, 1.0);
			}

			break;
//-----------------------------------------------------------------------------------------------------------------------------------------
		case KOOP_GAME_MODE:
			if (ballSpeedLabel.getParent() == null) {
				addComponent(sliderPanel, gbl, ballSpeedLabel, 0, 0, 1, 1, 0.5, 1.0);
				addComponent(sliderPanel, gbl, ballSpeedSlider, 2, 0, 1, 1, 0.5, 1.0);
			}
			if (playerSpeedLeftLabel.getParent() == null) {
				addComponent(sliderPanel, gbl, playerSpeedLeftLabel, 0, 1, 1, 1, 0.7, 1.0);
				addComponent(sliderPanel, gbl, playerSpeedLeftSlider, 2, 1, 1, 1, 0.3, 1.0);
			}
			if (playerSpeedRightLabel.getParent() == null) {
				addComponent(sliderPanel, gbl, playerSpeedRightLabel, 0, 2, 1, 1, 0.7, 1.0);
				addComponent(sliderPanel, gbl, playerSpeedRightSlider, 2, 2, 1, 1, 0.3, 1.0);
			}
			break;

		}
	}

	/*
	 * Returns: (when float value equals) 0.0 -> float start 1.0 -> float ende 0.25
	 * -> ~6 0.5 -> ~7.75 0.75 -> ~8.99
	 */
	private float playerSpeedLerp(float start, float ende, float value) {
		value = (float) Math.pow(value, 0.3676);
		return (1.0f - value) * start + value * ende;
	}

	/*
	 * Returns: (when float value equals) 0.0 -> 0 1.0 -> 12 0.25 -> ~5 0.5 -> ~7.5
	 * 0.75 -> ~10
	 */
	private float botSpeedLerp(float start, float ende, float value) {
		value = (float) Math.pow(value, 0.63);
		return (1.0f - value) * start + value * ende;
	}

	/*
	 * Returns: (when float value equals) 0.0 -> 0 1.0 -> 1.5 0.25 -> ~0.7 0.5 ->
	 * ~1.0 0.75 -> ~1.25
	 */
	private static float ballSpeedLerp(float start, float ende, float value) {
		value = (float) Math.pow(value, 0.55);
		return (1.0f - value) * start + value * ende;
	}

	// Last Data from normal Menu:
	private String tempLeftName = " ", tempRightName = " "; // FOR CUSTOM

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(returnToMainMenu)) { // SHOW MAIN-MENU
			pongFrame.showPane(pongFrame.MAIN_MENU);

		} else if (e.getSource().equals(playCustom)) { // SHOW CUSTOM SELECTION-MENU
			layout.show(changePanel, "custom");

		} else if (e.getSource().equals(editSpecial)) { // SHOW SPECIAL SELECTION-MENU

			setSlidersVisibility();
			layout.show(changePanel, "special");

		} else if (e.getSource().equals(returnToNormalSelection)) {

			layout.show(changePanel, "normal");

		} else if (e.getSource().equals(playNormal)) { // PLAYER VS BOT
			toggleSwitch.setEnabled(true);

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
			toggleSwitch.setEnabled(false);
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
			toggleSwitch.setEnabled(false);
			playNormal.setEnabled(true);
			playBotvsBot.setEnabled(true);
			playKOOP.setEnabled(false);
			leftName.setEditable(true);
			rightName.setEditable(true);

			if (!toggleSwitch.isActivated()) {// Linker Spieler
				if (!tempLeftName.equals(" ")) {
					leftName.setText(tempLeftName);
					tempLeftName = " ";
				} else {
					leftName.setText("Spieler");
				}
				rightName.setText("Spieler 2");
			} else {
				if (!tempRightName.equals(" ")) {
					rightName.setText(tempRightName);
					tempRightName = " ";
				} else {
					rightName.setText("Spieler 2");
				}
				leftName.setText("Spieler");
			}
			if (leftName.getText().equals(rightName.getText())) {
				rightName.setText(rightName.getText() + " 2");
			}
			SELECTED_GAME_MODE = KOOP_GAME_MODE;
		} else if (e.getSource().equals(startPlaying)) { // START CUSTOM-GAME

			boolean isLeftPlayerBot = false, isRightPlayerBot = true;
			float playerLeftSpeed = 0.0f, playerRightSpeed = 0.0f, botSpeed = 0.0f, botFailFactor = 0.0f,
					ballSpeed = 0.0f;
			int erfBereichLinkerBot = 0, erfBereichRechterBot = 0;

			switch (SELECTED_GAME_MODE) {
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
			//calculate slider states into numbers, which the singleplayer understands
			ballSpeed = ballSpeedLerp(0.0f, 1.5f, ballSpeedSlider.getValue() / 100.0f);

			playerLeftSpeed = playerSpeedLerp(0.0f, 10.0f, (100 - playerSpeedLeftSlider.getValue()) / 100.0f);
			playerRightSpeed = playerSpeedLerp(0.0f, 10.0f, (100 - playerSpeedRightSlider.getValue()) / 100.0f);

			botFailFactor = ((100 - botFailFactorSlider.getValue()) / 100.0f) * 0.3f; // 0.3 = MAX (entspricht Slider
																						// auf 0%)
			botSpeed = botSpeedLerp(0.0f, 12.0f, botSpeedSlider.getValue() / 100.0f);
			erfBereichLinkerBot = (int) (1920 - ((erfassungsbereichBotLinksSlider.getValue() / 100.) * 1920));// Von
																												// links
																												// gesehen
			erfBereichRechterBot = (int) (1920 - ((erfassungsbereichBotRechtsSlider.getValue() / 100.) * 1920)); // Von
																													// rechts
																													// gesehen

			pongFrame.getSinglePlayer().setNameLabel(leftName.getText(), rightName.getText());
			pongFrame.getSinglePlayer().restartGame(isLeftPlayerBot, isRightPlayerBot, playerLeftSpeed,
					playerRightSpeed, ballSpeed, botSpeed, botFailFactor, erfBereichLinkerBot, erfBereichRechterBot);
			pongFrame.showPane(pongFrame.SINGLEPLAYER);

		} else if (e.getSource().equals(playEasy)) {
			setCustomSliderValue(EASY_MODE);

		} else if (e.getSource().equals(playMiddle)) {
			setCustomSliderValue(MIDDLE_MODE);

		} else if (e.getSource().equals(playHard)) {
			setCustomSliderValue(HARD_MODE);
		}
	}
}
