package core;

import java.awt.event.KeyEvent;

public class Storage {

    private long time = 0, lastTime = 0;

    public void addMouse(int x, int y, int button) {
        System.out.format("(%03d,%03d) Button=%d [+%d milliseg]\n", x, y, button, calculateRangeTime());
    }

    public void addKey(int keyCode) {
        System.out.format("keyText=%s [+%d milliseg]\n", KeyEvent.getKeyText(keyCode), calculateRangeTime());
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
