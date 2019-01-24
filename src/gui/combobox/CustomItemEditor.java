package gui.combobox;
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.nio.channels.SelectableChannel;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import hauptmenu.PongFrame;
 
/**
 * Editor for JComboBox
 * @author wwww.codejava.net
 *
 */
public class CustomItemEditor extends BasicComboBoxEditor {
    private JPanel panel = new JPanel();
    private JLabel labelItem = new JLabel();
    private String selectedValue;
     
    public CustomItemEditor(PongFrame frame, boolean invertColors) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 5, 2, 2);
         
        labelItem.setFont(frame.getGLOBAL_FONT().deriveFont(12f * frame.getASPECT_RATIO())); 
        labelItem.setOpaque(false);
        labelItem.setHorizontalAlignment(JLabel.LEFT);
                
//        labelItem.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1f)));
        
	    if(!invertColors) {
	    	labelItem.setForeground(Color.BLACK);
	    	labelItem.setBackground(Color.white);
	    }
	    else {
	    	labelItem.setForeground(Color.WHITE);
	    	labelItem.setBackground(Color.black);
	    }
	    
	    panel.add(labelItem, constraints);
	    panel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1f)));
	    if(!invertColors)panel.setBackground(Color.WHITE); 
	    else panel.setBackground(Color.BLACK);
    }
     
    public Component getEditorComponent() {
        return this.panel;
    }
     
    public Object getItem() {
        return this.selectedValue;
    }
     
    public void setItem(Object item) {
        if (item == null) {
            return;
        }
        System.out.println(item);
        System.out.println("cast "+ (String)item);
        
//        String[] countryItem = (String[]) item;
//        selectedValue = countryItem[0];
        labelItem.setText((String)item);
//        labelItem.setIcon(new ImageIcon(countryItem[1]));      
    }  
}