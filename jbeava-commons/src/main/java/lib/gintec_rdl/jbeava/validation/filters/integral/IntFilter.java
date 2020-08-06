package lib.gintec_rdl.jbeava.validation.filters.integral;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("int")
public class IntFilter extends NumberFilter<Integer> {
    public IntFilter() {
        super("int", TYPE_INT);
    }
}