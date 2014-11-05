package core;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class User {

    private Robot robot;

    public User() throws AWTException {
        robot = new Robot();
    }

    public void clickKey(int keyCode) {
        robot.keyPress(keyCode);
        robot.delay(10);
        robot.keyRelease(keyCode);
    }

    public void clickMouse1() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void clickMouse2() {
        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
    }
}
