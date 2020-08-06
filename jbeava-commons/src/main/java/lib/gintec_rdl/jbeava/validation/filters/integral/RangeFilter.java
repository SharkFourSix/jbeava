package lib.gintec_rdl.jbeava.validation.filters.integral;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.util.List;

@PrepackagedFilter("range")
public class RangeFilter<T extends Number> extends ValidationFilterImpl<T, T> {
    private static final String MESSAGE_TEMPLATE = "${name} must be within ${arg1} and ${arg2}.";
    private static final String ERROR_ILLEGAL_PARAMETERS = "range filter requires both minimum and maximum values to be set with valid values.";

    public RangeFilter() {
        super("range");
    }

    @Override
    public T filter(String name, String message, T value, List<String> args) throws JBeavaException {
        double min;
        double max;

        if (value == null) {
            return null;
        }

        if (args.size() == 2) {
            try {
                min = Double.parseDouble(args.get(0));
                max = Double.parseDouble(args.get(1));
            } catch (NumberFormatException e) {
                throw new JBeavaException(ERROR_ILLEGAL_PARAMETERS);
            }
            if (!LocaleUtils.isNumberInRange(value, min, max)) {
                throw new JBeavaException(generateExceptionMessage(name, value, args,
                        messageTemplate(message, MESSAGE_TEMPLATE)));
            }
            return value;
        }
        throw new JBeavaException(ERROR_ILLEGAL_PARAMETERS);
    }
}
