package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("futureOrPresent")
public class FutureOrPresentConstraintFilter extends TemporalConstraintFilter {
    public FutureOrPresentConstraintFilter() {
        super("futureOrPresent", Constraint.FutureOrPresent);
    }
}
