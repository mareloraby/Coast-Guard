package code;

import java.util.*;

public class CoastGuard extends SearchProblem{

    static int numExpandedNodes;
    public static Deque<Node> BFQueue; //queue
    public static Deque<Node> DFQueue; //stack
    public static Deque<Node> IDQueue; //stack

    public static String solveSearchProblem(String grid, String strategy ){

        // create initialState and root node from input grid
        String initialState = createInitialState(grid);
        Node rootNode = new Node(initialState,null,"0,0",null,0);

        // counter for expanded nodes
        numExpandedNodes = 0;

        // implement search based on strategy
        switch(strategy){
            case "BF":
                BFQueue = new ArrayDeque<Node>();
                BFQueue.add(rootNode);
                return BF();
            case "DF":
                DFQueue = new ArrayDeque<>();
                DFQueue.push(rootNode);
                return DF();
            case "ID":
                IDQueue = new ArrayDeque<>();
                IDQueue.push(rootNode);
                return ID();
            case "GR1":;
            case "GR2":;
            case "AS1":;
            case "AS2":;
        }

        return "";
    }

    public static String createInitialState(String grid){

        /*
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
         */

        String [] parsedGrid =  grid.split(";");

        //    * Grid dimensions index 0
        //    * Max capacity  index 1
        //    * Current Coast Guard Location index 2
        //    * Locations of all Stations  index 3
        //    * Location of all ships and the number of remaining passengers on each: shipX1, shipY1, shipRP1  index 4

        String [] shipsLocations = parsedGrid[4].split(",");

        //    * Location of all wrecks and each box's current damage: wrX1, wrfY1, wrD1  index 5
        //    * Remaining Coast Guard Capacity  index 6
        //    * Remaining Passengers Counter  index 7

        int totalPassengers = 0;
        int i = 2;

        while(i<shipsLocations.length){
            totalPassengers += Integer.parseInt(shipsLocations[i]);
            i+=3;
        }

        //    * Remaining Ships Counter  index 8

        int totalShips = (shipsLocations.length)/3;

        //    * Remaining Boxes Counter  index 9
        //    * Dead passengers  index 10
        //    * Retrieved boxes  index 11

        String initialState = grid+"$;"+parsedGrid[1]+";"+totalPassengers+";"+totalShips+";"+0+";"+0+";"+0;

        System.out.println("Initial State: " + initialState);
        return initialState;
    }

    public static boolean isGoal(Node n){

         /*
             You reach your goal when there are no living passengers who are not rescued,
           there are no undamaged boxes which have not been retrieved, and the rescue boat is not
           carrying any passengers.

             * Remaining Coast Guard Capacity  index 6 (*)
             * Remaining Passengers Counter  index 7 (*)
             * Remaining Ships Counter  index 8
             * Remaining Boxes Counter  index 9 (*)
             * Dead passengers  index 10
             * Retrieved boxes  index 11
         */

         String currState = n.currentState;
         String [] currStateArr = currState.split(";");

         byte maxCapacity = Byte.parseByte(currStateArr[1]);
         byte remainingCapacity = Byte.parseByte(currStateArr[6]);
         int remainingPassengersOnGrid = Integer.parseInt(currStateArr[7]);
         byte remainingBoxesNotRetrieved = Byte.parseByte(currStateArr[9]);

         if((remainingCapacity == maxCapacity ) &&
                 (remainingPassengersOnGrid == 0) &&
                 (remainingBoxesNotRetrieved == 0) ){

             System.out.println("Goal Node: " + Arrays.toString(currStateArr));
             return true;
         }

         // not goal
         return false;
     }

    private static ArrayList<Integer> emptyCells;
    public static String genGrid(){

        /*randomly generates a grid. The dimensions of the grid, the locations of
the coast guard, stations, ships, number of passengers for each ship and the coast
guard capacity are all to be randomly generated. A minimum of 1 ship and 1 station
have to be generated. However, there is no upper limit for the number of ships or
stations generated as long as no 2 objects occupy the same cell.*/


        // grid of cells where 5 < m; n < 15
        byte height = (byte)random(5,15);
        byte width = (byte)random(5,15);
        String[][] grid = new String[height][width];


        emptyCells = new ArrayList<Integer>(height*width);
        for(int i=0;i<height*width;i++)
            emptyCells.add(i);

        // coast guard's boat capacity 30 <= boatCapacity <= 100
        byte boatCapacity = (byte)random(30,100);

        // coast guard location
        int coastGuardCell = randomCell();
        byte coastGr = (byte) (coastGuardCell/width); // get height
        byte coastGc = (byte) (coastGuardCell%width); // get width

        grid[coastGr][coastGc] = "cg";

        // ships and number of passengers
        int maxShipsCount =  (emptyCells.size()-1);
        int shipsCount = random(1,maxShipsCount);

        // number of passengers in each ship
        byte [] numOfPassengers = new byte[shipsCount];

        // locations of ships: size = (# of ships * 2) + 1  & 2i 2i+1 where i is the index in numOfPassengers
        byte [] shipsLocations = new byte[shipsCount*2 + 1];

        for(int i = 0; i< shipsCount; i++){

            numOfPassengers[i] = (byte) random(1,100);

            int shipLocation = randomCell();
            shipsLocations[2*i] =  (byte) (shipLocation/width);
            shipsLocations[2*i + 1] =  (byte) (shipLocation%width);

            grid[shipsLocations[2*i]][shipsLocations[2*i + 1]] = "sh";

        }


        //stations
        int maxStationCount = (emptyCells.size());
        int stationsCount = random(1,maxStationCount);

        byte [] stationsLocations = new byte[2*stationsCount + 1];

        for(int i = 0; i< stationsCount; i++){

            int stationLocation = randomCell();
            stationsLocations[2*i] = (byte) (stationLocation/width);
            stationsLocations[2*i + 1] = (byte) (stationLocation%width);

            grid[stationsLocations[2*i]][stationsLocations[2*i + 1]] = "st";

        }

        StringBuilder gridString = new StringBuilder();

        gridString.append(width+","+height+";");
        gridString.append(boatCapacity+";");
        gridString.append(coastGc+","+coastGr+";");

        gridString.append(stationsLocations[1]+","+stationsLocations[0]);
        for(int st = 1; st< stationsCount; st++) {
            gridString.append("," + stationsLocations[2 * st + 1] + "," + stationsLocations[2 * st]);
        }
        gridString.append(";");

        gridString.append(shipsLocations[1]+","+shipsLocations[0]+","+numOfPassengers[0]);
        for(int sh = 1; sh< shipsCount; sh++) {
            gridString.append("," + shipsLocations[2 * sh + 1] + "," + shipsLocations[2 * sh]+","+numOfPassengers[sh]);
        }
        gridString.append(";");


        return gridString.toString();

    }

    private static int random(int start,int end) {
        return start   +  (int)( Math.random() * (end-start+1) );
    }

    private static int randomCell() {
        int cellIndex = random(0,emptyCells.size()-1);
        int cell = emptyCells.get(cellIndex);

        emptyCells.remove(cellIndex);

        return cell;
    }

    public static void visualizeGrid(String grid){

        String [] gridArray = grid.split(";");

        String [] dimensions = gridArray[0].split(",");

        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);

        String [][] gameBoard = new String[height][width];

        // Capacity
        String boatCap = gridArray[1];

        // Coast Guard
        String [] cgLocation = gridArray[2].split(",");
        int cgX = Integer.parseInt(cgLocation[0]);
        int cgY = Integer.parseInt(cgLocation[1]);

        gameBoard[cgY][cgX] = "cg"+boatCap;


        // Stations
        String [] gridStations = gridArray[3].split(",");

        for(int i = 0; i< gridStations.length -1; i++ ){
            int stX = Integer.parseInt(gridStations[i]);
            int stY = Integer.parseInt(gridStations[i+1]);
            gameBoard[stY][stX] = "st"+(i/2);
            i++;
        }

        // Ships
        String [] gridShips = gridArray[4].split(",");

        for(int i = 0; i< gridShips.length -2; i++ ){
            int shX = Integer.parseInt(gridShips[i]);
            int shY = Integer.parseInt(gridShips[i+1]);
            int numPass =  Integer.parseInt(gridShips[i+2]);

            gameBoard[shY][shX] = numPass+"sh"+(i/3);

            i+=2;
        }

        System.out.println();
        for (String[] strings : gameBoard) {
            for (int j = 0; j < strings.length; j++) {
                System.out.print(strings[j] + " ");
            }
            System.out.println();
        }

    }

    public static String solve( String grid, String strategy,  boolean visualize ){

        String searchResult = solveSearchProblem(grid,strategy);

        if(visualize){



            // visualize steps

        }
        return searchResult;
    }

    public static HashSet<String> prevStates = new HashSet<String>();
    public static ArrayList<Node> expandNode(Node n){

        ArrayList<Node> expandedNodes = new ArrayList<Node>();
        String currState = n.currentState;
        Actions prevAction = n.actionTaken;

        String [] currStateArr = currState.split(";");

        //get grid dimensions to validate motion in 4 directions
        String [] gridDimensions = currStateArr[0].split(",");
        byte gridX = Byte.parseByte(gridDimensions[0]);
        byte gridY = Byte.parseByte(gridDimensions[1]);

        //get guard location to validate its actions
        String [] coastGuardLocation = currStateArr[2].split(",");
        byte guardX = Byte.parseByte(coastGuardLocation[0]);
        byte guardY = Byte.parseByte(coastGuardLocation[1]);

        //get stations locations for validating dropping passengers at a valid cell
        String [] stationsLocations = currStateArr[3].split(",");

        //get remaining guard boat capacity to validate picking up or dropping passengers
        byte remainingCapacity = Byte.parseByte(currStateArr[6]);

        //get boat maximum capacity
        byte boatMaxCapacity = Byte.parseByte(currStateArr[1]);

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
        if(prevAction!=Actions.UP && guardY<gridY-1){
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
        if(prevAction!=Actions.LEFT && guardX<gridX-1){
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
            if(Byte.parseByte(stationsLocations[i]) == guardX && Byte.parseByte(stationsLocations[i+1]) == guardY){
                onStation = true;
                break;
            }
            i+=2;
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
        byte shipPassengers = 0;
        while(j<shipsLocations.length && shipsLocations.length>1){
            if(Byte.parseByte(shipsLocations[j]) == guardX && Byte.parseByte(shipsLocations[j+1]) == guardY){
                onShip = true;
                shipPassengers = Byte.parseByte(shipsLocations[j+2]);
                break;
            }
            j+=3;
        }
        if(onShip && shipPassengers>1 && remainingCapacity>0  &&
                (prevAction==Actions.UP || prevAction==Actions.DOWN || prevAction==Actions.LEFT || prevAction==Actions.RIGHT || prevAction==null)){
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
        byte boxCount = 20;
        while(k<wrecksLocations.length && wrecksLocations.length>1){
            if(Byte.parseByte(wrecksLocations[k]) == guardX && Byte.parseByte(wrecksLocations[k+1]) == guardY){
                onWreck = true;
                boxCount = Byte.parseByte(wrecksLocations[k+2]);
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

    public static String BF(){

        while(!BFQueue.isEmpty()){

            Node currNode = BFQueue.poll();

            //check if it's the goal node
            if(isGoal(currNode)){
                /*
                plan;deaths;retrieved;nodes

                where:
                – plan: the sequence of actions that lead to the goal (if such a sequence exists) separated
                by commas. For example: left,right,pickup,up,drop,down,retrieve.

                – deaths: number of passengers who have died in the solution starting from the
                initial state to the found goal state.

                – retrieved: number of black boxes successfully retrieved starting from the initial
                state to the found goal state.

                – nodes: is the number of nodes chosen for expansion during the search.

                 */

                String [] currNodeStateArray = currNode.currentState.split(";");
                String plan = currNode.getAncestors();
                String deaths = currNodeStateArray[10];
                String retrieved = currNodeStateArray[11];


                return plan + ";" + deaths + ";" + retrieved + ";" + numExpandedNodes;

            }

            // get all child nodes of the current node
            ArrayList<Node> childrenOfNode = expandNode(currNode);
            numExpandedNodes++;


            //add all nodes to BFQueue
            BFQueue.addAll(childrenOfNode);

        }

        return "";
    }

    public static String DF(){


        while(!DFQueue.isEmpty()){

            Node currNode = DFQueue.pop();

            //check if it's the goal node
            if(isGoal(currNode)){
                /*
                plan;deaths;retrieved;nodes

                where:
                – plan: the sequence of actions that lead to the goal (if such a sequence exists) separated
                by commas. For example: left,right,pickup,up,drop,down,retrieve.

                – deaths: number of passengers who have died in the solution starting from the
                initial state to the found goal state.

                – retrieved: number of black boxes successfully retrieved starting from the initial
                state to the found goal state.

                – nodes: is the number of nodes chosen for expansion during the search.

                 */

                String [] currNodeStateArray = currNode.currentState.split(";");
                String plan = currNode.getAncestors();
                String deaths = currNodeStateArray[10];
                String retrieved = currNodeStateArray[11];

                return plan + ";" + deaths + ";" + retrieved + ";" + numExpandedNodes;

            }

            // get all child nodes of the current node
            ArrayList<Node> childrenOfNode = expandNode(currNode);
            numExpandedNodes++;

            //add all nodes to Queue
            for (Node ni : childrenOfNode){
                DFQueue.push(ni);
            }

        }

        return "";
    }


    public static String ID(){
        int limit = 0;
        Node root = IDQueue.peek();

        while(true){

            while (!IDQueue.isEmpty()) {

                Node currNode = IDQueue.pop();

                //check if it's the goal node
                if (isGoal(currNode)) {
                /*
                plan;deaths;retrieved;nodes

                where:
                – plan: the sequence of actions that lead to the goal (if such a sequence exists) separated
                by commas. For example: left,right,pickup,up,drop,down,retrieve.

                – deaths: number of passengers who have died in the solution starting from the
                initial state to the found goal state.

                – retrieved: number of black boxes successfully retrieved starting from the initial
                state to the found goal state.

                – nodes: is the number of nodes chosen for expansion during the search.

                 */

                    String[] currNodeStateArray = currNode.currentState.split(";");
                    String plan = currNode.getAncestors();
                    String deaths = currNodeStateArray[10];
                    String retrieved = currNodeStateArray[11];

                    return plan + ";" + deaths + ";" + retrieved + ";" + numExpandedNodes;

                }

                if (currNode.depth < limit ) {

                    // get all child nodes of the current node
                    ArrayList<Node> childrenOfNode = expandNode(currNode);

                    if(childrenOfNode.size()>0){ // there is children

                        numExpandedNodes++;

                        //add all nodes to Queue
                        for (Node ni : childrenOfNode) {
                            IDQueue.push(ni);
                        }

                    }


                }
            }


            limit++;
            IDQueue.push(root);

        }





    }



}