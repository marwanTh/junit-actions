package com.pixelogicmedia.delivery;
import com.pixelogicmedia.delivery.api.v1.ConnectionsControllerTests;
import com.pixelogicmedia.delivery.api.v1.ContactsControllerTests;
import com.pixelogicmedia.delivery.api.v1.ProfileControllerTests;
import com.pixelogicmedia.delivery.api.v1.StoragesControllerTests;
import org.junit.platform.suite.api.*;
import org.springframework.boot.test.context.SpringBootTest;


@Suite
@SelectClasses({
        ConnectionsControllerTests.class,
        ContactsControllerTests.class,
        StoragesControllerTests.class,
        ProfileControllerTests.class
})
/*
@Suite.SuiteClasses({
        ConnectionsControllerTests.class,
        ContactsControllerTests.class,
        StoragesControllerTests.class,
        ProfileControllerTests.class
})

/*
@Suite
//@SelectPackages({
//        "com.github.noconnor.junitperf.examples.existing"
//})
@SelectClasses({
        TestClassOne.class,
        TestClassTwo.class
})
// ConfigurationParameter: Required to enable Test Suite Interceptor Reference: https://www.baeldung.com/junit-5-extensions#1-automatic-extension-registration
@ConfigurationParameter(key = "junit.jupiter.extensions.autodetection.enabled", value = "true")

 */
@ConfigurationParameter(key = "junit.jupiter.extensions.autodetection.enabled", value = "true")

@SpringBootTest(classes = { ConnectionsControllerTests.class,
        ContactsControllerTests.class,
        StoragesControllerTests.class,
        ProfileControllerTests.class})

public class RegressionSuite {
}