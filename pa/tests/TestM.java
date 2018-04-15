package pa.tests;

import pa.tests.domain.Blue;
import pa.tests.domain.Color;
import pa.tests.domain.Red;
import pa.tests.domain.Yellow;

public class TestM {
    public static void main(String[] args) {
        Color[] colors = new Color[]{new Red(), new Blue(), new Yellow()};
        for (Color c1 : colors)
            for (Color c2 : colors)
                System.out.println(Color.mix(c1, c2));
    }
}
