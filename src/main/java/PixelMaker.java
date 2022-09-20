import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PixelMaker {
    private int pixelSize;
    private int newWidth;
    private int newHeight;
    private BufferedImage readImage;
    private BufferedImage writeImage;


    public void drawBufferedImage() {
        for(int i = 0; i<newWidth; i++) {
            for(int j = 0; j<newHeight; j++) {
                Color c = getOneSquare(i*pixelSize,j*pixelSize);
                writeImage.setRGB(i,j,c.getRGB());
            }
        }
    }
    public void compress(File readFile, int pixelSize){
        this.pixelSize = pixelSize;
        try {
            readImage = ImageIO.read(readFile);
        } catch (IOException e) {
            System.out.println("Could not read file");
        }

        newWidth = readImage.getWidth()/pixelSize;
        newHeight = readImage.getHeight()/pixelSize;
        writeImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);

        drawBufferedImage();

        File out = new File(readFile.getName().substring(0,readFile.getName().length()-3) +"new.png");
        try {
            ImageIO.write(writeImage,"png",out);
        } catch(IOException e) {
            System.out.println("Could not write file");
        }
    }

    public void unCompress(File readFile) {

    }
    public Color getOneSquare(int x, int y) {

        int redAverage = 0;
        int greenAverage = 0;
        int blueAverage = 0;
        int sqr = (int)Math.pow(pixelSize, 2);

        for(int k = x; k<(x+pixelSize);k++) {
            for(int h = y; h<(y+pixelSize); h++) {
                int rgb = readImage.getRGB(k, h);
                // Извлекаем одно значение RGB, хранящееся в 24 битах, которое можно понимать как байт в int
                redAverage += ((rgb & 0x00ff0000) >> 16)/sqr;
                greenAverage += ((rgb & 0x0000ff00) >> 8)/sqr;
                blueAverage += (rgb & 0x000000ff)/sqr;
            }
        }
        return new Color(redAverage, greenAverage, blueAverage);
    }

    public BufferedImage getWriteImage() {
        return writeImage;
    }

    public BufferedImage getReadImage() {
        return readImage;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public static void main(String[] args) {
        PixelMaker pixelMaker = new PixelMaker();

        pixelMaker.compress(new File("ng.pxl.png"),4);

    }
}