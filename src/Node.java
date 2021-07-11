/**
 * @class Node
 * @description This class defines the node object. An extension of JButton,
 * used as the both a display component and logical object for the gameboard class.
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