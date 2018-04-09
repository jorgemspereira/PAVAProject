package ist.meic.pa.GenericFunctions;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dispatcher {

    public static Object dispatch(Object [] objects, String className)
    {
        System.out.println("[Dispatch]");
        System.out.println("--Objects");
        Arrays.asList(objects).forEach(x-> System.out.println(x));
        /*
        System.out.println("--Class");
        System.out.println(className);*/
        //System.out.println("[Classes]");
        //Arrays.asList(classes).forEach(x-> System.out.println(x.getName()));


        try {
            Class invokableClass = Class.forName(className);
            ArrayList<Class[]> classArray = getParametersArray(invokableClass);

            printArray(classArray);

            for(Class [] c : classArray) {
                try {

                    Method method = invokableClass.getDeclaredMethod(invokableClass.getDeclaredMethods()[0].getName(), c);

                    System.out.println("iasdisamdimsaidmisadmisa");
                    Arrays.asList(method.getParameterTypes()).forEach(x-> System.out.println(x.getName()));
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
