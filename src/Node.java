/**
 * @class Node
 * @description This is the node class, used to instantiate a node object that 
 * represents nodes on the gameboard. The class has one field `status` that represents
 * the current state of the node. true for alive and false for dead.
 * @author Damon Greenhalgh
 */

import javax.swing.JButton;

public class Node extends JButton{

    // Fields
    private boolean state = false;    
    private int[] coords; 

    // Constructor
    public Node(int[] coords) { this.coords = coords; }

    // Methods
    public String toString() {
        String str = "";
        if(state) {
            str += "*";
        } else {
            str += ".";
        }
        return str;
    }
    public boolean getState() { return state; }
    public void setState(boolean state) { this.state = state; };
    public int[] getCoords() { return coords; }

 }