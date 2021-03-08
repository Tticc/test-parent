package com.tester.testercommon.util.classscanner;

import com.tester.testercommon.dao.BaseDomain;
import com.tester.testercommon.model.request.IdAndNameModel;
import com.tester.testercommon.model.request.TextRequest;
import com.tester.testercommon.model.request.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author 温昌营
 * @Date 2021-3-8 10:09:47
 */
public class InstanceUtil {
    public static void main(String[] args) {
        String path = BaseDomain.class.getName();
        String prefix = path.substring(0, path.lastIndexOf(".") + 1);
        String className = "BaseDomain";
        BaseDomain newi = getBeanByFullPath(prefix + className);
        System.out.println(newi);
    }

    /**
     * 根据类全路径获取类实例，用于不知道类名且需要实例化，但可以根据某些条件拼接出来，且又有父类
     * <br/>T 可以为base domain
     * @param classFullPath
     * @return T
     * @Date 10:19 2021/3/8
     * @Author 温昌营
     **/
    public static <T> T getBeanByFullPath(String classFullPath){
        // classFullPath = com.tester.testercommon.util.classscanner.InstanceUtil
        HashSet<String> objects = new HashSet<>();
        objects.add(classFullPath);
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        List<Object> springFactoriesInstances = createSpringFactoriesInstances(Object.class, new Class[]{}, defaultClassLoader, new Object[]{}, objects);
        return (T)springFactoriesInstances.get(0);
    }




    /**
     * copy from spring
     * <br/>
     * org.springframework.boot.SpringApplication#createSpringFactoriesInstances(java.lang.Class, java.lang.Class[], java.lang.ClassLoader, java.lang.Object[], java.util.Set)
     * @Date 14:04 2021/2/8
     * @Author 温昌营
     **/
    private static <T> List<T> createSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes,
                                                              ClassLoader classLoader, Object[] args, Set<String> names) {
        List<T> instances = new ArrayList<>(names.size());
        for (String name : names) {
            try {
                Class<?> instanceClass = ClassUtils.forName(name, classLoader);
                Assert.isAssignable(type, instanceClass);
                Constructor<?> constructor = instanceClass.getDeclaredConstructor(parameterTypes);
                T instance = (T) BeanUtils.instantiateClass(constructor, args);
                instances.add(instance);
            }
            catch (Throwable ex) {
                throw new IllegalArgumentException("Cannot instantiate " + type + " : " + name, ex);
            }
        }
        return instances;
    }
}
