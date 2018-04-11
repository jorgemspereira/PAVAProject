public class MainExplainComplex {
    public static void main(String [] args) {
        Ser[] objs = new Ser[]{new Animal(), new Gato(), new Cao(), new Homem()};
        for (Ser o : objs) {
            for(Ser a: objs) {
                for(Ser c: objs){
                    ExplainComplex.it(o,a,c);
                }
            }

        }
    }
}
