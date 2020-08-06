package lib.gintec_rdl.jbeava.validation;

import lib.gintec_rdl.jbeava.validation.annotations.Filter;
import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

final class ValidationContext {
    private final Filter filter;
    private final String fieldName;
    private final String fieldLabel;
    private final List<FilterContext> filterContexts;
    private final Field field;

    ValidationContext(Field field, Filter filter, List<FilterContext> filterContexts) {
        this.field = field;
        this.filter = filter;
        this.filterContexts = filterContexts;

        if (LocaleUtils.isNullOrEmpty(filter.name())) {
            fieldName = field.getName();
        } else {
            fieldName = filter.name();
        }
        if (LocaleUtils.isNullOrEmpty(filter.label())) {
            fieldLabel = fieldName;
        } else {
            fieldLabel = filter.label();
        }
    }

    @SuppressWarnings("unchecked")
    public Object validate(Object input, int context) {
        if (isMatchingContext(context)) {
            for (FilterContext filterContext : filterContexts) {
                input = filterContext.filter.filter(fieldLabel, filter.message(), input, filterContext.arguments);
            }
        }
        return input;
    }

    void setBeanValue(Object bean, Object value, int context) {
        if (isMatchingContext(context)) {
            try {
                if (!this.field.canAccess(bean)) {
                    this.field.trySetAccessible();
                }
                this.field.set(bean, value);
            } catch (IllegalAccessException e) {
                throw new JBeavaException(String.format(Locale.US, "Error when mapping results to field %s.",
                        getFieldLabel()), e);
            }
        }
    }

    private boolean isMatchingContext(int context) {
        if (filter.contexts().length == 0) {
            return true;
        }
        for (int c : filter.contexts()) {
            if (c == context) {
                return true;
            }
        }
        return false;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }
}
