package multiplayer.datapacks;

import java.io.Serializable;

public class GameRequestData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String playerLeftName, playerRightName;
	private boolean playerLeftAccepted, playerRightAccepted;
	private int GAME_REQUEST_ID;
	/**
	 * @param playerLeftName The name of the left player.
	 * @param playerRightName The name of the right player.
	 * @param GAME_REQUEST_ID The Game-ID for which Game the Request is called.
	 */
	public GameRequestData(String playerLeftName, String playerRightName, int GAME_REQUEST_ID) {
		this.playerLeftName = playerLeftName;
		this.playerRightName = playerRightName;
		this.GAME_REQUEST_ID = GAME_REQUEST_ID;
		this.playerLeftAccepted = false;
		this.playerRightAccepted = false;
	}

	public int getGAME_REQUEST_ID() {
		return GAME_REQUEST_ID;
	}

	public void setGAME_REQUEST_ID(int gAME_REQUEST_ID) {
		GAME_REQUEST_ID = gAME_REQUEST_ID;
	}

	public String getPlayerLeftName() {
		return playerLeftName;
	}

	public void setPlayerLeftName(String playerLeftName) {
		this.playerLeftName = playerLeftName;
	}

	public String getPlayerRightName() {
		return playerRightName;
	}

	public void setPlayerRightName(String playerRightName) {
		this.playerRightName = playerRightName;
	}

	public boolean isPlayerLeftAccepted() {
		return playerLeftAccepted;
	}

	public void setPlayerLeftAccepted(boolean playerLeftAccepted) {
		this.playerLeftAccepted = playerLeftAccepted;
	}

	public boolean isPlayerRightAccepted() {
		return playerRightAccepted;
	}

	public void setPlayerRightAccepted(boolean playerRightAccepted) {
		this.playerRightAccepted = playerRightAccepted;
	}

//	public void refreshAcepptedData(GameRequestData gRD) {
//		this.playerLeftAccepted = gRD.isPlayerLeftAccepted();
//		this.playerRightAccepted = gRD.isPlayerRightAccepted();		
//	}
}