package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.util.DisplayElement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 */
public class FormattedLabel extends DisplayElement {
    protected JFormattedTextField label;

    public void init() {
        setLayout(new BorderLayout());

        label = new JFormattedTextField();
	label.setEditable(false);
        label.setColumns(10);
	label.setValue("Label");

        add(label, BorderLayout.CENTER);
        revalidate();
        repaint();

	setProperty(foregroundProperty, label.getForeground());
	setProperty(backgroundProperty, label.getBackground());
	setProperty(fontSizeProperty, label.getFont().getSize());
    	setProperty(widthProperty, getWidth());
	setProperty(heightProperty, getHeight());
	setProperty(textProperty, "Label");
    }

    public void update(final Record r) {
        final FormattedLabel myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Object realValue = r.getValue();
		label.setValue(realValue);
                myself.revalidate();
                myself.repaint();
            }
        });
    }

    public static Types.Type[] getSupportedTypes() {
        return Types.Type.values();
    }

    @Override
    public boolean propertyChange(String key, Object value) {
	if (key == foregroundProperty) {
	    label.setForeground((Color) value);
	} else if (key == backgroundProperty) {
	    label.setBackground((Color) value);
	} else if (key == fontSizeProperty) {
	    label.setFont( new Font(label.getFont().getFontName(),
					 label.getFont().getStyle(),
					 (int) Integer.parseInt((String) value)) );
	} else if (key == widthProperty) {
	    setSize(new Dimension(Integer.parseInt((String) value),
				  getHeight()));
	} else if (key == heightProperty) {
	    setSize(getWidth(),
		    Integer.parseInt((String) value));
	} else if (key == textProperty) {
	    label.setValue((String) value);
	}
	return true;
    }

    @Override
    public Object getPropertyValue(String key) {
	if (key == foregroundProperty) {
	    return label.getForeground();
	} else if (key == backgroundProperty) {
	    return label.getBackground();
	} else if (key == fontSizeProperty) {
	    return label.getFont().getSize();
	} else if (key == widthProperty) {
	    return getWidth();
	} else if (key == heightProperty) {
	    return getHeight();
	} else if (key == textProperty) {
	    return label.getValue();
	} else return null;
    }

    protected final String widthProperty = "Width",
	foregroundProperty = "Foreground Color",
	backgroundProperty = "Background Color",
	fontSizeProperty = "Font size",
	heightProperty = "Height",
	textProperty = "Label";

}
