package routine.core;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.jnativehook.GlobalScreen;

public class TaskBar extends TrayIcon {
    
    private static final String NAME = "Routine";
    public static TaskBar it;
    
    private User user;
    
    public static void createTaskBar() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                it = new TaskBar();
            }
        });
    }
    
    private TaskBar() {
        super(new ImageIcon("").getImage(), NAME);
        this.setToolTip(NAME);
        this.setImageAutoSize(true);
        MenuItem runItem = new MenuItem("Run");
        MenuItem exitItem = new MenuItem("Exit");
        //Add components to popup menu
        PopupMenu popup = new PopupMenu();
        popup.add(runItem);
        popup.addSeparator();
        popup.add(exitItem);
        this.setPopupMenu(popup);
        try {
            SystemTray.getSystemTray().add(this);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added!");
            return;
        }
        try {
            user = new User();
            msgInfo("Start!");
        } catch (AWTException ex) {
            msgError("User error! " + ex);
        }
        runItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser(new File(System.getProperty("user.home"), "routine"));
                int returnVal = fc.showOpenDialog(null);
                if (returnVal != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File file = fc.getSelectedFile();
                if (!file.exists()) {
                    msgError(file.getAbsolutePath() + " not exist!");
                    return;
                }
                try {
                    user.interpret(file);
                } catch (Exception ex) {
                    msgError(ex.toString());
                }
            }
        });
        exitItem.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                SystemTray.getSystemTray().remove(TaskBar.this);
                GlobalScreen.unregisterNativeHook();
                System.out.println("unregisterNativeHook!");
                System.exit(0);
            }
        });
    }
    
    public void msgError(String msg) {
        System.out.println(msg);
        displayMessage(NAME, msg, TrayIcon.MessageType.ERROR);
    }
    
    public void msgInfo(String msg) {
        System.out.println(msg);
        displayMessage(NAME, msg, TrayIcon.MessageType.INFO);
    }
    
    public void msgWarn(String msg) {
        System.out.println(msg);
        displayMessage(NAME, msg, TrayIcon.MessageType.WARNING);
    }
}
