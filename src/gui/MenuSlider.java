package gui;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

import hauptmenu.PongFrame;

public class MenuSlider extends JSlider{

	public MenuSlider(PongFrame frame) {
        super(0,100);
        
        setOpaque(false);
        setPaintTicks(true);
        setPaintLabels(true);
        setMinorTickSpacing(1);
        setMajorTickSpacing(25);
        putClientProperty( "JComponent.sizeVariant", "regular" );
        setFont(frame.getGLOBAL_FONT().deriveFont(200*frame.getASPECT_RATIO()));
        
        Hashtable<Integer,JLabel> labels = new Hashtable<Integer,JLabel>( );
        int percent = 0;
        for(int i = 0; i < 5; i++) {
        	JLabel l = new JLabel(Integer.toString(percent)+"%");
        	l.setFont(frame.getGLOBAL_FONT().deriveFont(8*frame.getASPECT_RATIO()));
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
	}

}
