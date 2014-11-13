package core;

import java.awt.event.KeyEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class GetActions implements NativeKeyListener, NativeMouseListener {
    /*
    Não pega cursor do mouse selecionando uma parte de um texto, não retorna evento nenhum,
    pelo menos o de nativeMouseClicked não retorna!
    */
    
    private User user;
    private Storage storage;
    private boolean recOn = false;
    private int lastKeyCode;

    public GetActions() {
        try {
            GlobalScreen.registerNativeHook();
            user = new User();
            storage = new Storage();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        GlobalScreen.getInstance().addNativeKeyListener(this);
        GlobalScreen.getInstance().addNativeMouseListener(this);
        //GlobalScreen.getInstance().addNativeMouseWheelListener(this);
        System.out.println("Init ok.");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        // Scrool + F12
        if (nke.getKeyCode() == KeyEvent.VK_F12 && lastKeyCode == KeyEvent.VK_SCROLL_LOCK) {
            recOn = !recOn;
            if (recOn) {
                System.out.println("Start rec...");
            } else {
                System.out.println("\nStop rec!");
            }
        }
        lastKeyCode = nke.getKeyCode();
        if (recOn) {
            storage.addKey(lastKeyCode);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nme) {
        if (recOn) {
            storage.addMouse(nme.getX(), nme.getY(), nme.getButton());
        }
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nme) {
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nme) {
    }
}
