package code;

import java.util.ArrayList;

class CoastGuard extends SearchProblem{

    // dimensions of Grid
    public static byte height;
    public static byte width;

    public static byte boatCapacity; //30 <= boatCapacity <= 100

    // location of the coast guard
    public static byte coastGr,coastGc;

    // number of passengers in each ship
    public static byte [] numOfPassengers; // i
    // locations of ships
    public static byte [] shipsLocations; // size = (# of ships * 2) + 1 // 2i 2i+1 where i is the index in numOfPassengers

    // locations of stations
    public static byte [] stationsLocations;

    private static ArrayList<Integer> emptyCells;

    public static String genGrid(){

        /*randomly generates a grid. The dimensions of the grid, the locations of
the coast guard, stations, ships, number of passengers for each ship and the coast
guard capacity are all to be randomly generated. A minimum of 1 ship and 1 station
have to be generated. However, there is no upper limit for the number of ships or
stations generated as long as no 2 objects occupy the same cell.*/


        // grid of cells where 5 < m; n < 15
        height = (byte)random(5,15);
        width = (byte)random(5,15);
        String[][] grid = new String[height][width];

        emptyCells = new ArrayList<Integer>(height*width);
        for(int i=0;i<height*width;i++)
            emptyCells.add(i);

        // boat capacity
        boatCapacity = (byte)random(30,100);

        // coast guard location
        int coastGuardCell = randomCell();
        coastGr = (byte) (coastGuardCell/width); // get height
        coastGc = (byte) (coastGuardCell%width); // get width

        grid[coastGr][coastGc] = "cg";


        // ships and number of passengers
        int maxShipsCount =  (emptyCells.size()-1);
        int shipsCount = random(1,maxShipsCount);

        numOfPassengers = new byte[shipsCount];
        shipsLocations = new byte[shipsCount*2 + 1];

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

        stationsLocations = new byte[2*stationsCount + 1];

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
    public void visualizeGrid(String grid){

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
        for(int k = 0;k<gameBoard.length; k++){
            for(int j=0 ; j< gameBoard[k].length;j++){
                System.out.print(gameBoard[k][j] + " ");
            }
            System.out.println();
        }

    }

    private static String solve( String grid, String strategy,  boolean visualize ){

        String searchResult = solveSearchProblem(grid,strategy);

        if(visualize){
            // visualize steps

        }

        return searchResult;


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

    public static void main(String[] args) {


    }


}