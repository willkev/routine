package gui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.Inet4Address;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jnativehook.GlobalScreen;
import routine.core.Communicator;
import static routine.core.Communicator.DEFAULT_PORT;
import routine.core.GetActions;
import routine.core.Storage;
import routine.core.User;

public class TaskBar extends TrayIcon {

    private static final String NAME = "Routine";
    private static TaskBar INSTANCE;

    private MenuItem runItem = new MenuItem("Run");
    private MenuItem sendItem = new MenuItem("Send");
    private MenuItem receiveItem = new MenuItem("Receive");
    private MenuItem exitItem = new MenuItem("Exit");

    private User user;

    public static TaskBar getInstance() {
        if (INSTANCE == null) {
            createTaskBar();
        }
        return INSTANCE;
    }

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

    private TaskBar() {
        super(new ImageIcon("").getImage());
        this.setImageAutoSize(true);
        try {
            this.setToolTip(Inet4Address.getLocalHost().getHostAddress());
        } catch (Exception ex) {
        }
        //Add components to popup menu
        PopupMenu popup = new PopupMenu();
        popup.add(runItem);
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
            msgInfo("Start!");
        } catch (AWTException ex) {
            msgError("User error! " + ex.getMessage());
        }
        runItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                run();
            }
        });
        sendItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });
        receiveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                receive();
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

    private void send() {
        JTextField serverPort = new JTextField("server:" + DEFAULT_PORT, 10);
        JOptionPane.showMessageDialog(null, serverPort, "server:port", JOptionPane.QUESTION_MESSAGE);
        try {
            Storage.communicator = new Communicator(serverPort.getText());
            GetActions.REC_ON = !GetActions.REC_ON;
        } catch (Exception ex) {
            TaskBar.getInstance().msgError(ex);
            ex.printStackTrace();
        }
    }

    private void receive() {
        JTextField port = new JTextField(DEFAULT_PORT, 10);
        JOptionPane.showMessageDialog(null, port, "port", JOptionPane.QUESTION_MESSAGE);
        try {
            Communicator communicator = new Communicator(Integer.parseInt(port.getText().trim()));
            user.interpret(communicator);
        } catch (Exception ex) {
            TaskBar.getInstance().msgError(ex);
            ex.printStackTrace();
        }
    }

    private void run() {
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
