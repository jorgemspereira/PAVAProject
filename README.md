# Advanced Programming - First Project - Group 04

To execute the project you will need JDK8. If you have gradle installed you only need to run:

```sh
$ gradle run -PappArgs="['MainClass']"
```
Where *MainClass* is the class containing the declaration of the main method, for example:
```sh
$ gradle run -PappArgs="['Test.TestCacheMain']"
```

If you do not like gradle, you can always run it using:
```sh
$ java -cp ./genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctions.WithGenericFunctions MainClass
```

## Extensions

Our extensions consist of:
1. Caching of effective methods;
2. Other methods combination (LIST, AND, OR, PLUS, MAX, MIN);
3. Caching the result of other methods combination.

We use the *@GenericFunction*, *@After* and *@Before* annotations of the non-extended version. Therefore, if you want to run tests made for the non-extended version in the extended version, you **do not have to import** a different annotation.

To run the extended version of the project, using gradle, you have to change the main class on **build.gradle** from/to:
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
We have some tests for the extensions in the package *"Tests"*. For example, you can run the *Test.TestCacheMain* with the extended version versus the non-extended version and compare the execution time. You can also check how to use other method combinations using. for example, the *Test.TestCombinationMAXMain* test, for the MAX type.
