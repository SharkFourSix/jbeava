package lib.gintec_rdl.jbeava.validation.filters;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

import java.util.List;

@PrepackagedFilter("required")
public class RequiredFilter extends ValidationFilterImpl<Object, Object> {
    private static final String MESSAGE_TEMPLATE = "${name} is required.";

    public RequiredFilter() {
        super("required");
    }

    @Override
    public Object filter(String name, String message, Object value, List<String> args) throws JBeavaException {
        if (value != null) {
            return value;
        }
        throw new JBeavaException(generateExceptionMessage(name,
                value, args, messageTemplate(message, MESSAGE_TEMPLATE)));
    }
}
