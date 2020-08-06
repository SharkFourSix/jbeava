package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;

/**
 * <p>Base filter for temporal data types</p>
 *
 * @param <Out>
 */
public abstract class TemporalFilter<Out extends Temporal> extends ValidationFilterImpl<String, Out> {
    private static final String MESSAGE_TEMPLATE = "${name} is not a valid date.";
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm";

    public TemporalFilter(String filterName) {
        super(filterName);
    }

    @Override
    public Out filter(String name, String message, String value, List<String> args) throws JBeavaException {
        if (LocaleUtils.isNullOrEmpty(value)) {
            return null;
        }
        try {
            String pattern;
            pattern = args.size() >= 1 ? args.get(0) : getPattern();
            return parse(value, Optional.ofNullable(pattern).orElse(DEFAULT_FORMAT));
        } catch (Exception e) {
            getLogger().error("Error when filtering {}", name, e);
            throw new JBeavaException(generateExceptionMessage(name, value, args,
                    messageTemplate(message, MESSAGE_TEMPLATE)));
        }
    }

    /**
     * @return Preferred formatting pattern. Default is null
     */
    protected String getPattern() {
        return null;
    }

    protected abstract Out parse(String input, String pattern);
}
