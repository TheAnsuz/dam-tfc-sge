package dev.amrv.sge.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.imgscalr.Scalr;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class ImageLoader {

    private static final BufferedImage PLACEHOLDER = new BufferedImage(100, 100, BufferedImage.OPAQUE);
    private static final ImageIcon PLACEHOLDER_ICON = new ImageIcon(new BufferedImage(100, 100, BufferedImage.OPAQUE));

    public static BufferedImage getResourceImageSafe(String path, int width, int height) {
        try {
            return getResourceImage(path, width, height);
        } catch (IOException ex) {
            return new BufferedImage(100, 100, BufferedImage.OPAQUE);
        }
    }

    public static BufferedImage getResourceImage(String path, int width, int height) throws IOException {
        InputStream stream = ImageLoader.class.getClassLoader().getResourceAsStream(path);

        if (stream == null)
            return new BufferedImage(width, height, BufferedImage.OPAQUE);

        BufferedImage read = ImageIO.read(stream);

        return Scalr.resize(read, width, height);
    }

    public static ImageIcon getResouceIconSafe(String path, int width, int height) {
        try {
            return getResourceIcon(path, width, height);
        } catch (IOException ex) {
            return new ImageIcon(new BufferedImage(width, height, BufferedImage.OPAQUE));
        }
    }

    public static ImageIcon getResourceIcon(String path, int width, int height) throws IOException {
        return new ImageIcon(getResourceImage(path, width, height));
    }

}
