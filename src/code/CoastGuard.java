package code;

import java.util.*;

public class CoastGuard extends SearchProblem{

    static int numExpandedNodes;

    private static ArrayList<Integer> emptyCells;

    public static HashSet<String> prevStates;

    public static Deque<Node> BFQueue; //queue
    public static Stack<Node> DFQueue; //stack
    public static Deque<Node> IDQueue; //stack
    public static PriorityQueue<Node> ucQueue ;

    public static Stack<Node> IDQueue2;


    public static String genGrid(){
        /*
                randomly generates a grid. The dimensions of the grid, the locations of
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

        // build grid
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

    public static String solve( String grid, String strategy,  boolean visualize ){

//        System.out.println(switchInputXY(grid));
        prevStates = new HashSet<String>();
        String searchResult = solveSearchProblem(switchInputXY(grid),strategy);

        if(visualize){

            // visualize steps
            /*
            *
            visualize is a Boolean parameter which, when set to true, results in your
            program’s side-effecting a visual presentation of the board as it undergoes the
            different steps of the (discovered solution).
            *
            * */

        }



        return searchResult.toLowerCase();
    }

    // ----------------------------------------------------------------------------------------------------------------------------


    public static String solveSearchProblem(String grid, String strategy ){

        // create initialState and root node from input grid
        String initialState = createInitialState(grid);
        Node rootNode = new Node(initialState,null,"0,0",null,0, "0,0");

        // counter for expanded nodes
        numExpandedNodes = 0;

        // implement search based on strategy
        switch(strategy){
            case "BF":
                BFQueue = new ArrayDeque<Node>();
                BFQueue.add(rootNode);
                return BF();
            case "DF":
                DFQueue = new Stack<Node>();
                DFQueue.push(rootNode);
                return DF();
            case "ID":
                IDQueue = new ArrayDeque<Node>();
                IDQueue.push(rootNode);
                return ID();
            case "UC":
                ucQueue = new PriorityQueue<Node>();
                ucQueue.add(rootNode);
                return UC();
            case "GR1":;
            case "GR2":;
            case "AS1":;
            case "AS2":;
        }

        return "";
    }

    public static void visualizeState(String currState){

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

        String [] currStateArray = currState.split(";");

        //    * Grid dimensions index 0
        String [] dims = currStateArray[0].split(",");
        byte gridX = Byte.parseByte(dims[0]);
        byte gridY = Byte.parseByte(dims[1]);

        String [][] gameBoard = new String[gridY][gridX];


        //    * Max capacity  index 1
        byte maxCapacity = Byte.parseByte(currStateArray[1]);

        //    * Current Coast Guard Location index 2
        String [] guardLocation = currStateArray[2].split(",");
        byte guardX = Byte.parseByte(guardLocation[0]);
        byte guardY = Byte.parseByte(guardLocation[1]);

        gameBoard[guardY][guardX] = "cg"+maxCapacity;


        //    * Locations of all Stations  index 3
        String [] stationLocations = currStateArray[3].split(",");

        for(int i = 0; i < stationLocations.length -1; i++ ){

            byte stX = Byte.parseByte(stationLocations[i]);
            byte stY = Byte.parseByte(stationLocations[i+1]);
            gameBoard[stY][stX] = "st"+(i/2);

            if(stY == guardY && stX == guardX) {
                gameBoard[stY][stX] = "(c)st" + (i / 2);
            }
            else gameBoard[stY][stX] = "st"+(i/2);


            i++;

        }

        //    * Location of all ships and the number of remaining passengers on each: shipX1, shipY1, shipRP1  index 4
        String [] shipsLocations = currStateArray[4].split(",");

        if(shipsLocations.length>1)  // if there's a ship
        {
            for (int i = 0; i < shipsLocations.length - 2; i++) {
                byte shX = Byte.parseByte(shipsLocations[i]);
                byte shY = Byte.parseByte(shipsLocations[i + 1]);
                byte numPass = Byte.parseByte(shipsLocations[i + 2]);

                if(gameBoard[shY][shX]!=null)
                    gameBoard[shY][shX] = "(c)"+numPass + "sh" + (i / 3);
                else gameBoard[shY][shX] = numPass + "sh" + (i / 3);
                i += 2;
            }
        }

        // if the input is not the grid
        if(currStateArray.length>5) {
            //    * Location of all wrecks and each box's current damage: wrX1, wrY1, wrD1  index 5
            String[] wrecksLocations = currStateArray[5].split(",");

            if (wrecksLocations.length > 1) {
                for (int i = 0; i < wrecksLocations.length - 2; i++) {
                    byte wrX = Byte.parseByte(wrecksLocations[i]);
                    byte wrY = Byte.parseByte(wrecksLocations[i + 1]);
                    byte wrDamage = Byte.parseByte(wrecksLocations[i + 2]);

                    if(gameBoard[wrY][wrX] != null)
                        gameBoard[wrY][wrX] = "(c)"+wrDamage + "wr" + (i / 3);
                    else gameBoard[wrY][wrX] = wrDamage + "wr" + (i / 3);

                    i += 2;
                }

            }


            //    * Remaining Coast Guard Capacity  index 6
            byte remainingCapacity = Byte.parseByte(currStateArray[6]);

            //    * Remaining Passengers Counter  index 7
            short remainingPassengers = Short.parseShort(currStateArray[7]);

            //    * Remaining Ships Counter  index 8
            //    * Remaining Boxes Counter  index 9

            //    * Dead passengers  index 10
            short deadSoFar = Short.parseShort(currStateArray[10]);

            //    * Retrieved boxes  index 11
            short retrievedBoxes = Short.parseShort(currStateArray[11]);

            //    * Retrieved boxes  index 12
            short lostBoxes = Short.parseShort(currStateArray[12]);

            System.out.println("Dead so far: " + deadSoFar + "  Retrieved boxes so far: " + retrievedBoxes + " Lost boxes so far: " + lostBoxes);
        }

        printStringGrid(gameBoard);
        System.out.println("##########################################################################################################");
    }

    // creates the initial state from generated grid
    public static String createInitialState(String grid){

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
        //    * Lost boxes  index 12

        String initialState = grid+"$;"+parsedGrid[1]+";"+totalPassengers+";"+totalShips+";"+0+";"+0+";"+0 +";"+0;

        System.out.println("Initial State: " + initialState);
        return initialState;
    }

    // expands nodes
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
                String pathCost = nextStateArr[10] + "," + nextStateArr[12];
                Node newNode = new Node(nextState, n, pathCost, Actions.UP, n.depth+1, pathCost);
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
                String pathCost = nextStateArr[10] + "," + nextStateArr[12];
                Node newNode = new Node(nextState, n, pathCost, Actions.DOWN, n.depth+1, pathCost);
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
                String pathCost = nextStateArr[10] + "," + nextStateArr[12];
                Node newNode = new Node(nextState, n, pathCost, Actions.RIGHT, n.depth+1, pathCost);
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
                String pathCost = nextStateArr[10] + "," + nextStateArr[12];
                Node newNode = new Node(nextState, n, pathCost, Actions.LEFT, n.depth+1, pathCost);
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
                String pathCost = nextStateArr[10] + "," + nextStateArr[12];
                Node newNode = new Node(nextState, n, pathCost, Actions.DROP, n.depth+1, pathCost);
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
        if(onShip && shipPassengers>0 && remainingCapacity>0  &&
                (prevAction==Actions.UP || prevAction==Actions.DOWN || prevAction==Actions.LEFT || prevAction==Actions.RIGHT || prevAction==null)){
            String nextState = n.generateNextState(currState, Actions.PICKUP);
            if(!(prevStates.contains(nextState))){
                prevStates.add(nextState);
                //get dead passengers and retrieved boxes to include them in the path cost
                String [] nextStateArr = nextState.split(";");
                String pathCost = nextStateArr[10] + "," + nextStateArr[12];
                Node newNode = new Node(nextState, n, pathCost, Actions.PICKUP, n.depth+1, pathCost);
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
                String pathCost = nextStateArr[10] + "," + nextStateArr[12];
                Node newNode = new Node(nextState, n, pathCost, Actions.RETRIEVE, n.depth+1, pathCost);
                expandedNodes.add(newNode);
            }
        }

//        for (Node node : expandedNodes){
//            System.out.print(node.actionTaken + " ");
//        }
//        System.out.println("");

        return expandedNodes;
    }

    // Checks whether current node is the goal node
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


    // ---------------------------------------------------   Search algorithms:  ---------------------------------------------------

    public static String BF(){

        while(!BFQueue.isEmpty()){

            Node currNode = BFQueue.poll();
            numExpandedNodes++;

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

                System.out.println(plan);
                return plan + ";" + deaths + ";" + retrieved + ";" + numExpandedNodes;

            }

            // get all child nodes of the current node
            ArrayList<Node> childrenOfNode = expandNode(currNode);

//            if(childrenOfNode.size()!=0) numExpandedNodes++;


            //add all nodes to BFQueue
            if(childrenOfNode.size() !=0){
            BFQueue.addAll(childrenOfNode);}

        }

        return "fail";
    }

    public static String DF(){

        while(!DFQueue.isEmpty()){

            Node currNode = DFQueue.pop();
            numExpandedNodes++;

            System.out.println("currNode: " + currNode.currentState);
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

//            if(childrenOfNode.size()>0) numExpandedNodes++;

            //add all nodes to Stack
            for (Node ni : childrenOfNode){
                DFQueue.push(ni);
//                DFQueue.addLast(ni);
            }

        }

        return "fail";
    }

    public static String ID(){
        int limit = 0;
        Node root = IDQueue.poll();

//        System.out.println(root.currentState);
        while(true){
//            System.out.println("new iteration limit: " + limit);
            while (!IDQueue.isEmpty()) {
//                System.out.println("queue size: " + IDQueue.size());
                Node currNode = IDQueue.pop();
//                System.out.println(currNode.depth);
                numExpandedNodes++;

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

//                    System.out.println(currNode.currentState + " " + currNode.parent + " " + currNode.pathCost + " " + currNode.actionTaken + " " + currNode.depth);
//                    System.out.println("here");
                    // get all child nodes of the current node
                    ArrayList<Node> childrenOfNode = expandNode(currNode);

                    if(childrenOfNode.size()>0){ // there is children
                        //add all nodes to Queue
                        for (Node ni : childrenOfNode) {
                            IDQueue.addLast(ni);
                        }

                    }


                }
            }


            limit++;
            IDQueue.addLast(root);
            prevStates = new HashSet<String >();

        }

    }

    public String GR1(){
        return "";
    }

    public String GR2(){
        return "";
    }

    public String AS1(){
        return "";
    }

    public String AS2(){
        return "";
    }

    public static String UC(){

        while(!ucQueue.isEmpty())
        {
            Node currNode = (Node) ucQueue.poll();
//            System.out.println("curr path cost " + currNode.pathCost);
            numExpandedNodes++;

            if(isGoal(currNode)){

                String [] currNodeStateArray = currNode.currentState.split(";");
                String plan = currNode.getAncestors();
                String deaths = currNodeStateArray[10];
                String retrieved = currNodeStateArray[11];

                return plan + ";" + deaths + ";" + retrieved + ";" + numExpandedNodes;

            }


            // get all child nodes of the current node
            ArrayList<Node> childrenOfNode = expandNode(currNode);

//            if(childrenOfNode.size()>0) numExpandedNodes++;

            //add all nodes to Stack
            for (Node ni : childrenOfNode){
                ni.finalPathCost = ni.pathCost; //didn't really need to do it because it's set to path cost by default upon expanding
                ucQueue.add(ni);
            }


        }
        return "fail";
    }

    // ---------------------------------------------------    Helper Methods:    ---------------------------------------------------


    // Helper method to genGrid()
    private static int random(int start,int end) {
        return start   +  (int)( Math.random() * (end-start+1) );
    }

    private static String switchInputXY(String grid){

        String [] parsedGrid =  grid.split(";");


        //    * Grid dimensions index 0
        String gridDimensionsString = parsedGrid[0];

        //    * Max capacity  index 1
        String maxCapacity = parsedGrid[1];

        //    * Current Coast Guard Location index 2
        String [] coastGuardLocation = parsedGrid[2].split(",");
        // switched XY!
        String guardY = coastGuardLocation[0];
        String guardX = coastGuardLocation[1];

        String coastGuardLocationString = guardX+","+guardY;


        //    * Locations of all Stations  index 3
        String [] stationLocations = parsedGrid[3].split(",");
        String stationLocationsString = "";
        for(int i = 0; i < stationLocations.length - 1; i++ ){
            // switched XY!
            String stY = stationLocations[i];
            String stX = stationLocations[i+1];

            stationLocationsString += stX+","+stY + ",";
            i++;
        }
        stationLocationsString = stationLocationsString.substring(0, stationLocationsString.length() - 1);


        //    * Location of all ships and the number of remaining passengers on each: shipX1, shipY1, shipRP1  index 4
        String [] shipsLocations = parsedGrid[4].split(",");
        String shipsLocationsString = "";
        for (int i = 0; i < shipsLocations.length - 2; i++) {
            // switched XY!
            String shY = shipsLocations[i];
            String shX = shipsLocations[i + 1];
            String numPass = shipsLocations[i + 2];

            shipsLocationsString += shX+","+shY + "," + numPass +",";
            i += 2;
        }
        shipsLocationsString = shipsLocationsString.substring(0, shipsLocationsString.length() - 1);



        String gridString = gridDimensionsString + ";" + maxCapacity +";" + coastGuardLocationString +";" + stationLocationsString + ";" + shipsLocationsString+";";

        return gridString;

    }


    // Helper method to genGrid()
    private static int randomCell() {
        int cellIndex = random(0,emptyCells.size()-1);
        int cell = emptyCells.get(cellIndex);

        emptyCells.remove(cellIndex);

        return cell;
    }

    // Helper method to visualizeState
    private static void printStringGrid(String[][] array){

        System.out.println(array[0].length + "x" + array.length);

        for (int i = 0; i < array.length; i++){
            System.out.print("+--------+");
            for (int j = 0; j < array[0].length-1; j++){
                System.out.print("---------+");
            }
            System.out.println();

            for (int j = 0; j < array[0].length; j++){

                if(array[i][j] == null)
                    array[i][j] = "*";
                if (array[i][j].length() < 10){
                    int spaces = (9 - array[i][j].length()) / 2;
                    for (int k = 0; k < spaces; k++){
                        System.out.print(" ");
                    }
                    System.out.print(array[i][j]);
                    for (int k = 0; k < (9 - array[i][j].length()) - spaces; k++){
                        System.out.print(" ");
                    }
                }
                else{
                    System.out.print(array[i][j].substring(0, 9));
                }
                System.out.print("|");
            }
            System.out.println();

        }
        System.out.print("---------+");
        for (int j = 0; j < array[0].length-1; j++){
            System.out.print("---------+");
        }
        System.out.println();
    }

    // path cost for UC
//    public static int calculatePathCostUC(Node n){
//
//        String [] pathCost = n.pathCost.split(","); // deathSoFar, retrievedBoxes
//
//        int deathSoFar = Integer.parseInt(pathCost[0]);
//        short retrievedBoxes = Short.parseShort(pathCost[1]);
//
//        // minimize deaths and maximize retrieved boxes
//        //  2*deathSoFar - retrievedBoxes
//
//        return (2*deathSoFar) - retrievedBoxes;
//
//    }




}