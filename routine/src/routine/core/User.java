package routine.core;

import gui.Screen;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import routine.core.Storage.EventType;

public class User {

    public static boolean ABORT = false;

    private final Robot robot;
    private final Rectangle rectangle;
    private BufferedReader reader;
    private ArrayList<String> lines;

    // Campos de trabalho
    private String[] spliter;
    private EventType evenType;
    private long time;
    private int mouseX;
    private int mouseY;
    private int mouseButton;
    private int keyCode;
    private String text;

    public User() throws AWTException {
        this(null);
    }

    public User(Rectangle rectangle) throws AWTException {
        robot = new Robot();
        if (rectangle == null) {
            this.rectangle = Screen.getScreenBounds(0);
        } else {
            this.rectangle = rectangle;
        }
    }

    public void interpret(File file) throws IOException {
        ABORT = false;
        reader = new BufferedReader(new FileReader(file));
        lines = new ArrayList<String>();
        while (reader.ready()) {
            lines.add(reader.readLine());
        }
        reader.close();
        new Thread() {
            @Override
            public void run() {
                for (String line : lines) {
                    interpretLine(line, true);
                    if (ABORT) {
                        System.out.println("Abortado!");
                        return;
                    }
                }
            }
        }.start();
    }

    public void startInterpret(final Communicator communicator) throws IOException {
        new Thread() {
            @Override
            public void run() {
                String line;
                while (true) {
                    line = (String) communicator.receive();
                    interpretLine(line, false);
                    // Retorna a tela
                    communicator.send(getScreen());
                }
            }
        }.start();
    }

    private static final boolean JUMP_USER = false;

    private void interpretLine(String line, boolean useTime) {
        System.out.println("[#]" + line);
        if (!parserLine(line)) {
            return;
        }
        if (JUMP_USER) {
            return;
        }
        if (useTime) {
            sleepMs(time);
        }
        try {
            switch (evenType) {
                case Mouse:
                    robot.mouseMove(mouseX, mouseY);
                    if (mouseButton == 1) {
                        mouseClickLeft();
                    } else if (mouseButton == 2) {
                        mouseClickRight();
                    }
                    break;
                case Text:
                    textInput(keyCode);
                    break;
                case Control:
                    textInput(keyCode);
                    break;
            }
        } catch (Exception ex) {
            System.out.println("ERROR do action!" + ex.getMessage());
        }
    }

    private boolean parserLine(String line) {
        try {
            spliter = line.split(",");
            evenType = EventType.getEventType(spliter[0]);
            time = Long.parseLong(spliter[1]);
            switch (evenType) {
                case Mouse:
                    mouseX = Integer.parseInt(spliter[2]);
                    mouseY = Integer.parseInt(spliter[3]);
                    mouseButton = Integer.parseInt(spliter[4]);
                    break;
                case Text:
                case Control:
                    //text = spliter[2];
                    keyCode = Integer.parseInt(spliter[3]);
                    break;
                default:
                    System.out.println("IGNORED! " + line);
                    return false;
            }
        } catch (Exception ex) {
            System.out.println("ERROR parserLine:" + line);
            return false;
        }
        return true;
    }

    private void sleepMs(long ms) {
        if (ms == 0) {
            return;
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
        }
    }

    private void textInput(int keyCode) {
        robot.delay(5);
        robot.keyPress(keyCode);
        robot.delay(10);
        robot.keyRelease(keyCode);
    }

    private void mouseClickLeft() {
        robot.delay(10);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void mouseClickRight() {
        robot.delay(10);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public ScreenCapture getScreen() {
        BufferedImage bufferImg = robot.createScreenCapture(rectangle);
        return new ScreenCapture(bufferImg);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
