package org.graylog.plugins.nsq;

import org.apache.log4j.Logger;
import org.junit.Test;

public class VerifyLog4jAvailabilityTest {

    @Test
    public void log4jAvailable(){
        Logger.getLogger(VerifyLog4jAvailabilityTest.class).info("Hello World");
    }
}
