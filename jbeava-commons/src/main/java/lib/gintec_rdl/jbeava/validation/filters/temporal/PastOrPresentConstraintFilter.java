package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("pastOrPresent")
public class PastOrPresentConstraintFilter extends TemporalConstraintFilter {
    public PastOrPresentConstraintFilter() {
        super("pastOrPresent", Constraint.PastOrPresent);
    }
}
