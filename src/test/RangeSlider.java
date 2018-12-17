package test;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JSlider;

/**
 * An extension of JSlider to select a range of values using two thumb controls.
 * The thumb controls are used to select the lower and upper value of a range
 * with pre-determined minimum and maximum values.
 * 
 * <p>RangeSlider makes use of the default BoundedRangeModel, which supports an
 * inner range defined by a value and an extent.  The upper value returned by
 * RangeSlider is simply the lower value plus the extent.</p>
 * 
 * @author Ernie Yu, LimeWire LLC
 */
public class RangeSlider extends JSlider {

    /**
     * Constructs a RangeSlider with default minimum and maximum values of 0
     * and 100.
     */
    public RangeSlider() {
    }

    /**
     * Constructs a RangeSlider with the specified default minimum and maximum 
     * values.
     */
    public RangeSlider(int min, int max) {
        super(min, max);
    }

    /**
     * Overrides the superclass method to install the UI delegate to draw two
     * thumbs.
     */
    @Override
    public void updateUI() {
        setUI(new RangeSliderUI(this));
        // Update UI for slider labels.  This must be called after updating the
        // UI of the slider.  Refer to JSlider.updateUI().
        updateLabelUIs();
    }

    /**
     * Returns the lower value in the range.
     */
    @Override
    public int getValue() {
        return super.getValue();
    }

    /**
     * Sets the lower value in the range.
     */
    @Override
    public void setValue(int value) {
        int oldValue = getValue();
        if (oldValue == value) {
            return;
        }

        // Compute new value and extent to maintain upper value.
        int oldExtent = getExtent();
        int newValue = Math.min(Math.max(getMinimum(), value), oldValue + oldExtent);
        int newExtent = oldExtent + oldValue - newValue;

        // Set new value and extent, and fire a single change event.
        getModel().setRangeProperties(newValue, newExtent, getMinimum(), 
            getMaximum(), getValueIsAdjusting());
    }

    /**
     * Returns the upper value in the range.
     */
    public int getUpperValue() {
        return getValue() + getExtent();
    }

    /**
     * Sets the upper value in the range.
     */
    public void setUpperValue(int value) {
        // Compute new extent.
        int lowerValue = getValue();
        int newExtent = Math.min(Math.max(0, value - lowerValue), getMaximum() - lowerValue);
        
        // Set extent to set upper value.
        setExtent(newExtent);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1000, 500);
//        frame.setLayout(null);
        frame.setBackground(Color.black);
        RangeSlider slider = new RangeSlider(0, 100);
//       slider.
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