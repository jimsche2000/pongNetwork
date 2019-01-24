package multiplayer.server.createGame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import gui.MenuButton;
import gui.MenuLabel;
import gui.combobox.CustomComboBox;
import hauptmenu.PongFrame;
import multiplayer.datapacks.ClientAttributes;
import pongtoolkit.ImageLoader;

// JPanel shown in the modal JDialog above
@SuppressWarnings("serial")
public class CreateGamePanel extends JPanel implements ActionListener {

	private static final Color BG = new Color(255, 255, 255);
	private PongFrame frame;
	private MenuButton back, switchLeftRightPlayer, createGame, switchToSingleGame, switchToTournament;
	private MenuLabel createGameTitle, createTournamentTitle, validationErrorLabel;
	private JPanel contentPane, buttonSwitchPanel, switchPanel, createSingleGame, createTournament;
	private CardLayout switchLayout = new CardLayout();
	private final String switchSingleGameString = "SINGLE_GAME";
	private final String switchTournamentString = "TOURNAMENT_GAME";
	private CustomComboBox leftPlayerComboBox, rightPlayerComboBox;
	private boolean leftComboBoxIsShowingPopUp = false, rightComboBoxIsShowingPopUp = false;
	private final String VE_LEFT_MISSING = "Es ist kein linker Spieler ausgewählt!";
	private final String VE_RIGHT_MISSING = "Es ist kein rechter Spieler ausgewählt!";
	private final String VE_LEFT_N_RIGHT_MISSING = "Es sind keine Spieler ausgewählt!";
	private final String VE_LEFT_RIGHT_EQUAL = "Wähle 2 unterschiedliche Spieler aus!";

	public CreateGamePanel(PongFrame frame) {
		this.frame = frame;
		setPreferredSize(
				new Dimension(Math.round(1000 * frame.getASPECT_RATIO()), Math.round(550 * frame.getASPECT_RATIO())));
		setBackground(BG);

		contentPane = new JPanel();
		contentPane.setPreferredSize(getPreferredSize());
		contentPane.setOpaque(false);
		contentPane.setLayout(new BorderLayout());

		switchPanel = new JPanel();
		switchPanel.setPreferredSize(new Dimension(getPreferredSize().width,
				Math.round((getPreferredSize().height - 90f) * frame.getASPECT_RATIO())));
		switchPanel.setOpaque(false);
		switchPanel.setLayout(switchLayout);

		/*
		 * createSingleGame:
		 * 
		 * 2 Player, 2 Comboboxes. In future need a way to add bots into this
		 * 
		 * 
		 */
		createSingleGame = new JPanel();
		createSingleGame.setBackground(Color.LIGHT_GRAY);
		createSingleGame.setPreferredSize(switchPanel.getPreferredSize());

		createGameTitle = new MenuLabel(frame, "Neues Spiel Erstellen");
		createGameTitle.setSize(new Dimension(getPreferredSize().width, Math.round(125f * frame.getASPECT_RATIO())));
		createGameTitle.setFont(frame.getGLOBAL_FONT().deriveFont(35f * frame.getASPECT_RATIO()));
		createGameTitle.setDrawBackground(false);
		createGameTitle.setAlignment(createGameTitle.ALIGN_MID);
		createSingleGame.add(createGameTitle);

		/*
		 * playerLeftRightWrapperPanel:
		 * 
		 * holds leftPlayer and rightPlayer Panel
		 * 
		 */
		JPanel playerLeftRightWrapperPanel = new JPanel();
		playerLeftRightWrapperPanel.setOpaque(false);
		playerLeftRightWrapperPanel.setPreferredSize(
				new Dimension(createSingleGame.getPreferredSize().width, Math.round(200f * frame.getASPECT_RATIO())));
		/*
		 * leftPlayer:
		 * 
		 * title-label "left-player" and custom-combo-box with all available players
		 */
		MenuLabel leftPlayerTitle = new MenuLabel(frame, "Linker Spieler");
		leftPlayerTitle.setAlignment(leftPlayerTitle.ALIGN_LEFT);
		leftPlayerTitle.setSize(
				new Dimension(Math.round(285f * frame.getASPECT_RATIO()), Math.round(25 * frame.getASPECT_RATIO())));
		leftPlayerTitle.setFont(frame.getGLOBAL_FONT().deriveFont(20f * frame.getASPECT_RATIO()));
		leftPlayerTitle.setDrawBackground(false);

		leftPlayerComboBox = new CustomComboBox(frame, false);
		leftPlayerComboBox.setPreferredSize(
				new Dimension(Math.round(200f * frame.getASPECT_RATIO()), Math.round(35 * frame.getASPECT_RATIO())));
		leftPlayerComboBox.setEditable(true);
		leftPlayerComboBox.setFont(frame.getGLOBAL_FONT().deriveFont(30 * frame.getASPECT_RATIO()));
		leftPlayerComboBox.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// This method is called before the popup menu becomes visible.
				leftComboBoxIsShowingPopUp = true;
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// This method is called before the popup menu becomes invisible
				leftComboBoxIsShowingPopUp = false;
			}

			public void popupMenuCanceled(PopupMenuEvent e) {
				// This method is called when the popup menu is canceled
			}
		});
		leftPlayerComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("LEFT ITEM STATE CHANGED");
				validationErrorLabel.setText("");
			}
		});

		switchLeftRightPlayer = new MenuButton(frame, "Links-Rechts vertauschen");
		switchLeftRightPlayer.setFont(frame.getGLOBAL_FONT().deriveFont(12f * frame.getASPECT_RATIO()));
		switchLeftRightPlayer.setIcon(ImageLoader.loadIcon("swapLeftRight.png"));
		switchLeftRightPlayer.setInvertedIcon(ImageLoader.loadIcon("inverted-swapLeftRight.png"));
		switchLeftRightPlayer.setSize(
				new Dimension(Math.round(100f * frame.getASPECT_RATIO()), Math.round(50f * frame.getASPECT_RATIO())));
		switchLeftRightPlayer.addActionListener(this);
		/*
		 * rightPlayer:
		 * 
		 * title-label "right-player" and custom-combo-box with all available players
		 */
		MenuLabel rightPlayerTitle = new MenuLabel(frame, "Rechter Spieler");
		rightPlayerTitle.setAlignment(rightPlayerTitle.ALIGN_RIGHT);
		rightPlayerTitle.setSize(
				new Dimension(Math.round(310f * frame.getASPECT_RATIO()), Math.round(25 * frame.getASPECT_RATIO())));
		rightPlayerTitle.setFont(frame.getGLOBAL_FONT().deriveFont(20f * frame.getASPECT_RATIO()));
		rightPlayerTitle.setDrawBackground(false);

		rightPlayerComboBox = new CustomComboBox(frame, false);
		rightPlayerComboBox.setPreferredSize(
				new Dimension(Math.round(200f * frame.getASPECT_RATIO()), Math.round(35 * frame.getASPECT_RATIO())));
		rightPlayerComboBox.setEditable(true);
		rightPlayerComboBox.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// This method is called before the popup menu becomes visible.
				rightComboBoxIsShowingPopUp = true;
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// This method is called before the popup menu becomes invisible
				rightComboBoxIsShowingPopUp = false;
			}

			public void popupMenuCanceled(PopupMenuEvent e) {
				// This method is called when the popup menu is canceled
			}
		});
		rightPlayerComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("RIGHT ITEM STATE CHANGED");
				validationErrorLabel.setText("");
			}
		});
		JPanel fillPanel = new JPanel();
		fillPanel.setOpaque(false);
		fillPanel.setPreferredSize(new Dimension(Math.round(switchLeftRightPlayer.getPreferredSize().width * 1.33f),
				switchLeftRightPlayer.getPreferredSize().height));

		JPanel f1 = new JPanel();
		f1.setOpaque(false);
		f1.setPreferredSize(new Dimension(leftPlayerTitle.getPreferredSize().width,
				Math.round(leftPlayerTitle.getPreferredSize().height
						+ Math.round(leftPlayerComboBox.getPreferredSize().height * 1.3f))));
		JPanel f2 = new JPanel();
		f2.setPreferredSize(new Dimension(rightPlayerTitle.getPreferredSize().width,
				Math.round(rightPlayerTitle.getPreferredSize().height
						+ Math.round(rightPlayerComboBox.getPreferredSize().height * 1.3f))));
		f2.setOpaque(false);

		f1.add(leftPlayerTitle);
		f2.add(rightPlayerTitle);
		f1.add(leftPlayerComboBox);
		f2.add(rightPlayerComboBox);

		playerLeftRightWrapperPanel.add(f1);
		playerLeftRightWrapperPanel.add(switchLeftRightPlayer);
		playerLeftRightWrapperPanel.add(f2);
		createSingleGame.add(playerLeftRightWrapperPanel);

		validationErrorLabel = new MenuLabel(frame, "");
		validationErrorLabel.setAlignment(validationErrorLabel.ALIGN_MID);
		validationErrorLabel.setSize(
				new Dimension(createSingleGame.getPreferredSize().width, Math.round(30 * frame.getASPECT_RATIO())));
		validationErrorLabel.setFont(frame.getGLOBAL_FONT().deriveFont(16 * frame.getASPECT_RATIO()));

		validationErrorLabel.setDrawBackground(false);
		createSingleGame.add(validationErrorLabel);

		createGame = new MenuButton(frame, "Spiel erstellen");
		createGame.setSize(
				new Dimension(Math.round(400f * frame.getASPECT_RATIO()), Math.round(50f * frame.getASPECT_RATIO())));
		createGame.setFont(frame.getGLOBAL_FONT().deriveFont(16f * frame.getASPECT_RATIO()));
		createGame.addActionListener(this);
		createSingleGame.add(createGame);

		createTournament = new JPanel();
		createTournament.setPreferredSize(switchPanel.getPreferredSize());
		createTournament.setBackground(Color.LIGHT_GRAY);

		createTournamentTitle = new MenuLabel(frame, "Turnier Erstellen");
		createTournamentTitle
				.setSize(new Dimension(getPreferredSize().width, Math.round(100f * frame.getASPECT_RATIO())));
		createTournamentTitle.setFont(frame.getGLOBAL_FONT().deriveFont(35f * frame.getASPECT_RATIO()));
		createTournamentTitle.setDrawBackground(false);
		createTournamentTitle.setAlignment(createTournamentTitle.ALIGN_MID);
		createTournament.add(createTournamentTitle);

		MenuLabel comingSoon = new MenuLabel(frame, "Coming Soon.. Probably..");
		comingSoon.setSize(new Dimension(getPreferredSize().width, Math.round(100f * frame.getASPECT_RATIO())));
		comingSoon.setFont(frame.getGLOBAL_FONT().deriveFont(25f * frame.getASPECT_RATIO()));
		comingSoon.setDrawBackground(false);
		comingSoon.setAlignment(comingSoon.ALIGN_MID);
		createTournament.add(comingSoon);

		switchPanel.add(createSingleGame, switchSingleGameString);
		switchPanel.add(createTournament, switchTournamentString);
		contentPane.add(switchPanel, BorderLayout.CENTER);

		buttonSwitchPanel = new JPanel();
		buttonSwitchPanel.setOpaque(false);
		buttonSwitchPanel
				.setPreferredSize(new Dimension(getPreferredSize().width, Math.round(65f * frame.getASPECT_RATIO())));

		switchToSingleGame = new MenuButton(frame, "Einzel-Spiel");
		switchToSingleGame.setFont(frame.getGLOBAL_FONT().deriveFont(16f * frame.getASPECT_RATIO()));
		switchToSingleGame.setSize(
				new Dimension(Math.round(getPreferredSize().width / 3.5f), Math.round(50f * frame.getASPECT_RATIO())));
		switchToSingleGame.addActionListener(this);
		switchToSingleGame.setEnabled(false);

		switchToTournament = new MenuButton(frame, "Turnier erstellen");
		switchToTournament.setFont(frame.getGLOBAL_FONT().deriveFont(16f * frame.getASPECT_RATIO()));
		switchToTournament.setSize(
				new Dimension(Math.round(getPreferredSize().width / 3.5f), Math.round(50f * frame.getASPECT_RATIO())));
		switchToTournament.addActionListener(this);

		back = new MenuButton(frame, "Zurück");
		back.setFont(frame.getGLOBAL_FONT().deriveFont(16f * frame.getASPECT_RATIO()));
		back.setSize(
				new Dimension(Math.round(getPreferredSize().width / 3.5f), Math.round(50f * frame.getASPECT_RATIO())));
		back.addActionListener(this);

		buttonSwitchPanel.add(back);
		buttonSwitchPanel.add(switchToSingleGame);
		buttonSwitchPanel.add(switchToTournament);
		contentPane.add(buttonSwitchPanel, BorderLayout.PAGE_END);

		this.add(contentPane);

		switchLayout.show(switchPanel, switchSingleGameString);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(back)) {
			resume();
		} else if (e.getSource().equals(switchToSingleGame)) {
			switchToSingleGame.setEnabled(false);
			switchToTournament.setEnabled(true);
			switchLayout.show(switchPanel, switchSingleGameString);
		} else if (e.getSource().equals(switchToTournament)) {
			switchToSingleGame.setEnabled(true);
			switchToTournament.setEnabled(false);
			switchLayout.show(switchPanel, switchTournamentString);
		} else if (e.getSource().equals(back)) {
			resume();
		} else if (e.getSource().equals(switchLeftRightPlayer)) {

			Object left = leftPlayerComboBox.getSelectedItem();
			leftPlayerComboBox.setSelectedItem(rightPlayerComboBox.getSelectedItem());
			rightPlayerComboBox.setSelectedItem(left);

		} else if (e.getSource().equals(createGame)) {
			/*
			 * 
			 * Spiel erstellen!
			 * 
			 * Zuerst Validieren, ob 2 Spieler ausgewählt sind, und wenn ja, ob diese gerade
			 * nicht im Spiel sind usw..
			 * 
			 */
//			System.out.println("left: \"" + leftPlayerComboBox.getSelectedItem()+"\"");
//			System.out.println("right: \"" + rightPlayerComboBox.getSelectedItem()+"\"");
			if (leftPlayerComboBox.getSelectedItem() != null && !leftPlayerComboBox.getSelectedItem().equals(" ")) {
				if (rightPlayerComboBox.getSelectedItem() != null && !rightPlayerComboBox.getSelectedItem().equals(" ")) {
					if (leftPlayerComboBox.getSelectedItem() == rightPlayerComboBox.getSelectedItem()) {
						validationErrorLabel.setText(VE_LEFT_RIGHT_EQUAL);
					}else {
						System.out.println("Server: alles richtig, anfrage wird vielleicht an client gesendet");
						String leftPlayerName = "", rightPlayerName = "";
						try {
							leftPlayerName = (String) leftPlayerComboBox.getSelectedItem();
							rightPlayerName = (String) rightPlayerComboBox.getSelectedItem();
						} catch (Exception ex) {
							// ERROR, muss vom Programm verarbeitet werden
							ex.printStackTrace();
							return;
						}
						// Check if these player can play
						if (frame.getHostServer().couldThesePlayerPlay(leftPlayerName, rightPlayerName)) {
							// Get ClientAttributes-Objects from them, and send request to them, if they
							// want to play this game
							System.out.println("Server: anfrage wird nun an client gesendet");
							if (frame.getHostServer().sendPlayersPlayRequest(leftPlayerName, rightPlayerName)) {
								// Succesfully sent Game-Requests to these 2 Players. 
								//TODO: Now Add the new Game to
								// ServerControlPanels game-list,
								// and let ServerMainThread create a new Game with these 2 players!
								// BUT firstly just in status "PENDING" or "REQUESTET"
								resume();
							}
						}
					}
				} else {// Nur kein rechter Spieler ausgewählt
					validationErrorLabel.setText(VE_RIGHT_MISSING);
				}
			} else {// ERROR-Ausgabe, kein linker Spieler ausgewählt
				if (rightPlayerComboBox.getSelectedItem() == null || rightPlayerComboBox.getSelectedItem().equals(" ")) {// Gar keine Spieler ausgewählt
					validationErrorLabel.setText(VE_LEFT_N_RIGHT_MISSING);

				} else {// Nur kein linker Spieler ausgewählt
					validationErrorLabel.setText(VE_LEFT_MISSING);
				}
			}
		}
	}

	// Disposes the JDialog, resumes to Game
	public void resume() {
		Window win = SwingUtilities.getWindowAncestor((Component) back);
		win.dispose(); // here -- dispose of the JDialog
	}

	public MenuButton getResume() {
		return back;
	}

	public void setResume(MenuButton resume) {
		this.back = resume;
	}

	public void refreshLists(boolean automaticly) {
		leftPlayerComboBox.removeAllItems();
		rightPlayerComboBox.removeAllItems();
		ArrayList<ClientAttributes> playerList = frame.getHostServer().getListClients();
		if(playerList != null) {
			if(playerList.size() >0) {
				for (ClientAttributes ca : playerList) {
					leftPlayerComboBox.addItem(ca.getName());
					rightPlayerComboBox.addItem(ca.getName());
				}
			}else {
				//Damit der ausgewählte spieler beim leaven nicht noch zu sehen ist
				leftPlayerComboBox.setSelectedItem(" ");
				rightPlayerComboBox.setSelectedItem(" ");
			}
		}
		leftPlayerComboBox.revalidate();
		leftPlayerComboBox.repaint();
		rightPlayerComboBox.revalidate();
		rightPlayerComboBox.repaint();

		if (automaticly) {
			if (leftComboBoxIsShowingPopUp) {
				leftPlayerComboBox.hidePopup();
				leftPlayerComboBox.showPopup();

			} else if (rightComboBoxIsShowingPopUp) {
				rightPlayerComboBox.hidePopup();
				rightPlayerComboBox.showPopup();
			}
		}
	}
}