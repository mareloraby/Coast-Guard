public class TestingClass {

    public static void main(String[] args) {

        CoastGuard costGuard = new CoastGuard();
        String generated = costGuard.genGrid();

        System.out.println(generated);

        costGuard.visualizeGrid(generated);



    }
}
