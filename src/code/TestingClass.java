package code;

public class TestingClass {

    public static void main(String[] args) {

//        CoastGuard costGuard = new CoastGuard();
//        String generated = costGuard.genGrid();
//
//        System.out.println(generated);
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

        String state = "5,5;50;1,0;1,0,3,4;4,4,1;$;31;76;1;0;4;1";
        Node n = new Node(state, null, "0,0", null, 0);
        System.out.println(n.generateNextState(state, Actions.DROP));








    }
}
