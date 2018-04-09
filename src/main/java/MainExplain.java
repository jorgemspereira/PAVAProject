public class MainExplain {
    public static void main(String [] args) {
        Object[] objs = new Object[]{"Hello", 1, 2.0};
        for (Object o : objs) {
            Explain.it(o);
        }
    }
}
