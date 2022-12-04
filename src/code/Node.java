package code;

import java.util.ArrayList;
import java.util.Stack;

import static code.CoastGuard.isViz;
import static code.CoastGuard.stateStrings;

public class Node implements Comparable<Node> {

    /*
    state of the world: (same as initial grid)

     * Grid dimensions index 0
     * Max capacity  index 1
     * Current Coast Guard Location index 2
     * Locations of all Stations  index 3
     * Location of all ships and the number of remaining passengers on each: shipX1, shipY1, shipRP1  index 4
     * Location of all wrecks and each box's current damage: wrX1, wrfY1, wrD1  index 5

     * Remaining Coast Guard Capacity  index 6
     * Remaining Passengers Counter  index 7
     * Remaining Ships Counter  index 8
     * Remaining Boxes Counter  index 9
     * Dead passengers  index 10
     * Retrieved boxes  index 11
     * Lost boxes index 12

     griddims;maxCapacity; location of coast guard; location of stations; location of ships;$;maxCapacity;0;0;0;0;0
     $: no wrecks
     */


    String currentState;
    Node parent; //parent Node of current Node
    String pathCost;    // actual cost -> (deathSoFar, lostBoxes)
    Actions actionTaken; //action to get to this node
    int depth;
    String finalPathCost; // path cost to be used to enqueue nodes
                          //UC: "actual deathSoFar,actual lostBoxes"
                          //Greedy: H1-> "estimated deathSoFar,0" & H2-> "0,estimated lostBoxes"
                          //A*: H1-> "estimated deathSoFar+ actual deathSoFar,actual lostBoxes" & H2-> "actual deathSoFar,estimated lostBoxes + actual lostBoxes"


    public Node(String currentState, Node parent, String pathCost, Actions actionTaken, int depth, String finalPathCost) {

        this.currentState = currentState;
        this.parent = parent;
        this.pathCost = pathCost;
        this.actionTaken = actionTaken;
        this.depth = depth;
        this.finalPathCost = finalPathCost;
    }

    // generate next state
    public String generateNextState(String currState, Actions nextAction){
        // extract info from current state
        String [] parsedState = currState.split(";");

        //get maximum boat capacity
        byte maxCapacity = Byte.parseByte(parsedState[1]);

        //get coast guard location
        String coastGuardLocation = parsedState[2];

         // get ships locations and passengers
        String [] shipsLocationsAndPassengers = parsedState[4].split(",");

        //get remaining boat capacity
        byte remainingCapacity = Byte.parseByte(parsedState[6]);

        //get remaining passengers
        int remainingPassengers = Integer.parseInt(parsedState[7]);

        //get dead passengers
        int deadPassengers = Integer.parseInt(parsedState[10]);

        //get number of retrieved boxes
        short retrievedBoxes = Short.parseShort(parsedState[11]);

        //get number of lost boxes
        short lostBoxes = Short.parseShort(parsedState[12]);

        //get number of remaining boxes
        short remainingBoxes = Short.parseShort(parsedState[9]);

        //get number of remaining ships
        short remainingShips = Short.parseShort(parsedState[8]);

        //generate new ships locations and passengers count string
        String updatedShips = "";

        //decrements passengers in ships and update passenger counts and wrecks
        String newWrecks = ""; //string to contain newly converted wrecks from ships
        int i=2;
        while(i<shipsLocationsAndPassengers.length && nextAction!=Actions.PICKUP){
            shipsLocationsAndPassengers[i] = (Byte.parseByte(shipsLocationsAndPassengers[i])-1) + "";
            remainingPassengers--;
            deadPassengers++;
            if(Byte.parseByte(shipsLocationsAndPassengers[i]) == 0){  //ship will be a wreck
                remainingBoxes++;
                remainingShips--;
                if(newWrecks.equals("")){ //there was no wrecks before
                    newWrecks = shipsLocationsAndPassengers[i-2] + "," + shipsLocationsAndPassengers[i-1] + "," + 0; //add new wreck with ship's old location and set box count to 0
                }
                else{ //this is not the first wreck
                    newWrecks += "," + shipsLocationsAndPassengers[i-2] + "," + shipsLocationsAndPassengers[i-1] + "," + 0; //append new wreck with ship's old location and set box count to 0
                }
            }
            else{ //ship still has passengers
                if(updatedShips.equals("")){ //first ship in the string
                    updatedShips = shipsLocationsAndPassengers[i-2] + "," + shipsLocationsAndPassengers[i-1] + "," + shipsLocationsAndPassengers[i]; //add ship to new string
                }
                else{ //not the first ship in the string
                    updatedShips += "," + shipsLocationsAndPassengers[i-2] + "," + shipsLocationsAndPassengers[i-1] + "," + shipsLocationsAndPassengers[i]; //add ship to new string
                }
            }
            i+=3;
        }

        //increment box counts and eliminate expired ones from grid

        //get existing wrecks
        String wrecksLocations = parsedState[5];
        //generate updated wrecks string
        String updatedWrecks = "";

        if(wrecksLocations.equals("$")){ //there are no wrecks in the current state
            if(newWrecks.equals("")){ //no ships were converted to wrecks
                updatedWrecks = "$";
            }
            else{ //the wrecks converted from ships are currently the only existent wrecks
                updatedWrecks = newWrecks;
            }
        }
        else{ //there are wrecks in the current state
            String [] wreckArray = wrecksLocations.split(",");

            //increment black boxes count and remove expired ones
            int j=2;
            while (j<wreckArray.length){
                wreckArray[j] = (Byte.parseByte(wreckArray[j])+1) + "";
                if(Byte.parseByte(wreckArray[j])==20){ //expired box
                    remainingBoxes--;
                    lostBoxes++;
                }
                else{ // non expired box
                    if(updatedWrecks.equals("")){ //first wreck
                        updatedWrecks = wreckArray[j-2] + "," + wreckArray[j-1] + "," + wreckArray[j];
                    }
                    else{
                        updatedWrecks += "," +  wreckArray[j-2] + "," + wreckArray[j-1] + "," + wreckArray[j];
                    }
                }
                j+=3;
            }
            if(updatedWrecks.equals("")){ //there are no wrecks anymore in the current state
                if(newWrecks.equals("")){ //no new wrecks were converted from ships
                    updatedWrecks = "$";
                }
                else{
                    updatedWrecks = newWrecks;
                }
            }
            else{ //there still are wrecks in the current state
                if(!(newWrecks.equals(""))){ // add converted wrecks from ships
                    updatedWrecks += "," + newWrecks;
                }
            }
        }

        //get guard current location
        String [] coastGuardLocationArr = parsedState[2].split(",");
        byte guardX = Byte.parseByte(coastGuardLocationArr[0]);
        byte guardY = Byte.parseByte(coastGuardLocationArr[1]);
        //formulate next state according to the action
        switch (nextAction){
            case UP:
                coastGuardLocation = guardX + "," + (guardY-1);
                break;

            case DOWN:
                coastGuardLocation = guardX + "," + (guardY+1);
                break;

            case LEFT:
                coastGuardLocation = (guardX-1) + "," + guardY;
                break;

            case RIGHT:
                coastGuardLocation = (guardX+1) + "," + guardY;
                break;

            case DROP:
                remainingPassengers -= (maxCapacity-remainingCapacity);
                remainingCapacity = maxCapacity;
                break;

            case PICKUP:
                //get number of passengers of this cell's ship (cell at which the coast guard is currently standing on)
                String [] updatedShipsArr = shipsLocationsAndPassengers;
                int k=0;
                String tempShips = "";
                String shipWrecks = "";
                while(k<updatedShipsArr.length){
                    byte shipPassengers = Byte.parseByte(updatedShipsArr[k+2]);
                    if(Byte.parseByte(updatedShipsArr[k])==guardX && Byte.parseByte(updatedShipsArr[k+1])==guardY){ //this is the ship the guard is currently standing at
                        if(shipPassengers>remainingCapacity){
                            shipPassengers -= remainingCapacity+1;
                            remainingCapacity = 0;
                            remainingPassengers--;
                            deadPassengers++;
                            if(shipPassengers==0){
                                remainingBoxes++;
                                if(shipWrecks.equals("")){
                                    shipWrecks = updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + 0;
                                }
                                else{
                                    shipWrecks+= "," + updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + 0;
                                }
                            }
                            else {
                                if (tempShips.equals("")) { //the first ship to encounter
                                    tempShips = updatedShipsArr[k] + "," + updatedShipsArr[k + 1] + "," + shipPassengers;
                                } else {
                                    tempShips += "," + updatedShipsArr[k] + "," + updatedShipsArr[k + 1] + "," + shipPassengers;
                                }
                            }
                        }
                        else{ //all ship's passengers will be saved and the ship will be a wreck with a black box of count 0

//                            //revive dead one
//                            if(shipPassengers!=remainingCapacity) {
////                               remainingPassengers++;
//                                deadPassengers--;
//                            }
                            remainingCapacity -= shipPassengers;
                            shipPassengers = 0;
                            remainingShips--;
                            remainingBoxes++;
                            if(shipWrecks.equals("")){
                                shipWrecks = updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + 0;
                            }
                            else{
                                shipWrecks+= "," + updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + 0;
                            }
                        }
                    }
                    else{ //this is not the ship the guard is currently standing at
                        remainingPassengers--;
                        deadPassengers++;
                        shipPassengers--;
                        if(shipPassengers==0){
                            remainingBoxes++;
                            if(shipWrecks.equals("")){
                                shipWrecks = updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + 0;
                            }
                            else{
                                shipWrecks+= "," + updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + 0;
                            }
                        }
                        else {
                            if (tempShips.equals("")) { //the first ship to encounter
                                tempShips = updatedShipsArr[k] + "," + updatedShipsArr[k + 1] + "," + shipPassengers;
                            } else {
                                tempShips += "," + updatedShipsArr[k] + "," + updatedShipsArr[k + 1] + "," + shipPassengers;
                            }
                        }
                    }

                    k+=3;
                }
                updatedShips = tempShips;
                if(!(shipWrecks.equals(""))){
                    if(updatedWrecks.equals("$")){
                        updatedWrecks = shipWrecks;
                    }
                    else{
                        updatedWrecks += "," + shipWrecks;
                    }
                }
//                System.out.println("ships updated: " + updatedShips);
//                System.out.println("updated wrecks: " + updatedWrecks);

                break;

            case RETRIEVE:
                String [] updatedWrecksArr = updatedWrecks.split(",");
                String tempWrecks = "";
                int m=0;
                while (m<updatedWrecksArr.length){
                    if(Byte.parseByte(updatedWrecksArr[m]) == guardX && Byte.parseByte(updatedWrecksArr[m+1]) == guardY){ //that's the wreck i want to retrieve its box
                        retrievedBoxes++;
                        remainingBoxes--;
                    }
                    else{
                        if(tempWrecks.equals("")){
                            tempWrecks = updatedWrecksArr[m] + "," + updatedWrecksArr[m+1] + "," + updatedWrecksArr[m+2];
                        }
                        else{
                            tempWrecks += "," + updatedWrecksArr[m] + "," + updatedWrecksArr[m+1] + "," + updatedWrecksArr[m+2];
                        }
                    }

                    m+=3;
                }
                if(tempWrecks.equals("")){
                    updatedWrecks = "$";
                }
                else{
                    updatedWrecks = tempWrecks;
                }
                break;
        }

        if(updatedShips.equals("")){
            updatedShips = "#";
        }

        String nextState = parsedState[0] + ";" + parsedState[1] + ";" + coastGuardLocation + ";" + parsedState[3] + ";" + updatedShips + ";"
                + updatedWrecks + ";" + remainingCapacity + ";" + remainingPassengers + ";" + remainingShips + ";" + remainingBoxes + ";"
                + deadPassengers + ";" + retrievedBoxes + ";" + lostBoxes;

        return nextState;
    }

    // get path that lead to current node
    public String getAncestors(){
        String res = "";
        Node n = this;
        while(n.parent!=null){

            if(isViz){
                stateStrings.push(n);
            }

            if(res.equals("")){
                res = n.actionTaken + "";
            }
            else{
                res = n.actionTaken + "," + res;
            }
            n = n.parent;
        }
        return res;
    }

    @Override
    public int compareTo(Node n) {

        // (deathSoFar, retrievedBoxes)

        int deathSoFarThis = Integer.parseInt(this.finalPathCost.split(",")[0]);
        int lostBoxesSoFarThis = Integer.parseInt(this.finalPathCost.split(",")[1]);

        int deathSoFarN = Integer.parseInt(n.finalPathCost.split(",")[0]);
        int lostBoxesSoFarN = Integer.parseInt(n.finalPathCost.split(",")[1]);

        if(deathSoFarN > deathSoFarThis) //this is smaller
            return -1;
        if(deathSoFarN < deathSoFarThis)  // n is smaller
            return 1;
        if(deathSoFarN == deathSoFarThis)
        {
            if(lostBoxesSoFarN < lostBoxesSoFarThis)
                return 1;
            if(lostBoxesSoFarN > lostBoxesSoFarThis)
                return -1;
        }
        return 0;
    }


}

