package ist.meic.pa.GenericFunctions;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class GenericFunctionsTranslator implements Translator {
    @Override
    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

    }

    @Override
    public void onLoad(ClassPool pool, String classname) throws NotFoundException {
        CtClass ctClass = pool.get(classname);
        replaceMethodCall(ctClass);
    }

    // Interception of method call to give access to Dispatcher class
    private void replaceMethodCall(CtClass cc) {
        for (CtMethod method : cc.getDeclaredMethods()) {
            try {
                method.instrument(
                        new ExprEditor() {
                            public void edit(MethodCall m) throws CannotCompileException {
                                try {
                                    CtMethod method = m.getMethod();
                                    CtClass declaringClass = method.getDeclaringClass();

                                    if (declaringClass.hasAnnotation(GenericFunction.class)) {
                                        String packageName = this.getClass().getPackage().getName();
                                        m.replace("{ $_ = ($r)" + packageName + ".Dispatcher.dispatch($args, \"" + m.getClassName() + "\"" + "); }");
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
    }
}
