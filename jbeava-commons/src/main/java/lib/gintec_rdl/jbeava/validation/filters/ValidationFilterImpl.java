package lib.gintec_rdl.jbeava.validation.filters;

import lib.gintec_rdl.jbeava.validation.ValidationFilter;
import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;
import lib.gintec_rdl.jbeava.validation.utils.StringTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p>Basic filter implementation that provides logging</p>
 *
 * @param <In>  Type of data this filter accepts. The VM will throw and exception if this filter is preceded by a
 *              filter that passes a type incompatible with this generic type
 * @param <Out> The type that this filter produces
 */
public abstract class ValidationFilterImpl<In, Out> implements ValidationFilter<In, Out> {
    private final String filterName;
    private static final StringTemplateEngine templateEngine = new StringTemplateEngine();
    private final Logger logger;

    /**
     * @param filterName The unique name of the filter to be used for lookup
     */
    public ValidationFilterImpl(String filterName) {
        this.filterName = filterName;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public abstract Out filter(String name, String message, In value, List<String> args) throws JBeavaException;

    @Override
    public String getName() {
        return filterName;
    }

    public Logger getLogger() {
        return logger;
    }

    protected final String messageTemplate(String message, String defaultValue) {
        if (LocaleUtils.isNullOrEmpty(message)) {
            return defaultValue;
        }
        return message;
    }

    protected String generateExceptionMessage(String name, Object value, List<String> args, String message) {
        Map<String, Object> model;

        model = new LinkedHashMap<>();
        model.put("name", name);
        model.put("value", value);
        addArgumentsToModel(model, args);
        return renderMessage(message, model);
    }

    protected void addArgumentsToModel(Map<String, Object> model, List<String> args) {
        for (int i = 0; i < args.size(); i++) {
            model.put("arg" + (i + 1), args.get(i));
        }
    }

    protected String renderMessage(String template, Map<String, Object> model) {
        return templateEngine.render(template, model);
    }

    protected String format(String message, Object... args) {
        return String.format(Locale.US, message, args);
    }
}
