package Tests;

public class TestCacheMain {
    public static void main(String[] args) {
        long time = callWithCache();
        System.out.println(String.format("Computation took %d milliseconds.", time));
    }

    private static long callWithCache() {
        int i = 0;
        long startTime = System.currentTimeMillis();

        while(i < 1000000) {
            Object[] objs = new Object[]{"Hello", 1, 2.0};
            for (Object o : objs) TestCache.it(o);
            i++;
        }

        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }
}
