package lib.gintec_rdl.jbeava.validation.filters.temporal;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DateTimeFilterTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    public void testFilter() {
        DateTimeFilter filter;
        LocalDateTime localDateTime;

        filter = new DateTimeFilter();

        localDateTime = filter.filter("test", "${value} is not recognized as valid time", "2020-08-03 01:04", new ArrayList<>() {{
            add("yyyy-MM-dd HH:mm");
        }});
    }
}