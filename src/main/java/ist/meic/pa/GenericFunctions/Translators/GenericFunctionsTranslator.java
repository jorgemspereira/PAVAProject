package ist.meic.pa.GenericFunctions.Translators;

import ist.meic.pa.GenericFunctions.Annotations.BeforeMethod;
import ist.meic.pa.GenericFunctions.Annotations.GenericFunction;
import javassist.*;
import javassist.bytecode.FieldInfo;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GenericFunctionsTranslator implements Translator {
    @Override
    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

    }

    @Override
    public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
        System.out.println("-----" + classname);
        CtClass ctClass = pool.get(classname);
        //ctClass.setModifiers(Modifier.PUBLIC);
        grabAllMethod(ctClass);
        //todo(ctClass, pool);
        //before(ctClass);
    }
    private void grabAllMethod(CtClass cc) {

            for (CtMethod method : cc.getDeclaredMethods()) {
                try {
                    method.instrument(
                            new ExprEditor() {
                                public void edit(MethodCall m) throws CannotCompileException {
                                    try {
                                        CtClass c = ClassPool.getDefault().get(m.getClassName());
                                        //Class c = Class.forName();
                                        if (c.hasAnnotation(GenericFunction.class)) {
                                            m.replace("{ $_ = ($r)ist.meic.pa.GenericFunctions.Dispatcher.dispatch($args, \"" + m.getClassName() + "\"" + "); }");
                                        }
                                    } catch (NotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } catch (CannotCompileException e) {
                    //e.printStackTrace();
                    continue;
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

    void before(CtClass ctClass) throws NotFoundException, ClassNotFoundException {
        if(ctClass.hasAnnotation(GenericFunction.class))
        {
            ArrayList<CtClass[]> methodsParamsBefore = new ArrayList<>();

            for(CtMethod ctMethod : ctClass.getDeclaredMethods())
            {
                if(ctMethod.hasAnnotation(BeforeMethod.class))
                {
                    CtClass [] parameters = ctMethod.getParameterTypes();
                    methodsParamsBefore.add(parameters);
                }
            }

            printArray(methodsParamsBefore);
            //ArrayList <CtClass[]> orderedMethodsParams = sortArray(methodsParamsBefore);
            printArray(methodsParamsBefore);


        }
    }



    public void writeArray(ArrayList <CtClass []> array)
    {
        String result = "new ";
        for(int i = 0;i<array.size();i++)
        {

        }
    }

    public void printArray(ArrayList <CtClass []> array)
    {
        for(int i = 0;i<array.size();i++)
        {
            for(CtClass c : array.get(i))
            {
               System.out.print(c.getName() + " ");
            }
            System.out.println();
        }
        System.out.println("-------");
    }
}
