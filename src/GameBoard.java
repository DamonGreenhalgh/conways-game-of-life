/**
 * @class GameBoard
 * @description This class defines the gameboard. The gameboard object is the 
 * logical foundation of the game.
 * @author Damon Greenhalgh
 */

 // dependencies
import java.util.Random;

public class GameBoard {
    
    // Fields
    private Node[][] nodes; 
    private int rows, columns;

    /**
     * Constructor
     * Generates the gameboard.
     */
    public GameBoard(int rows, int columns) { 
        this.rows = rows;
        this.columns = columns;
        nodes = new Node[rows][columns]; 
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                nodes[i][j] = new Node(new int[] {i, j});
            }
        }
    }

    // Accessors/Mutators
    public Node getNode(int i, int j) { return nodes[i][j]; }
    public void setNode(int i, int j, boolean state) { nodes[i][j].setState(state); }
    public int getRows() { return rows; }
    public int getColumns() { return columns; }

    /**
     * Clear
     * This method clears the board, sets all nodes to false (dead).
     */
    public void clear() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                nodes[i][j].setState(false);
            }
        }
    }

    /**
     * Random
     * This method randomizes each node on the board, setting it either alive
     * or dead.
     */
    public void random() {
        Random random = new Random();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(random.nextBoolean()) {
                    nodes[i][j].setState(true);
                } else {
                    nodes[i][j].setState(false);
                }
            }
        }
    }

    /**
     * Preset
     * This method draws a preset structure based on the parameter BrushType.
     * 
     * @param preset    the structure to draw.
     * @param brushState    true for paint, false for erase
     * @param row           the row to start
     * @param column        the column to start
     */
    public void preset(BrushType pt, boolean brushState, int row, int column) {
        int[][] structure = pt.getStructure();
        int structureRows = structure.length;
        int structureColumns = structure[0].length;
        Node node;

        try {

        } catch(Exception ex) {};
        for(int i = 0; i < structureRows; i++){
            for(int j = 0; j < structureColumns; j++) {
                try {
                    node = nodes[i + row][j + column];
                    if(brushState) {
                        if(structure[i][j] == 1 && !node.getState()) {
                            node.setState(true);
                        }
                    } else {
                        if(structure[i][j] == 1 && node.getState()) {
                            node.setState(false);
                        }
                    }
                } catch(Exception ex) {};
            }
        }
    }

    /**
     * Next
     * This method generates the next iteration of the game.
     * 
     * @return boolean    false if the game has halted, true if it has.
     */
    public boolean next() {

        int sum;
        int numChanges = 0;    // used to check if the game has halted

        // create new temporary board that is a clone of the game board
        Node[][] tmp = new Node[rows][columns];

        // iterate through each node on the game board.
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {

                // replicate original gameboard node
                tmp[i][j] = new Node(new int[] {i, j});
                tmp[i][j].setState(nodes[i][j].getState());
                
                // define the sum and the points to check around the node
                sum = 0;
                int[][] points = {
                    {i - 1, j - 1}, 
                    {i - 1, j}, 
                    {i - 1, j + 1}, 
                    {i, j + 1}, 
                    {i + 1, j + 1}, 
                    {i + 1, j}, 
                    {i + 1, j - 1}, 
                    {i, j - 1} 
                };

                // determine the number of alive neighbours around the node 
                for(int k = 0; k < points.length; k++) {
                    try {
                        if(nodes[points[k][0]][points[k][1]].getState()) {
                            sum++;
                        }
                    } catch(Exception ex) {
                        continue;
                    }   
                }

                // determine action depending on the rules of the game
                if(nodes[i][j].getState()) {          // if the node is alive
                    if(sum < 2 || sum > 3) {          // sum < 2, dies of loneliness
                        tmp[i][j].setState(false);    // sum > 3, dies of overcrowding
                        numChanges++;
                    }
                } else {                              // if the node is dead
                    if(sum == 3) {                    // sum == 3, lives due to repopulation
                        tmp[i][j].setState(true);  
                        numChanges++;
                    }
                }
            }
        }
        
        // overwrite the original values
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                nodes[i][j].setState(tmp[i][j].getState());
            }
        }

        if(numChanges == 0) {
            return true;
        }
        return false;
    }
}
