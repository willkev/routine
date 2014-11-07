package core;

import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_NUMPAD0;
import static java.awt.event.KeyEvent.VK_NUMPAD9;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_Z;

public class Storage {

    private long time = 0, lastTime = 0;

    public void addMouse(int x, int y, int button) {
        System.out.format("[%03d,%03d] button-%d [+%d ms]\n", x, y, button, calculateRangeTime());
    }

    public void addKey(int keyCode) {
        if (keyCode >= VK_0 && keyCode <= VK_9
                || keyCode >= VK_A && keyCode <= VK_Z
                || keyCode >= VK_NUMPAD0 && keyCode <= VK_NUMPAD9
                || keyCode == VK_SPACE) {
            if (keyCode >= VK_NUMPAD0 && keyCode <= VK_NUMPAD9) {
                keyCode -= 48;
            }
            System.out.format("%c", keyCode);
        } else {
            System.out.format("[%s] [+%d ms]\n", KeyEvent.getKeyText(keyCode), calculateRangeTime());
        }
    }

    private long calculateRangeTime() {
        lastTime = time;
        time = System.currentTimeMillis();
        if (lastTime == 0) {
            return 0;
        }
        return time - lastTime;
    }
}
