package ist.meic.pa.GenericFunctions.Translators;

import ist.meic.pa.GenericFunctions.Annotations.BeforeMethod;
import ist.meic.pa.GenericFunctions.Annotations.GenericFunction;
import javassist.*;
import javassist.bytecode.FieldInfo;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
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
        try {
            todo(ctClass, pool);
            //before(ctClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
            ArrayList <CtClass[]> orderedMethodsParams = sortArray(methodsParamsBefore);
            printArray(methodsParamsBefore);


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
            CtClass returnClass = pool.get("java.lang.Object");
            CtClass[] args = new CtClass[count];
            Arrays.fill(args, class1);

            CtMethod m = new CtMethod(returnClass, methodName, args, ctClass);

            String className = ctClass.getName();

            String argsTypes = "new String[]{";
            for(CtClass c : args)
            {
                argsTypes += "\"" + c.getName()+"\",";
            }
            argsTypes = argsTypes.substring(0, argsTypes.length()-1);
            argsTypes += "}";
            m.setModifiers(ctMethod.getModifiers());

            String template = "{\n" +
                    "ist.meic.pa.GenericFunctions.Dispatcher dispatcher = new ist.meic.pa.GenericFunctions.Dispatcher();\n" +
                    "return dispatcher.dispatch($args, \"" + className + "\",\"" + methodName + "\"," + argsTypes+");\n"+
                    "}\n";

            m.setBody(template);

            /*ArrayList<CtClass[]> methodsParams = new ArrayList<>();
            CtMethod [] methods = ctClass.getDeclaredMethods();
            for(CtMethod method : methods)
            {
                CtClass [] parameters = method.getParameterTypes();
                methodsParams.add(parameters);
            }

            ArrayList <CtClass[]> orderedMethodsParams = sortArray(methodsParams);

            printArray(methodsParams);*/


            //pool.importPackage("java.util.ArrayList");

            ctClass.addMethod(m);

            try {
                ctClass.writeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<CtClass[]> sortArray(ArrayList<CtClass[]> array) throws ClassNotFoundException {
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
