package testt;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// JPanel shown in the modal JDialog above
@SuppressWarnings("serial")
class DeDialogPanel extends JPanel {
//    private static final Color BG = new Color(123, 63, 0);
    private static final Color BG = new Color(255, 255, 255);

    public DeDialogPanel() {
        JLabel pausedLabel = new JLabel("PAUSED");
        pausedLabel.setForeground(Color.ORANGE);
        JPanel pausedPanel = new JPanel();
        pausedPanel.setOpaque(false);
        pausedPanel.add(pausedLabel);

        setBackground(BG);
        int eb = 15;
        setBorder(BorderFactory.createEmptyBorder(eb, eb, eb, eb));
        setLayout(new GridLayout(0, 1, 10, 10));
        add(pausedPanel);
        add(new JButton(new FooAction("RESUME")));
        add(new JButton(new FooAction("RESTART")));
        add(new JButton(new FooAction("EXIT TO MAP")));
    }

    // simple action -- all it does is to make the dialog no longer visible
    private class FooAction extends AbstractAction {
        public FooAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Component comp = (Component) e.getSource();
            Window win = SwingUtilities.getWindowAncestor(comp);
            win.dispose();  // here -- dispose of the JDialog
        }
    }
}