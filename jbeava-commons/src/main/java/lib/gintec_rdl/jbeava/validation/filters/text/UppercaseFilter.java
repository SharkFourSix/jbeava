package lib.gintec_rdl.jbeava.validation.filters.text;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("upper")
public class UppercaseFilter extends CaseTransformationFilter {
    public UppercaseFilter() {
        super("upper", Transformation.Upper);
    }
}
