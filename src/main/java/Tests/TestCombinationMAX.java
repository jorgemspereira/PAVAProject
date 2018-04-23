package Tests;

import ist.meic.pa.GenericFunctionsExtended.Combination;

import static ist.meic.pa.GenericFunctionsExtended.CombinationOrder.MOST_TO_LEAST;
import static ist.meic.pa.GenericFunctionsExtended.CombinationType.MAX;

@Combination(order=MOST_TO_LEAST, type=MAX)
public class TestCombinationMAX {
    public static Object combineMax(Integer b) {
        return 3;
    }
    public static Object combineMax(Float b) {
        return 4;
    }
    public static Object combineMax(Number b) {
        return 1;
    }
    public static Object combineMax(Object b) {
        return 2;
    }
}
