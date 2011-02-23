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
 * @author pmalmsten
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

	double speed = 2,
	    grabber = 0,
	    arm = 0;
	int time, matchTime = 120; // seconds
	long startTime = System.nanoTime();

        while (true) {
	    // Update
	    speed += (4 * Math.random()) - 1.9;
	    if (speed < 0) speed = 0;
	    if (speed > 20) speed = 20;
	    
	    grabber += Math.random()/2-0.1;
	    grabber %= 10;
	    
	    arm += (Math.random() - 0.5) / 4;
	    if (arm < 0) arm = 0;
	    if (arm > 2) arm = 2;

	    time = (int) (matchTime - (((double) (System.nanoTime() - startTime))
				       / 1000000000));
	    if (time < 0) {
		startTime = System.nanoTime();
		time = matchTime;
	    }

	    // Send
	    SmartDashboard.log(speed, "Speed");
	    if (grabber > 5) {
		SmartDashboard.log(true, "Grabber");
	    } else {
		SmartDashboard.log(false, "Grabber");
	    }
		
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
