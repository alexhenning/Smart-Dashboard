package edu.wpi.first.smartdashboard;

import edu.wpi.first.smartdashboard.gui.DashboardPrefs;
import edu.wpi.first.smartdashboard.gui.MainWindow;
import edu.wpi.first.smartdashboard.net.UDPListener;
import edu.wpi.first.smartdashboard.video.VideoController;
import javax.swing.JOptionPane;
/**
 * Main SmartDashboard logic
 * @author pmalmsten
 */
public class main {
    private static final int PORT = 1165;
    private static final int UPDATE_NUM_OFFSET = 26;
    private static UDPListener m_listener;
    private static StateManager m_stateMan;

    public static void main(String[] args) {
        try {
            m_listener = new UDPListener(PORT, UPDATE_NUM_OFFSET);
        } catch (java.net.SocketException ex) {
            // need some errors here, particularly one if the address is already in
            // use, indicating that there is another instance of the dashboard running
            // somewhere on this system.
            JOptionPane.showMessageDialog(null,
                                          "The dashboard was unable to bind to "
                                          + "port " + PORT + ". Is an instance of "
                                          + "the dashboard already running?",
                                          "Unable to Bind to Port",
                                          JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Initialize the background network thread
        m_stateMan = new StateManager(m_listener);
        m_stateMan.start();

        // Initialize GUI
        MainWindow.init(m_stateMan);

        // Initialize video
        DashboardPrefs prefs = DashboardPrefs.getInstance();
        if(prefs.getShowCameraVideo()) {
            VideoController.showVideo();
            VideoController.startReceivingVideo(prefs.getCameraVideoTeamNumber());
        }
        prefs.addPreferenceChangeListener(VideoController.videoPreferencesChangeListener);
    }
}
