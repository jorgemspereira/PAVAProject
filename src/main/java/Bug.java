
import ist.meic.pa.GenericFunctions.AfterMethod;
import ist.meic.pa.GenericFunctions.BeforeMethod;
import ist.meic.pa.GenericFunctions.GenericFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@GenericFunction
public class Bug {
    // Test F
    public static void bug(Object o, Object o1) {
        System.out.println("Object");
    }

    public static void bug(Foo f, Bar f1){
        System.out.println("FooBar");
    }
    public static void bug(Bar b, Bar b2){ System.out.println("Bar"); }
    
    public static void bug(Foo b, Foo b2){ System.out.println("Foo"); }
}

