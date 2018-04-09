package ist.meic.pa.GenericFunctions;

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
        System.out.println("[Dispatch]");
        System.out.println("--Objects");
        Arrays.asList(objects).forEach(x-> System.out.println(x));
        System.out.println("--Class");
        System.out.println(className);
        System.out.println("--FuncName");
        System.out.println(funcName);
        System.out.println("--Arguments");
        Arrays.asList(arguments).forEach(x-> System.out.println(x));

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
        System.out.println("TNHnh");
        Arrays.asList(classes).forEach(x-> System.out.println(x.getName()));

        try {
            Class invokableClass = Class.forName(className);
            try {
                Method method = invokableClass.getDeclaredMethod(funcName, classes);
                return method.invoke(null, objects);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
