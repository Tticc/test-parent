
public class LambdaTest {
    // 0.打开cmd
    // 1.javac LambdaTest.java
    // 2.javap -p -v -c LambdaTest.class
    // 3.java -Djdk.internal.lambda.dumpProxyClasses LambdaTest
    public void printString(String s, Consumer<String> print) {
        print.accept(s);
    }

    public void callPrint(){
        printString("test", (x)->System.out.println(x));
    }

    public static void main(String[] args){}

}

@FunctionalInterface
interface Consumer<T> {
    void accept(T x);
}