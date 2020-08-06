package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@PrepackagedFilter("datetime")
public class DateTimeFilter extends TemporalFilter<LocalDateTime> {

    public DateTimeFilter() {
        super("datetime");
    }

    @Override
    protected LocalDateTime parse(String input, String pattern) {
        return LocalDateTime.parse(input, DateTimeFormatter.ofPattern(pattern));
    }
}