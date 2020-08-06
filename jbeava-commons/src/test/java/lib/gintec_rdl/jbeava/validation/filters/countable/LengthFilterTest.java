package lib.gintec_rdl.jbeava.validation.filters.countable;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class LengthFilterTest extends TestCase {

    public void testFilter() {
        LengthFilter filter;

        filter = new LengthFilter();
        test("Hello World", filter, 11);
        test(new long[]{1, 2, 3}, filter, 3);
        test(new ArrayList<Integer>() {{
            add(1);
        }}, filter, 1);
    }

    private void test(Object o, LengthFilter filter, int count) {
        filter.filter("test", null, o, List.of("1", "11"));
    }
}