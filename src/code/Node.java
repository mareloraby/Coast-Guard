package code;

public class Node {


    // state

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



     griddims;maxCapacity; location of coast guard; location of stations; location of ships;$;maxCapacity;0;0;0;0;0
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

// expansion here

    // generate next state
    public String generateNextState(String currState, Actions nextAction){
        // extract info from current state
        String [] parsedState = currState.split(";");

        //get maximum boat capacity
        int maxCapacity = Integer.parseInt(parsedState[1]);

        //get coast guard location
        String coastGuardLocation = parsedState[2];

         // get ships locations and passengers
        String [] shipsLocationsAndPassengers = parsedState[4].split(",");

        //get remaining boat capacity
        int remainingCapacity = Integer.parseInt(parsedState[6]);

        //get remaining passengers
        int remainingPassengers = Integer.parseInt(parsedState[7]);

        //get dead passengers
        int deadPassengers = Integer.parseInt(parsedState[10]);

        //get number of retrieved boxes
        int retrievedBoxes = Integer.parseInt(parsedState[11]);

        //generate new ships locations and passengers count string
        String updatedShips = "";


        //get number of remaining boxes
        int remainingBoxes = Integer.parseInt(parsedState[9]);

        //get number of remaining ships
        int remainingShips = Integer.parseInt(parsedState[8]);

        //decrements passengers in ships and update passenger counts and wrecks
        String newWrecks = ""; //string to contain newly converted wrecks from ships
        int i=2;
        while(i<shipsLocationsAndPassengers.length){
            shipsLocationsAndPassengers[i] = (Integer.parseInt(shipsLocationsAndPassengers[i])-1) + "";
            remainingPassengers--;
            deadPassengers++;
            if(Integer.parseInt(shipsLocationsAndPassengers[i]) == 0){  //ship will be a wreck
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
                wreckArray[j] = (Integer.parseInt(wreckArray[j])+1) + "";
                if(Integer.parseInt(wreckArray[j])==20){ //expired box
                    remainingBoxes--;
                }
                else{ // non expired box
                    if(updatedWrecks.equals("")){ //first wreck
                        updatedWrecks = wreckArray[j-2] + "," + wreckArray[j-1] + "," + wreckArray[j];
                    }
                    else{
                        updatedWrecks = "," +  wreckArray[j-2] + "," + wreckArray[j-1] + "," + wreckArray[j];
                    }
                }
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
        int guardX = Integer.parseInt(coastGuardLocationArr[0]);
        int guardY = Integer.parseInt(coastGuardLocationArr[1]);
        //formulate next state according to the action
        switch (nextAction){
            case UP:
                coastGuardLocation = guardX + "," + (guardY+1);
                break;

            case DOWN:
                coastGuardLocation = guardX + "," + (guardY-1);
                break;

            case LEFT:
                coastGuardLocation = (guardX-1) + "," + guardY;
                break;

            case RIGHT:
                coastGuardLocation = (guardX+1) + "," + guardY;
                break;

            case DROP:
                remainingPassengers -= remainingCapacity;
                remainingCapacity = maxCapacity;
                break;

            case PICKUP:
                //get number of passengers of this cell's ship (cell at which the coast guard is currently standing on)
                String [] updatedShipsArr = updatedShips.split(",");
                int k=0;
                String tempShips = "";
                while(k<updatedShipsArr.length){
                    if(Integer.parseInt(updatedShipsArr[k])==guardX && Integer.parseInt(updatedShipsArr[k+1])==guardY){ //this is the ship the guard is currently standing at
                        int shipPassengers = Integer.parseInt(updatedShipsArr[k+2]);
                        if(shipPassengers>remainingCapacity){
                            shipPassengers -= remainingCapacity;
                            remainingCapacity = 0;

                            if(tempShips.equals("")){ //the first ship to encpunter
                                tempShips = updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + shipPassengers;
                            }
                            else{
                                tempShips += "," + updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + shipPassengers;
                            }
                        }
                        else{ //all ship's passengers will be saved and the ship will be a wreck with a black box of count 0
                            remainingCapacity -= shipPassengers;
                            remainingShips--;
                            remainingBoxes++;
                            if(updatedWrecks.equals("$")){
                                updatedWrecks = updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + 0;
                            }
                            else{
                               updatedWrecks+= "," + updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + 0;
                            }
                        }
                    }
                    else{ //this is not the ship the guard is currently standing at
                        if(tempShips.equals("")){ //the first ship to encpunter
                            tempShips = updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + updatedShipsArr[k+2];
                        }
                        else{
                            tempShips += "," + updatedShipsArr[k] + "," + updatedShipsArr[k+1] + "," + updatedShipsArr[k+2];
                        }
                    }

                    k+=3;
                }
                updatedShips = tempShips;
                break;

            case RETRIEVE:
                String [] updatedWrecksArr = updatedWrecks.split(",");
                String tempWrecks = "";
                int m=0;
                while (m<updatedWrecksArr.length){
                    if(Integer.parseInt(updatedWrecksArr[m]) == guardX && Integer.parseInt(updatedWrecksArr[m+1]) == guardY){ //that's the wreck i want to retrieve its box
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
                updatedWrecks = tempWrecks;
                break;
        }

        String nextState = parsedState[0] + ";" + parsedState[1] + ";" + coastGuardLocation + ";" + parsedState[3] + ";" + updatedShips + ";"
                + updatedWrecks + ";" + remainingCapacity + ";" + remainingPassengers + ";" + remainingShips + ";" + remainingBoxes + ";"
                + deadPassengers + ";" + retrievedBoxes;

        return nextState;
    }

    public String getAncestors(){
        String res = "";
        Node n = this;
        while(n.parent!=null){
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




}

