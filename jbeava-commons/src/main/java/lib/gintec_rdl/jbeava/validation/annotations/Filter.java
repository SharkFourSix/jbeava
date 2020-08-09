package lib.gintec_rdl.jbeava.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter {
    /**
     * @return returns a list of filters to apply on the annotated target field.
     * The filters are processed in order of occurrence
     */
    String[] filters();

    /**
     * @return Returns the overridden name of the field. By default, the name is taken from {@link #name()}
     */
    String label() default "";

    /**
     * @return The unique name of the field. By default, the name is taken from the field
     */
    String name() default "";

    /**
     * <p>Validation contexts for this field.</p>
     * <p>Sometimes you may want to validate specific fields in specific contexts.</p>
     * <p>By default, the target field will be validated in all contexts</p>
     */
    int[] contexts() default {};

    /**
     * @return <p>Custom message to display in case validation fails. You can make reference to field name and
     * values like so: `${name}` and `${value}`.</p><p></p>
     * <p>For example: <code>"${name} is not a valid integer.</code></p>
     * <p>The following properties are guaranteed to be available for all filters:</p>
     * <p>
     * <li>${name} - Friendly name of the field.</li>
     * <li>${value} - Value as received by the filter.</li>
     * <li>${arg1}, ${arg2}, ${argN}, ... ${arg100} - Arguments passed to the filter.</li>
     * </p>
     * <p>The argument values are only present if the filter receives the arguments.</p>
     */
    String message() default "";
}
