package ist.meic.pa.GenericFunctions;

import javassist.NotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

public class Dispatcher {

    public static Object dispatch(Object [] objects, String className)
    {
        try {
            Class invokableClass = Class.forName(className);
            handleAnnotations(invokableClass, objects, 0);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        Object toReturn = null;

        try {
            Class invokableClass = Class.forName(className);
            ArrayList<Class[]> classArray = getParametersArray(invokableClass);

            for(Class [] c : classArray) {
                try {

                    Method method = invokableClass.getDeclaredMethod(invokableClass.getDeclaredMethods()[0].getName(), c);
                    method.setAccessible(true);
                    toReturn = method.invoke(null, objects);
                    break;
                } catch (IllegalArgumentException e) {
                    continue;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Class invokableClass = Class.forName(className);
            handleAnnotations(invokableClass, objects, 1);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    static void handleAnnotations(Class c, Object [] objects, int type) throws NotFoundException, ClassNotFoundException {
        if(c.getAnnotation(GenericFunction.class) != null)
        {
            ArrayList<Class[]> methodsParams = new ArrayList<>();

            Class annotation = (type == 0) ? BeforeMethod.class: AfterMethod.class;

            for(Method method : c.getDeclaredMethods())
            {
                if(method.getAnnotation(annotation)!=null)
                {
                    Class [] parameters = method.getParameterTypes();
                    methodsParams.add(parameters);
                }
            }

            // Sort the arrays according to specificity
            ArrayList <Class[]> orderedParams = sortArray(methodsParams);
            if(type==1){Collections.reverse(orderedParams);}

            for(Class[] params : orderedParams)
            {
                Method method = null;
                try {
                    method = c.getDeclaredMethod(c.getDeclaredMethods()[0].getName(), params);
                    method.setAccessible(true);
                    method.invoke(null, objects);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch(IllegalArgumentException e)
                {
                    continue;
                }
            }
        }
    }

    private static ArrayList<Class[]> getParametersArray(Class c)
    {
        ArrayList<Class[]> methodsParams = new ArrayList<>();
        Method[] methods = c.getDeclaredMethods();
        for(Method method : methods)
        {
            Class [] parameters = method.getParameterTypes();
            methodsParams.add(parameters);
        }

        return sortArray(methodsParams);
    }

    public static ArrayList<Class[]> sortArray(ArrayList<Class[]> array) {
        int n = array.size();
        Class [] temp = null;

        if(n == 0) {
            return array;
        }

        for(int k = (array.get(0).length - 1); k >= 0 ; k--) {
            for(int i = 0 ; i < n - 1 ; i++){
                for (int j = 0; j < (n-i-1); j ++) {
                    try {
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
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return array;
    }

}
