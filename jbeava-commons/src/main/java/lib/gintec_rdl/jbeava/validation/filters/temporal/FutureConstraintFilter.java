package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("future")
public class FutureConstraintFilter extends TemporalConstraintFilter {
    public FutureConstraintFilter() {
        super("future", Constraint.Future);
    }
}
