package game;




import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import pongtoolkit.ImageLoader;

public class Schlag extends JLabel{
/**
	 * 
	 */
	private static final long serialVersionUID = 3819804074948555813L;
private ImageIcon schlagIcon = ImageLoader.loadIcon("Schläger.png");

//	private float ASPECT_RATIO;
	public Schlag(int x,int y){
		this.setLocation(x, y);
		this.setSize(10,200);
		this.setIcon(schlagIcon);
//		ASPECT_RATIO = 1.0f;
	}
//	public Point getLocation() { //For painting
//		Point location = super.getLocation();
//		location.setLocation(Math.round(location.x*ASPECT_RATIO),Math.round(location.y*ASPECT_RATIO));
//		return location;
//	}
//	public Point getFHDLocation() {
//		return super.getLocation();
//	}
//	public void reLocate(float aspect_ratio) {
//		this.ASPECT_RATIO = aspect_ratio;
//	}
}
