package pa.tests;

import pa.tests.domain.Bug;
import pa.tests.domain.C1;
import pa.tests.domain.C2;

public class TestF {
    public static void main(String[] args) {
        Object c1 = new C1(), c2 = new C2();
        Bug.bug(c1);
        Bug.bug(c2);
    }
}
