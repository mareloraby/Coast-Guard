package code;

public class TestingClass {

    public static void main(String[] args) {

        CoastGuard costGuard = new CoastGuard();
        String generated = costGuard.genGrid();

        System.out.println(generated);

        costGuard.visualizeGrid(generated);


//        String [] x = ("d;h;$".split(";"));
//        for(String xi: x){
//            if(xi == ""){
//                System.out.println("Fady");
//            }
//            else
//                System.out.println(xi);
//        }

        // initial state: no wrecks
        // wreck-> append





    }
}
