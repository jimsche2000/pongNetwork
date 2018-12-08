package pause;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gui.MenuButton;
import gui.MenuLabel;
import hauptmenu.PongFrame;

// JPanel shown in the modal JDialog above
@SuppressWarnings("serial")
public class PausePanel extends JPanel implements ActionListener {
	private static final Color BG = new Color(255, 255, 255);
	private PongFrame frame;
	private MenuButton resume, restart, back;
	private MenuLabel pausedLabel;

	public PausePanel(PongFrame frame) {
		this.frame = frame;
		pausedLabel = new MenuLabel(frame, "Spiel Pausiert");
		pausedLabel.setSize(new Dimension(400, 75));
		JPanel pausedPanel = new JPanel();
		pausedPanel.setOpaque(false);
		pausedPanel.add(pausedLabel);

		setBackground(BG);
		int eb = 15;
		setBorder(BorderFactory.createEmptyBorder(eb, eb, eb, eb));

		add(pausedPanel);
		resume = new MenuButton(frame, "Fortfahren");
		resume.setSize(new Dimension(400, 75));
		resume.addActionListener(this);
		restart = new MenuButton(frame, "Neustarten");
		restart.setSize(new Dimension(400, 75));
		restart.addActionListener(this);
		back = new MenuButton(frame, "Hauptmenü");
		back.setSize(new Dimension(400, 75));
		back.addActionListener(this);
		add(resume);
		add(restart);
		add(back);

		setPreferredSize(new Dimension(450, 365));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(resume)) {
			frame.getSinglePlayer().continueGame();

		} else if (e.getSource().equals(restart)) {
			frame.getSinglePlayer().restartlastGame();

		} else if (e.getSource().equals(back)) {
			frame.getSinglePlayer().stopGame();
			frame.showPane(frame.LEVEL_SELECTION);

		}
		resume();
	}

	// Disposes the JDialog, resumes to Game
	public void resume() {
		Window win = SwingUtilities.getWindowAncestor((Component) resume);
		win.dispose(); // here -- dispose of the JDialog
	}

	public MenuButton getResume() {
		return resume;
	}

	public void setResume(MenuButton resume) {
		this.resume = resume;
	}
}