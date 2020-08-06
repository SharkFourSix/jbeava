package lib.gintec_rdl.jbeava.validation.filters;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.util.List;

@PrepackagedFilter("enum")
public class EnumFilter extends ValidationFilterImpl<String, Enum<?>> {
    private static final String MESSAGE_TEMPLATE = "${value} is not am enum constant member of ${arg1}.";

    public EnumFilter() {
        super("enum");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enum<?> filter(String name, String message, String value, List<String> args) throws JBeavaException {
        if (LocaleUtils.isNullOrEmpty(value)) {
            return null;
        }
        Class clazz;
        String className;

        try {
            className = args.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new JBeavaException(
                    format("%s: Filter requires first parameter to be a FQCN.", getName()));
        }
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new JBeavaException(format("Failure instantiating enum class %s.", className), cnfe);
        }
        try {
            return Enum.valueOf(clazz, value);
        } catch (IllegalArgumentException e) {
            throw new JBeavaException(generateExceptionMessage(name, value, args,
                    messageTemplate(message, MESSAGE_TEMPLATE)));
        }
    }
}
