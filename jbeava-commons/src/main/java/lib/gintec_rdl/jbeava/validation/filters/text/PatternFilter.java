package lib.gintec_rdl.jbeava.validation.filters.text;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.util.List;

@PrepackagedFilter("pattern")
public class PatternFilter extends ValidationFilterImpl<String, String> {
    private static final String MESSAGE_TEMPLATE = "Value from field '${name}' `${value}` did not match expression: ${arg1}.";

    public PatternFilter() {
        super("pattern");
    }

    @Override
    public String filter(String name, String message, String value, List<String> args) throws JBeavaException {
        boolean matched;
        if (LocaleUtils.isNullOrEmpty(value)) {
            return null;
        }
        if (args.isEmpty()) {
            throw new JBeavaException(getName() + "(rgx): Missing regular expression argument.");
        }
        // isolate the exception
        try {
            matched = value.matches(args.get(0));
        } catch (Exception e) {
            throw new JBeavaException(e);
        }
        if (!matched) {
            throw new JBeavaException(generateExceptionMessage(name, value, args,
                    messageTemplate(message, MESSAGE_TEMPLATE)));
        }
        return value;
    }
}
