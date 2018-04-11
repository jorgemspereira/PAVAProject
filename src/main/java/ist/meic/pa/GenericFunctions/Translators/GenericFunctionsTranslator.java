package ist.meic.pa.GenericFunctions.Translators;

import ist.meic.pa.GenericFunctions.AfterMethod;
import ist.meic.pa.GenericFunctions.BeforeMethod;
import ist.meic.pa.GenericFunctions.GenericFunction;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.io.IOException;
import java.util.ArrayList;

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
        //before2(ctClass);
    }

    private void grabAllMethod(CtClass cc) {
            for (CtMethod method : cc.getDeclaredMethods()) {
                try {
                    method.instrument(
                            new ExprEditor() {
                                public void edit(MethodCall m) throws CannotCompileException {
                                    try {
                                        //System.out.println("Name: " +cc.getName());
                                        CtMethod method = m.getMethod();
                                        CtClass declaringClass = method.getDeclaringClass();
                                        //Class c = Class.forName();
                                        if (declaringClass.hasAnnotation(GenericFunction.class)) {
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
            }
    }

    private void before2(CtClass cc) {

        for (CtMethod method : cc.getDeclaredMethods()) {
            try {
                method.instrument(
                        new ExprEditor() {
                            public void edit(MethodCall m) throws CannotCompileException {
                                try {
                                    CtClass c = ClassPool.getDefault().get(m.getClassName());
                                    //Class c = Class.forName();
                                    if (c.hasAnnotation(GenericFunction.class)) {
                                        m.replace("{ $_ = ($r)ist.meic.pa.GenericFunctions.Dispatcher.dispatchBefore($args, \"" + m.getClassName() + "\"" + "); }");
                                    }
                                } catch (NotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (CannotCompileException | RuntimeException e) {
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
            // Get the annotated methods - Before and After
            ArrayList<CtClass[]> methodsParamsBefore = new ArrayList<>();
            ArrayList<CtClass[]> methodsParamsAfter = new ArrayList<>();

            for(CtMethod ctMethod : ctClass.getDeclaredMethods())
            {
                if(ctMethod.hasAnnotation(BeforeMethod.class))
                {
                    CtClass [] parameters = ctMethod.getParameterTypes();
                    methodsParamsBefore.add(parameters);
                }
                else if (ctMethod.hasAnnotation(AfterMethod.class))
                {
                    CtClass [] parameters = ctMethod.getParameterTypes();
                    methodsParamsAfter.add(parameters);
                }
            }

            // Sort the arrays according to specificity
            ArrayList <CtClass[]> orderedBeforeParams = sortArray(methodsParamsBefore);
            ArrayList <CtClass[]> orderedAfterParams = sortArray(methodsParamsAfter);

            System.out.println("Printing before ordering array");
            printArray(orderedBeforeParams);
            System.out.println("------------");

            // Cycle through the other methods without annotations
            // Get its parameters and see if it can be invoked
            for(CtMethod ctMethod : ctClass.getDeclaredMethods()) {
                if(!ctMethod.hasAnnotation(BeforeMethod.class) && !ctMethod.hasAnnotation(AfterMethod.class))
                {
                    CtClass [] params = ctMethod.getParameterTypes();

                    // Before methods
                    for(CtClass[] beforeMethod : orderedBeforeParams)
                    {
                        boolean callable = true;
                        for(int i = 0;i<params.length;i++)
                        {
                            Class p1 = Class.forName(params[i].getName());
                            Class p2 = Class.forName(beforeMethod[i].getName());

                            System.out.println(p1.getName() + " " + p2.getName());
                            if(!p2.isAssignableFrom(p1))
                            {
                                callable = false;
                            }
                        }

                        // Write to method
                        if(callable)
                        {
                            CtMethod method = ctClass.getDeclaredMethod(ctMethod.getName(), beforeMethod);
                            try {
                                ctMethod.insertBefore(method.getName() + "($$);");
                            } catch (CannotCompileException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // After method

                    // Before methods
                    for(CtClass[] afterMethod : orderedAfterParams)
                    {
                        boolean callable = true;
                        for(int i = 0;i<params.length;i++)
                        {
                            Class p1 = Class.forName(params[i].getName());
                            Class p2 = Class.forName(afterMethod[i].getName());

                            System.out.println(p1.getName() + " " + p2.getName());
                            if(!p2.isAssignableFrom(p1))
                            {
                                callable = false;
                            }
                        }

                        // Write to method
                        if(callable)
                        {
                            CtMethod method = ctClass.getDeclaredMethod(ctMethod.getName(), afterMethod);
                            try {
                                ctMethod.insertAfter(method.getName() + "($$);");
                            } catch (CannotCompileException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            try {
                ctClass.writeFile();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<CtClass[]> sortArray(ArrayList<CtClass[]> array) throws ClassNotFoundException {
        int n = array.size();
        CtClass [] temp = null;

        if(n == 0) {
            return array;
        }

        //Para cada argumento
        for(int k = (array.get(0).length - 1); k >= 0 ; k--) {
            for(int i = 0 ; i < n - 1 ; i++){
                for (int j = 0; j < (n-i-1); j ++) {
                    Class c1 = Class.forName(array.get(j+1)[k].getName());
                    Class c2 = Class.forName(array.get(j)[k].getName());

                    if(!c1.getName().equals(c2.getName()))
                    {
                        if (!c1.isAssignableFrom(c2))
                        {
                            // Swap
                            temp = array.get(j);
                            array.set(j, array.get(j+1));
                            array.set(j+1, temp);
                        }
                    }
                }
            }
        }
        return array;
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
