package ist.meic.pa.GenericFunctionsExtended;

import ist.meic.pa.GenericFunctions.AfterMethod;
import ist.meic.pa.GenericFunctions.BeforeMethod;
import ist.meic.pa.GenericFunctions.Dispatcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

// Dispatcher used on the extended version, inheriting from non-extended Dispatcher
public class DispatcherExtended extends Dispatcher {

    // Caching of effective methods
    private static Map<Integer, List<List<Method>>> cache = new HashMap<>();

    // Types of methods in the cache
    private enum Type {
        BEFORE,
        MAIN,
        AFTER,
        COMBINATION
    }

    public static Object dispatch(Object [] objects, String className) {
        Object toReturn =  null;
        Class invokableClass = getClassFromName(className);
        Class[] args = getClassesOfObjects(objects);

        Combination combination = (Combination)invokableClass.getAnnotation(Combination.class);
        if(combination != null){
            return handleCombinationClass(invokableClass, args, objects, combination);
        }

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

        return toReturn;
    }

    /************************** CACHE RELATED CODE ***********************************/

    // Verification and invocation, if cache contains method
    private static Map.Entry<Object, Boolean> verifyCache(Class[] args, Object[] objects) {
        List<List<Method>> m = cache.get(Arrays.hashCode(args));
        if(m == null) {
            return new AbstractMap.SimpleEntry<>(null, false);
        }

        callFromCache(m.get(0), objects);
        Object toReturn = callFromCache(m.get(1), objects);
        callFromCache(m.get(2), objects);

        return new AbstractMap.SimpleEntry<>(toReturn, true);
    }

    private static Map.Entry<List<Method>, Boolean> verifyCacheForCombinations(Class[] args) {
        List<List<Method>> m = cache.get(Arrays.hashCode(args));
        if(m == null) {
            return new AbstractMap.SimpleEntry<>(null, false);
        }
        return new AbstractMap.SimpleEntry<>(m.get(3), true);
    }

    private static Object callFromCache(List<Method> methods, Object[] objects) {
        Object toReturn = null;
        for(Method method : methods) {
            try {
                toReturn = method.invoke(null, objects);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return toReturn;
    }

    // Initialization of cache, with an ArrayList for each type (Before, Main, After, Combination)
    private static void initCache(Class[] args) {
        if (cache.get(Arrays.hashCode(args)) == null) {
            List<List<Method>> m = new ArrayList<>();
            m.add(new ArrayList<>());
            m.add(new ArrayList<>());
            m.add(new ArrayList<>());
            m.add(new ArrayList<>());
            cache.put(Arrays.hashCode(args), m);
        }
    }

    private static void addToCache(Class[] args, List<Method> called, Type type) {
        switch (type) {
            case BEFORE:
                initCache(args);
                cache.get(Arrays.hashCode(args)).get(0).addAll(called);
                break;
            case MAIN:
                initCache(args);
                cache.get(Arrays.hashCode(args)).get(1).addAll(called);
                break;
            case AFTER:
                initCache(args);
                cache.get(Arrays.hashCode(args)).get(2).addAll(called);
                break;
            case COMBINATION:
                initCache(args);
                cache.get(Arrays.hashCode(args)).get(3).addAll(called);
                break;
        }
    }

    /******************** OTHER METHODS COMBINATION CODE *****************************/

    private static Object handleCombinationClass(Class c, Class[] args, Object[] objects, Combination combination){
        CombinationOrder order = combination.order();
        CombinationType type = combination.type();

        Map.Entry<List<Method>, Boolean> fromCache = verifyCacheForCombinations(args);
        List<Method> orderedMethods;

        if(fromCache.getValue()) {
            orderedMethods = fromCache.getKey();
        } else {
            List<Method> methods = getCallableMethods(c, args);
            orderedMethods = sortArray(c, methods, args);
            addToCache(args, orderedMethods, Type.COMBINATION);
        }

        if(order.equals(CombinationOrder.LEAST_TO_MOST)){
            Collections.reverse(orderedMethods);
        }

        return doCombinations(type, orderedMethods, objects);
    }

    private static Object doCombinations(CombinationType type, List<Method> orderedMethods, Object[] objects){
        List<Object> results = invokeMethods(orderedMethods, objects);

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
        }
        return null;
    }

    private static Object handleLISTCombination(List<Object> results){
        return Arrays.toString(results.toArray());
    }

    private static Object handlePLUSCombination(List<Object> results){
        Double result = 0.0;
        for(Object r: results){
            result += ((Number) r).doubleValue();
        }
        return result;
    }

    private static Object handleMAXCombination(List<Object> results){
        Double max = 0.0;
        for(Object r: results){
            if(((Number)r).doubleValue() > max){
                max = ((Number)r).doubleValue();
            }
        }
        return max;
    }

    private static Object handleMINCombination(List<Object> results){
        Double min = Double.MAX_VALUE;
        for(Object r: results){
            if(((Number)r).doubleValue() < min){
                min = ((Number)r).doubleValue();
            }
        }
        return min;
    }

    private static Object handleANDCombination(List<Object> results){
        for (Object result : results) {
            if (result.equals(false)) {
                return false;
            }
        }
        return true;
    }

    private static Object handleORCombination(List<Object> results){
        for (Object result : results) {
            if (result.equals(true)) {
                return true;
            }
        }
        return false;
    }

    private static List<Object> invokeMethods(List<Method> orderedMethods, Object[] objects){
        List<Object> toReturn = new ArrayList<>();
        for(Method m: orderedMethods){
            try {
                m.setAccessible(true);
                toReturn.add(m.invoke(null, objects));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
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

    // Used in invoking before and after methods
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

    // Used in invoking main methods
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
}
