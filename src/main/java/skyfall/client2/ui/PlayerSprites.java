package skyfall.client2.ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PlayerSprites {

    private BufferedImage sheet;
    private final int frameWidth;
    private final int frameHeight;

    private BufferedImage[] idleRight;
    private BufferedImage[] idleLeft;
    private BufferedImage[] runRight;
    private BufferedImage[] runLeft;

    public PlayerSprites(String resourcePath, int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        try {
            sheet = ImageIO.read(PlayerSprites.class.getResource(resourcePath));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load sprite sheet: " + resourcePath, e);
        }

        slice();
    }

    private void slice() {
        idleRight = cutRow(0, 2);
        idleLeft = reverse(cutRow(1, 2));
        runRight = cutRow(2, 8);
        runLeft = reverse(cutRow(3, 8));
    }

    private BufferedImage cutFrame(int row, int col) {
        return sheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight);
    }

    private BufferedImage[] cutRow(int row, int count) {
        BufferedImage[] frames = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            frames[i] = cutFrame(row, i);
        }
        return frames;
    }

    private BufferedImage[] reverse(BufferedImage[] source) {
        BufferedImage[] result = new BufferedImage[source.length];
        for (int i = 0; i < source.length; i++) {
            result[i] = source[source.length - 1 - i];
        }
        return result;
    }

    public BufferedImage[] getIdleRight() {
        return idleRight;
    }

    public BufferedImage[] getIdleLeft() {
        return idleLeft;
    }

    public BufferedImage[] getRunRight() {
        return runRight;
    }

    public BufferedImage[] getRunLeft() {
        return runLeft;
    }

}
