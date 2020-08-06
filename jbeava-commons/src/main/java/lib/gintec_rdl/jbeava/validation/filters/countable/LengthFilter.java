package lib.gintec_rdl.jbeava.validation.filters.countable;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@PrepackagedFilter("length")
public class LengthFilter extends ValidationFilterImpl<Object, Object> {
    private static final String MESSAGE_TEMPLATE = "${name} must have between ${arg1} and ${arg2} ${element}.";

    public LengthFilter() {
        super("length");
    }

    @Override
    public Object filter(String name, String message, Object value, List<String> args) throws JBeavaException {
        int min;
        int max;
        int length;
        String type;
        int distance;
        Map<String, Object> model;

        try {
            min = Math.abs(Integer.parseInt(args.get(0)));
            max = Math.abs(Integer.parseInt(args.get(1)));
            distance = max - min;
        } catch (Exception e) {
            throw new JBeavaException(format("%s: Filter requires two parameters: min and max.",
                    getName()));
        }

        if (distance < 1) {
            throw new JBeavaException(format("%s: Filter requires the difference between min and max be at least 1.", getName()));
        }

        if (value == null) {
            return null;
        }

        model = new LinkedHashMap<>();
        model.put("name", name);
        model.put("value", value);
        addArgumentsToModel(model, args);

        if (value instanceof CharSequence) {
            length = ((CharSequence) value).length();
            type = "characters";
        } else if (value instanceof Collection) {
            length = ((Collection<?>) value).size();
            type = "elements";
        } else if (value.getClass().isArray()) {
            length = Array.getLength(value);
            type = "items";
        } else {
            throw new JBeavaException(format("%s must be of a countable type."));
        }

        model.put("element", type);
        if (!LocaleUtils.isNumberInRange(length, min, max)) {
            throw new JBeavaException(renderMessage(messageTemplate(message, MESSAGE_TEMPLATE), model));
        }

        return value;
    }
}
