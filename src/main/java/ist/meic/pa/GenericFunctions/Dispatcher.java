package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dispatcher {

    public static Object dispatch(Object [] objects, String className) {

        Object toReturn =  null;

        try {
            Class invokableClass = Class.forName(className);
            if (getCallableMethods(invokableClass, getClassesOfObjects(objects)).size() != 0) {
                handleBefore(invokableClass, objects);
                toReturn = handleMainMethods(invokableClass, objects);
                handleAfter(invokableClass, objects);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    private static Class[] getClassesOfObjects(Object[] objects) {
        List<Class> classes = new ArrayList<>();
        for(Object o : objects) {
            classes.add(o.getClass());
        }
        Class[] toReturn = new Class[classes.size()];
        return classes.toArray(toReturn);
    }

    private static List<Class[]> getParametersArray(List<Method> methods) {
        ArrayList<Class[]> methodsParams = new ArrayList<>();
        for(Method method : methods) {
            Class[] parameters = method.getParameterTypes();
            methodsParams.add(parameters);
        }
        return methodsParams;
    }

    private static boolean isCallable(Method m, Class[] args) {
        if(m.getParameterTypes().length != args.length) {
            return false;
        }

        for (int i = 0; i < m.getParameterTypes().length; i++) {
            if (!m.getParameterTypes()[i].isAssignableFrom(args[i])) {
                return false;
            }
        }
        return true;
    }

    private static List<Method> getCallableMethods(Class c, Class[] args) {
        List<Method> toReturn = new ArrayList<>();
        for (Method m : c.getDeclaredMethods()) {
            if (m.getAnnotation(BeforeMethod.class) == null && m.getAnnotation(AfterMethod.class) == null) {
                if (isCallable(m, args)) {
                    toReturn.add(m);
                }
            }
        }
        return toReturn;
    }

    private static List<Method> getAnnotatedCallableMethods(Class c, Class[] args, Class annotation) {
        List<Method> toReturn = new ArrayList<>();
        for (Method m : c.getDeclaredMethods()) {
            if (m.getAnnotation(annotation) != null) {
                if (isCallable(m, args)) {
                    toReturn.add(m);
                }
            }
        }
        return toReturn;
    }

    private static Object handleMainMethods(Class c, Object[] objects)  {
        Class[] arguments = getClassesOfObjects(objects);
        List<Method> methods = getCallableMethods(c, arguments);
        List<Class[]> methodsParams = getParametersArray(methods);
        List<Class[]> orderedParams = sortArray(methodsParams, arguments);
        return callMethod(c, orderedParams, objects);
    }

    private static void handleBefore(Class c, Object [] objects) {
        Class[] arguments = getClassesOfObjects(objects);
        List<Method> methods = getAnnotatedCallableMethods(c, arguments, BeforeMethod.class);
        List<Class[]> methodsParams = getParametersArray(methods);
        List<Class[]> orderedParams = sortArray(methodsParams, arguments);
        callMethods(c, orderedParams, objects);
    }

    private static void handleAfter(Class c, Object [] objects) {
        Class[] arguments = getClassesOfObjects(objects);
        List<Method> methods = getAnnotatedCallableMethods(c, arguments, AfterMethod.class);
        List<Class[]> methodsParams = getParametersArray(methods);
        List<Class[]> orderedParams = sortArray(methodsParams, arguments);
        Collections.reverse(orderedParams);
        callMethods(c, orderedParams, objects);
    }

    private static void callMethods(Class c, List<Class[]> orderedParams, Object [] objects)  {
        for(Class[] params : orderedParams) {
            try {
                Method method = c.getDeclaredMethod(c.getDeclaredMethods()[0].getName(), params); //FIXME
                method.setAccessible(true);
                method.invoke(null, objects);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static Object callMethod(Class c, List<Class[]> orderedParams, Object [] objects){
        try {
            Method method = c.getDeclaredMethod(c.getDeclaredMethods()[0].getName(), orderedParams.get(0)); //FIXME
            method.setAccessible(true);
            return method.invoke(null, objects);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static int indexOf(Class [] objects, Class object)
    {
        for(int i = 0;i<objects.length;i++)
        {
            if(object.getName().equals(objects[i].getName()))
            {
                return i;
            }
        }
        return -1;
    }

    private static List<Class[]> sortArray2(List<Class[]> array, Class[] objects) throws ClassNotFoundException {
        //array : ((Foo), (Bar), (Obj))
        //objects: (C1)
        //interfaces: C1 - Foo, Bar

        int n = array.size();

        Class [] temp = null;

        for (int k = (array.get(0).length - 1); k >= 0; k--) {
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < (n - i - 1); j++) {
                    Class[] interfaces = objects[k].getInterfaces();

                    Class c1 = Class.forName(array.get(j + 1)[k].getName());
                    Class c2 = Class.forName(array.get(j)[k].getName());

                    int c1Index = indexOf(interfaces, c1);
                    int c2Index = indexOf(interfaces, c2);

                    if (c1Index == -1 || c2Index == -1) {
                        continue;
                    }

                    if (c1Index < c2Index) {
                        // Swap
                        temp = array.get(j);
                        array.set(j, array.get(j + 1));
                        array.set(j + 1, temp);
                    }
                }
            }
        }
        return array;
    }

    private static List<Class[]> sortArray(List<Class[]> array, Class[] objects) {
        int n = array.size();
        Class [] temp = null;
        if(n == 0) {
            return array;
        }

        try {
            for (int k = (array.get(0).length - 1); k >= 0; k--) {
                for (int i = 0; i < n - 1; i++) {
                    for (int j = 0; j < (n - i - 1); j++) {
                        Class c1 = Class.forName(array.get(j + 1)[k].getName());
                        Class c2 = Class.forName(array.get(j)[k].getName());

                        if (!c1.isAssignableFrom(c2)) {
                            // Swap
                            temp = array.get(j);
                            array.set(j, array.get(j + 1));
                            array.set(j + 1, temp);
                        }

                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            return sortArray2(array, objects);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
