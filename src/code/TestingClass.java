package code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

public class TestingClass {

    static int numExpandedNodes = 0;

    public static void main(String[] args) {


        CoastGuard costGuard = new CoastGuard();

//        String generated = costGuard.genGrid();
//        String generated = "2,2;30;1,0;0,0;1,1,20;";
//        String generated = "3,4;97;1,2;0,1;3,2,65;";
        String generated = "3,4;97;2,1;1,0;2,3,65;";
//        String state = "3,4;97;1,2;0,1;3,2,65;$;97;65;1;0;0;0";
//        String generated = "7,5;40;2,3;3,6;1,1,10,4,5,90;";
//        String generated = "5,6;50;0,1;0,4,3,3;1,1,90;";

//        System.out.println(generated);
//        System.out.println("Generated Grid: " + generated);
//        System.out.println("Result: " + costGuard.solve(generated,"ID",false));
//        System.out.println("Result: " + costGuard.solve(generated,"DF",false));






//
//
//        costGuard.visualizeGrid(generated);


//        String [] x = ("d;h;$;7678132687216".split(";",2));
//        for(String xi: x){
////            if(xi == ""){
////                System.out.println("Fady");
////            }
//           // else
//                System.out.println(xi);
//        }

        // initial state: no wrecks
        // wreck-> append
//



//        String state = "5,5;50;1,0;1,0,3,4;4,4,60;$;30;60;0;0;5;3";
//        Node n = new Node(state, null, "0,0", null, 0);
//        ArrayList<Node> temp = CoastGuard.expandNode(n);
//        System.out.println(temp.size());
//        System.out.println(costGuard.isGoal(n));



        //        ArrayList<Node> returned_nodes = costGuard.expandNode(n);
//        for(Node ni : returned_nodes){
//            System.out.println("Action: " + ni.actionTaken + " CurrentState: " +  ni.currentState);
//        }
//        System.out.println(n.generateNextState(state, Actions.DROP));

//        System.out.println(null == Actions.DOWN);





//        System.out.println("#".split(",")[]);





    }
}
