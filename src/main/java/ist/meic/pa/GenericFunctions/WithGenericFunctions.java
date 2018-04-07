package ist.meic.pa.GenericFunctions;

import ist.meic.pa.GenericFunctions.Translators.GenericFunctionsTranslator;
import javassist.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.sql.SQLOutput;

public class WithGenericFunctions {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.print("Usage java -cp genericFuncions.jar WithGenericFunctions ClassName");
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
