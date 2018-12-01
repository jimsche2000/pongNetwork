package hauptmenu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.MenuButton;
import pongtoolkit.ImageLoader;

public class Credits extends JPanel implements ActionListener{

	private static final long serialVersionUID = -3784130396160148728L;
	private MenuButton returnToMainMenu;
	private ImageIcon background = ImageLoader.loadIcon("Bims_FHD_16_9.png");
	private JLabel backgroundLabel;
	private PongFrame pongFrame;
	
	public Credits(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		this.setSize(pongFrame.getSize());
		this.setLayout(null);
		backgroundLabel = new JLabel();
		backgroundLabel.setSize(new Dimension(1920, 1080));
		backgroundLabel.setLocation(0, 0);
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new FlowLayout());
		
		returnToMainMenu = new MenuButton(pongFrame, "Zurück");
		returnToMainMenu.setSize(new Dimension(200, 75));
		returnToMainMenu.addActionListener(this);
		backgroundLabel.add(returnToMainMenu);
		
		this.add(backgroundLabel);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(returnToMainMenu)) {
			
			pongFrame.showPane(pongFrame.MAIN_MENU);
		}
	}
}
