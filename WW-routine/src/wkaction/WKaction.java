package wkaction;

import java.awt.event.KeyEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class WKaction implements NativeKeyListener, NativeMouseListener {

    public static void main(String[] args) {
        new WKaction();
    }

    private User user;
    private long lastTime = 0;
    private boolean recOn = false;
    private int lastKeyCode;

    public WKaction() {
        try {
            GlobalScreen.registerNativeHook();
            user = new User();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        GlobalScreen.getInstance().addNativeKeyListener(this);
        GlobalScreen.getInstance().addNativeMouseListener(this);
        System.out.println("Started WKaction!");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        if (nke.getKeyCode() == KeyEvent.VK_F12 && lastKeyCode == KeyEvent.VK_SCROLL_LOCK) {
            recOn = !recOn;
            if (recOn) {
                lastTime = System.currentTimeMillis();
            }
        }
        lastKeyCode = nke.getKeyCode();
        if (!recOn) {
            return;
        }
        long time = System.currentTimeMillis();
        System.out.format("KeyCode=%d, keyText=%s [%d] [+%d miliseg]\n", nke.getKeyCode(), KeyEvent.getKeyText(nke.getKeyCode()), time, (time - lastTime));
        lastTime = time;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nme) {
        if (!recOn) {
            return;
        }
        long time = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = time;
        }
        System.out.format("(%d,%d) Button=%d [%d] [+%d miliseg]\n", nme.getX(), nme.getY(), nme.getButton(), time, (time - lastTime));
        lastTime = time;
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nme) {
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nme) {
    }
}
