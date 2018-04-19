public class TestH {
    public static void main(String[] args) {
        int i = 0;
        while(i < 10000) {
            Object[] objs = new Object[]{"Hello", 1, 2.0};
            for (Object o : objs) Explain.it(o);
            i++;
        }
    }
}
