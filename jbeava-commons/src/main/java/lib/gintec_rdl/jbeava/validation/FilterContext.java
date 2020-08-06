package lib.gintec_rdl.jbeava.validation;

import java.util.List;

class FilterContext {
    ValidationFilter filter;
    List<String> arguments;

    public FilterContext(ValidationFilter<?, ?> filter, List<String> arguments) {
        this.filter = filter;
        this.arguments = arguments;
    }

    public ValidationFilter<?, ?> getFilter() {
        return filter;
    }

    public void setFilter(ValidationFilter<?, ?> filter) {
        this.filter = filter;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
}