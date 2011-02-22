package edu.wpi.first.smartdashboard.state;

import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.types.Types.Type;
import edu.wpi.first.smartdashboard.util.IStateUpdatable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * RandomRecord: A record that randomly changes it's value without a network
 *               intended to be used to simulate a record when testing and designing
 *               a dashboard without the CRIO
 */
public class RandomRecord extends Record {
    protected static int NEXT_ID = 10;

    public RandomRecord(String name, Type type) {
	super(name, NEXT_ID, type);
	NEXT_ID += 1;
	update();

	// Randomly toggle the value of this record to simulate random data
	new Thread(new Runnable() {
	    public void run() {
		while (true) {
		    update();
                    try {Thread.sleep(100);
                    } catch (InterruptedException ex) {}
		}
	    }
	}).start();
    }

    public void update() {
	if (m_type == Type.BOOLEAN) {
	    setValue(randomBoolean());
	}
    }
    
    public boolean randomBoolean() {
	if (Math.random() >= 0.5) {
	    return true;
	} else {
	    return false;
	}
    }
}