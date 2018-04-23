package Tests;

import ist.meic.pa.GenericFunctionsExtended.*;

import static ist.meic.pa.GenericFunctionsExtended.CombinationOrder.MOST_TO_LEAST;
import static ist.meic.pa.GenericFunctionsExtended.CombinationType.AND;
import static ist.meic.pa.GenericFunctionsExtended.CombinationType.LIST;

@Combination(order=MOST_TO_LEAST, type=LIST)
public class TestCombinationMAX {
    public static Object combineMax(Integer b) {
        return "Integer";
    }
    public static Object combineMax(Object b) {
        return "Object";
    }
    public static Object combineMax(Float b) {
        return "Float";
    }
    public static Object combineMax(Number b) {
        return "Number";
    }
}
