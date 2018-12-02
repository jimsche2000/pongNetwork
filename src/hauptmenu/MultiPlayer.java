package hauptmenu;

import java.awt.CardLayout;
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

public class MultiPlayer extends JPanel implements ActionListener {

	private static final long serialVersionUID = -4370387734717892311L;
	private JLabel title, downTitle;
	private MenuButton returnToMainMenu, joinServer, createServer;
	private ImageIcon background = ImageLoader.loadIcon("network-wallpaper.jpg");
	private JLabel backgroundLabel;
	private CardLayout layout = new CardLayout();
	private JPanel changePanel;
	private JoinServerPanel joinServerPanel;
	private CreateServerPanel createServerPanel;
	private PongFrame pongFrame;
	private boolean isJoinServerPanelActive = true;

	public MultiPlayer(PongFrame pongFrame) {

		this.pongFrame = pongFrame;
		this.setSize(1920, 1080);
		this.setPreferredSize(new Dimension(1920, 1080));
		this.setLayout(null);

		backgroundLabel = new JLabel();
		backgroundLabel.setSize(new Dimension(1920, 1080));
		backgroundLabel.setLocation(0, 0);
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new FlowLayout());

		title = new JLabel("<html>Mehrspieler</html>");
		title.setForeground(Color.white);
		title.setFont(pongFrame.getGLOBAL_FONT().deriveFont(0, 70));
		title.setPreferredSize(new Dimension(1920, 100));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setHorizontalTextPosition(SwingConstants.CENTER);
		title.setVerticalTextPosition(SwingConstants.CENTER);
		title.setOpaque(false);

		downTitle = new JLabel("<html>Spielt Gemeinsam im lokalen Netzwerk<br/><br/><br/><br/></html>");
		downTitle.setForeground(Color.white);
		downTitle.setFont(pongFrame.getGLOBAL_FONT().deriveFont(0, 30));
		downTitle.setPreferredSize(new Dimension(1920, 150));
		downTitle.setHorizontalAlignment(SwingConstants.CENTER);
		downTitle.setHorizontalTextPosition(SwingConstants.CENTER);
		downTitle.setVerticalTextPosition(SwingConstants.CENTER);
		downTitle.setOpaque(false);

		backgroundLabel.add(title);
		backgroundLabel.add(downTitle);
		Dimension middleSize = new Dimension(1920, (int) Math.round(1080. / 1.6));
		changePanel = new JPanel();
		changePanel.setSize(middleSize);
		changePanel.setPreferredSize(middleSize);
		changePanel.setOpaque(false);
		changePanel.setLayout(layout);

		createServerPanel = new CreateServerPanel(pongFrame);
		createServerPanel.setSize(middleSize);
		createServerPanel.setOpaque(false);
		changePanel.add(createServerPanel, "createServerPanel");

		joinServerPanel = new JoinServerPanel(pongFrame);
		joinServerPanel.setSize(middleSize);
		joinServerPanel.setOpaque(false);
		changePanel.add(joinServerPanel, "joinServerPanel");

		backgroundLabel.add(changePanel);
		backgroundLabel.add(returnToMainMenu = new MenuButton(pongFrame, "Zur�ck"));
		returnToMainMenu.addActionListener(this);
		returnToMainMenu.setSize(new Dimension(200, 50));

		backgroundLabel.add(joinServer = new MenuButton(pongFrame, "Server beitreten"));
		joinServer.addActionListener(this);
		joinServer.setSize(new Dimension(400, 50));
		joinServer.setEnabled(false);

		backgroundLabel.add(createServer = new MenuButton(pongFrame, "Server erstellen"));
		createServer.addActionListener(this);
		createServer.setSize(new Dimension(400, 50));

		this.add(backgroundLabel);

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
