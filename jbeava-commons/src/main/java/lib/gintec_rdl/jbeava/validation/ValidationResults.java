package lib.gintec_rdl.jbeava.validation;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class ValidationResults {
    Object bean;
    boolean success;
    final List<String> violations;
    final Map<String, Object> raw;
    final Map<String, Object> results;

    public ValidationResults() {
        this.raw = new LinkedHashMap<>();
        this.results = new LinkedHashMap<>();
        this.violations = new LinkedList<>();
    }

    public boolean success() {
        return success;
    }

    public Map<String, Object> getRaw() {
        return raw;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean() {
        return (T) bean;
    }

    public Map<String, Object> getResults() {
        return results;
    }

    public List<String> getViolations() {
        return violations;
    }
}
