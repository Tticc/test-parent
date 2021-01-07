package com.tester.testercommon;

import com.tester.testercommon.util.file.MyFileReaderWriter;
import com.tester.testerstarter.util.Demo;
import org.junit.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @Author 温昌营
 * @Date
 */
public class SPITest {

    private static final Map<ClassLoader, MultiValueMap<String, String>> cache = new ConcurrentReferenceHashMap<>();

    @Test
    public void test_loader() throws IOException {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        Enumeration<URL> urls =
                ClassLoader.getSystemResources("META-INF/spring.factories");
        int count = 0;
        while (urls.hasMoreElements()) {
            if(count ++ >= 3){
                break;
            }
            URL url = urls.nextElement();
            System.out.println(url.getPath());
            UrlResource resource = new UrlResource(url);
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                String factoryClassName = ((String) entry.getKey()).trim();
                for (String factoryName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
                    result.add(factoryClassName, factoryName.trim());
                }
            }
        }
        System.out.println();
        System.out.println();
        System.out.println();
        List<String> strings = result.get("org.springframework.boot.autoconfigure.EnableAutoConfiguration");
        System.out.println();
    }

    @Test
    public void test_springboot_spi() throws Exception {
        String file = "/META-INF/my.spring.factories";
        URL resource = this.getClass().getResource(file);
        String path = resource.getPath();
        byte[] bytes = MyFileReaderWriter.file2Byte(new File(path));
        String s = new String(bytes, "UTF-8");
        String[] split = s.split("=\\\\\r\n");
        System.out.println("split size:"+split.length);

        ClassLoader classLoader = this.getClass().getClassLoader();
        Class<?> aClass = classLoader.loadClass(split[1].trim());
        System.out.println(split[1].trim());

        ServiceLoader<?> load = ServiceLoader.load(aClass);
        Iterator<?> iterator = load.iterator();
        while (iterator.hasNext()) {
            Object ser = iterator.next();
            Demo demo = castT(ser, Demo.class);
            System.out.println(demo.getMyName());
        }
//
//        Object o = aClass.newInstance();
//        if(aClass.isAssignableFrom(SpringBeanContextUtil.class)){
//            SpringBeanContextUtil aa = (SpringBeanContextUtil)o;
//            String s1 = aa.toString();
//            System.out.println(s1);
//        }
    }

    private <T> T castT(Object ser,Class<T> clazz){
        T cast = clazz.cast(ser);
        return cast;
    }
}
