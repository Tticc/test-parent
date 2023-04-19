package com.tester.testerwebapp;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibTest {
	public static void main(String[] args) {
		CglibTest test = getProxy();
		test.bePpri();
	}

	public static CglibTest getProxy() {
		//System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "c://tmp");//将动态代理生成的.class文件保存到路径
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(CglibTest.class);
		enhancer.setCallback(new MethodIntec());
		return (CglibTest)enhancer.create();
	}
	public static void pri() {
		System.out.println("this is the Test.pri");
	}
	public void bePpri() {

		System.out.println("this is the Test.beP");
		pri();
	}
}
class MethodIntec implements MethodInterceptor {
	CglibTest target = new CglibTest();
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object result;
		if(method.getName().indexOf("beP")!=-1) {
			System.out.println("//you never said this");
		}
		// 1. 使用反射 + target
//		result = method.invoke(target,args);
		// 2. 使用methodProxy.invoke + target
//		result = proxy.invoke(target,args);
		// 3. 使用methodProxy.invokeSuper + target
		result = proxy.invokeSuper(obj, args);
		//do something
		return result;
	}
	
}
