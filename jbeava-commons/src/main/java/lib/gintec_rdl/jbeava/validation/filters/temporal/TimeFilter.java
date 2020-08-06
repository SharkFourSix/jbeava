package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@PrepackagedFilter("time")
public class TimeFilter extends TemporalFilter<LocalTime> {
    public TimeFilter() {
        super("time");
    }

    @Override
    protected LocalTime parse(String input, String pattern) {
        return LocalTime.parse(input, DateTimeFormatter.ofPattern(pattern));
    }
}
