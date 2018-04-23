gradle compileJava build

javac -cp build/libs/genericFunctions.jar:. pa/tests/TestA.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestB.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestC.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestD.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestE.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestF.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestG.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestH.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestI.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestJ.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestK.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestL.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestM.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestN.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestO.java
javac -cp build/libs/genericFunctions.jar:. pa/tests/TestP.java

java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestA > pa/a.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestB > pa/b.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestC > pa/c.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestD > pa/d.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestE > pa/e.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestF > pa/f.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestG > pa/g.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestH > pa/h.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestI > pa/i.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestJ > pa/j.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestK > pa/k.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestL > pa/l.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestM > pa/m.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestN > pa/n.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestO > pa/o.ourout
java -cp ./build/libs/genericFunctions.jar:./javassist.jar:. ist.meic.pa.GenericFunctionsExtended.WithGenericFunctions pa.tests.TestP > pa/p.ourout

diff --strip-trailing-cr pa/a.ourout pa/A.out
diff --strip-trailing-cr pa/b.ourout pa/B.out
diff --strip-trailing-cr pa/c.ourout pa/C.out
diff --strip-trailing-cr pa/d.ourout pa/D.out
diff --strip-trailing-cr pa/e.ourout pa/E.out
diff --strip-trailing-cr pa/f.ourout pa/F.out
diff --strip-trailing-cr pa/g.ourout pa/G.out
diff --strip-trailing-cr pa/h.ourout pa/H.out
diff --strip-trailing-cr pa/i.ourout pa/I.out
diff --strip-trailing-cr pa/j.ourout pa/J.out
diff --strip-trailing-cr pa/k.ourout pa/K.out
diff --strip-trailing-cr pa/l.ourout pa/L.out
diff --strip-trailing-cr pa/m.ourout pa/M.out
diff --strip-trailing-cr pa/n.ourout pa/N.out
diff --strip-trailing-cr pa/o.ourout pa/O.out
diff --strip-trailing-cr pa/p.ourout pa/P.out