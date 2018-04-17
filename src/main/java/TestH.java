public class TestH {
    public static void main(String[] args) {
        int i = 0;

        long start = System.nanoTime();

        while (i < 100000) {
            Object[] objs = new Object[]{"Hello", 1, 2.0};
            for (Object o : objs) {
                Explain.it(o);
            }
            i++;
        }

        long elapsedTime = System.nanoTime() - start;

        System.out.println(elapsedTime);
    }
}
