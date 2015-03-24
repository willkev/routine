package gui;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import routine.core.ScreenCapture;

public class Screen extends JPanel {

    private BufferedImage image;
    private JFrame screen;

    public Screen() {
        screen = new JFrame();
        screen.setBounds(0, 0, 600, 600);
        //screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.add(this);
        screen.setVisible(true);
//        screen.addWindowStateListener(new WindowStateListener() {
//            @Override
//            public void windowStateChanged(WindowEvent e) {
//                System.out.println("$$$ " + e.toString());
//                if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
//                    screen.setVisible(false);
//                    screen.setUndecorated(true);
//                    screen.setVisible(true);
//                } else {
//                    screen.setVisible(false);
//                    screen.setUndecorated(false);
//                    screen.setVisible(true);
//                }
//            }
//        });
    }

    public void refreshScreen(ScreenCapture sc) {
        image = sc.getImage();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
        System.out.println("paintComponent!");
    }

    public static Rectangle getScreenBounds(int monitor) {
        GraphicsDevice[] gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        return gd[monitor].getDefaultConfiguration().getBounds();
    }
}
