/**
 * @class ColorScanner
 * @description This singleton class is used to scan png images for use of mapping
 * colors to the nodes on the grid.
 * @author Damon Greenhalgh
 */


// DEPENDENCIES
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;

public class ColorScanner {

    private static ColorScanner scanner = new ColorScanner();
    private Color[][] colorMap;

    private ColorScanner() {}; 

    public void scan(File file, int scale) {
        // build colors array to match the gradient image
        try {
            BufferedImage image = ImageIO.read(file);
            int rows = image.getHeight() / scale;
            int columns = image.getWidth() / scale;
            colorMap = new Color[rows][columns];
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    int pixel = image.getRGB(j * scale, i * scale);
                    Color color = new Color(pixel, true);
                    colorMap[i][j] = color;
                }
            }
        } catch(Exception ex) { System.out.println(ex); }
    }
    public Color[][] getColorMap() { return colorMap; }
    public static ColorScanner getColorScanner() { return scanner; }
}