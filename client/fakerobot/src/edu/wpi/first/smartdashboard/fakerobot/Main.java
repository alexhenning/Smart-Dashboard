package edu.wpi.first.smartdashboard.fakerobot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.System;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author pmalmsten (Modified by Alex Henning)
 */
public class Main {

    public static class SenderThread extends Thread {

        DatagramSocket ds;
        InetAddress localhost;
        ByteArrayOutputStream bastream = new ByteArrayOutputStream();

        @Override
        public void run() {
            try {
                ds = new DatagramSocket();
            } catch (SocketException ex) {
                ex.printStackTrace();
                System.exit(1);
            }

            try {
                localhost = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                System.exit(1);
            }

            while (true) {
                synchronized (DriverStation.getInstance().getStatusDataMonitor()) {
                    DriverStation.getInstance().writePacket(bastream);
                }

                DatagramPacket p = new DatagramPacket(bastream.toByteArray(),
                        bastream.size(),
                        localhost,
                        1165);
                try {
                    ds.send(p);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Clear buffer
                bastream.reset();

                // Emulate robot loop delay
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SenderThread sender = new SenderThread();
        sender.start();

        System.out.println("Running...");

	double positions[] = {1.87, 1.680, 0.915, 0.700, 0};
	
	double v = 0,
	    a = 0,
	    arm = 0;
	int time = 120, matchTime = 120; // seconds
	long startTime = System.nanoTime();
	int target = 4, changeTime = 115;
	boolean grabber;

        while (true) {
	    // Update
	    a += (Math.random() - 0.4) * 2;
	    v += a;
	    if (v < 0) {
		v = 0;
		a = 0;
	    } else if (v > 20) {
		v = 20;
		a = 0;
	    }
	    
	    if (time <= changeTime) {
		target = (int) (Math.random() * positions.length);
		System.out.println((1 + target)+" --- "+positions[target]);
		changeTime = time - (int) (Math.random() * 20);
	    }
	    arm = (arm * .95) + (positions[target] * .05);
	    if (arm < 0) arm = 0;
	    if (arm > 2) arm = 2;

	    if (Math.abs(arm - positions[target]) < 0.1) {
		grabber = true;
	    } else {
		grabber = false;
	    }

	    time = (int) (matchTime - (((double) (System.nanoTime() - startTime))
				       / 1000000000));
	    if (time < 0) {
		startTime = System.nanoTime();
		time = matchTime;
		changeTime = 115;
	    }

	    // Send
	    SmartDashboard.log(v, "Speed");
	    
	    SmartDashboard.log(grabber, "Grabber");
	    SmartDashboard.log(arm, "Arm");
	    if (arm < 0.1) {
		SmartDashboard.log(true, "Limit");
	    } else {
		SmartDashboard.log(false, "Limit");
	    }

	    SmartDashboard.log(time, "Time");

	    // Wait
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException ex) {
		ex.printStackTrace();
	    }
        }
    }
}
