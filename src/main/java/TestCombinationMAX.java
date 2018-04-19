import ist.meic.pa.GenericFunctionsExtended.*;

import static ist.meic.pa.GenericFunctionsExtended.CombinationOrder.MOST_TO_LEAST;
import static ist.meic.pa.GenericFunctionsExtended.CombinationType.LIST;

@Combination(order=MOST_TO_LEAST, type=LIST)
public class TestCombinationMAX {
    public static Object combineMax(Integer b) {
        return false;
    }
    public static Object combineMax(Object b) {
        return 2;
    }
    public static Object combineMax(Float b) {
        return "3r";
    }
    public static Object combineMax(Number b) {
        return "f";
    }
}
