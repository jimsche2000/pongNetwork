package gui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.plaf.synth.SynthSliderUI;

/**
 *
 * @see http://stackoverflow.com/a/12297384/714968
 */
public class CustomSliderUI extends BasicSliderUI {
	/*

	 */
    private BasicStroke stroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, 
            BasicStroke.JOIN_ROUND, 0f, new float[]{1f, 2f}, 0f);
    private static float[] fracs = {0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f};
    private LinearGradientPaint p;

    public CustomSliderUI(JSlider b) {
        super(b);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g, c);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(12, 16);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle t = trackRect;
        Point2D start = new Point2D.Float(t.x, t.y);
        Point2D end = new Point2D.Float(t.width, t.height);
        Color[] colors = {Color.magenta, Color.blue, Color.cyan,
            Color.green, Color.yellow, Color.red};
        p = new LinearGradientPaint(start, end, fracs, colors);
        g2d.setPaint(p);
        g2d.fillRect(t.x, t.y-t.height, t.width, t.height*2);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        Rectangle t = thumbRect;
        g2d.setColor(Color.black);
        
        g2d.fillOval(t.x-t.width, t.y - t.height, t.height*2, t.height*2);
//        int tw2 = t.width / 2;
//        g2d.drawLine(t.x, t.y, t.x + t.width - 1, t.y);
//        g2d.drawLine(t.x, t.y, t.x + tw2, t.y + t.height);
//        g2d.drawLine(t.x + t.width - 1, t.y, t.x + tw2, t.y + t.height);
    }
    
    
//    @Override
//    public void paintTrack(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        Stroke old = g2d.getStroke();
//        g2d.setStroke(stroke);
//        g2d.setPaint(Color.BLACK);
//        if (slider.getOrientation() == SwingConstants.HORIZONTAL) {
//            g2d.drawLine(trackRect.x, trackRect.y + trackRect.height / 2, 
//                    trackRect.x + trackRect.width, trackRect.y + trackRect.height / 2);
//        } else {
//            g2d.drawLine(trackRect.x + trackRect.width / 2, trackRect.y, 
//                    trackRect.x + trackRect.width / 2, trackRect.y + trackRect.height);
//        }
//        g2d.setStroke(old);
//    }
//
//    @Override
//    public void paintThumb(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        int x1 = thumbRect.x + 2;
//        int x2 = thumbRect.x + thumbRect.width - 2;
//        int width = thumbRect.width - 4;
//        int topY = thumbRect.y + thumbRect.height / 2 - thumbRect.width / 3;
//        
////        g2d.fillOval(x1, 0, width, topY);
//        GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
//        shape.moveTo(x1, topY);
//        shape.lineTo(x2, topY);
//        shape.lineTo((x1 + x2) / 2, topY + width);
//        shape.closePath();
//        g2d.setPaint(new Color(81, 83, 186));
//        g2d.fill(shape);
//        Stroke old = g2d.getStroke();
//        g2d.setStroke(new BasicStroke(2f));
//        g2d.setPaint(new Color(131, 127, 211));
//        g2d.draw(shape);
//        g2d.setStroke(old);
//    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1000, 500);
//        frame.setLayout(null);
        frame.setBackground(Color.black);
        JSlider slider = new JSlider(0, 100);
//        slider.setOpaque(false);
//        slider.setLocation(0,0);
//        slider.setPreferredSize(new Dimension(1000, 500));
//        slider.setMinimumSize(new Dimension(1000, 500));
//        slider.setPaintTicks(true);
//        slider.setPaintLabels(true);
//        slider.setMinorTickSpacing(1);
//        slider.setMajorTickSpacing(10);
//        slider.setUI(new CustomSliderUI(slider));
//        slider.setUI(new MetalSliderUI());
//        slider.setUI(new SynthSliderUI(slider));
        frame.add(slider);
//        frame.getContentPane().add(slider);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
        frame.setVisible(true);
    }
}