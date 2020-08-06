package lib.gintec_rdl.jbeava.validation.filters.text;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;

import java.util.ArrayList;

public class PatternFilterTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    public void testFilter() {
        String code;
        String result;
        String pattern;
        PatternFilter filter;
        ArrayList<String> args;

        code = "9045421";
        pattern = "^[A-Z0-9]{7}$";
        args = new ArrayList<>() {{
            add(pattern);
        }};
        filter = new PatternFilter();
        result = filter.filter("Student ID", "${name} (${value}) is not a properly formatted ID.",
                code, args);
        assertEquals(code, result);
    }
}