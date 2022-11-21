package code;

public class Node {


    // state

    /*
    state of the world: (same as initial grid)
     * Remaining Coast Guard Capacity
     * Current Coast Guard Location
     * Locations of all Stations
     * Location of all ships and the number of remaining passengers on each: shipX1, shipY1, shipRP1
     * Location of all wrecks and each box's current damage: wrX1, wrfY1, wrD1

     remCapacity; location of coast guard; location of stations; location of ships;$
     $: no wrecks

     */

    String currentState;
    Node parent;
    String pathCost;    // cost -> (deathSoFar, lostBoxes)
    Actions actionTaken; //action to get to this node
    int depth;


    public Node(String currentState, Node parent, String pathCost, Actions actionTaken, int depth) {

        this.currentState = currentState;
        this.parent = parent;
        this.pathCost = pathCost;
        this.actionTaken = actionTaken;
        this.depth = depth;
    }



}

