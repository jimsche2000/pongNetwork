package hauptmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.MenuButton;
import pongtoolkit.ImageLoader;

public class Credits extends JPanel implements ActionListener {

	private static final long serialVersionUID = -3784130396160148728L;
	private MenuButton returnToMainMenu;
	private ImageIcon background;
	private JLabel backgroundLabel;
	private PongFrame pongFrame;

	public Credits(PongFrame pongFrame) {
		this.pongFrame = pongFrame;
		Dimension preferredSize = pongFrame.getGraphicResolution();
		background = ImageLoader.loadIcon("Bims_FHD_16_9.png", preferredSize);
		this.setLayout(new BorderLayout());
		backgroundLabel = new JLabel();
		backgroundLabel.setPreferredSize(preferredSize);
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new FlowLayout());

		returnToMainMenu = new MenuButton(pongFrame, "Zurück");
		returnToMainMenu.setSize(new Dimension(preferredSize.width / 5, preferredSize.height / 15));
		returnToMainMenu.addActionListener(this);
		backgroundLabel.add(returnToMainMenu);
		this.setBackground(Color.black);
		this.add(backgroundLabel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(returnToMainMenu)) {

			pongFrame.showPane(pongFrame.MAIN_MENU);
		}
	}
}
