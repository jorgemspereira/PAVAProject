package Tests;

import ist.meic.pa.GenericFunctionsExtended.Combination;

import static ist.meic.pa.GenericFunctionsExtended.CombinationOrder.LEAST_TO_MOST;
import static ist.meic.pa.GenericFunctionsExtended.CombinationType.LIST;

@Combination(order=LEAST_TO_MOST, type=LIST)
public class TestCombinationLIST {
    public static Object combineList(Integer b) {
        return "Integer";
    }
    public static Object combineList(Object b) {
        return "Object";
    }
    public static Object combineList(Float b) {
        return "Float";
    }
    public static Object combineList(Number b) {
        return "Number";
    }
}