package ist.meic.pa.GenericFunctions.Translators;

import ist.meic.pa.GenericFunctions.Annotations.GenericFunction;
import javassist.*;

import java.io.IOException;
import java.util.Arrays;

public class GenericFunctionsTranslator implements Translator {
    @Override
    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

    }

    @Override
    public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
        System.out.println("-----" + classname);

        CtClass ctClass = pool.get(classname);
        //ctClass.setModifiers(Modifier.PUBLIC);
        try {
            todo(ctClass, pool);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void todo(CtClass ctClass, ClassPool pool) throws NotFoundException, CannotCompileException, ClassNotFoundException {
        if(ctClass.hasAnnotation(GenericFunction.class)) {
            // Get declared method
            CtMethod ctMethod = ctClass.getDeclaredMethods()[0];
            String methodName = ctMethod.getName();

            // Change method name
            for (CtMethod method: ctClass.getDeclaredMethods()) {
                method.setName(method.getName() + "$original");
            }

            int count = ctMethod.getParameterTypes().length;
            CtClass class1 = pool.get("java.lang.Object");
            CtClass[] args = new CtClass[count];
            Arrays.fill(args, class1);

            CtMethod m = new CtMethod(class1, methodName, args, ctClass);

            m.setBody("{" + "System.out.println(\"Hello\");" + "return \"ola\";" +"}");
            m.setModifiers(ctMethod.getModifiers());

            ctClass.addMethod(m);
        }
    }
}
