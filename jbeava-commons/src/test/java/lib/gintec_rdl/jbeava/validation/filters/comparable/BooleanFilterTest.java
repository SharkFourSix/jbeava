package lib.gintec_rdl.jbeava.validation.filters.comparable;

import junit.framework.TestCase;

import java.util.ArrayList;

public class BooleanFilterTest extends TestCase {

    public void testFilter() {
        BooleanFilter filter;

        filter = new BooleanFilter();
        assertTrue(filter.filter("test", "Not valid boolean","true", new ArrayList<>()));
    }
}