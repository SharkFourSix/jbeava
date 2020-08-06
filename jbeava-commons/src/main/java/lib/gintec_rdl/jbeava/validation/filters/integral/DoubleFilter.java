package lib.gintec_rdl.jbeava.validation.filters.integral;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("double")
public class DoubleFilter extends NumberFilter<Double> {
    public DoubleFilter() {
        super("double", TYPE_DOUBLE);
    }
}