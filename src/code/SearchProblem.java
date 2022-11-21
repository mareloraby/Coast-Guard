package code;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class SearchProblem{



    public static int gridW,gridH;
    public static int maxCapacity;
    public static int expandedNodes = 0;

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


        initialState = parsedGrid[1]+"$";
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

    public static boolean isValidMovement(Node currNode, Actions actionWantToPerform){
        // Checking if movement actions won't get me to the prev node

        Actions prevAction =  currNode.actionTaken;
        if(currNode.actionTaken != null) {
            if (prevAction.equals(Actions.UP)) {
                if (actionWantToPerform.equals(Actions.DOWN)) {
                    return false;
                }
            }
            if (prevAction.equals(Actions.DOWN)) {
                if (actionWantToPerform.equals(Actions.UP)) {
                    return false;
                }
            }
            if (prevAction.equals(Actions.LEFT)) {
                if (actionWantToPerform.equals(Actions.RIGHT)) {
                    return false;
                }
            }
            if (prevAction.equals(Actions.RIGHT)) {
                if (actionWantToPerform.equals(Actions.LEFT)) {
                    return false;
                }
            }

        }

        return true;

    }



}