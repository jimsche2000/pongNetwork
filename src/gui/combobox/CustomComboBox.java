package gui.combobox;
 
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import hauptmenu.PongFrame;
 
/**
 * A custom combo box with its own renderer and editor.
 * @author wwww.codejava.net
 *
 */
public class CustomComboBox extends JComboBox<Object> {
    /**
	 * https://www.codejava.net/java-se/swing/create-custom-gui-for-jcombobox
	 */
	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel model;
//    private boolean invertColors = false;
    private PongFrame pongFrame;
    
    public CustomComboBox(PongFrame pongFrame, boolean invertColors) {
    	this.pongFrame = pongFrame;
//    	this.invertColors = invertColors;
    	
        model = new DefaultComboBoxModel<String[]>();
        setModel(model);
        setRenderer(new CustomItemRenderer(pongFrame, invertColors));
//        setRenderer(new MyComboBoxRenderer<? super Object>());
        setEditor(new CustomItemEditor(pongFrame, invertColors));
    }
     
    /**
     * Add an array items to this combo box.
     * Each item is an array of two String elements:
     * - first element is country name.
     * - second element is path of an image file for country flag.
     * @param items
     */
    public void addItems(String[][] items) {
        for (String[] anItem : items) {
            model.addElement(anItem);
        }
    }

//	public void setInvertColors(boolean b) {
//		this.invertColors = b;
//		
//	}
}