package chessclub.com.icc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ListTests.class,
                /*BoardTest.class,
                CNTEST.class,
                HostTests.class,
                PortTests.class,
                TestLevel2Settings.class,
                UseridTests.class,
                ZShowCommands.class,
                PGNTest.class */})
public class AllTests {

}
