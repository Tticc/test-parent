package com.tester.testercv.office;

import com.tester.base.dto.dao.BaseDomain;
import com.tester.testercommon.util.SpringBeanContextUtil;
import com.tester.testercommon.util.file.MyFileReaderWriter;
import com.tester.testermybatis.dao.domain.DemoDomain;
import org.junit.Test;
import sun.misc.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * @Author 温昌营
 * @Date
 */
public class OfficeTest {
    public static void main(String[] args) {
        Iterator<SpringBeanContextUtil> providers = Service.providers(SpringBeanContextUtil.class);
        ServiceLoader<SpringBeanContextUtil> load = ServiceLoader.load(SpringBeanContextUtil.class);

        while (providers.hasNext()) {
            SpringBeanContextUtil ser = providers.next();
            System.out.println(ser);
        }
        System.out.println("--------------------------------");
        Iterator<SpringBeanContextUtil> iterator = load.iterator();
        while (iterator.hasNext()) {
            SpringBeanContextUtil ser = iterator.next();
            System.out.println(ser);
        }
    }


    @Test
    public void test_springboot_spi() throws UnsupportedEncodingException {
        String file = "/META-INF/my.spring.factories";
        byte[] bytes = MyFileReaderWriter.file2Byte(new File(file));
        String s = new String(bytes, "UTF-8");
        System.out.println(s);

    }


    @Test
    public void test_con() {
        newCon();
    }

    private void newCon(Supplier... args) {
        for (Supplier arg : args) {
            System.out.println(arg);
        }

    }

    @Test
    public void test_buildImg() throws Exception {
        Integer i = 3;
        boolean b = i instanceof Integer;
        boolean assignableFrom = Integer.class.isAssignableFrom(i.getClass());

        System.out.println("b:" + b + ",ass:" + assignableFrom);


        Object domain = getDomain();
        Object baseDomain = getBaseDomain();

        boolean b1 = domain instanceof BaseDomain;
        boolean assignableFrom1 = BaseDomain.class.isAssignableFrom(domain.getClass());

        System.out.println("b1:" + b + ",ass1:" + assignableFrom1);


    }

    private Object getDomain() {
        DemoDomain demoDomain = new DemoDomain();
        return demoDomain;
    }

    private Object getBaseDomain() {
        return new BaseDomain();
    }
}
