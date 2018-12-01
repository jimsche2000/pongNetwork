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
//	private ShadowLabel maxUserLabel, title, labelServerName;
	private MenuLabel maxUserLabel, labelServerName;
	private MenuButton hostServer;
	private JPanel properties;
//	private boolean serverIsRunning = false;


	private PongFrame pongFrame;
	
	public CreateServerPanel(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
//		this.setLayout(new BorderLayout());
		properties = new JPanel();
		properties.setOpaque(false);
		properties.setBackground(Color.black);
		this.setSize(pongFrame.getSize());
		this.setPreferredSize(pongFrame.getSize());
		properties.setPreferredSize(new Dimension((int) (pongFrame.getWidth() * 0.5), (int) (pongFrame.getHeight())));
//		properties.setMaximumSize(new Dimension((int) (size.getWidth() * 0.4), (int) (size.getHeight())));
//		properties.setPreferredSize(size);
//		properties.setLayout(null);

		properties.setAlignmentX(SwingConstants.CENTER);
		properties.setAlignmentY(SwingConstants.CENTER);
//		title = new ShadowLabel("Pong - Server", 40, 300, 49);
//		title.setBounds(25, 0, 300, 59);
//		properties.add(title);

//		labelServerName = new ShadowLabel("Server-Name", 24, 300, 31);
		labelServerName = new MenuLabel(pongFrame, "Server-Name:");
		labelServerName.setSize(new Dimension(300, 50));
		labelServerName.setFont(pongFrame.getGLOBAL_FONT().deriveFont(24f));
//		labelServerName.setBounds(44, 65, 300, 31);
		properties.add(labelServerName);

		serverNameTextField = new MenuTextField(pongFrame, System.getProperty("user.name"));
		serverNameTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
//		serverNameTextField.setColumns(10);
		serverNameTextField.setSize(new Dimension(300, 50));
		serverNameTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
//		serverNameTextField.setOpaque(false);
//		userNameTextField.setBounds(44, 100, 300, 25);
		properties.add(serverNameTextField);

//		maxUserLabel = new ShadowLabel("Maximale Anzahl der User", 24, 300, 31);
		maxUserLabel = new MenuLabel(pongFrame, "Maximale Anzahl der User:");
		maxUserLabel.setSize(new Dimension(510, 50));
		maxUserLabel.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
//		maxUserLabel.setBorder(new EmptyBorder(0, 800, 0, 800));
//		maxUserLabel.setBounds(44, 130, 300, 31);
		properties.add(maxUserLabel);

		maxUserTextField = new MenuTextField(pongFrame, "100");
		maxUserTextField.setFont(pongFrame.getGLOBAL_FONT().deriveFont(20f));
		maxUserTextField.setSize(new Dimension(90, 50));
		maxUserTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		maxUserTextField.setOpaque(false);
//		maxUserTextField.setBounds(44, 165, 300, 25);
		properties.add(maxUserTextField);

		hostServer = new MenuButton(pongFrame, "Server hosten");
//		hostServer.setBounds(44, 215, 300, 31);
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
		if(pongFrame.getHostServer().isShouldRun()) {
			
			hostServer.setText("Zum Server");
		}else {
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
				
				if(pongFrame.getHostServer()==null) {
					
//					setServerIsRunning(true);
					if(firsttime) {
						
						pongFrame.setHostServer(new ServerMainThread(serverNameTextField.getText(),
								Long.valueOf(maxUserTextField.getText()), pongFrame));
						Thread serverThread = new Thread(pongFrame.getHostServer());
						serverThread.start();
						
						System.err.println("STARTING SERVER\nDISCOVERY ALIVE: "+pongFrame.getHostServer().getDiscoveryThread().isAlive());
//						if(!(pongFrame.getHostServer().getDiscoveryThread().isAlive()))pongFrame.getHostServer().getDiscoveryThread().start();
						pongFrame.getHostServer().setShouldRun(true);
						pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(true);
						refresh();
						firsttime = false;
					}else {
						
						
						pongFrame.getHostServer().setName(serverNameTextField.getText());
						pongFrame.getHostServer().setMaxUser(maxUserTextField.getText());
						pongFrame.getHostServer().setShouldRun(true);
						pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(true);
						refresh();
					}

				}else if(!pongFrame.getHostServer().isShouldRun()) {
					if(firsttime) {
						
						pongFrame.setHostServer(new ServerMainThread(serverNameTextField.getText(),
								Long.valueOf(maxUserTextField.getText()), pongFrame));
						Thread serverThread = new Thread(pongFrame.getHostServer());
						serverThread.start();
						
						System.err.println("STARTING SERVER\nDISCOVERY ALIVE: "+pongFrame.getHostServer().getDiscoveryThread().isAlive());
//						if(!(pongFrame.getHostServer().getDiscoveryThread().isAlive()))pongFrame.getHostServer().getDiscoveryThread().start();
						pongFrame.getHostServer().setShouldRun(true);
						pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(true);
						refresh();
						
						firsttime = false;
					}else {
						
						pongFrame.getHostServer().setName(serverNameTextField.getText());
						pongFrame.getHostServer().setMaxUser(maxUserTextField.getText());
						pongFrame.getHostServer().setShouldRun(true);
						pongFrame.getHostServer().getDiscoveryThreadInstance().setShouldDiscover(true);
						refresh();
					}
				}

			}catch(Exception e2) {
				e2.printStackTrace();
			}

			pongFrame.showPane(pongFrame.SERVER_CONTROL_PANEL);
		} else if (e.getActionCommand().equals("SKIP_BACK")) {
			pongFrame.showPane(pongFrame.MAIN_MENU);
		}
	}
}