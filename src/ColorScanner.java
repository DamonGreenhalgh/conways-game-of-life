/**
 * @class ColorScanner
 * @description This singleton class is used to scan png images for use of mapping
 * colors to the nodes on the grid.
 * @author Damon Greenhalgh
 */


// dependencies
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;

public class ColorScanner {

    // Fields 
    private static ColorScanner scanner = new ColorScanner();
    private Color[][] colorMap;

    // Constructor
    private ColorScanner() {}; 

    /**
     * Scan
     * This method scans the parameter file for each pixel color. The pixel color 
     * is then mapped to the colorMap array used for the grid. This method
     * assumes a 16:9 resolution
     * @param file     the file to scan.
     * @param scale    the scaling factor of the grid.
     */
    public void scan(File file, int scale) {
        // build colors array to match the gradient image
        try {
            BufferedImage image = ImageIO.read(file);
            int rows = image.getHeight() / scale;
            int columns = image.getWidth() / scale;
            colorMap = new Color[rows][columns];

            // iterate through each pixel, store the color at the corresponding
            // location in the colorMap array.
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < columns; j++) {
                    int pixel = image.getRGB(j * scale, i * scale);
                    Color color = new Color(pixel, true);
                    colorMap[i][j] = color;
                }
            }
        } catch(Exception ex) { System.out.println(ex); }
    }

    // Accessors / Mutators
    public Color[][] getColorMap() { return colorMap; }
    public static ColorScanner getColorScanner() { return scanner; }
}