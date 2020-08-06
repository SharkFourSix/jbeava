package lib.gintec_rdl.jbeava.validation.filters.integral;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("float")
public class FloatFilter extends NumberFilter<Float> {
    public FloatFilter() {
        super("float", TYPE_FLOAT);
    }
}