package code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public abstract class SearchProblem{

    abstract public String solveSearchProblem(String grid, String strategy );

    abstract public boolean isGoal(Node node);


//    public static String BF(){
//        String result = "";
//
//        while(!BFQueue.isEmpty()){
//
//            Node currentNode = BFQueue.poll();
//            expandedNodes++;
//
//            // is goal test?
//
//            // expand with all valid actions:
//            /*
//            UP,
//            DOWN,
//            LEFT,
//            RIGHT,
//            PICKUP,
//            DROP,
//            RETRIEVE
//            */
//
//        }
//        return result;
//    }




}