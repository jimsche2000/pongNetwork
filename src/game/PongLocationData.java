package game;

import java.awt.Point;
import java.io.Serializable;

public class PongLocationData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8289523661297350083L;
	Point sliderLeft, sliderRight, ball;
	String score;

	public PongLocationData(Point sliderLeft, Point sliderRight, Point ball) {
		this.sliderLeft = sliderLeft;
		this.sliderRight = sliderRight;
		this.ball = ball;
		this.score = "0 : 0";
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