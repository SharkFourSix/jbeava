package lib.gintec_rdl.jbeava.validation.filters.integral;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("long")
public class LongFilter extends NumberFilter<Long> {
    public LongFilter() {
        super("long", TYPE_LONG);
    }
}