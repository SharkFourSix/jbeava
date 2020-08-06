package lib.gintec_rdl.jbeava.validation.filters.text;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("lower")
public class LowercaseFilter extends CaseTransformationFilter {
    public LowercaseFilter() {
        super("lower", Transformation.Lower);
    }
}
