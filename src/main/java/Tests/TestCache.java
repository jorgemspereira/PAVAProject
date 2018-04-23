package Tests;

import ist.meic.pa.GenericFunctions.*;


@GenericFunction
public interface TestCache {
    public static void it(Integer i) {
    }

    public static void it(Double i) {
    }

    public static void it(String s) {
    }

    @BeforeMethod
    public static void it(Number n) {
    }

    @AfterMethod
    public static void it(Object o) {
    }
}
