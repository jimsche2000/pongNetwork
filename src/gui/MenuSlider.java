package gui;

import java.awt.Color;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

import hauptmenu.PongFrame;

public class MenuSlider extends JSlider{

	public MenuSlider(PongFrame frame) {
        super(0,100);
        
//        JComponent 
        
//        setBackground(new Color(255,255,255, 160));
        setOpaque(false);
        setPaintTicks(false);
        setPaintLabels(true);
//        setpai
        setMinorTickSpacing(1);
        setMajorTickSpacing(25);
        setPaintTicks(false);
        
        putClientProperty( "JComponent.sizeVariant", "regular" );
        putClientProperty("JSlider.isFilled", Boolean.TRUE);
        putClientProperty("Slider.paintThumbArrowShape", Boolean.TRUE);
        
        setFont(frame.getGLOBAL_FONT().deriveFont(200*frame.getASPECT_RATIO()));
        
        Hashtable<Integer,JLabel> labels = new Hashtable<Integer,JLabel>( );
        int percent = 0;
        float fontSize = 8*frame.getASPECT_RATIO();
        if(fontSize < 8)fontSize = 8;
        for(int i = 0; i < 5; i++) {
        	JLabel l = new JLabel(Integer.toString(percent)+"%");
        	
        	l.setFont(frame.getGLOBAL_FONT().deriveFont(fontSize));
        	l.setBackground(new Color(255,255,255, 160));
        	l.setOpaque(true);
        	labels.put(new Integer(percent), l);
        	percent+=25;
        }
//        labels.put( new Integer(   0 ), new JLabel( "0%" ) );
//        labels.put( new Integer(  25 ), new JLabel( "25%" ) );
//        labels.put( new Integer(  50 ), new JLabel( "50%" ) );
//        labels.put( new Integer(  75 ), new JLabel( "75%" ) );
//        labels.put( new Integer( 100 ), new JLabel( "100%" ) );
        
        setLabelTable( labels );
        
//        for(JLabel label : labels) {
//        	
//        }
        
//        setUI(new CustomSliderUI(this));

//        System.out.println(paramString());
	}

}
