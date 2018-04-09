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

    public void a(Object b, Object c)
    {
        System.out.println("aaaa");
        System.out.println(b.getClass().getName());
        System.out.println(c.getClass().getName());
    }
    public Object dispatch(Object [] objects, String className, String funcName, String [] arguments)
    {
        /*System.out.println("[Dispatch]");
        System.out.println("--Objects");
        Arrays.asList(objects).forEach(x-> System.out.println(x));
        System.out.println("--Class");
        System.out.println(className);
        System.out.println("--FuncName");
        System.out.println(funcName);
        System.out.println("--Arguments");
        Arrays.asList(arguments).forEach(x-> System.out.println(x));*/

        ArrayList<Class> argsClasses = new ArrayList<>();
        for(Object obj : objects)
        {
            try {
                argsClasses.add(Class.forName(obj.getClass().getName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        Class [] classes = new Class[argsClasses.size()];
        classes = argsClasses.toArray(classes);
        System.out.println("[Classes]");
        //Arrays.asList(classes).forEach(x-> System.out.println(x.getName()));


        try {
            Class invokableClass = Class.forName(className);
            ArrayList<Class[]> classArray = getParametersArray(invokableClass);

            for(Class [] c : classArray) {
                try {

                    Method method = invokableClass.getDeclaredMethod(funcName+"$original", c);

                    System.out.println("INVOKING");
                    //Arrays.asList(method.getParameterTypes()).forEach(x-> System.out.println(x.getName()));
                    method.setAccessible(true);
                    return method.invoke(null, objects);
                } catch (IllegalArgumentException|NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    //e.printStackTrace();
                    continue;
                }
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Class[]> getParametersArray(Class c)
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

    public ArrayList<Class[]> sortArray(ArrayList<Class[]> array) throws ClassNotFoundException {
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
