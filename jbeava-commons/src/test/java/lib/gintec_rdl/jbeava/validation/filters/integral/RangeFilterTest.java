package lib.gintec_rdl.jbeava.validation.filters.integral;

import junit.framework.TestCase;

import java.util.ArrayList;

public class RangeFilterTest extends TestCase {

    public void testFilter() {
        ArrayList<String> args;
        RangeFilter<Integer> rangeFilter;

        args = new ArrayList<>() {{
            add("40");
            add("56");
        }};
        rangeFilter = new RangeFilter<>();
        assertEquals("Value outside range", 40,
                (int) rangeFilter.filter("test", null, 40, args));
    }
}