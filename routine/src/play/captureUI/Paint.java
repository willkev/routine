package captureUI;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Paint extends JFrame {

    private static BufferedImage screen;
    private static final AtomicLong timeToWait = new AtomicLong(500);

    public Paint() {
        super("8 = more and 2 = less : timeToWait=" + timeToWait.get());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(new Rectangle(800, 600));
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        if (screen != null) {
            g.drawImage(screen, 20, 40, null);
        } else {
            System.out.println("ERROR IN PAINT! scren=NULL");
        }
    }

    private static BufferedImage capture(WinDef.HWND hWnd) {
        HDC hdcWindow = User32.INSTANCE.GetDC(hWnd);
        HDC hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);

        RECT bounds = new RECT();
        User32Extra.INSTANCE.GetClientRect(hWnd, bounds);

        int width = bounds.right - bounds.left;
        int height = bounds.bottom - bounds.top;

        HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, width, height);

        HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);
        GDI32Extra.INSTANCE.BitBlt(hdcMemDC, 0, 0, width, height, hdcWindow, 0, 0, WinGDIExtra.SRCCOPY);

        GDI32.INSTANCE.SelectObject(hdcMemDC, hOld);
        GDI32.INSTANCE.DeleteDC(hdcMemDC);

        BITMAPINFO bmi = new BITMAPINFO();
        bmi.bmiHeader.biWidth = width;
        bmi.bmiHeader.biHeight = -height;
        bmi.bmiHeader.biPlanes = 1;
        bmi.bmiHeader.biBitCount = 32;
        bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

        Memory buffer = new Memory(width * height * 4);
        GDI32.INSTANCE.GetDIBits(hdcWindow, hBitmap, 0, height, buffer, bmi, WinGDI.DIB_RGB_COLORS);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, buffer.getIntArray(0, width * height), 0, width);

        GDI32.INSTANCE.DeleteObject(hBitmap);
        User32.INSTANCE.ReleaseDC(hWnd, hdcWindow);
        return image;
    }

    public static void main(String[] args) {
        String winTitle = "Nova Guia - Google Chrome";
        WinDef.HWND hWnd = User32.INSTANCE.FindWindow(null, winTitle);
        if (hWnd == null) {
            System.out.println("**** Não encontrou: " + winTitle);
            System.exit(-1);
        }
        final Paint paint = new Paint();
        java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new java.awt.KeyEventDispatcher() {

            long aux;

            @Override
            public boolean dispatchKeyEvent(java.awt.event.KeyEvent k) {
                if (k.getID() != java.awt.event.KeyEvent.KEY_TYPED) {
                    return false;
                }
                char key = k.getKeyChar();
                if (key == '8') {
                    timeToWait.set(timeToWait.get() + 10);
                }
                if (key == '2') {
                    aux = timeToWait.get() - 10;
                    if (aux > 1) {
                        timeToWait.set(aux);
                    } else {
                        timeToWait.set(1);
                    }
                }
                paint.setTitle("timeToWait=" + timeToWait.get());
                return false;
            }
        });
        while (true) {
            screen = capture(hWnd);
            paint.repaint();
            try {
                Thread.sleep(timeToWait.get());
            } catch (Exception e) {
            }
        }
    }

}
