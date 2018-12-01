package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class Ball extends JPanel{
//	private float ASPECT_RATIO;
	public Ball (){
		super();
		
		this.setLayout(null);
//		ASPECT_RATIO = 1.0f;
	}

		public void paint (Graphics gr)
		{
			Graphics2D g2d = (Graphics2D)gr;
	    	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.fillOval(0, 0, this.getWidth()-1, this.getHeight());
		}
//		public Point getLocation(String text) {
//			System.out.println("Der wars: "+text);
//			return super.getLocation();
//			
//		}
//		public Point getLocation() { //For painting
//			Point location = super.getLocation();
//			location.setLocation(Math.round(location.x*ASPECT_RATIO),Math.round(location.y*ASPECT_RATIO));
//			return location;
//		}
//		public Point getFHDLocation() {
//			return super.getLocation();
//		}
//		public void reLocate(float aspect_ratio) {
//			this.ASPECT_RATIO = aspect_ratio;
//		}
}