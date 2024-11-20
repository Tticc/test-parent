package com.tester.testerwebapp.junit;

import lombok.SneakyThrows;


public class ClassToBeMock {
    // 需要在 MyClass 中添加一个 protected 或 public 方法用于创建 Helper
    public static class MyClass {
        public String doSomething() {
            this.privateMethod("doSomething call private method", "hb");
            Helper helper = new Helper("MyClass.helping");
            return helper.help();
        }

        @SneakyThrows
        private void privateMethod(String here, String hb) {
            System.out.println(here);
            throw new Exception();
        }

        @SneakyThrows
        private String privateMethodReturn(String here) {
            System.out.println(here);
            if("ojida".equals(here)) {
                throw new Exception();
            }
            return here;
        }
    }

    public static class Helper {
        private String message;
        public Helper(){
            message = "helping";
        }
        public Helper(String he1){
            message = he1;
        }
        public String help() {
            return message;
        }
    }
}
