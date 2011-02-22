/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author brad
 */
public class ProgressBar extends StatefulDisplayElement {

    private JProgressBar progressBar;
    private final String backgroundProperty = "Background";
    private final String maximumProperty = "Maximum";
    private final String minimumProperty = "Minimum";

    @Override
    public void init() {
	progressBar = new JProgressBar();
	progressBar.setMaximum(0);
	progressBar.setMaximum(100);
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	add(new JLabel(m_name));
	add(progressBar);
	revalidate();
	repaint();

	setProperty(backgroundProperty, progressBar.getBackground());
	setProperty(maximumProperty, progressBar.getMaximum());
	setProperty(minimumProperty, progressBar.getMinimum());
    }

    @Override
    public boolean propertyChange(String key, Object value) {
	if (key == backgroundProperty) {
	    progressBar.setBackground((Color) value);
	} else if (key == maximumProperty) {
	    progressBar.setMaximum(Integer.parseInt((String) value));
	} else if (key == minimumProperty) {
	    progressBar.setMinimum(Integer.parseInt((String) value));
	}
	return true;
    }

    public static Types.Type[] getSupportedTypes() {
	return new Types.Type[]{
		    Types.Type.BYTE,
		    Types.Type.INT,
		    Types.Type.SHORT
		};
    }

    @Override
    public Object getPropertyValue(String key) {
	if (key == backgroundProperty) {
	    return progressBar.getBackground();
	}
	if (key == maximumProperty) {
	    return progressBar.getMaximum();
	}
	if (key == minimumProperty) {
	    return progressBar.getMinimum();
	} else {
	    return 0;
	}
    }

    public void update(final Record r) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
		Number value = (Number) r.getValue();
                progressBar.setValue(value.intValue());
                ProgressBar.this.revalidate();
                ProgressBar.this.repaint();
            }
        });
    }
}
