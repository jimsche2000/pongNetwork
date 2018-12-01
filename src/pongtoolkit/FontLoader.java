package pongtoolkit;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

	/*
	 * 
	 * 
	 * @param name: Just the file-name like "Arial" - without "/fonts/" and without file-ending ".ttf" 
	 * 
	 */
	public Font loadFont(String name) {
		Font myFont = null;
		try {
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     this.getClass().getClassLoader();

		     InputStream istream = ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/"+name+".ttf");//.TTF

		     myFont = Font.createFont(Font.TRUETYPE_FONT, istream);

		     ge.registerFont(myFont);
		     
		     istream.close();

		} catch (IOException|FontFormatException e) {
			e.printStackTrace();
		}
		return myFont.deriveFont(40.0f);		
	}
}