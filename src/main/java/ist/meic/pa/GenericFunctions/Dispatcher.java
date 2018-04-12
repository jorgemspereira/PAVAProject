package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dispatcher {

    private static List<Class[]> sortedBefore = new ArrayList<>();
    private static List<Class[]> sortedAfter = new ArrayList<>();
    private static List<Class[]> sortedMain = new ArrayList<>();

    public static Object dispatch(Object [] objects, String className) {

        Object toReturn =  null;

        try {
            Class invokableClass = Class.forName(className);
            handleAnnotations(invokableClass, objects, 0);
            toReturn = handleMainMethods(invokableClass, objects);
            handleAnnotations(invokableClass, objects, 1);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    private static Object handleMainMethods(Class invokableClass, Object[] objects) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        Class[] arguments = getClassesOfObjects(objects);
        List<Class[]> classArray;

        if (sortedMain.size() == 0) {
            classArray = getParametersArray(invokableClass);
            sortedMain = classArray;
        } else {
            classArray = sortedMain;
        }

        Object toReturn = null;

        for(Class [] c : classArray) {

            Method method = invokableClass.getDeclaredMethod(invokableClass.getDeclaredMethods()[0].getName(), c);
            method.setAccessible(true);
            if(method.getAnnotation(BeforeMethod.class) == null && method.getAnnotation(AfterMethod.class) == null) {
                if(verifyCallable(c, arguments)) {
                    toReturn = method.invoke(null, objects);
                    break;
                }
            }
        }

        return toReturn;
    }

    private static Class[] getClassesOfObjects(Object[] objs) {
        List<Class> classes = new ArrayList<>();
        for(Object o : objs) {
            classes.add(o.getClass());
        }
        Class[] toReturn = new Class[classes.size()];
        return classes.toArray(toReturn);
    }

    private static boolean verifyCallable(Class[] method, Class[] args) {
        for(int i = 0; i < method.length; i++) {
            if(!method[i].isAssignableFrom(args[i])) {
                return false;
            }
        }
        return true;
    }

    private static List<Class[]> getAnnotatedMethods(Class c, Class annotation) {
        ArrayList<Class[]> methodsParams = new ArrayList<>();
        for(Method method : c.getDeclaredMethods()) {
            if(method.getAnnotation(annotation) != null) {
                Class [] parameters = method.getParameterTypes();
                methodsParams.add(parameters);
            }
        }
        return methodsParams;
    }

    private static void handleAnnotations(Class c, Object [] objects, int type)  {
        if(c.getAnnotation(GenericFunction.class) != null) {

            Class annotation = (type == 0) ? BeforeMethod.class: AfterMethod.class;
            List<Class[]> methodsParams = getAnnotatedMethods(c, annotation);
            List<Class[]> orderedParams;

            if(type==1) {
                if (sortedBefore.size() == 0) {
                    orderedParams = sortArray(methodsParams);
                    Collections.reverse(orderedParams);
                    sortedBefore = orderedParams;
                } else {
                    orderedParams = sortedBefore;
                }
            } else {
                if (sortedAfter.size() == 0) {
                    orderedParams = sortArray(methodsParams);
                    sortedAfter = orderedParams;
                } else {
                    orderedParams = sortedAfter;
                }
            }

            Class[] arguments = getClassesOfObjects(objects);

            for(Class[] params : orderedParams)
            {
                try {
                    Method method = c.getDeclaredMethod(c.getDeclaredMethods()[0].getName(), params);
                    //FIXME Methods from super class
                    if(verifyCallable(params, arguments)) {
                        method.setAccessible(true);
                        method.invoke(null, objects);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static List<Class[]> getParametersArray(Class c)
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

    private static List<Class[]> sortArray(List<Class[]> array) {
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

                        if (!c1.getName().equals(c2.getName())) {
                            if (!c1.isAssignableFrom(c2)) {
                                // Swap
                                temp = array.get(j);
                                array.set(j, array.get(j + 1));
                                array.set(j + 1, temp);
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return array;
    }
}
