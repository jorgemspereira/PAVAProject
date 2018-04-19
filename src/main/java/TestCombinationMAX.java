import ist.meic.pa.GenericFunctionsExtended.*;

@Combination(order=CombinationOrder.LEAST_TO_MOST, type=CombinationType.LIST)
public class TestCombinationMAX {
    public static Object combineMax(Integer b) {
        return 1;
    }
    public static Object combineMax(Object b) {
        return 2;
    }
    public static Object combineMax(Float b) {
        return 3;
    }
    public static Object combineMax(Number b) {
        return 1;
    }
}
