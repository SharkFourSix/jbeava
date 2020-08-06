package lib.gintec_rdl.jbeava.validation.filters.integral;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

import java.util.List;

@PrepackagedFilter("positive")
public class PositiveNumberFilter<T extends Number> extends ValidationFilterImpl<T, T> {
    private static final String MESSAGE_TEMPLATE = "${name} must be a positive number.";

    public PositiveNumberFilter() {
        super("positive");
    }

    @Override
    public T filter(String name, String message, T value, List<String> args) throws JBeavaException {
        if (value == null) {
            return null;
        }
        if (value.doubleValue() < 0) {
            throw new JBeavaException(generateExceptionMessage(name, value, args,
                    messageTemplate(message, MESSAGE_TEMPLATE)));
        }
        return value;
    }
}
