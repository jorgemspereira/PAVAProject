package ist.meic.pa.GenericFunctions;

import javassist.CtClass;
import javassist.NotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Dispatcher {

    public static Object dispatch(Object [] objects, String className)
    {
        /*
        System.out.println("--Class");
        System.out.println(className);*/
        //System.out.println("[Classes]");
        //Arrays.asList(classes).forEach(x-> System.out.println(x.getName()));


        try {
            Class invokableClass = Class.forName(className);
            ArrayList<Class[]> classArray = getParametersArray(invokableClass);

            //printArray(classArray);

            for(Class [] c : classArray) {
                try {

                    Method method = invokableClass.getDeclaredMethod(invokableClass.getDeclaredMethods()[0].getName(), c);

                    method.setAccessible(true);
                    return method.invoke(null, objects);
                } catch (IllegalArgumentException e) {
                    continue;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object dispatchBefore(Object [] objects, String className)
    {
        System.out.println("[PRINTING BEFORE]");
        try {
            Class invokableClass = Class.forName(className);
            beforeAfter(invokableClass, objects, 0);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("[PRINTING PRIMARY]");
        // Handle Before methods
        // Primary method
        try {
            Class invokableClass = Class.forName(className);
            ArrayList<Class[]> classArray = getParametersArray(invokableClass);

            //printArray(classArray);

            for(Class [] c : classArray) {
                try {
                    Method method = invokableClass.getDeclaredMethod(invokableClass.getDeclaredMethods()[0].getName(), c);
                    method.setAccessible(true);
                    method.invoke(null, objects);
                } catch (IllegalArgumentException e) {
                    continue;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("[PRINTING AFTER]");

        try {
            Class invokableClass = Class.forName(className);
            beforeAfter(invokableClass, objects,1);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    static void beforeAfter(Class c, Object [] objects, int type) throws NotFoundException, ClassNotFoundException {
        if(c.getAnnotation(GenericFunction.class) != null)
        {
            // Get the annotated methods - Before and After
            ArrayList<Class[]> methodsParams = new ArrayList<>();

            Class annotation = (type == 0)? BeforeMethod.class: AfterMethod.class;

            for(Method method : c.getDeclaredMethods())
            {
                Class c1 = Class.forName(method.getParameterTypes()[0].getName());
                Class c2 = Class.forName(objects[0].getClass().getName());
                if(method.getAnnotation(annotation)!=null && c2.isAssignableFrom(c1))
                {
                    Class [] parameters = method.getParameterTypes();
                    methodsParams.add(parameters);
                    System.out.println(c1.getName() + " " +c2.getName());
                }
            }

            for(Class [] a : methodsParams)
            {
                System.out.println("asdosaodk " + a[0].getName());
            }

            // Sort the arrays according to specificity
            ArrayList <Class[]> orderedParams = sortArray(methodsParams);
            if(type==1){Collections.reverse(orderedParams);}

            // Cycle through the other methods without annotations
            // Get its parameters and see if it can be invoked
            for(Method method : c.getDeclaredMethods()) {
                if(method.getAnnotation(BeforeMethod.class)==null && method.getAnnotation(AfterMethod.class)==null)
                {
                    Class [] params = method.getParameterTypes();

                    // Before methods
                    for(Class[] annotationMethod : orderedParams)
                    {
                        boolean callable = true;
                        for(int i = 0;i<params.length;i++)
                        {
                            Class p1 = Class.forName(params[i].getName());
                            Class p2 = Class.forName(annotationMethod[i].getName());

                            if(!p2.isAssignableFrom(p1))
                            {
                                callable = false;
                            }
                        }

                        // Write to method
                        if(callable)
                        {
                            Arrays.asList(params).forEach(x-> System.out.println(x.getName()));
                            Arrays.asList(annotationMethod).forEach(x-> System.out.println(x.getName()));

                            try {
                                ///
                                System.out.println("Method name "+ method.getName());
                                System.out.println("Annotation method");
                                for(Class a : annotationMethod)
                                {
                                    System.out.println(a.getName());
                                }
                                System.out.println("..................");
                                for(Object b : objects)
                                {
                                    System.out.println(b.getClass().getName());
                                }
                                System.out.println("...................");
                                Method toInvoke = c.getDeclaredMethod(method.getName(), annotationMethod);
                                System.out.println("To invoke: " + toInvoke.getName());
                                toInvoke.setAccessible(true);
                                toInvoke.invoke(null, objects);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<CtClass[]> sortArray2(ArrayList<CtClass[]> array) throws ClassNotFoundException {
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

    private static ArrayList<Class[]> getParametersArray(Class c)
    {
        ArrayList<Class[]> methodsParams = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();
        for(Method method : methods)
        {
            Class [] parameters = null;
            parameters = method.getParameterTypes();
            methodsParams.add(parameters);
        }

        try {
            return sortArray(methodsParams);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printArray(ArrayList<Class[]> array)
    {
        for(int i = 0;i<array.size();i++)
        {
            for(Class c : array.get(i))
            {
                System.out.print(c.getName() + " ");
            }
            System.out.println();
        }
        System.out.println("-------");
    }

    public static ArrayList<Class[]> sortArray(ArrayList<Class[]> array) throws ClassNotFoundException {
        int n = array.size();
        Class [] temp = null;

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

}
