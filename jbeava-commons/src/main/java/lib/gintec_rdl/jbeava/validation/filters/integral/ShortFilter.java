package lib.gintec_rdl.jbeava.validation.filters.integral;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("short")
public class ShortFilter extends NumberFilter<Short> {
    public ShortFilter() {
        super("short", TYPE_SHORT);
    }
}