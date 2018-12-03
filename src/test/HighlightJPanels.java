package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class HighlightJPanels extends JFrame {
	private static final long serialVersionUID = 7163215339973706671L;
	private static final Dimension containerSize = new Dimension(640, 477);
	private JLayeredPane layeredPane;
	static JPanel container;

	public HighlightJPanels() {
		super("Highlight Test");
		setSize(640, 477);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(containerSize);
		getContentPane().add(layeredPane);

		createContainer();

		layeredPane.add(container, JLayeredPane.DEFAULT_LAYER);

		createChildren(4, 4);

		container.addMouseMotionListener(new HighlightJPanelsContainerMouseListener());
	}

	private void createChildren(int columns, int rows) {
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				JPanel child = new JPanel(new BorderLayout());
				child.setBackground(Color.LIGHT_GRAY);
				child.addMouseListener(new HighlightJPanelsChildMouseListeners());
				container.add(child);
			}
		}
	}

	private JPanel createContainer() {
		container = new JPanel();
		container.setLayout(createLayout(4, 4, 1, 1));
		container.setPreferredSize(containerSize);
		container.setBounds(0, 0, containerSize.width, containerSize.height);
		return container;
	}

	private GridLayout createLayout(int rows, int columns, int hGap, int vGap) {
		GridLayout layout = new GridLayout(rows, columns);
		layout.setHgap(hGap);
		layout.setVgap(vGap);
		return layout;
	}

	public static void main(String[] args) {
		new HighlightJPanels();
	}

	public class HighlightJPanelsChildMouseListeners implements MouseListener {
		private Border grayBorder = BorderFactory.createLineBorder(Color.DARK_GRAY);

		public HighlightJPanelsChildMouseListeners() {
		}

		public void mouseEntered(MouseEvent e) {
			Component comp = HighlightJPanels.container.findComponentAt(HighlightJPanelsContainerMouseListener.eX,
					HighlightJPanelsContainerMouseListener.eY);
			JPanel parent = (JPanel) e.getSource();
			parent.setBorder(grayBorder);
			parent.revalidate();
		}

		public void mouseExited(MouseEvent e) {
			Component comp = HighlightJPanels.container.findComponentAt(HighlightJPanelsContainerMouseListener.eX,
					HighlightJPanelsContainerMouseListener.eY);
			JPanel parent = (JPanel) e.getSource();
			parent.setBorder(null);
			parent.revalidate();
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}
	}

	public static class HighlightJPanelsContainerMouseListener implements MouseMotionListener {
		static int eX;
		static int eY;

		public void mouseDragged(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			eX = e.getX();
			eY = e.getY();
		}
	}
}