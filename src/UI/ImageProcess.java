package UI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcess {
    private BufferedImage img, croppedImage;
    private BufferedImage[] imgPieces;
    private int width, height, outputSize;

    public ImageProcess(BufferedImage img) throws IOException {
        this.img = img;
        imgPieces = new BufferedImage[8];

        width = img.getWidth();
        height = img.getHeight();
    }

    private void crop() throws IOException {
        int x, y;
        if (width > height) {
            outputSize = height;

            x = (width - outputSize) / 2;
            y = 0;
        }
        else {
            outputSize = width;

            x = 0;
            y = (height - outputSize) / 2;
        }

        croppedImage = img.getSubimage(x, y, outputSize, outputSize);

        cutToPieces(croppedImage);
    }

    private void cutToPieces(BufferedImage cropped) throws IOException {
        int croppedSize = cropped.getWidth() / 3;
        int count = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                BufferedImage piece = croppedImage.getSubimage(j * croppedSize, i * croppedSize, croppedSize, croppedSize);
                ImageIO.write(piece, "jpg", new File("res/output/" + count + ".jpg"));
                count++;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedImage img = ImageIO.read(new File("res/pictures/hollywood.jpg"));
        ImageProcess test = new ImageProcess(img);

        System.out.println(img.getWidth() + ", " + img.getHeight());
        test.crop();
    }
}
