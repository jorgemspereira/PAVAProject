package pa.tests;

import pa.tests.domain.C1;
import pa.tests.domain.C2;
import pa.tests.domain.MakeIt;

public class TestO {
    public static void main(String[] args) {
        Object c = new C1();
        MakeIt.ddouble(c);
    }
}
