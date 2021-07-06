


// dependencies
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;


public class ColorScanner {

    private Color[][] colorMap;

    public ColorScanner() {}; 

    public void scan(File file) {
        // build colors array to match the gradient image
        try {
            BufferedImage image = ImageIO.read(file);
            colorMap = new Color[image.getHeight()][image.getWidth()];
            for(int i = 0; i < image.getHeight(); i++) {
                for(int j = 0; j < image.getWidth(); j++) {
                    int pixel = image.getRGB(j, i);
                    Color color = new Color(pixel, true);
                    colorMap[i][j] = color;
                }
            }
        } catch(Exception ex) {};
    }
    public Color[][] getColorMap() { return colorMap; }
}