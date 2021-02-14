package com.tester.testercommon.pattern.singleton;

public class DemoSingleton {

    public static void main(String[] args){
        DemoSingleton instance3 = DemoSingleton.getInstance3();
        System.out.println(instance3);
        DemoSingleton instance31 = DemoSingleton.getInstance3();
        System.out.println(instance31);
        DemoSingleton instance32 = DemoSingleton.getInstance3();
        System.out.println(instance32);
        DemoSingleton instance33 = DemoSingleton.getInstance3();
        System.out.println(instance33);
    }

    private static DemoSingleton demoSingleton1;

    private static final DemoSingleton demoSingleton2 = new DemoSingleton();

    private DemoSingleton(){}

    public static synchronized DemoSingleton getInstance1() {
        if(null == demoSingleton1){
            demoSingleton1 = new DemoSingleton();
        }
        return demoSingleton1;
    }

    public static final DemoSingleton getInstance2(){
        return demoSingleton2;
    }

    /**
     * should use
     * @return
     */
    public static final DemoSingleton getInstance3(){
        return SingletonCreator.DEMO_SINGLETON3;
    }

    private static class SingletonCreator{
        private static final DemoSingleton DEMO_SINGLETON3 = new DemoSingleton();
    }
}
