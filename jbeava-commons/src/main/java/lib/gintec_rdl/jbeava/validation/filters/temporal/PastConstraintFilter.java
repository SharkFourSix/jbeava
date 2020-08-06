package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("past")
public class PastConstraintFilter extends TemporalConstraintFilter {
    public PastConstraintFilter() {
        super("past", Constraint.Past);
    }
}
