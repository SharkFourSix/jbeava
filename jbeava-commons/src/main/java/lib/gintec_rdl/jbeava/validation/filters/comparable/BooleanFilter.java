package lib.gintec_rdl.jbeava.validation.filters.comparable;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.util.List;

@PrepackagedFilter("bool")
public class BooleanFilter extends ValidationFilterImpl<String, Boolean> {
    private static final String MESSAGE_TEMPLATE = "${name} is not a valid boolean value.";

    public BooleanFilter() {
        super("bool");
    }

    @Override
    public Boolean filter(String name, String message, String value, List<String> args) throws JBeavaException {
        if (LocaleUtils.isNullOrEmpty(value)) {
            return null;
        }
        try {
            return Boolean.valueOf(value);
        } catch (Exception e) {
            throw new JBeavaException(generateExceptionMessage(name,
                    value, args, messageTemplate(message, MESSAGE_TEMPLATE)));
        }
    }
}
