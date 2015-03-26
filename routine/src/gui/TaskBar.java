package gui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.net.Inet4Address;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jnativehook.GlobalScreen;
import routine.core.Communicator;
import static routine.core.Communicator.PORT;
import routine.core.GetActions;
import routine.core.Storage;
import routine.core.User;

public class TaskBar extends TrayIcon {

    private static final String NAME = "Routine";
    private static TaskBar INSTANCE;

    private MenuItem imitateItem = new MenuItem("Imitate");
    private MenuItem screenItem = new MenuItem("Screen");
    private MenuItem sendItem = new MenuItem("Send");
    private MenuItem receiveItem = new MenuItem("Receive");
    private MenuItem exitItem = new MenuItem("Exit");

    private User user;
    private GetActions getActions;

    public static void createTaskBar() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        if (INSTANCE != null) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                INSTANCE = new TaskBar();
            }
        });
    }

    public static TaskBar getInstance() {
        if (INSTANCE == null) {
            createTaskBar();
        }
        return INSTANCE;
    }

    private TaskBar() {
        super(new ImageIcon("").getImage());
        this.setImageAutoSize(true);
        try {
            this.setToolTip(Inet4Address.getLocalHost().getHostAddress());
        } catch (Exception ex) {
        }
        //Add components to popup menu
        PopupMenu popup = new PopupMenu();
        popup.add(imitateItem);
        popup.addSeparator();
        popup.add(screenItem);
        popup.add(sendItem);
        popup.add(receiveItem);
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
            getActions = new GetActions(true, true);
            msgInfo("Start!");
        } catch (AWTException ex) {
            msgError("User error! " + ex.getMessage());
        }
        imitateItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imitate();
            }
        });
        sendItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (send()) {
                    if (!getActions.isRecOn()) {
                        msgInfo("Rec!");
                    }
                    getActions.setRecOn(true);
                }
            }
        });
        receiveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                receive();
            }
        });
        screenItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                screen();
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

    private boolean send() {
        JTextField serverPort = new JTextField("server:" + PORT, 10);
        JOptionPane.showMessageDialog(null, serverPort, "server:port", JOptionPane.QUESTION_MESSAGE);
        try {
            Storage.communicator = new Communicator(serverPort.getText());
        } catch (Exception ex) {
            msgError(ex);
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private void receive() {
        JTextField port = new JTextField(PORT, 10);
        JOptionPane.showMessageDialog(null, port, "port", JOptionPane.QUESTION_MESSAGE);
        try {
            Communicator communicator = new Communicator(Integer.parseInt(port.getText().trim()));
            user.interpret(communicator);
        } catch (Exception ex) {
            msgError(ex);
            ex.printStackTrace();
        }
    }

    private void imitate() {
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
            msgError(ex);
        }
    }

    private void screen() {
        if (send()) {
            Storage.screen = new Screen();
            if (!getActions.isRecOn()) {
                msgInfo("Rec!");
            }
            getActions.setRecOn(true);
        }
    }

    public void msgError(Exception ex) {
        msgError(ex.getMessage());
    }

    public void msgError(String msg) {
        System.out.println("[ERROR]" + msg);
        displayMessage(NAME, msg, TrayIcon.MessageType.ERROR);
    }

    public void msgInfo(String msg) {
        System.out.println(msg);
        displayMessage(NAME, msg, TrayIcon.MessageType.INFO);
    }

    public void msgWarn(String msg) {
        System.out.println("[WARN]" + msg);
        displayMessage(NAME, msg, TrayIcon.MessageType.WARNING);
    }

}
