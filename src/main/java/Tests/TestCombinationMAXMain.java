package Tests;

public class TestCombinationMAXMain {
    public static void main(String[] args) {
        int i = 0;
        long startTime = System.currentTimeMillis();
        while (i < 100000) {
            Object[] objects = new Object[]{1, 1.1f, 4};
            for (Object c : objects) {
                TestCombinationMAX.combineMax(c);
            }
            i++;
        }
        long stopTime = System.currentTimeMillis();
        System.out.println(String.format("Computation took %d milliseconds.", stopTime - startTime));
    }
}
