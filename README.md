# Advanced Programming - First Project - Group 04

To execute the project you will need JDK8, since we are having some problems when run it with JDK9. If you have gradle installed you only need to run:

```sh
$ gradle run -PappArgs="['MainClass']"
```
Where main class, is the class where the main method is declared, for example:
```sh
$ gradle run -PappArgs="['Test.TestCacheMain']"
```

If you don't like gradle, you can always run it using:
```sh
$ java -cp ./genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctions.WithGenericFunctions MainClass
```

## Extensions

Our extensions consists in:
1. Caching of effective methods;
2. Other methods combination (like: LIST, AND, OR...);
3. We also cache the result of the other methods combination.

We use the *@GenericFunction*, *@After* and *@Before* annotations of the non extended version. So if you want to run tests maded $CORRIGE GODINHO$ for the non extended version in the extended version, you **don't have to import** a different annotation.

To run the extended version of the project, with gradle, you have to change the main class on **build.gradle** from/to:
```json
mainClassName = 'ist.meic.pa.GenericFunctions.WithGenericFunctions'
```
```json
mainClassName = 'ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions'
```

To run it with java you only need the command bellow (instead of the previous command):
```sh
$ java -cp ./genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions MainClass
```

## Tests
We have some tests for the extensions in the package *"Tests"*. For example, you can run the *Test.TestCacheMain* with the extended version and with the non extended version and compare the execution time. You can also check how to use other method combinations with for example the *Test.TestCombinationMAXMain* test.
