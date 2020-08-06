package lib.gintec_rdl.jbeava.validation.filters.text;

import junit.framework.TestCase;

import java.util.List;

public class CaseTransformationFilterTest extends TestCase {

    public void testFilter() {
        CaseTransformationFilter filter;

        test("Hello", "hello", new UCFirstFilter());
        test("HELLO", "HeLlo", new UppercaseFilter());
        test("hello", "hElLo", new LowercaseFilter());
        test("Hello World!", "heLlO wORlD!", new UCStringFilter());
    }

    private void test(String expected, String input, CaseTransformationFilter filter) {
        assertEquals("Results do not match", expected,
                filter.filter("test_value", null, input, List.of()));
    }
}