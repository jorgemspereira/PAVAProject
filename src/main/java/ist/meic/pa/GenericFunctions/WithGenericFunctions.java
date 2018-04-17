package ist.meic.pa.GenericFunctions;

import javassist.*;

public class WithGenericFunctions {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.print("Usage java -cp ./genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctions.WithGenericFunctions ClassName");
        } else {
            try {
                Translator translator = new GenericFunctionsTranslator();
                ClassPool pool = ClassPool.getDefault();
                pool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
                Loader classLoader = new Loader();
                classLoader.addTranslator(pool, translator);
                classLoader.run(args[0], null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
