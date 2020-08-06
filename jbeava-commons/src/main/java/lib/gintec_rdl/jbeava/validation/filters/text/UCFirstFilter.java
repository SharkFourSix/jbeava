package lib.gintec_rdl.jbeava.validation.filters.text;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("ucfirst")
public class UCFirstFilter extends CaseTransformationFilter {
    public UCFirstFilter() {
        super("ucfirst", Transformation.UpperFirstLetter);
    }
}
