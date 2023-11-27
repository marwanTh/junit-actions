package com.pixelogicmedia.delivery;
import com.pixelogicmedia.delivery.api.v1.ConnectionsControllerTests;
import com.pixelogicmedia.delivery.api.v1.ContactsControllerTests;
import com.pixelogicmedia.delivery.api.v1.ProfileControllerTests;
import com.pixelogicmedia.delivery.api.v1.StoragesControllerTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        ConnectionsControllerTests.class,
        ContactsControllerTests.class,
        StoragesControllerTests.class,
        ProfileControllerTests.class
})

public class RegressionSuite {
}