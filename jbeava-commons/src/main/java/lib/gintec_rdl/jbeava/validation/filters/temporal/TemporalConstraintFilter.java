package lib.gintec_rdl.jbeava.validation.filters.temporal;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

abstract class TemporalConstraintFilter extends ValidationFilterImpl<Temporal, Temporal> {
    static final String TEMPLATE_LE = "${name} cannot be after ${now}.";
    static final String TEMPLATE_EQ = "${name} must be the same as ${now}.";
    static final String TEMPLATE_GT = "${name} must be after ${now}.";
    static final String TEMPLATE_LEQ = "${name} can only be ${article} or before ${now}.";
    static final String TEMPLATE_GEQ = "${name} can only be ${article} or after ${now}.";

    private interface TemporalComparator {
        boolean check(Temporal value, LocalDateTime now) throws JBeavaException;

        default boolean throwUnsupportedTemporalTypeException() throws JBeavaException {
            throw new JBeavaException("Unsupported temporal type ${type}");
        }
    }

    enum Constraint implements TemporalComparator {
        /**
         * <
         */
        Past(TEMPLATE_LE) {
            @Override
            public boolean check(Temporal value, LocalDateTime now) {
                if (value instanceof LocalDateTime) {
                    return ((LocalDateTime) value).isBefore(now);
                }
                if (value instanceof LocalTime) {
                    return ((LocalTime) value).isBefore(now.toLocalTime());
                }
                if (value instanceof LocalDate) {
                    return ((LocalDate) value).isBefore(now.toLocalDate());
                }
                return throwUnsupportedTemporalTypeException();
            }
        },
        /**
         * ==
         */
        Present(TEMPLATE_EQ) {
            @Override
            public boolean check(Temporal value, LocalDateTime now) throws JBeavaException {
                if (value instanceof LocalDateTime) {
                    return ((LocalDateTime) value).isEqual(now);
                }
                if (value instanceof LocalTime) {
                    return ((LocalTime) value).equals(now.toLocalTime());
                }
                if (value instanceof LocalDate) {
                    return ((LocalDate) value).isEqual(now.toLocalDate());
                }
                return throwUnsupportedTemporalTypeException();
            }
        },
        /**
         * >
         */
        Future(TEMPLATE_GT) {
            @Override
            public boolean check(Temporal value, LocalDateTime now) throws JBeavaException {
                if (value instanceof LocalDateTime) {
                    return ((LocalDateTime) value).isAfter(now);
                }
                if (value instanceof LocalTime) {
                    return ((LocalTime) value).isAfter(now.toLocalTime());
                }
                if (value instanceof LocalDate) {
                    return ((LocalDate) value).isAfter(now.toLocalDate());
                }
                return throwUnsupportedTemporalTypeException();
            }
        },
        /**
         * <=
         */
        PastOrPresent(TEMPLATE_LEQ) {
            @Override
            public boolean check(Temporal value, LocalDateTime now) throws JBeavaException {
                return Past.check(value, now) || Present.check(value, now);
            }
        },
        /**
         * >=
         */
        FutureOrPresent(TEMPLATE_GEQ) {
            @Override
            public boolean check(Temporal value, LocalDateTime now) throws JBeavaException {
                return Future.check(value, now) || Present.check(value, now);
            }
        };

        Constraint(String template) {
            this.template = template;
        }

        private final String template;
    }

    private final Constraint constraint;

    public TemporalConstraintFilter(String name, Constraint constraint) {
        super(name);
        this.constraint = constraint;
    }

    @Override
    public Temporal filter(String name, String message, Temporal value, List<String> args) throws JBeavaException {
        LocalDateTime now;
        Map<String, Object> model;

        if (value == null) {
            return null;
        }

        now = LocalDateTime.now();
        model = new LinkedHashMap<>();

        model.put("now", LocaleUtils.formatLocalDateTime(now));
        model.put("name", name);
        model.put("value", value);
        model.put("article", getArticle(value));
        model.put("type", value.getClass());

        try {
            if (!constraint.check(value, now)) {
                throw new JBeavaException(renderMessage(messageTemplate(message, constraint.template), model));
            }
            return value;
        } catch (JBeavaException exception) {
            throw new JBeavaException(renderMessage(exception.getMessage(), model));
        }
    }

    private String getArticle(Temporal value) {
        if (value instanceof LocalDate || value instanceof LocalDateTime) {
            return "on";
        }
        if (value instanceof LocalTime) {
            return "at";
        }
        return "the same as";
    }
}
