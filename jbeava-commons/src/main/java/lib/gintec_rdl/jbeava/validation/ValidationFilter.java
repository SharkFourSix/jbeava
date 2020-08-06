package lib.gintec_rdl.jbeava.validation;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;

import java.util.List;

/**
 * <p>This is where it's at. The main heart of the lib.gintec_rdl.jbeava.validation library.</p>
 *
 * @param <In>  The expected input type. The benefit of generics here is that it imposes type restrictions in cases
 *              where filters are chained. Certain filters might expect data to be of a certain type only. So passing
 *              any other type will cause the VM to throw a type conversion exception.
 * @param <Out> The final type that this filter produces. For instance, the filter might decode a number in the form of
 *              a string, then convert it into a int.
 */
public interface ValidationFilter<In, Out> {
    /**
     * <p>The actual filtering method. Not only can this method validate data but it can also transform
     * the data into a normalized type that's ready to be used.</p>
     *
     * @param name    Name of the field being filtered
     * @param message Optional message template to display to the user in case of an error.
     * @param value   The value to validate or transform.
     * @param args    Parameters passed to the filter function
     * @return The same object or its transformed equivalent
     * @throws JBeavaException If an exception is thrown along the calling chain. This could be a
     *                         misconfigured filter or a lib.gintec_rdl.jbeava.validation violation
     */
    Out filter(String name, String message, In value, List<String> args) throws JBeavaException;

    /**
     * @return Returns the unique name of this filter
     */
    String getName();
}
