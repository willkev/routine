package socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class WinSocket extends JFrame {

    public JTextArea output = new JTextArea(0, 30);

    public WinSocket(String title) {
        super(title);
        setBounds(0, 0, 350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel();
        p.add(output);
        add(p);
        setVisible(true);
    }

}
