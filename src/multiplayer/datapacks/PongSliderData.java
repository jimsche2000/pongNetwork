package multiplayer.datapacks;

import java.awt.Point;
import java.io.Serializable;

public class PongSliderData implements Serializable {

	private static final long serialVersionUID = 1L;
	private Point slider;
	private int orientation;
	private int gameID;
	public final int SPECTATOR = -1;
	public final int LEFT = 0;
	public final int RIGHT = 1;

	public PongSliderData() {

	}

	public PongSliderData(Point slider, int orientation, int gameID) {
		this.slider = slider;
		this.orientation = orientation;
	}

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public Point getSlider() {
		return slider;
	}

	public void setSlider(Point slider) {
		this.slider = slider;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
}
