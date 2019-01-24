package multiplayer.datapacks;

import java.io.Serializable;

public class GameCountdownData implements Serializable{

	private static final long serialVersionUID = 1L;
	String nowtime;
	public GameCountdownData() {
		nowtime = "";
	}
	public GameCountdownData(String nowtime) {
		this.nowtime = nowtime;
	}
	public String getNowtime() {
		return nowtime;
	}
	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}
}
