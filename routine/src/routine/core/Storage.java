package routine.core;

import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_BACK_SLASH;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_BEGIN;
import static java.awt.event.KeyEvent.VK_CANCEL;
import static java.awt.event.KeyEvent.VK_CAPS_LOCK;
import static java.awt.event.KeyEvent.VK_CLEAR;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_COMPOSE;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_DIVIDE;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_END;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_HOME;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_MULTIPLY;
import static java.awt.event.KeyEvent.VK_NUMPAD0;
import static java.awt.event.KeyEvent.VK_NUMPAD9;
import static java.awt.event.KeyEvent.VK_NUM_LOCK;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_PAGE_DOWN;
import static java.awt.event.KeyEvent.VK_PAGE_UP;
import static java.awt.event.KeyEvent.VK_PAUSE;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_SCROLL_LOCK;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_Z;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Storage {

    public boolean showLog;
    public boolean writeData;

    public enum EventType {

        Mouse, Text, Control, None;

        public String getCode() {
            switch (this) {
                case Mouse:
                    return "M";
                case Text:
                    return "T";
                case Control:
                    return "C";
            }
            return "X";
        }

        public static EventType getEventType(String name) {
            if (name == null) {
                return null;
            }
            if (name.equals("M")) {
                return Mouse;
            } else {
                if (name.equals("T")) {
                    return Text;
                } else {
                    if (name.equals("C")) {
                        return Control;
                    }
                }
            }
            return null;
        }
    }

    private long time = 0, lastTime = 0;
    private EventType lastEvent = EventType.None;
    private FileWriter writer;

    public Storage(boolean showLog, boolean writeData) {
        this.showLog = showLog;
        this.writeData = writeData;
    }

    public void addMouse(int x, int y, int button) {
        storeMouse(x, y, button);
        lastEvent = EventType.Mouse;
    }

    public void addKey(int keyCode) {
        if (addText(keyCode)) {
            lastEvent = EventType.Text;
            return;
        }
        switch (keyCode) {
            // Virgula é o token de separação no arquivo .dat
            case VK_COMMA:
            case VK_ENTER:
            case VK_DELETE:
            case VK_BACK_SPACE:
            case VK_CANCEL:
            case VK_CLEAR:
            case VK_COMPOSE:
            case VK_PAUSE:
            case VK_CAPS_LOCK:
            case VK_ESCAPE:
            case VK_PAGE_UP:
            case VK_PAGE_DOWN:
            case VK_END:
            case VK_HOME:
            case VK_LEFT:
            case VK_UP:
            case VK_RIGHT:
            case VK_DOWN:
            case VK_BEGIN:
            case VK_NUM_LOCK:
            case VK_SCROLL_LOCK:
                storeTextNewLine();
                lastEvent = EventType.Control;
                break;
            default:
                if (lastEvent != EventType.Text) {
                    storeTextNewLine();
                }
                lastEvent = EventType.Text;
        }
        if (keyCode == 0) {
            storeTextUndefined();
        } else {
            storeTextControl(keyCode);
        }
    }

    private boolean addText(int keyCode) {
        if (keyCode >= VK_0 && keyCode <= VK_9
                || keyCode >= VK_A && keyCode <= VK_Z
                || keyCode >= VK_NUMPAD0 && keyCode <= VK_NUMPAD9
                || keyCode == VK_SPACE
                || keyCode >= VK_MULTIPLY && keyCode <= VK_DIVIDE) {
            if (keyCode >= VK_NUMPAD0 && keyCode <= VK_NUMPAD9) {
                keyCode -= 48;
            }
            if (keyCode >= VK_MULTIPLY && keyCode <= VK_DIVIDE) {
                keyCode -= 64;
            }
            if (lastEvent != EventType.Text) {
                storeTextNewLine();
            }
            storeText(keyCode);
            return true;
        }
        switch (keyCode) {
            case VK_PERIOD:
            case VK_SLASH:
            case VK_SEMICOLON:
            case VK_EQUALS:
            case VK_OPEN_BRACKET:
            case VK_BACK_SLASH:
            case VK_CLOSE_BRACKET:
            case VK_MINUS:
                if (lastEvent != EventType.Text) {
                    storeTextNewLine();
                }
                storeText(keyCode);
                return true;
        }
        return false;
    }

    private long calculateRangeTime() {
        lastTime = time;
        time = System.currentTimeMillis();
        if (lastTime == 0) {
            return 0;
        }
        return time - lastTime;
    }

    private void storeMouse(int x, int y, int button) {
        if (showLog) {
            System.out.format("\n[%03d,%03d] button-%d", x, y, button);
        }
        writeFile(EventType.Mouse, x + "," + y + "," + button);
    }

    private void storeText(int keyCode) {
        if (showLog) {
            System.out.print((char) keyCode);
        }
        writeFile(EventType.Text, KeyEvent.getKeyText(keyCode) + "," + keyCode);
    }

    private void storeTextControl(int keyCode) {
        String text = KeyEvent.getKeyText(keyCode);
        if (showLog) {
            System.out.print("[" + text + "]");
        }
        writeFile(EventType.Control, text + "," + keyCode);
    }

    private void storeTextNewLine() {
        if (showLog) {
            System.out.println();
        }
    }

    private void storeTextUndefined() {
        if (showLog) {
            System.out.print("[?]");
        }
        writeFile(EventType.None, "[?]");
    }

    private void writeFile(EventType eventType, String text) {
        if (!writeData) {
            return;
        }
        openFile();
        if (writer == null) {
            return;
        }
        try {
            writer.write(eventType.getCode() + "," + calculateRangeTime() + "," + text + "\n");
            writer.flush();
        } catch (IOException ex) {
            System.out.println("ERROR writeFile:" + ex.getMessage());
        }
    }

    private void openFile() {
        if (writer != null) {
            return;
        }
        try {
            File dir = new File(System.getProperty("user.home", ""), "routine");
            dir.mkdir();
            writer = new FileWriter(new File(dir, System.currentTimeMillis() + ".dat"));
        } catch (IOException ex) {
            System.out.println("ERROR openFile:" + ex.getMessage());
            try {
                writer.close();
            } catch (IOException ex2) {
            }
        }
    }

    public void reset() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException ex) {
            System.out.println("ERROR reset:" + ex.getMessage());
        }
        writer = null;
        time = 0;
    }
}
