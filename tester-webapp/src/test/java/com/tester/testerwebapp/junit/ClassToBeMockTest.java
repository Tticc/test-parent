package com.tester.testerwebapp.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

// 需要使用@RunWith(PowerMockRunner.class)
@RunWith(PowerMockRunner.class)
// 以及@PrepareForTest(ClassToBeMock.MyClass.class)。ClassToBeMock为被测试类
@PrepareForTest({ClassToBeMock.class, ClassToBeMock.MyClass.class})
public class ClassToBeMockTest {
    @Mock
    ClassToBeMock.Helper mockHelper;
    @InjectMocks
    ClassToBeMock.MyClass myClass;

    @Test
    public void testDoSomething() throws Exception {
        // Mock 内部方法创建队形
        PowerMockito.whenNew(ClassToBeMock.Helper.class).withAnyArguments().thenReturn(mockHelper);
        Mockito.when(mockHelper.help()).thenReturn("mocked helping");

        // mock 私有方法
        ClassToBeMock.MyClass spy = PowerMockito.spy(myClass);
        PowerMockito.doNothing().when(spy, "privateMethod", ArgumentMatchers.any(), ArgumentMatchers.any());
        // 如果非void方法
         PowerMockito.doReturn("aaa").when(spy, "privateMethodReturn", ArgumentMatchers.any());
//         PowerMockito.when(spy, "privateMethodReturn", ArgumentMatchers.any()).thenReturn("aaa");

        // 调用方法并断言结果
        String result = spy.doSomething();
        System.out.println("result = " + result);
    }
}

