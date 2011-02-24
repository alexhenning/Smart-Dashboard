package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 */
public class FormattedField extends StatefulDisplayElement {
    private JFormattedTextField valueField;

    public void init() {
        setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel(m_name);
        valueField = new JFormattedTextField();
        valueField.setEditable(false);
        valueField.setColumns(10);

        add(nameLabel, BorderLayout.LINE_START);
        add(valueField, BorderLayout.CENTER);
        revalidate();
        repaint();

	setProperty(widthProperty, valueField.getColumns());
	setProperty(backgroundProperty, valueField.getBackground());
    }

    public void update(final Record r) {
        final FormattedField myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Object realValue = r.getValue();
		valueField.setValue(realValue);
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
	if (key == widthProperty) valueField.setColumns(Integer.parseInt((String)value));
	else if (key == backgroundProperty) valueField.setBackground((Color) value);
	return true;
    }

    @Override
    public Object getPropertyValue(String key) {
	if (key == widthProperty) return valueField.getColumns();
	else if (key == backgroundProperty) return valueField.getBackground();
	else return null;
    }

    private final String widthProperty = "Width";
    private final String backgroundProperty = "Background color";
}
