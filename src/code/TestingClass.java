package code;

import java.util.ArrayList;
import java.util.Arrays;

public class TestingClass {


    public static void main(String[] args) {

        CoastGuard costGuard = new CoastGuard();

        String generated = costGuard.genGrid();
        System.out.println(generated);
        costGuard.visualizeState(generated);
//        System.out.println("Generated Grid: " + generated);
//        System.out.println("Result: " + costGuard.solve(generated,"DF",false));
//
//        String [][] dumm = new String[2][2];
//        System.out.println(dumm[0][1] == null);





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
//        String state = "5,5;50;1,0;1,0,3,4;#;$;50;0;0;0;5;3";
//        Node n = new Node(state, null, "0,0", null, 0);
//        System.out.println(costGuard.isGoal(n));



        //        ArrayList<Node> returned_nodes = costGuard.expandNode(n);
//        for(Node ni : returned_nodes){
//            System.out.println("Action: " + ni.actionTaken + " CurrentState: " +  ni.currentState);
//        }
//        System.out.println(n.generateNextState(state, Actions.UP));

//        System.out.println(null == Actions.DOWN);





//        System.out.println("#".split(",")[]);





    }
}
