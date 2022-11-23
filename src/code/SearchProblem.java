package code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class SearchProblem{



    public static int gridW,gridH;

    public static int maxCapacity;

    public static int expandedNodes = 0;

    public static int coastGuardX;
    public static int coastGuardY;

    public static int totalPassengers = 0;
    public static int totalShips;
    public static int totalWrecks = 0;
    public static String initialState;

    public static HashSet<String> prevStates;

    public static Queue<Node> BFQueue;

    public static String solveSearchProblem(String grid, String strategy ){

        // Parse Grid
        // m,n;Capacity; location of coast guard; location of stations; location of ships;

        String [] parsedGrid =  grid.split(";");

        // dims of grid
        String [] dims = parsedGrid[0].split(",");
        gridW = Integer.parseInt(dims[0]);
        gridH = Integer.parseInt(dims[1]);

        // Capacity
        maxCapacity = Integer.parseInt(parsedGrid[1]);

        //coast guard location
        String [] coastGuardLocation = parsedGrid[2].split(",");
        coastGuardX = Integer.parseInt(coastGuardLocation[0]);
        coastGuardY = Integer.parseInt(coastGuardLocation[1]);

        //stations locations
        String stationsLocations = parsedGrid[3];

        //ships locations
        String shipsLocationsAndPassengers = parsedGrid[4];

        //number of ships
        totalShips = shipsLocationsAndPassengers.length()/3;

        //number of passengers
        int i = 2;
        String [] parsedShips = shipsLocationsAndPassengers.split(",");
        while(i<shipsLocationsAndPassengers.length()){
            totalPassengers += Integer.parseInt(parsedShips[i]);
            i+=3;
        }



        initialState = gridW + ";" + gridH + ";"+ maxCapacity + ";" + coastGuardX + "," + coastGuardY + ";" + stationsLocations + ";" +
                       shipsLocationsAndPassengers + ";" + "$" + ";" + maxCapacity+ ";" + totalPassengers + ";" + totalShips + ";" + 0 + ";" + 0 + ";" + 0 ;
        prevStates = new HashSet<String>();
        prevStates.add(initialState);

        Node rootNode = new Node(initialState,null,"0,0",null, 0 );



        switch(strategy){

            case "BF":
                BFQueue = new LinkedList<Node>() {};
                BFQueue.add(rootNode);
                return BF();
            case "DF":;
            case "ID":;
            case "GR1":;
            case "GR2":;
            case "AS1":;
            case "AS2":;

        }
        return "";
    }

    public static String BF(){
        String result = "";
        
        while(!BFQueue.isEmpty()){

            Node currentNode = BFQueue.poll();
            expandedNodes++;

            // is goal test?

            // expand with all valid actions:
            /*
            UP,
            DOWN,
            LEFT,
            RIGHT,
            PICKUP,
            DROP,
            RETRIEVE
            */

        }
        return result;
    }


    public static ArrayList<Node> expandNode(Node n){
        ArrayList<Node> expandedNodes = new ArrayList<Node>();
        String currState = n.currentState;
        Actions prevAction = n.actionTaken;
        String [] currStateArr = currState.split(";");

        //get grid dimensions to validate motion in 4 directions
        String [] gridDimensions = currStateArr[0].split(",");
        int gridX = Integer.parseInt(gridDimensions[0]);
        int gridY = Integer.parseInt(gridDimensions[1]);

        //get guard location to validate its actions
        String [] coastGuardLocation = currStateArr[2].split(",");
        int guardX = Integer.parseInt(coastGuardLocation[0]);
        int guardY = Integer.parseInt(coastGuardLocation[1]);

        //get stations locations for validating dropping passengers at a valid cell
        String [] stationsLocations = currStateArr[3].split(",");

        //get remaining guard boat capacity to validate picking up or dropping passengers
        int remainingCapacity = Integer.parseInt(currStateArr[6]);

        //get boat maximum capacity
        int boatMaxCapacity = Integer.parseInt(currStateArr[1]);

        //get ships locations and number of passengers to validate pickup action
        String [] shipsLocations = currStateArr[4].split(",");

        //get wrecks locations to validate retrieve action
        String [] wrecksLocations = currStateArr[5].split(",");

        //check validity of moving up
        if(prevAction!=Actions.DOWN && guardY>0){
            String nextState = n.generateNextState(currState, Actions.UP);
            if(!(prevStates.contains(nextState))){
                prevStates.add(nextState);
                //get dead passengers and retrieved boxes to include them in the path cost
                String [] nextStateArr = nextState.split(";");
                String pathCost = nextStateArr[10] + "," + nextStateArr[11];
                Node newNode = new Node(nextState, n, pathCost, Actions.UP, n.depth+1);
                expandedNodes.add(newNode);
            }
        }

        //check validity of moving down
        if(prevAction!=Actions.UP && guardY<gridY){
            String nextState = n.generateNextState(currState, Actions.DOWN);
            if(!(prevStates.contains(nextState))){
                prevStates.add(nextState);
                //get dead passengers and retrieved boxes to include them in the path cost
                String [] nextStateArr = nextState.split(";");
                String pathCost = nextStateArr[10] + "," + nextStateArr[11];
                Node newNode = new Node(nextState, n, pathCost, Actions.DOWN, n.depth+1);
                expandedNodes.add(newNode);
            }
        }

        //check validity of moving right
        if(prevAction!=Actions.LEFT && guardX<gridX){
            String nextState = n.generateNextState(currState, Actions.RIGHT);
            if(!(prevStates.contains(nextState))){
                prevStates.add(nextState);
                //get dead passengers and retrieved boxes to include them in the path cost
                String [] nextStateArr = nextState.split(";");
                String pathCost = nextStateArr[10] + "," + nextStateArr[11];
                Node newNode = new Node(nextState, n, pathCost, Actions.RIGHT, n.depth+1);
                expandedNodes.add(newNode);
            }
        }

        //check validity of moving left
        if(prevAction!=Actions.RIGHT && guardX>0){
            String nextState = n.generateNextState(currState, Actions.LEFT);
            if(!(prevStates.contains(nextState))){
                prevStates.add(nextState);
                //get dead passengers and retrieved boxes to include them in the path cost
                String [] nextStateArr = nextState.split(";");
                String pathCost = nextStateArr[10] + "," + nextStateArr[11];
                Node newNode = new Node(nextState, n, pathCost, Actions.LEFT, n.depth+1);
                expandedNodes.add(newNode);
            }
        }

        //check validity of dropping passengers
        int i = 0;
        boolean onStation = false;  //is true when the coast guard is standing in a cell containing a station
        while(i<stationsLocations.length){
            if(Integer.parseInt(stationsLocations[i]) == guardX && Integer.parseInt(stationsLocations[i+1]) == guardY){
                onStation = true;
                break;
            }
            i+=3;
        }
        if(onStation && remainingCapacity<boatMaxCapacity){ //currently on a station and I have passengers to drop
            String nextState = n.generateNextState(currState, Actions.DROP);
            if(!(prevStates.contains(nextState))){
                prevStates.add(nextState);
                //get dead passengers and retrieved boxes to include them in the path cost
                String [] nextStateArr = nextState.split(";");
                String pathCost = nextStateArr[10] + "," + nextStateArr[11];
                Node newNode = new Node(nextState, n, pathCost, Actions.DROP, n.depth+1);
                expandedNodes.add(newNode);
            }
        }

        //check validity of picking up passengers
        int j = 0;
        boolean onShip = false;  //is true when the coast guard is standing in a cell containing a ship
        int shipPassengers = 0;
        while(j<shipsLocations.length){
            if(Integer.parseInt(shipsLocations[j]) == guardX && Integer.parseInt(shipsLocations[j+1]) == guardY){
                onShip = true;
                shipPassengers = Integer.parseInt(shipsLocations[j+2]);
                break;
            }
            j+=3;
        }
        if(onShip && shipPassengers>1 && remainingCapacity>0  && (prevAction==Actions.UP || prevAction==Actions.DOWN || prevAction==Actions.LEFT || prevAction==Actions.RIGHT)){
            String nextState = n.generateNextState(currState, Actions.PICKUP);
            if(!(prevStates.contains(nextState))){
                prevStates.add(nextState);
                //get dead passengers and retrieved boxes to include them in the path cost
                String [] nextStateArr = nextState.split(";");
                String pathCost = nextStateArr[10] + "," + nextStateArr[11];
                Node newNode = new Node(nextState, n, pathCost, Actions.PICKUP, n.depth+1);
                expandedNodes.add(newNode);
            }
        }

        //check validity of retrieving a box
        int k = 0;
        boolean onWreck = false;  //is true when the coast guard is standing in a cell containing a wreck
        int boxCount = 20;
        while(k<wrecksLocations.length){
            if(Integer.parseInt(wrecksLocations[k]) == guardX && Integer.parseInt(wrecksLocations[k+1]) == guardY){
                onWreck = true;
                boxCount = Integer.parseInt(shipsLocations[j+2]);
                break;
            }
            k+=3;
        }
        if(onWreck && boxCount<19){
            String nextState = n.generateNextState(currState, Actions.RETRIEVE);
            if(!(prevStates.contains(nextState))){
                prevStates.add(nextState);
                //get dead passengers and retrieved boxes to include them in the path cost
                String [] nextStateArr = nextState.split(";");
                String pathCost = nextStateArr[10] + "," + nextStateArr[11];
                Node newNode = new Node(nextState, n, pathCost, Actions.RETRIEVE, n.depth+1);
                expandedNodes.add(newNode);
            }
        }

        return expandedNodes;
    }

//    public static boolean isValidMovement(Node currNode, Actions actionWantToPerform){
//
//        // get past action to check if action to be performed won't get me to the prev node
//        Actions prevAction =  currNode.actionTaken;
//        String currState = currNode.currentState;
//        String [] currStateArr = currState.split(";");
//
//        //get grid dimensions to validate motion in 4 directions
//        String [] gridDimensions = currStateArr[0].split(",");
//        int gridX = Integer.parseInt(gridDimensions[0]);
//        int gridY = Integer.parseInt(gridDimensions[1]);
//
//        //get guard dimensions to validate motion in any of the 4 directions
//        String [] coastGuardLocation = currStateArr[2].split(",");
//        int guardX = Integer.parseInt(coastGuardLocation[0]);
//        int guardY = Integer.parseInt(coastGuardLocation[1]);
//
//        switch (actionWantToPerform){
//            case UP:
//                if(prevAction!= Actions.DOWN && guardY>0){ //won't go back to previous node and won't exceed grid limits
//                    return true;
//                }
//                return false;
//
//            case DOWN:
//                if(prevAction!= Actions.UP && guardY<gridY){ //won't go back to previous node and won't exceed grid limits
//                    return true;
//                }
//                return false;
//
//            case RIGHT:
//                if(prevAction!= Actions.LEFT && guardX<gridX){  //won't go back to previous node and won't exceed grid limits
//                    return true;
//                }
//                return false;
//
//            case LEFT:
//                if(prevAction!=Actions.RIGHT && guardX>0){ //won't go back to previous node and won't exceed grid limits
//                    return true;
//                }
//                return false;
//        }
//
//        return false;
//    }



}