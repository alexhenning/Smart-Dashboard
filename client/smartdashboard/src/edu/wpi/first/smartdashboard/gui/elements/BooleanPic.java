package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.MainWindow;
import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Implements an element whose state switches between two pictures.
 * @author Alex Henning
 */
public class BooleanPic extends StatefulDisplayElement {
    protected String imageTruePath = "images/true.png",
                     imageFalsePath = "images/false.png";
    protected transient BufferedImage imageTrue, imageFalse, image;

    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        revalidate();
        repaint();
	setPreferredSize(new Dimension(100, 100));
	loadImages();

	setProperty(statusBackgroundImageTrue, imageTruePath);
        setProperty(statusBackgroundImageFalse, imageFalsePath);
    }

    public void loadImages() {
        try {
            imageTrue = ImageIO.read(new File(imageTruePath));
            imageFalse = ImageIO.read(new File(imageFalsePath));
	    image = imageFalse;
	    setSize(new Dimension(image.getWidth(), image.getHeight()));
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    
    @Override
    public void paintComponent(Graphics g) {
	if (imageTrue == null || imageFalse == null) {
	    loadImages();
	}
	g.setColor(MainWindow.bgColor);
    g.drawRect(0, 0, 5*getWidth(), 5*getHeight());
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }

    public void update(final Record r) {
        final BooleanPic myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                boolean realValue = (Boolean) r.getValue();
                if(realValue) {
		    image = imageTrue;
		} else {
		    image = imageFalse;
		}

		setSize(new Dimension(image.getWidth(), image.getHeight()));
                myself.revalidate();
                myself.repaint();
            }
        });
    }

    public static Types.Type[] getSupportedTypes() {
        return new Types.Type[] {Types.Type.BOOLEAN};
    }

    @Override
    public boolean propertyChange(String key, Object value) {
	if (key == statusBackgroundImageTrue) {
	    imageTruePath = ((File) value).getPath();
	    try {
		imageTrue = ImageIO.read(new File(imageTruePath));
	    } catch (IOException ex) {}
        }
	else if (key == statusBackgroundImageFalse) {
	    imageFalsePath = ((File) value).getPath();
	    try {
		imageFalse = ImageIO.read(new File(imageFalsePath));
	    } catch (IOException ex) {}
        }
	this.repaint();
	return true;
    }

    @Override
    public Object getPropertyValue(String key) {
	if (key == statusBackgroundImageTrue) return new File(imageTruePath);
	else if (key == statusBackgroundImageFalse) return new File(imageFalsePath);
	return null;
    }

    private final String statusBackgroundImageTrue = "Path to image to show when true";
    private final String statusBackgroundImageFalse = "Path to image to show when false";
}
