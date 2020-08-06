package lib.gintec_rdl.jbeava.validation.filters.temporal;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.List;

public class TemporalConstraintFilterTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    public void testFilter() {
        LocalDate date;
        LocalTime time;
        LocalDateTime localDateTime;

        date = LocalDate.now();
        time = LocalTime.now();
        localDateTime = LocalDateTime.now().plusDays(1);

        test("date", date, new PresentConstraintFilter());
        test("time", time, new PastConstraintFilter());
        test("Date time", localDateTime, new FutureOrPresentConstraintFilter());
    }

    private void test(String name, Temporal temporal, TemporalConstraintFilter filter) {
        filter.filter(name, null, temporal, List.of());
    }
}