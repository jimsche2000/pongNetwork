package multiplayer.datapacks;

import java.awt.Point;
import java.io.Serializable;

public class PongLocationData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8289523661297350083L;
	private Point sliderLeft, sliderRight, ball;
	private String score;
	private int GAME_ID;

	public PongLocationData(Point sliderLeft, Point sliderRight, Point ball) {
		this.sliderLeft = sliderLeft;
		this.sliderRight = sliderRight;
		this.ball = ball;
		this.score = "0 : 0";
		GAME_ID = -1;
	}

	public int getGAME_ID() {
		return GAME_ID;
	}

	public void setGAME_ID(int gAME_ID) {
		GAME_ID = gAME_ID;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public PongLocationData() {

	}

	public Point getSliderLeft() {
		return sliderLeft;
	}

	public void setSliderLeft(Point sliderLeft) {
		this.sliderLeft = sliderLeft;
	}

	public Point getSliderRight() {
		return sliderRight;
	}

	public void setSliderRight(Point sliderRight) {
		this.sliderRight = sliderRight;
	}

	public Point getBall() {
		return ball;
	}

	public void setBall(Point ball) {
		this.ball = ball;
	}
}