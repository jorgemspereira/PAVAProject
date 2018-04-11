import ist.meic.pa.GenericFunctions.AfterMethod;
import ist.meic.pa.GenericFunctions.BeforeMethod;
import ist.meic.pa.GenericFunctions.GenericFunction;

@GenericFunction
public class ExplainComplex {
    public static void it(Animal a, Gato g, Ser s) {
        System.out.print("A, G, S");
    }
    public static void it(Ser s, Animal g, Ser a) {
        System.out.print("S, A, S");
    }
    public static void it(Animal a, Animal g, Gato s) {
        System.out.print("A, A, G");
    }
    public static void it(Gato a, Ser g, Animal b) {
        System.out.print("G, S, A");
    }
    public static void it(Cao a, Gato g, Homem s) {
        System.out.print("C, G, H");
    }
    public static void it(Gato g, Ser s, Homem h){
        System.out.print("G, S, H");
    }
    public static void it(Ser s, Ser sa, Ser sb){
        System.out.print("S, S, S");
    }
    public static void it(Animal a, Animal b, Animal c){
        System.out.print("A, A, A");
    }
    public static void it(Animal a, Gato g, Animal c){
        System.out.print("A, G, A");
    }

    @BeforeMethod
    public static void it(Object n, Object a, Object b) {
        System.out.println(".");
    }


}

class Ser {

}

class Animal extends Ser{

}

class Homem extends Ser{

}

class Cao extends Animal{

}

class Gato extends Animal{

}