package ist.meic.pa.GenericFunctions.Translators;

import ist.meic.pa.GenericFunctions.Annotations.GenericFunction;
import javassist.*;

import java.io.IOException;

public class GenericFunctionsTranslator implements Translator {
    @Override
    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

    }

    @Override
    public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(classname);
        //ctClass.setModifiers(Modifier.PUBLIC);
        try {
            todo(ctClass, pool);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void todo(CtClass ctClass, ClassPool pool) throws NotFoundException, CannotCompileException, ClassNotFoundException {
        System.out.println("Im here");
        if(ctClass.hasAnnotation(GenericFunction.class)) {
            // Get declared method
            CtMethod ctMethod = ctClass.getDeclaredMethods()[0];
            String methodName = ctMethod.getName();
            int numArguments = ctMethod.getParameterTypes().length;

            System.out.println(ctMethod.getName() + " " + numArguments);
            System.out.println(ctMethod.getLongName());

            // Change method name
            for (CtMethod method: ctClass.getDeclaredMethods()) {
                method.setName(method.getName() + "$original");
            }

            CtClass klass = pool.get("java.lang.Object");

            CtMethod m = new CtMethod(CtClass.voidType, methodName, new CtClass[] {CtClass.intType, CtClass.intType}, ctClass);

            System.out.println(m.getLongName());

            m.setBody("{" +
                            "System.out.println(\"Hello\");" +
                            "}");

            m.setModifiers(ctMethod.getModifiers());
            ctClass.addMethod(m);
            System.out.println(m.getModifiers());
        }
    }
}
