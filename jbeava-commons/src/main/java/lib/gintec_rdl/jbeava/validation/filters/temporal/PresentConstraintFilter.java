package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("present")
public class PresentConstraintFilter extends TemporalConstraintFilter {
    public PresentConstraintFilter() {
        super("present", Constraint.Present);
    }
}
