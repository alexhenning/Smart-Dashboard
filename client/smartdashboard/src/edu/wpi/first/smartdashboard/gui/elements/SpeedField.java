package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
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
 * A field for displaying our speed as desired
 * Essentially the label is above and there's some preformatting of the text
 * @author Alex Henning
 */
public class SpeedField extends FormattedField {
    public void init() {
	super.init();

        add(nameLabel, BorderLayout.PAGE_START);
        add(valueField, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void update(final Record r) {
        final SpeedField myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
		String realValue = r.getValue().toString();
		realValue = realValue.substring(0, realValue.indexOf("."));
		if (realValue.length() < 2) { realValue = "0"+realValue; }
		valueField.setValue(realValue);
                myself.revalidate();
                myself.repaint();
            }
        });
    }

    public static Types.Type[] getSupportedTypes() {
        return new Types.Type[]{ Types.Type.DOUBLE };
    }
}
