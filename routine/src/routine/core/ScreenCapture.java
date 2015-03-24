package routine.core;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class ScreenCapture implements Serializable {

    private static final long serialVersionUID = 1L;
    private int[] _bytesOut = null;
    private int _height, _width;

    public ScreenCapture(BufferedImage bi) {
        _height = bi.getHeight();
        _width = bi.getWidth();
        _bytesOut = new int[_width * _height];
        bi.getRGB(0, 0, _width, _height, _bytesOut, 0, _width);
    }

    public BufferedImage getImage() {
        // TYPE_BYTE_GRAY
        BufferedImage bi = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, _width, _height, _bytesOut, 0, _width);
        return bi;
    }
}
