package routine.core;

import java.awt.event.KeyEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class GetActions implements NativeKeyListener, NativeMouseListener {

    private static final int ON_OFF_1 = KeyEvent.VK_INSERT;
    private static final int ON_OFF_2 = KeyEvent.VK_INSERT;

    private Storage storage;
    public boolean recOn = false;
    private int lastKeyCode;

    public GetActions(boolean showLog, boolean writeData) {
        try {
            GlobalScreen.registerNativeHook();
            storage = new Storage(showLog, writeData);
        } catch (Exception ex) {
            ex.printStackTrace();
            TaskBar.it.msgError("" + ex);
        }
        GlobalScreen.getInstance().addNativeKeyListener(this);
        GlobalScreen.getInstance().addNativeMouseListener(this);
        //GlobalScreen.getInstance().addNativeMouseWheelListener(this);
        System.out.println("Init GetActions OK!.");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        if (nke.getKeyCode() == ON_OFF_1 && lastKeyCode == ON_OFF_2) {
            recOn = !recOn;
            if (recOn) {
                TaskBar.it.msgInfo("Rec!");
                // Para não levar em consideração a tecla de ON_OFF
                lastKeyCode = nke.getKeyCode();
                return;
            }
            TaskBar.it.msgInfo("Stop!");
            storage.reset();
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
        lastKeyCode = 0;
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

/*
 Não pega cursor do mouse selecionando uma parte de um texto, não retorna evento nenhum,
 pelo menos o de nativeMouseClicked não retorna!
 */
