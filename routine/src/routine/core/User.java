package routine.core;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import routine.core.Storage.EventType;

public class User {

    public static boolean ABORT = false;

    private Robot robot;
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
        robot = new Robot();
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
                    interpretLine(line);
                    if (ABORT) {
                        System.out.println("Abortado!");
                        return;
                    }
                }
            }
        }.start();
    }

    public void interpret(final Communicator communicator) throws IOException {
        new Thread() {
            @Override
            public void run() {
                String line;
                while (true) {
                    line = communicator.receive();
                    interpretLine(line);
                }
            }
        }.start();
    }

    private void interpretLine(String line) {
        System.out.println("[#]" + line);
        if (!parserLine(line)) {
            return;
        }
        sleepMs(time);
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
                    text = spliter[2];
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

}


/*


 */
