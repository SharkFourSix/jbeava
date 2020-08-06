package lib.gintec_rdl.jbeava.validation;

/**
 * <p>This is the class that provides the interoperability of jbeava.</p>
 * <p>Validators use this class to obtain values from various sources, which are provided by the calling client</p>
 */
public abstract class FieldResolver {
    /**
     * @param fieldName The name of the field
     * @return The value of the field. Could also be null
     */
    public abstract Object getField(String fieldName);
}
