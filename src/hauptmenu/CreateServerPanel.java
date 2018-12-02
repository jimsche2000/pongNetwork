package hauptmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

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

	private PongFrame pongFrame;

	public CreateServerPanel(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		properties = new JPanel();
		properties.setOpaque(false);
		properties.setBackground(Color.black);
		this.setSize(pongFrame.getSize());
		this.setPreferredSize(pongFrame.getSize());
		properties.setPreferredSize(new Dimension((int) (pongFrame.getWidth() * 0.5), (int) (pongFrame.getHeight())));
		properties.setAlignmentX(SwingConstants.CENTER);
		properties.setAlignmentY(SwingConstants.CENTER);

		labelServerName = new MenuLabel(pongFrame, "Server-Name:");
		labelServerName.setSize(new Dimension(300, 50));
		labelServerName.setFont(pongFrame.getGLOBAL_FONT().deriveFont(24f));
		properties.add(labelServerName);

		serverNameTextField = new MenuTextField(pongFrame, System.getProperty("user.name"));
		serverNameTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
		serverNameTextField.setSize(new Dimension(300, 50));
		serverNameTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		properties.add(serverNameTextField);

		maxUserLabel = new MenuLabel(pongFrame, "Maximale Anzahl der User:");
		maxUserLabel.setSize(new Dimension(510, 50));
		maxUserLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
		properties.add(maxUserLabel);

		maxUserTextField = new MenuTextField(pongFrame, "100");
		maxUserTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
		maxUserTextField.setSize(new Dimension(90, 50));
		maxUserTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		maxUserTextField.setOpaque(false);
		properties.add(maxUserTextField);

		hostServer = new MenuButton(pongFrame, "Server hosten");
		hostServer.setSize(new Dimension(605, 100));
		hostServer.setBorder(new EmptyBorder(0, 800, 0, 800));
		hostServer.addActionListener(this);
		hostServer.setActionCommand("HOST_SERVER");
		properties.add(hostServer);

		this.setAlignmentX(SwingConstants.CENTER);
		this.setAlignmentY(SwingConstants.CENTER);
		this.add(properties, BorderLayout.CENTER);
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

	boolean firsttime = true;

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("HOST_SERVER")) {

			try {

				if (pongFrame.getHostServer() == null) {

					if (firsttime) {

						pongFrame.setHostServer(new ServerMainThread(serverNameTextField.getText(),
								Long.valueOf(maxUserTextField.getText()), pongFrame));
						Thread serverThread = new Thread(pongFrame.getHostServer());
						serverThread.start();

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
					if (firsttime) {

						pongFrame.setHostServer(new ServerMainThread(serverNameTextField.getText(),
								Long.valueOf(maxUserTextField.getText()), pongFrame));
						Thread serverThread = new Thread(pongFrame.getHostServer());
						serverThread.start();

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