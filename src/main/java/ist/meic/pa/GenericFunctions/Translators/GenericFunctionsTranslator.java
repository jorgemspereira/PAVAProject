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
        System.out.println("-----" + classname);

        CtClass ctClass = pool.get(classname);
        ctClass.setModifiers(Modifier.PUBLIC);
        try {
            todo(ctClass, pool);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void todo(CtClass ctClass, ClassPool pool) throws NotFoundException, CannotCompileException, ClassNotFoundException {
        System.out.println(ctClass.getName());
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

            String newName = "public static Object " + "" + methodName + "(";
            for(int i = 0; i < numArguments;i++)
            {
                if(i < numArguments - 1){
                    newName += "Object a,";
                }
                else{
                    newName += "Object b){}";
                }
            }

            System.out.println(newName);
            ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));

            CtMethod m = CtNewMethod.make(newName, ctClass);
            //CtMethod m = CtNewMethod.make("public Object mix(Object x, Object y) {")
            System.out.println(m.getLongName());

            m.setBody("{" +
                            "System.out.println(\"Hello\");" +
                            "String a = \"abc\"" +
                            "return ($r)a;" +
                            "}");

            ctClass.addMethod(m);

            try {
                ctClass.writeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
