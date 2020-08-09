package lib.gintec_rdl.jbeava.validation;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.DefaultValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public enum Jbeava {
    _$;

    private static final String FILTER_NAME_PATTERN = "^([a-zA-Z_]+([0-9_]+)?)+$";

    private final ConcurrentHashMap<String, ValidatorFactory> factoryMap;
    private final Logger logger;

    Jbeava() {
        factoryMap = new ConcurrentHashMap<>();
        logger = LoggerFactory.getLogger(Jbeava.class);
        addFactory0(new DefaultValidatorFactory());
    }

    private void addFactory0(ValidatorFactory factory) {
        factoryMap.computeIfAbsent(factory.getClass().getName(), s -> factory);
    }

    public static void addFactory(ValidatorFactory factory) {
        _$.addFactory0(factory);
    }

    private ValidationFilter<?, ?> getFilter(String filterName) {
        Iterator<ValidatorFactory> factoryIterator;

        if (!filterName.matches(FILTER_NAME_PATTERN)) {
            throw new JBeavaException("Invalid filter name " + filterName);
        }

        factoryIterator = _$.factoryMap.values().iterator();
        while (factoryIterator.hasNext()) {
            ValidationFilter<?, ?> filter = factoryIterator.next().get(filterName);
            if (filter != null) {
                return filter;
            }
        }
        return null;
    }

    private ValidationResults validate0(Class<?> c, FieldResolver fieldResolver, Options options) {
        ValidationResults results;
        Iterable<ValidationContext> validationContexts;

        results = new ValidationResults();
        results.typeReference = c;
        results.contextReference = options.context;
        validationContexts = createValidationContexts0(c, options.depth);

        for (ValidationContext validationContext : validationContexts) {
            Object input;

            try {
                input = fieldResolver.getField(validationContext.getFieldName());
                if (options.sticky) {
                    results.raw.put(validationContext.getFieldName(), input);
                }
                input = validationContext.validate(input, options.context);
                results.results.put(validationContext.getFieldName(), input);
            } catch (JBeavaException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Exception during validation.", e);
                }
                results.violations.add(e.getMessage());
                if (options.failFast) {
                    break;
                }
            }
        }
        if ((results.success = results.violations.isEmpty())) {
            if (options.map) {
                Object instance;
                if (options.instance == null) {
                    instance = options.create ? createBean(c) : null;
                } else {
                    instance = options.instance;
                }
                if (instance != null) {
                    mapResultsToBean(instance, results, options.context);
                }
                results.bean = instance;
            }
        }
        return results;
    }

    private Object createBean(Class<?> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Exception when creating bean of type {}.", type, e);
            throw new JBeavaException(e);
        }
    }

    private static final Map<Class<?>, ValidationContextInfo> info = new ConcurrentHashMap<>();

    private Iterable<ValidationContext> createValidationContexts0(Class<?> clazz, int depth) {
        ValidationContextInfo contextInfo;

        depth = depth < 0 ? 0xffffffff : depth;
        contextInfo = info.computeIfAbsent(clazz, ValidationContextInfo::new);
        contextInfo.loadContexts(depth);
        return contextInfo.getValidationContexts();
    }

    List<FilterContext> createValidationFilterContexts(String[] names) {
        List<FilterContext> filterContexts;

        filterContexts = new LinkedList<>();
        for (String name : names) {
            String filterName;
            List<String> arguments;
            ValidationFilter<?, ?> filter;

            if (hasParameters(name)) {
                int index, lastIndex;

                index = name.indexOf('(');
                lastIndex = name.lastIndexOf(')');
                lastIndex = lastIndex < 0 ? name.length() : lastIndex;

                filterName = name.substring(0, index);
                arguments = Arrays.asList(name.substring(index + 1, lastIndex).split(",")); // must escape the ','
            } else {
                filterName = name;
                arguments = List.of();
            }
            if ((filter = getFilter(filterName)) == null) {
                throw new JBeavaException("Could not find filter called " + name);
            }
            filterContexts.add(new FilterContext(filter, arguments));
        }
        return filterContexts;
    }

    private static boolean hasParameters(String filter) {
        int firstIndex, lastIndex;
        firstIndex = filter.indexOf('(');
        lastIndex = filter.lastIndexOf(')');
        return firstIndex != -1 && lastIndex != -1;
    }

    String format(String msg, Object... args) {
        return String.format(Locale.US, msg, args);
    }

    public static ValidationResults validate(Class<?> clazz, FieldResolver fieldResolver, Options options) {
        return _$.validate0(clazz, fieldResolver, options);
    }

    static void mapResultsToBean(Object bean, ValidationResults results, int context) {
        ValidationContextInfo contexts;

        if ((contexts = info.get(bean.getClass())) != null) {
            for (ValidationContext validationContext : contexts.getValidationContexts()) {
                validationContext.setBeanValue(bean, results.results.get(validationContext.getFieldName()), context);
            }
        }
    }
}
