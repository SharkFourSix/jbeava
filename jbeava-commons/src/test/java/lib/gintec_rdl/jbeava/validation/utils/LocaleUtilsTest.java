package lib.gintec_rdl.jbeava.validation.utils;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;

import java.time.LocalDateTime;

public class LocaleUtilsTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    public void testFormatLocalDateTime() {
        LocalDateTime now;

        now = LocalDateTime.now();
        System.out.println(LocaleUtils.formatLocalDateTime(now));
    }
}