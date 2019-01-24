package gui.combobox;
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxEditor;
 
public class CustomComboBoxEditor extends BasicComboBoxEditor {
    private JLabel label = new JLabel();
    private JPanel panel = new JPanel();
    private Object selectedItem;
     
    public CustomComboBoxEditor(boolean invertColors) {
        label.setOpaque(false);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        if(!invertColors) {
        	label.setForeground(Color.BLACK);
//        	label.setBackground(Color.white);
        }
        else {
        	label.setForeground(Color.white);
//        	label.setBackground(Color.black);
        }
//        panel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1f)));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
        panel.add(label);
        if(invertColors)panel.setBackground(Color.yellow);
        else panel.setBackground(Color.red);
    }
     
    public Component getEditorComponent() {
        return this.panel;
    }
     
    public Object getItem() {
        return "[" + this.selectedItem.toString() + "]";
    }
     
    public void setItem(Object item) {
        this.selectedItem = item;
        label.setText(item.toString());
    }
     
}