package lib.gintec_rdl.jbeava.validation.filters;

import lib.gintec_rdl.jbeava.validation.ValidationFilter;
import lib.gintec_rdl.jbeava.validation.ValidatorFactory;
import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;
import org.atteo.classindex.ClassIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class DefaultValidatorFactory implements ValidatorFactory {
    private final Logger logger = LoggerFactory.getLogger(DefaultValidatorFactory.class);
    private static final Map<String, ValidationFilter<?, ?>> filterMap;

    static {
        final Iterable<Class<?>> iterable = ClassIndex.getAnnotated(PrepackagedFilter.class);
        filterMap = new LinkedHashMap<>();
        iterable.forEach(new Consumer<>() {
            @Override
            public void accept(Class<?> aClass) {
                try {
                    ValidationFilter<?, ?> filter = (ValidationFilter<?, ?>) aClass.getConstructor().newInstance();
                    filterMap.put(filter.getName().toLowerCase(), filter);
                } catch (Exception e) {
                    throw new JBeavaException(e);
                }
            }
        });
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public ValidationFilter<?, ?> get(String name) {
        return filterMap.get(name.toLowerCase());
    }
}
