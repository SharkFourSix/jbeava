package lib.gintec_rdl.jbeava.validation.filters.text;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("ucstring")
public class UCStringFilter extends CaseTransformationFilter {
    public UCStringFilter() {
        super("ucstring", Transformation.UCString);
    }
}
