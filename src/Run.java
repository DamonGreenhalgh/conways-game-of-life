/**
 * @class Run
 * @description This is used for testing.
 * @author Damon Greenhalgh
 */

import java.util.Scanner;

public class Run {
    public static void main(String[] args) {

        // CLI IMPLEMENTATION
        Scanner scanner = new Scanner(System.in);
        boolean game = true;
        GameBoard board;
        int input;
    
        System.out.println("Welcome to Conway's Game of Life!");
        System.out.println("Enter the size of the grid: ");
        board = new GameBoard(scanner.nextInt());
        
        while(game) {
            try {
                //game
                System.out.println("Run Simulation - 1, Set Nodes - 2, End - Any Other Key: \n\n");
                board.display();
                input = scanner.nextInt();

                // determine action based on input
                switch (input) {
                    case 1: {
                        // run simulation
                        System.out.println("How many iterations?: ");
                        input = scanner.nextInt();
                        while(input > 0) {
                            board.next();
                            board.display();
                            input--;
                        }
                        break;

                    } case 2: {
                        // set nodes
                        System.out.println("Coordinate xy: ");
                        scanner.nextLine();
                        String coord = scanner.nextLine();
                        int x = Integer.parseInt(coord.substring(0, 1));
                        int y = Integer.parseInt(coord.substring(1, 2));
                        board.setNode(x, y, true);
                        break;

                    } default: { game = false; }    // end game
                }
            } catch(Exception ex) {
                break;
            }
            
        }
        scanner.close();
    }
}
