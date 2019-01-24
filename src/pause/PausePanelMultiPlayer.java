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
public class PausePanelMultiPlayer extends JPanel implements ActionListener {
	private static final Color BG = new Color(255, 255, 255);
	private PongFrame frame;
	private MenuButton resume, back;
	private MenuLabel pausedLabel;

	public PausePanelMultiPlayer(PongFrame frame) {
		this.frame = frame;
		int fourhundred = Math.round(400 * frame.getASPECT_RATIO());
		int seventyFive = Math.round(75 * frame.getASPECT_RATIO());
		pausedLabel = new MenuLabel(frame, "Spiel Pausiert");
		pausedLabel.setAlignment(pausedLabel.ALIGN_MID);
		pausedLabel.setSize(new Dimension(fourhundred, seventyFive));
		JPanel pausedPanel = new JPanel();
		pausedPanel.setOpaque(false);
		pausedPanel.add(pausedLabel);

		setBackground(BG);
		int eb = 15;
		setBorder(BorderFactory.createEmptyBorder(eb, eb, eb, eb));

		add(pausedPanel);
		resume = new MenuButton(frame, "Fortfahren");
		resume.setSize(new Dimension(fourhundred, seventyFive));
		resume.addActionListener(this);
		back = new MenuButton(frame, "Spiel abbrechen");
		back.setSize(new Dimension(fourhundred, seventyFive));
		back.addActionListener(this);
		add(resume);
		add(back);

		setPreferredSize(new Dimension(Math.round(450 * frame.getASPECT_RATIO()), Math.round(365 * frame.getASPECT_RATIO())));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(resume)) {
//			frame.getSinglePlayer().continueGame();
//TODO: Hier befand sich der einzige Unterschied zwischen pausePanelSinglePlayer, und diesem panel für den multiplayer.
			frame.getClientLiveGamePanel().wakeUp();
			
		} else if (e.getSource().equals(back)) {
//			frame.getSinglePlayer().stopGame();
//			frame.showPane(frame.LEVEL_SELECTION);
			/*
			 * Spiel abbrechen, aufgeben. Server darüber Bescheid geben,
			 * dieser soll das laufende Spiel abbrechen,
			 * und eine Chat-Nachricht über die Aufgabe schreiben.
			 * 
			 * Client-Seitig wieder das Client-Control-Panel zeigen
			 * 
			 */
			frame.showPane(frame.CLIENT_CONTROL_PANEL);
		}
		resume();
	}

	// Disposes the JDialog, resumes to Game
	public void resume() {
//		Window win = SwingUtilities.getWindowAncestor((Component) resume);
		Window win = SwingUtilities.getWindowAncestor((Component) this);
		win.dispose(); // here -- dispose of the JDialog
	}

	public MenuButton getResume() {
		return resume;
	}

	public void setResume(MenuButton resume) {
		this.resume = resume;
	}
}