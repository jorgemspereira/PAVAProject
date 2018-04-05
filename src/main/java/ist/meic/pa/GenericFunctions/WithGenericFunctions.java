package ist.meic.pa.GenericFunctions;

import ist.meic.pa.GenericFunctions.Translators.GenericFunctionsTranslator;
import javassist.*;

public class WithGenericFunctions {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.print("Usage java -cp genericFunctions.jar WithGenericFunctions ClassName");
        } else {
            try {
                Translator translator = new GenericFunctionsTranslator();
                ClassPool pool = ClassPool.getDefault();
                Loader classLoader = new Loader();
                classLoader.addTranslator(pool, translator);
                String[] restArgs = new String[args.length - 1];
                System.arraycopy(args, 1, restArgs, 0, restArgs.length);
                classLoader.run(args[0], restArgs);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
