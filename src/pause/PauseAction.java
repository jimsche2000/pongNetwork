package pause;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Graphics;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import hauptmenu.PongFrame;

// Action / ActionListener for JButton -- shows JDialog and darkens glasspane
@SuppressWarnings("serial")
public class PauseAction {
	private static final int ALPHA = 175; // how much see-thru. 0 to 255
	private static final Color GP_BG = new Color(0, 0, 0, ALPHA);
	private PausePanelSinglePlayer pausePanelSingle; // jpanel shown in JDialog
	private PausePanelMultiPlayer pausePanelMulti;
	private PongFrame frame;
	public String ID_SINGLEPLAYER_PANEL = "singlePlayer";
	public String ID_MULTIPLAYER_PANEL = "multiPlayer";
	private long lastAction;
	private long waitingTimeMS = 1000L;//ms
	
	
	public PauseAction(PongFrame frame) {
		this.frame = frame;
		pausePanelSingle = new PausePanelSinglePlayer(frame);
		pausePanelMulti = new PausePanelMultiPlayer(frame);
		lastAction = 0l;
	}

//	private boolean haveWaited() {
//		System.out.println("Have enough waited? ? "+(System.currentTimeMillis() - lastAction > waitingTimeMS)+" curr: "+System.currentTimeMillis()+" last: "+lastAction+" waitingTime: "+waitingTimeMS+" difference between now and last: "+(System.currentTimeMillis() - lastAction));
//		if(System.currentTimeMillis() - lastAction > waitingTimeMS) {
//
//			lastAction = System.currentTimeMillis();
//			return true;
//		}
//		return false;
//	}
	
	public void action(String ID) {
//		if(haveWaited()) {
			// create our glass pane
			JPanel glassPane = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					// magic to make it dark without side-effects
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
					super.paintComponent(g);
				}
			};
			// more magic below
			glassPane.setOpaque(false);
			glassPane.setBackground(GP_BG);

			// get the rootpane container, here the JFrame, that holds the JButton
			RootPaneContainer win = (RootPaneContainer) SwingUtilities.getWindowAncestor(frame.getSinglePlayer());
			win.setGlassPane(glassPane); // set the glass pane
			glassPane.setVisible(true); // and show the glass pane

			// create a *modal* JDialog
			JDialog dialog = new JDialog((Window) win, "", ModalityType.APPLICATION_MODAL);
//			System.out.println("ACTION? "+ID);
			if(ID.equals(ID_SINGLEPLAYER_PANEL)) {
//				System.out.println("ACTION: SINGLE");
				dialog.getContentPane().add(pausePanelSingle); // add its JPanel to it
			
			}else if(ID.equals(ID_MULTIPLAYER_PANEL)) {
//				System.out.println("ACTION: MULTI");
				dialog.getContentPane().add(pausePanelMulti); // add its JPanel to it
			}
			dialog.setUndecorated(true); // give it no borders (if desired)
			dialog.pack(); // size it
			dialog.setLocationRelativeTo((Window) win); // ** Center it over the JFrame **
			dialog.setVisible(true); // display it, pausing the GUI below it

			// at this point the dialog is no longer visible, so get rid of glass pane
			glassPane.setVisible(false);
//		}


	}

	public JPanel getPausePanel(String ID) {
		if(ID.equals(ID_MULTIPLAYER_PANEL)) {

//			System.out.println("PANEL: MULTI");
			return pausePanelMulti;
		}else if(ID.equals(ID_SINGLEPLAYER_PANEL)) {
//			System.out.println("PANEL: SINGLE");
			return pausePanelSingle;
		}else {
			return pausePanelSingle;
		}
	}

	public void setPausePanel(PausePanelSinglePlayer pausePanel) {
		this.pausePanelSingle = pausePanel;
	}
}