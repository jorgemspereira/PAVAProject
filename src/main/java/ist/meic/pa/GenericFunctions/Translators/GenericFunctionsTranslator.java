package ist.meic.pa.GenericFunctions.Translators;

import javassist.*;

public class GenericFunctionsTranslator implements Translator {
    @Override
    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

    }

    @Override
    public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(classname);
        try {
            todo(ctClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void todo(CtClass ctClass) throws NotFoundException, CannotCompileException, ClassNotFoundException {

    }
}
