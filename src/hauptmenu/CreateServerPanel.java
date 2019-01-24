package hauptmenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.JTextFieldCharLimit;
import gui.JTextFieldJustNumbersDocument;
import gui.MenuButton;
import gui.MenuLabel;
import gui.MenuTextField;
import multiplayer.server.ServerMainThread;

public class CreateServerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MenuTextField serverNameTextField, maxUserTextField;
	private MenuLabel maxUserLabel, labelServerName;
	private MenuButton hostServer;
	private JPanel properties;
	private boolean firsttime = true;
	private PongFrame pongFrame;

	public CreateServerPanel(PongFrame pongFrame, Dimension midSize) {
		this.pongFrame = pongFrame;
		setLayout(new FlowLayout());
		setPreferredSize(midSize);
		
		properties = new JPanel();
		properties.setPreferredSize(new Dimension(Math.round(800 * pongFrame.getASPECT_RATIO()), midSize.height));
		properties.setOpaque(false);
		properties.setLayout(new FlowLayout());
		
		labelServerName = new MenuLabel(pongFrame, "Server-Name:");
		labelServerName.setSize(new Dimension(Math.round(275 * pongFrame.getASPECT_RATIO()), Math.round(50 * pongFrame.getASPECT_RATIO())));
		labelServerName.setFont(pongFrame.getGLOBAL_FONT().deriveFont(24f * pongFrame.getASPECT_RATIO()));
		labelServerName.setDrawBackground(false);
		labelServerName.setForeground(Color.white);
		properties.add(labelServerName);

		serverNameTextField = new MenuTextField(pongFrame, System.getProperty("user.name"));
		serverNameTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f * pongFrame.getASPECT_RATIO()));
		serverNameTextField.setSize(new Dimension(Math.round(325 * pongFrame.getASPECT_RATIO()), Math.round(50 * pongFrame.getASPECT_RATIO())));
		serverNameTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		serverNameTextField.setDocument(new JTextFieldCharLimit(25));
		serverNameTextField.setText(System.getProperty("user.name")); //Wichtig, nach setDocument. Ansonsten kein start-text
		serverNameTextField.setForeground(Color.white);
		properties.add(serverNameTextField);

		maxUserLabel = new MenuLabel(pongFrame, "Maximale User-Anzahl:");
		maxUserLabel.setSize(new Dimension(Math.round(415 * pongFrame.getASPECT_RATIO()), Math.round(50 * pongFrame.getASPECT_RATIO())));
		maxUserLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f * pongFrame.getASPECT_RATIO()));
		maxUserLabel.setDrawBackground(false);
		maxUserLabel.setForeground(Color.white);
		maxUserLabel.setBorder(BorderFactory.createEmptyBorder(0, 500, 0, 0));
		properties.add(maxUserLabel);
		
		maxUserTextField = new MenuTextField(pongFrame, "100");
		maxUserTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f * pongFrame.getASPECT_RATIO()));
		maxUserTextField.setSize(new Dimension(Math.round(185 * pongFrame.getASPECT_RATIO()), Math.round(50 * pongFrame.getASPECT_RATIO())));
		maxUserTextField.setOpaque(false);
		maxUserTextField.setDocument(new JTextFieldJustNumbersDocument(3));
		maxUserTextField.setText("100"); //Wichtig, nach setDocument. Ansonsten kein start-text
		maxUserTextField.setForeground(Color.white);
		properties.add(maxUserTextField);

		hostServer = new MenuButton(pongFrame, "Server hosten");
		hostServer.setSize(new Dimension(Math.round(605 * pongFrame.getASPECT_RATIO()), Math.round(100 * pongFrame.getASPECT_RATIO())));
		hostServer.addActionListener(this);
		hostServer.setActionCommand("HOST_SERVER");
		properties.add(hostServer);

		this.add(properties);
	}

	public void refresh() {
		if (pongFrame.getHostServer().isShouldRun()) {

			hostServer.setText("Zum Server");
		} else {
			hostServer.setText("Server hosten");
		}
	}

	public void reload() {
		hostServer.setEnabled(!pongFrame.getHostServer().isShouldRun());
	}
	public void setFirstTimeBecauseThereIsAnotherServerRunning(boolean firstTime) {
		this.firsttime = firstTime;
	}
	//firsttime is also used, to say if last try to start server was successful or not. It is set to true if it wasn't successful


	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("HOST_SERVER")) {

			try {
				System.out.println("SERVER: "+pongFrame.getHostServer());
				if (pongFrame.getHostServer() == null) {
					System.out.println("firsttime? "+firsttime);
					if (firsttime) {

						pongFrame.setHostServer(new ServerMainThread(serverNameTextField.getText(),
								Long.valueOf(maxUserTextField.getText()), pongFrame));


						System.err.println("STARTING SERVER\nDISCOVERY ALIVE: "
								+ pongFrame.getHostServer().getDiscoveryThread().isAlive());
						pongFrame.getHostServer().setShouldRun(true);
						pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(true);
						refresh();
						firsttime = false;
					} else {

						pongFrame.getHostServer().setName(serverNameTextField.getText());
						pongFrame.getHostServer().setMaxUser(maxUserTextField.getText());
						pongFrame.getHostServer().setShouldRun(true);
						pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(true);
						refresh();
					}

				} else if (!pongFrame.getHostServer().isShouldRun()) {
					System.out.println("SECONDDDD... FIRSTTIME? "+firsttime);
					if (firsttime) {

						pongFrame.setHostServer(new ServerMainThread(serverNameTextField.getText(),
								Long.valueOf(maxUserTextField.getText()), pongFrame));
						
						pongFrame.getHostServer().setShouldRun(true);

						System.err.println("STARTING SERVER\nDISCOVERY ALIVE: "
								+ pongFrame.getHostServer().getDiscoveryThread().isAlive());
//						pongFrame.getHostServer().setShouldRun(true);

						pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(true);
						refresh();

						firsttime = false;
					} else {

						pongFrame.getHostServer().setName(serverNameTextField.getText());
						pongFrame.getHostServer().setMaxUser(maxUserTextField.getText());
						pongFrame.getHostServer().setShouldRun(true);
						pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(true);
						refresh();
					}
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

			pongFrame.showPane(pongFrame.SERVER_CONTROL_PANEL);
		} else if (e.getActionCommand().equals("SKIP_BACK")) {
			pongFrame.showPane(pongFrame.MAIN_MENU);
		}
	}
}