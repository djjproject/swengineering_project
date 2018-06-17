package kr.or.kpew.kieas.common;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;

public class TrayNotification {
	
	public void displayTray(String input) throws AWTException, MalformedURLException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage("경보 알림이 도착했습니다.",input, MessageType.INFO);
    }

}
