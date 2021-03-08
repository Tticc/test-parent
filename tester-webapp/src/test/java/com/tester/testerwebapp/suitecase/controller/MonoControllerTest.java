package com.tester.testerwebapp.suitecase.controller;

import com.tester.testerwebapp.TesterWebappApplication;
import com.tester.testerwebapp.service.UserManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TesterWebappApplication.class)
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class MonoControllerTest {

    @Autowired
    private UserManager userManager;
    @Test
    public void test_userManager(){
        userManager.selectUserById(1L).subscribe(e -> System.out.println(e));
    }
    @Test
    public void test(){
        System.out.println("test1");
    }
}
