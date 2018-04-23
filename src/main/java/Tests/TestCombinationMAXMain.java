package Tests;

public class TestCombinationMAXMain {
    public static void main(String[] args) {
        Object[] objects = new Object[]{1, 1.1f, 4};
        for (Object c : objects) {
            System.out.println(TestCombinationMAX.combineMax(c));
        }
    }
}
