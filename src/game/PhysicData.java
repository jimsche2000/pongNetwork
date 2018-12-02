package game;

public class PhysicData {
	private float ballX, ballY, playerOneX, playerOneY, playerTwoX, playerTwoY;
	private int ballSize, playerOneWidth, playerOneHeight, playerTwoWidth, playerTwoHeight;

	public PhysicData() {
		resetLocations();
	}

	public PhysicData(int ballSize, int playerWidth, int playerHeight) {
		resetLocations();
		this.ballSize = ballSize;
		this.playerOneWidth = playerWidth;
		this.playerOneHeight = playerHeight;
		this.playerTwoWidth = playerWidth;
		this.playerTwoHeight = playerHeight;
	}

	private void resetLocations() {
		ballX = 0.0f;
		ballY = 0.0f;
		playerOneX = 0.0f;
		playerOneY = 0.0f;
		playerTwoX = 0.0f;
		playerTwoY = 0.0f;
	}

	// Getter AND Setter:::
	public void setBallLocation(float ballX, float ballY) {
		this.ballX = ballX;
		this.ballY = ballY;
	}

	public void setPlayerOneLocation(float playerOneX, float playerOneY) {
		this.playerOneX = playerOneX;
		this.playerOneY = playerOneY;
	}

	public void setPlayerTwoLocation(float playerTwoX, float playerTwoY) {
		this.playerTwoX = playerTwoX;
		this.playerTwoY = playerTwoY;
	}

	public float getBallX() {
		return ballX;
	}

	public void setBallX(float ballX) {
		this.ballX = ballX;
	}

	public float getBallY() {
		return ballY;
	}

	public void setBallY(float ballY) {
		this.ballY = ballY;
	}

	public float getPlayerOneX() {
		return playerOneX;
	}

	public void setPlayerOneX(float playerOneX) {
		this.playerOneX = playerOneX;
	}

	public float getPlayerOneY() {
		return playerOneY;
	}

	public void setPlayerOneY(float playerOneY) {
		this.playerOneY = playerOneY;
	}

	public float getPlayerTwoX() {
		return playerTwoX;
	}

	public void setPlayerTwoX(float playerTwoX) {
		this.playerTwoX = playerTwoX;
	}

	public float getPlayerTwoY() {
		return playerTwoY;
	}

	public void setPlayerTwoY(float playerTwoY) {
		this.playerTwoY = playerTwoY;
	}

	public int getBallSize() {
		return ballSize;
	}

	public void setBallSize(int ballSize) {
		this.ballSize = ballSize;
	}

	public int getPlayerOneWidth() {
		return playerOneWidth;
	}

	public void setPlayerOneWidth(int playerOneWidth) {
		this.playerOneWidth = playerOneWidth;
	}

	public int getPlayerOneHeight() {
		return playerOneHeight;
	}

	public void setPlayerOneHeight(int playerOneHeight) {
		this.playerOneHeight = playerOneHeight;
	}

	public int getPlayerTwoWidth() {
		return playerTwoWidth;
	}

	public void setPlayerTwoWidth(int playerTwoWidth) {
		this.playerTwoWidth = playerTwoWidth;
	}

	public int getPlayerTwoHeight() {
		return playerTwoHeight;
	}

	public void setPlayerTwoHeight(int playerTwoHeight) {
		this.playerTwoHeight = playerTwoHeight;
	}

}
