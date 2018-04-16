import ist.meic.pa.GenericFunctions.GenericFunction;

@GenericFunction
public class App {

	public static void E(Object x) {
		System.out.println("Object");
	}

	public static void E(A x) {
		System.out.println("X");
	}

	public static void E(B x) {
		System.out.println("Y");
	}
}

class X implements A, B {

}

class Y implements B, A {

}


interface A {

}

interface B {

}