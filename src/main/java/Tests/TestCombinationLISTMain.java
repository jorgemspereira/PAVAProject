package Tests;

public class TestCombinationLISTMain {
    public static void main(String[] args) {
        Object[] objects = new Object[]{1, 1.1f, 4};
        for (Object c : objects) {
            System.out.println(TestCombinationLIST.combineList(c));
        }
    }
}
