package com.tester.testerwebapp.suitecase;

import com.tester.testerwebapp.suitecase.controller.LoginControllerTest;
import com.tester.testerwebapp.suitecase.controller.MonoControllerTest;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LoginControllerTest.class,
        MonoControllerTest.class
})
public class TestBooter extends TestSuite {
}


