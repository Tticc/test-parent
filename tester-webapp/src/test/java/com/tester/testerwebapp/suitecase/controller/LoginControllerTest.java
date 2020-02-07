package com.tester.testerwebapp.suitecase.controller;

import com.tester.testercommon.controller.RestResult;
import com.tester.testerwebapp.TesterWebappApplication;
import com.tester.testerwebapp.controller.mono.UserController;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.mapper.UserMapper;
import com.tester.testerwebapp.dao.service.UserManager;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = TesterWebappApplication.class)
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class LoginControllerTest extends TestCase {

    @InjectMocks
    private UserController userController;
    @InjectMocks
    @Spy
    private UserManager userManager;
    @Spy
    private UserMapper userMapper;

    @Test
    public void test_mock_injectMock(){
        Mockito.when(userMapper.selectUserById(Mockito.anyLong())).thenReturn(new UserDomain());
//        Mockito.when(userManager.selectUserById(Mockito.anyLong())).thenCallRealMethod();
//        Mockito.when(userManager.selectUserById(Mockito.anyLong())).thenReturn(Mono.just(new UserDomain()));
        Mono<RestResult<UserDomain>> name = userController.demoStart(5L, "name");
        name.subscribe(e -> System.out.println(e.getData()));
    }

    @Test
    public void test(){
        System.out.println("jiofs");
    }

}
