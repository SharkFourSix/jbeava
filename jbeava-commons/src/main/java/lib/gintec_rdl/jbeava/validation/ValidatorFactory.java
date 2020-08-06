package lib.gintec_rdl.jbeava.validation;

/**
 * Factory interface for creating filters
 */
public interface ValidatorFactory {
    /**
     * Creates/returns
     *
     * @param name The name of the filter to return
     * @return The filter supported by this factory
     */
    ValidationFilter<?, ?> get(String name);
}
