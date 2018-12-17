package test;
/*
 * Output:
 *  
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainClass extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public void paint(Graphics g) {
    int fontSize = 20;

    g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
    
    
    FontMetrics fm = g.getFontMetrics();

    String s = "www.java2s.com";
    
    int stringWidth = fm.stringWidth(s);

    int w = 300;
    int h = 300;
    
    int x = (w - stringWidth) / 2;
    int baseline = fm.getMaxAscent() + (h - (fm.getAscent() + fm.getMaxDescent()))/2;
    int ascent  = fm.getMaxAscent();
    int descent = fm.getMaxDescent();
    int fontHeight = fm.getMaxAscent() + fm.getMaxDescent();

    g.setColor(Color.white);
    g.fillRect(x, baseline-ascent , stringWidth, fontHeight);

    g.setColor(Color.gray);
    g.drawLine(x, baseline, x+stringWidth, baseline);

    g.setColor(Color.red);
    g.drawLine(x, baseline+descent, x+stringWidth, baseline+descent);

    g.setColor(Color.blue);
    g.drawLine(x, baseline-ascent,
               x+stringWidth, baseline-ascent);
    g.setColor(Color.black);
    g.drawString(s, x, baseline);
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.getContentPane().add(new MainClass());

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(200,200);
    frame.setVisible(true);
  }
}
  