package ist.meic.pa.GenericFunctionsExtended;

import ist.meic.pa.GenericFunctions.AfterMethod;
import ist.meic.pa.GenericFunctions.BeforeMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Dispatcher {

    private static Map<Integer, List<List<Method>>> cache = new HashMap<>();

    private enum Type {
        BEFORE,
        MAIN,
        AFTER
    }

    public static Object dispatch(Object [] objects, String className) {

        Object toReturn =  null;
        try {
            Class invokableClass = Class.forName(className);

            Combination combination = (Combination)invokableClass.getAnnotation(Combination.class);
            if(combination != null){
                return handleCombinationClass(invokableClass, objects, combination);
            }

            Class[] args = getClassesOfObjects(objects);
            Map.Entry<Object, Boolean> fromCache = verifyCache(args, objects);

            if(fromCache.getValue()) {
                return fromCache.getKey();
            }

            List<Method> methods = getCallableMethods(invokableClass, args);
            List<Method> orderedMethods = sortArray(invokableClass, methods, args);

            if (methods.size() != 0) {
                handleBefore(invokableClass, objects);
                toReturn = handleMainMethods(orderedMethods, args, objects);
                handleAfter(invokableClass, objects);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return toReturn;
    }


    private static Object handleCombinationClass(Class c, Object[] objects, Combination combination){
        CombinationOrder order = combination.order();
        CombinationType type = combination.type();
        Class[] args = getClassesOfObjects(objects);
        List<Method> methods = getCallableMethods(c, args);

        List<Method> orderedMethods = sortArray(c, methods, args);

        if(order.equals(CombinationOrder.LEAST_TO_MOST)){
            Collections.reverse(orderedMethods);
        }
        Object result = doCombinations(type, orderedMethods, objects);
        return result;
    }

    private static Object doCombinations(CombinationType type, List<Method> orderedMethods, Object[] objects){
        List<Object> results = invokeMethods(orderedMethods, objects);

        /*System.out.println("[DO COMBINATIONS]");

        for(int i = 0;i<orderedMethods.size();i++){
            System.out.print("Method: ");
            for(Class p : orderedMethods.get(i).getParameterTypes()){
                System.out.print(p.getName());
            }
            System.out.println();
            System.out.println("Result: " + results.get(i));
        }*/

        switch(type){
            case AND:
                return handleANDCombination(results);
            case OR:
                return handleORCombination(results);
            case MAX:
                return handleMAXCombination(results);
            case MIN:
                return handleMINCombination(results);
            case LIST:
                return handleLISTCombination(results);
            case PLUS:
                return handlePLUSCombination(results);
            case NCONC:
                return handleNCONCCombination(results); //FIXME see method
            case PROGN:
                return handlePROGNCombination(results); //Return last element
            case APPEND:
                return handleAPPENDCombination(results); //FIXME see method
        }
        return null;
    }

    private static Object handleNCONCCombination(List<Object> results){
        // FIXME I think NCONC and APPEND ARE THE SAME
        // Do not think it makes sense in implementing it in Java
        return null;
    }

    private static Object handleAPPENDCombination(List<Object> results){
        // FIXME I think NCONC and APPEND ARE THE SAME
        // Do not think it makes sense in implementing it in Java
        return null;
    }

    private static Object handlePROGNCombination(List<Object> results){
        return results.get(results.size()-1);
    }

    private static Object handleLISTCombination(List<Object> results){
        return Arrays.toString(results.toArray());
    }

    private static Object handlePLUSCombination(List<Object> results){
        Integer result = 0;
        for(Object r: results){
            result += (Integer)r;
        }
        return result;
    }

    private static Object handleMAXCombination(List<Object> results){
        Integer max = 0;
        for(Object r: results){
           if((Integer)r > max){
                max = (Integer)r;
           }
        }
        return max;
    }

    private static Object handleMINCombination(List<Object> results){
        Integer min = Integer.MAX_VALUE;
        for(Object r: results){
            if((Integer)r < min){
                min = (Integer)r;
            }
        }
        return min;
    }

    private static Object handleANDCombination(List<Object> results){
        for(int i = 1; i<results.size()-1;i++){
            if(results.get(i) != results.get(i+1))
            {
                return false;
            }
        }
        return true;
    }

    private static Object handleORCombination(List<Object> results){
        for(int i = 0; i<results.size();i++){
            if(results.get(i).equals(true))
            {
                return true;
            }
        }
        return false;
    }

    private static List<Object> invokeMethods(List<Method> orderedMethods, Object[] objects){
        List<Object> toReturn = new ArrayList<>();
        for(Method m: orderedMethods){
            m.setAccessible(true);
            try {
                toReturn.add(m.invoke(null, objects));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
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

    private static Object handleMainMethods(List<Method> orderedParams, Class[] arguments, Object[] objects)  {
        return callMethod(orderedParams, arguments, objects, Type.MAIN);
    }

    private static void handleBefore(Class c, Object [] objects) {
        Class[] arguments = getClassesOfObjects(objects);
        List<Method> methods = getAnnotatedCallableMethods(c, arguments, BeforeMethod.class);
        List<Method> orderedParams = sortArray(c, methods, arguments);
        callMethods(orderedParams, arguments, objects, Type.BEFORE);
    }

    private static void handleAfter(Class c, Object [] objects) {
        Class[] arguments = getClassesOfObjects(objects);
        List<Method> methods = getAnnotatedCallableMethods(c, arguments, AfterMethod.class);
        List<Method> orderedParams = sortArray(c, methods, arguments);
        Collections.reverse(orderedParams);
        callMethods(orderedParams, arguments, objects, Type.AFTER);
    }
    private static void initCache(Class[] args) {
        if (cache.get(Arrays.hashCode(args)) == null) {
            List<List<Method>> m = new ArrayList<>();
            m.add(new ArrayList<>());
            m.add(new ArrayList<>());
            m.add(new ArrayList<>());
            cache.put(Arrays.hashCode(args), m);
        }
    }

    private static Object callFromCache(List<Method> methods, Object[] objects) {
        Object toReturn = null;
        for(Method method : methods) {
            try {
                //method.setAccessible(true);
                toReturn = method.invoke(null, objects);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return toReturn;
    }

    private static Map.Entry<Object, Boolean> verifyCache(Class[] args, Object[] objects) {

        if(cache.get(Arrays.hashCode(args)) == null) {
            return new AbstractMap.SimpleEntry<>(null, false);
        }

        List<List<Method>> m = cache.get(Arrays.hashCode(args));

        callFromCache(m.get(0), objects);
        Object toReturn = callFromCache(m.get(1), objects);
        callFromCache(m.get(2), objects);

        return new AbstractMap.SimpleEntry<>(toReturn, true);
    }

    private static void addToCache(Class[] args, List<Method> called, Type type) {
        switch (type) {
            case BEFORE:
                initCache(args);
                cache.get(Arrays.hashCode(args)).get(0).addAll(called);
                return;
            case MAIN:
                initCache(args);
                cache.get(Arrays.hashCode(args)).get(1).addAll(called);
                return;
            case AFTER:
                initCache(args);
                cache.get(Arrays.hashCode(args)).get(2).addAll(called);
                return;
        }
    }

    private static void callMethods(List<Method> methods, Class[] arguments, Object [] objects, Type type)  {
        for(Method method : methods) {
            try {
                method.invoke(null, objects);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        addToCache(arguments, methods, type);
    }

    private static Object callMethod(List<Method> methods, Class[] arguments, Object [] objects, Type type){
        try {
            ArrayList<Method> methods1 = new ArrayList<>();
            methods1.add(methods.get(0));
            addToCache(arguments, methods1, type);
            return methods.get(0).invoke(null, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
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

    private static List<Class[]> sortArray2(List<Class[]> array, Class[] objects) {
        int n = array.size();

        Class [] temp = null;

        try {
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return array;
    }

    private static List<Method> getMethods(Class klass, List<Class[]> toGetMethods) {
        List<Method> toReturn = new ArrayList<>();
        for (Class[] args : toGetMethods) {
            try {
                Method method = klass.getDeclaredMethod(klass.getDeclaredMethods()[0].getName(), args);
                method.setAccessible(true);
                toReturn.add(method);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return toReturn;
    }

    private static List<Method> sortArray(Class klass, List<Method> methods, Class[] objects) {

        if(methods.size() == 0) {
            return methods;
        }

        List<Class[]> array = getParametersArray(methods);
        int n = array.size();
        Class[] temp = null;

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

        return getMethods(klass, sortArray2(array, objects));
    }
}
