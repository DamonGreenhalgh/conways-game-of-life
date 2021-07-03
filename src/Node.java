/**
 * @class Node
 * @description This is the node class, used to instantiate a node object that 
 * represents nodes on the gameboard. The class has one field `status` that represents
 * the current state of the node. true for alive and false for dead.
 * 
 * @author Damon Greenhalgh
 */

public class Node {

    // dead by default
    private boolean state = false;    

    public Node() {};

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
    public void setState(boolean state) {this.state = state; };

 }