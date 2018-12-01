package game;

public class Richtung {
	private static boolean richtungX, richtungY, yNichts, xNichts;

	public static boolean isRichtungX() {
		return richtungX;
	}

	public static void setRichtungX(boolean richtungX) {
		Richtung.richtungX = richtungX;
		
	}

	public static boolean isRichtungY() {
		return richtungY;
		
	}

	public static void setRichtungY(boolean richtungY) {
		Richtung.richtungY = richtungY;
				
	}

	public static boolean isyNichts() {
		return yNichts;
	}

	public static void setyNichts(boolean yNichts) {
		Richtung.yNichts = yNichts;
	}

	public static boolean isxNichts() {
		return xNichts;
	}

	public static void setxNichts(boolean xNichts) {
		Richtung.xNichts = xNichts;
	}
	
		
		
}
