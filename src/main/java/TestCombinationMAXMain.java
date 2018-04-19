public class TestCombinationMAXMain {
    public static void main(String[] args) {
        TestCombinationMAX testCombination = new TestCombinationMAX();
        Object[] objects = new Object[]{1, 1.2f, 4};
        for (Object c : objects) System.out.println(testCombination.combineMax(c));
    }
}
