package com.tester.testerwebapp.service;

import com.tester.testerwebapp.TesterWebappApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterWebappApplication.class)
@Slf4j
public class ResourceServiceTest {

    @Value("${file.path:file:C:\\Users\\Admin\\Desktop\\DxDiag.txt}")
    private Resource resource;

    @Test
    public void test_read() {
        StringBuilder result = new StringBuilder();
        boolean exists = resource.exists();
        System.out.println(exists);
        try (InputStreamReader br = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            BufferedReader brr = new BufferedReader(br);
            String s;
            while ((s = brr.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(s.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }


    @Test
    public void test_read2() {
        StringBuilder result = new StringBuilder();
        boolean exists = resource.exists();
        System.out.println(exists);
        try (InputStreamReader br = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            char[] cs = new char[1024];
            while (br.read(cs) != -1) {
                result.append(cs).append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}
