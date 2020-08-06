package lib.gintec_rdl.jbeava.validation.filters.integral;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.util.List;

public abstract class NumberFilter<Out extends Number> extends ValidationFilterImpl<String, Out> {
    static final String ERROR_UNSUPPORTED_TYPE = "Unsupported number type specified.";
    static final String MESSAGE_TEMPLATE = "${name} does not contain a valid number.";

    public static final int TYPE_BYTE = 1;
    public static final int TYPE_SHORT = 2;
    public static final int TYPE_INT = 3;
    public static final int TYPE_FLOAT = 4;
    public static final int TYPE_LONG = 5;
    public static final int TYPE_DOUBLE = 6;

    private final int type;

    public NumberFilter(String filterName, int type) {
        super(filterName);
        if (!LocaleUtils.isNumberInRange(type, TYPE_BYTE, TYPE_DOUBLE)) {
            throw new IllegalArgumentException(ERROR_UNSUPPORTED_TYPE);
        }
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Out filter(String name, String message, String value, List<String> args) throws JBeavaException {
        if (LocaleUtils.isNullOrEmpty(value)) {
            return (Out) (Number) 0;
        }
        try {
            double result;
            result = Double.parseDouble(value);
            switch (type) {
                case TYPE_BYTE:
                    return (Out) Byte.valueOf((byte) result);
                case TYPE_SHORT:
                    return (Out) Short.valueOf((short) result);
                case TYPE_INT:
                    return (Out) Integer.valueOf((int) result);
                case TYPE_FLOAT:
                    return (Out) Float.valueOf((float) result);
                case TYPE_LONG:
                    return (Out) Long.valueOf((long) result);
                case TYPE_DOUBLE:
                    return (Out) Double.valueOf(result);
                default:
                    throw new IllegalArgumentException(ERROR_UNSUPPORTED_TYPE);
            }
        } catch (NumberFormatException e) {
            throw new JBeavaException(generateExceptionMessage(name, value, args,
                    messageTemplate(message, MESSAGE_TEMPLATE)));
        }
    }
}
