public class CacheTest {
    public static void main(String[] args) {
        int i = 0;

        long startTime = System.currentTimeMillis();

        while(i < 1000000) {
            Object[] objs = new Object[]{"Hello", 1, 2.0};
            for (Object o : objs) Explain.it(o);
            i++;
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;



        System.out.println(elapsedTime);
    }
}

