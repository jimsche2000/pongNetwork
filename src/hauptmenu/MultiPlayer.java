package hauptmenu;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.MenuButton;
import gui.MenuLabel;
import pongtoolkit.ImageLoader;

public class MultiPlayer extends JPanel implements ActionListener {

	private static final long serialVersionUID = -4370387734717892311L;
//	private JLabel title, downTitle;
	private MenuLabel title, downTitle;
	private MenuButton returnToMainMenu, joinServer, createServer;
	private ImageIcon background;
	private JLabel backgroundLabel;
	private CardLayout layout = new CardLayout();
	private JPanel changePanel;
	private JoinServerPanel joinServerPanel;
	private CreateServerPanel createServerPanel;
	private PongFrame pongFrame;
	private boolean isJoinServerPanelActive = true;

	public MultiPlayer(PongFrame pongFrame) {

		this.pongFrame = pongFrame;
		Dimension preferredSize = pongFrame.getGraphicResolution();
		Insets resInsets = pongFrame.getGraphicInsets();
		this.setPreferredSize(preferredSize);
		this.setLayout(new BorderLayout());
		
		background = ImageLoader.loadIcon("network-wallpaper.jpg", preferredSize);
		
		backgroundLabel = new JLabel();
		backgroundLabel.setSize(preferredSize);
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new FlowLayout());

//-----------TITLE------------------------------------------------------------------------------------
		JPanel fillPanel = new JPanel();
		fillPanel.setOpaque(false);
		fillPanel.setPreferredSize(new Dimension(preferredSize.width, resInsets.top	));

		title = new MenuLabel(pongFrame, "Mehrspieler");
		title.setForeground(Color.white);
		title.setAlignment(title.ALIGN_MID);
		title.setSize(new Dimension(preferredSize.width, Math.round(150  * pongFrame.getASPECT_RATIO())));
		title.setDrawBackground(false);
		title.setFont(pongFrame.getGLOBAL_FONT().deriveFont(70.0f * pongFrame.getASPECT_RATIO()));

		downTitle = new MenuLabel(pongFrame, "Spielt Gemeinsam im lokalen Netzwerk");
		downTitle.setForeground(Color.white);
		downTitle.setAlignment(downTitle.ALIGN_MID);
		downTitle.setSize(new Dimension(preferredSize.width, Math.round(150  * pongFrame.getASPECT_RATIO())));
		downTitle.setDrawBackground(false);
		downTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(30.0f  * pongFrame.getASPECT_RATIO()));
		
		backgroundLabel.add(fillPanel);
		backgroundLabel.add(title);
		backgroundLabel.add(downTitle);
		
//-----------CHANGE-PANEL-----------------------------------------------------------------------------		
		Dimension middleSize = new Dimension(preferredSize.width, ((int) Math.round(675 * pongFrame.getASPECT_RATIO())));
		changePanel = new JPanel();
		changePanel.setPreferredSize(middleSize);
		changePanel.setOpaque(false);
		changePanel.setLayout(layout);

		createServerPanel = new CreateServerPanel(pongFrame, middleSize);
		createServerPanel.setOpaque(false);
		changePanel.add(createServerPanel, "createServerPanel");

		joinServerPanel = new JoinServerPanel(pongFrame, middleSize);
		joinServerPanel.setOpaque(false);
		
		changePanel.add(joinServerPanel, "joinServerPanel");
		backgroundLabel.add(changePanel);
		
		returnToMainMenu = new MenuButton(pongFrame, "Zurück");
		returnToMainMenu.addActionListener(this);
		returnToMainMenu.setSize(new Dimension(Math.round(200 * pongFrame.getASPECT_RATIO()), Math.round(50 * pongFrame.getASPECT_RATIO())));
		backgroundLabel.add(returnToMainMenu);
		
		joinServer = new MenuButton(pongFrame, "Server beitreten");
		joinServer.addActionListener(this);
		joinServer.setSize(new Dimension(Math.round(400 * pongFrame.getASPECT_RATIO()), Math.round(50 * pongFrame.getASPECT_RATIO())));
		joinServer.setEnabled(false);
		backgroundLabel.add(joinServer);
		
		createServer = new MenuButton(pongFrame, "Server erstellen");
		createServer.addActionListener(this);
		createServer.setSize(new Dimension(Math.round(400 * pongFrame.getASPECT_RATIO()), Math.round(50 * pongFrame.getASPECT_RATIO())));
		backgroundLabel.add(createServer);

		setBackground(Color.black);
		this.add(backgroundLabel, BorderLayout.CENTER);

		layout.show(changePanel, "joinServerPanel");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(returnToMainMenu)) {

			pongFrame.showPane(pongFrame.MAIN_MENU);
			pongFrame.getClientThread().setShouldSearchForServer(false);

		} else if (e.getSource().equals(joinServer)) {
			joinServer.setEnabled(false);
			createServer.setEnabled(true);
			pongFrame.getClientThread().setShouldSearchForServer(true);
			isJoinServerPanelActive = true;
			layout.show(changePanel, "joinServerPanel");
		} else if (e.getSource().equals(createServer)) {
			createServer.setEnabled(false);
			joinServer.setEnabled(true);
			pongFrame.getClientThread().setShouldSearchForServer(false);
			isJoinServerPanelActive = false;
			layout.show(changePanel, "createServerPanel");
		}
	}

	public JoinServerPanel getJoinServerPanel() {
		return joinServerPanel;
	}

	public void setJoinServerPanel(JoinServerPanel joinServerPanel) {
		this.joinServerPanel = joinServerPanel;
	}

	public CreateServerPanel getCreateServerPanel() {
		return createServerPanel;
	}

	public void setCreateServerPanel(CreateServerPanel createServerPanel) {
		this.createServerPanel = createServerPanel;
	}

	public boolean isJoinServerPanelActive() {
		return isJoinServerPanelActive;
	}

	public void setJoinServerPanelActive(boolean isJoinServerPanelActive) {
		this.isJoinServerPanelActive = isJoinServerPanelActive;
	}
}
