package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@PrepackagedFilter("date")
public class DateFilter extends TemporalFilter<LocalDate> {
    public DateFilter() {
        super("date");
    }

    @Override
    protected LocalDate parse(String input, String pattern) {
        return LocalDate.parse(input, DateTimeFormatter.ofPattern(pattern));
    }
}
