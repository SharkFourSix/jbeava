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

    /**
     * @return True if all input was validated
     */
    public boolean success() {
        return success;
    }

    /**
     * @return Returns all captured data in unfiltered state. The data contained here is not recommended for consumption.
     * @see Options#sticky(boolean)
     */
    public Map<String, Object> getRaw() {
        return raw;
    }

    /**
     * @param <T> .
     * @return The bean whose fields have been mapped from the validated fields. The existence of this instance is
     * dependent upon how {@link Options#map(boolean)} and {@link Options#create(boolean)} have been configured.
     * @see Options#map(boolean)
     * @see Options#create(boolean)
     * @see Options#instance(Object)
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean() {
        return (T) bean;
    }

    /**
     * @return Validation results. Whatever is in here is safe to consume.
     */
    public Map<String, Object> getResults() {
        return results;
    }

    public List<String> getViolations() {
        return violations;
    }
}
