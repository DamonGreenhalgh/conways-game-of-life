/**
 * @class GameBoard
 * @description This is the game board class.
 * @author Damon Greenhalgh
 */


public class GameBoard {
    
    private Node[][] board; 
    private int size;

    public GameBoard(int size) { 
        this.size = size; 
        board = new Node[size][size]; 
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                board[i][j] = new Node();
            }
        }
    }

    public void setNode(int i, int j, boolean state) { board[i][j].setState(state); }

    public void display() {
        for(int i  = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                System.out.printf("%s    ", board[i][j]);
            }
            System.out.println("\n");
        }
        System.out.println("\n");
    }

    public void next() {

        // create new temporary board that is a clone of the game board
        Node[][] tmp = new Node[size][size];

        // iterate through each node on the game board.
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {

                // replicate original gameboard node
                tmp[i][j] = new Node();
                tmp[i][j].setState(board[i][j].getState());
                
                // define the sum and the points to check around the node
                int sum = 0;
                int[][] coords = {
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
                for(int k = 0; k < coords.length; k++) {
                    try {
                        if(board[coords[k][0]][coords[k][1]].getState()) {
                            sum++;
                        }
                    } catch(Exception ex) {
                        continue;
                    }   
                }

                // determine action depending on the rules of the game
                if(board[i][j].getState()) {
                    if(sum < 2 || sum > 3) {
                        tmp[i][j].setState(false);
                    }
                } else {
                    if(sum == 3) {
                        tmp[i][j].setState(true); 
                    }
                }
            }
        }
        board = tmp;
    }

}
