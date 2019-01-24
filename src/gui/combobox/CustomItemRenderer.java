package gui.combobox;
 
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import hauptmenu.PongFrame;
 
/**
 * Customer renderer for JComboBox
 * @author www.codejava.net
 *
 */
public class CustomItemRenderer extends JPanel implements ListCellRenderer {
    private JLabel labelItem = new JLabel();
    private boolean invertColors; 
    private PongFrame frame;
    
    public CustomItemRenderer(PongFrame frame, boolean invertColors) {
    	this.frame = frame;
    	this.invertColors = invertColors;
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
//        constraints.insets = new Insets(2, 2, 2, 2);
        labelItem.setOpaque(true);
        labelItem.setHorizontalAlignment(JLabel.LEFT);
        labelItem.setPreferredSize(new Dimension(Math.round(200*frame.getASPECT_RATIO()), Math.round(35 * frame.getASPECT_RATIO())));
        labelItem.setFont(frame.getGLOBAL_FONT().deriveFont(12f * frame.getASPECT_RATIO())); 
        
        add(labelItem, constraints);
//        setBackground(Color.WHITE);
//        setForeground(Color.black);
    }
     
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
//        String[] countryItem = (String[]) value;
 
        // set country name
//        labelItem.setText(countryItem[0]);
         labelItem.setText((String)value);
         labelItem.setBorder(BorderFactory.createEmptyBorder(0, Math.round(4*frame.getASPECT_RATIO()), 0, 0));
//        JLabel label = new JLabel() {
//            public Dimension getPreferredSize() {
//              return new Dimension(200, 100);
//            }
//          };
        
        // set country flag
//        labelItem.setIcon(new ImageIcon(countryItem[1]));
        if(!invertColors)isSelected = !isSelected;
        if (isSelected) {
            labelItem.setForeground(Color.BLACK);
            labelItem.setBackground(Color.WHITE);
        } else {
            labelItem.setBackground(Color.BLACK);
            labelItem.setForeground(Color.WHITE);
        }
         
        return this;
    }
 
}