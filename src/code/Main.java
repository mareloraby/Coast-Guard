package code;

public class Main {

    public static void main(String[] args) {
        CoastGuard coastGuard = new CoastGuard();

        /*
        - Possible strategies:
        *   BF
        *   DF
        *   IDS
        *   UC
        *   GR1
        *   GR2
        *   AS1
        *   AS2

        * - Set visualize to true if you want to visualize the grid states throughout the chosen path
        */


//        System.out.println("Randomly initialized grid: ");
//        String generatedGrid = coastGuard.genGrid();
//        coastGuard.solve(generatedGrid,"BF",true);

        System.out.println("User generated grid: ");
        String gridExample1 = "6,7;82;1,4;2,3;1,1,58,3,0,58,4,2,72;";
        String gridExample2 = "5,6;50;0,1;0,4,3,3;1,1,90;";
        coastGuard.solve(gridExample1,"BF",true);


    }
}
