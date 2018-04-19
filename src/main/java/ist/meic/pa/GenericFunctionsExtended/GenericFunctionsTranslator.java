package ist.meic.pa.GenericFunctionsExtended;

import ist.meic.pa.GenericFunctions.GenericFunction;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.io.IOException;

public class GenericFunctionsTranslator implements Translator {
    @Override
    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

    }

    @Override
    public void onLoad(ClassPool pool, String classname) throws NotFoundException {
        System.out.println("Classname: " + classname);
        CtClass ctClass = pool.get(classname);
        handleClass(ctClass);
    }

    private void handleClass(CtClass cc) {
        for (CtMethod method : cc.getDeclaredMethods()) {
            try {
                method.instrument(
                        new ExprEditor() {
                            public void edit(MethodCall m) throws CannotCompileException {
                                try {
                                    CtMethod method = m.getMethod();
                                    CtClass declaringClass = method.getDeclaringClass();
                                    if (declaringClass.hasAnnotation(GenericFunction.class) || declaringClass.hasAnnotation(Combination.class)) {
                                        String packageName = this.getClass().getPackage().getName();
                                        m.replace("{ $_ = ($r)"+packageName+".Dispatcher.dispatch($args, \"" + m.getClassName() + "\"" + "); }");
                                    }
                                } catch (NotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
        try {
            cc.writeFile();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
