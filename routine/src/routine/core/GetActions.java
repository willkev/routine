package routine.core;

import gui.TaskBar;
import java.awt.event.KeyEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

public class GetActions implements NativeKeyListener, NativeMouseListener {

    private static final int ON_OFF_1 = KeyEvent.VK_INSERT;
    private static final int ON_OFF_2 = KeyEvent.VK_INSERT;

    private boolean recOn = false;
    private Storage storage;
    private int lastKeyCode;

    public GetActions(boolean showLog, boolean writeData) {
        try {
            GlobalScreen.registerNativeHook();
            storage = new Storage(showLog, writeData);
        } catch (Exception ex) {
            ex.printStackTrace();
            TaskBar.getInstance().msgError(ex);
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
                TaskBar.getInstance().msgInfo("Rec!");
                // Para não levar em consideração a tecla de ON_OFF
                lastKeyCode = nke.getKeyCode();
                return;
            }
            TaskBar.getInstance().msgInfo("Stop!");
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

    public boolean isRecOn() {
        return recOn;
    }

    public void setRecOn(boolean recOn) {
        this.recOn = recOn;
    }
    
}

/*
 Não pega cursor do mouse selecionando uma parte de um texto, não retorna evento nenhum,
 pelo menos o de nativeMouseClicked não retorna!
 */
