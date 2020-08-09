package lib.gintec_rdl.jbeava.validation;

import lib.gintec_rdl.jbeava.validation.annotations.Filter;
import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class is used for caching types and their validation contexts with the ability to add more
 * fields as new depth values are being specified.
 *
 *
 * <p>
 * For instance, consider the following:
 *
 * <pre>
 *     public class GrandParent {
 *         &#64;Filter(...)
 *         private long id;
 *
 *         public long getId(){ return this.id; }
 *
 *         public void setId(long id){ this.id = id; }
 *     }
 *
 *     public class Parent extends GrandParent {
 *         &#64;Filter(...)
 *         private String name;
 *     }
 *
 *     public class Child extends Parent {
 *         &#64;Filter(...)
 *         private int height;
 *     }
 * </pre>
 * <p>
 * To validate all inherited fields in the <code>Child</code> class, the {@link Options#depth(int)} parameter would
 * have to be set to 2 so that all the annotated fields will be collected.
 * <p>
 * Now, if the first validation call specified a depth of 0, only the <code>height</code> property would be collected.
 * For any subsequent validation calls, if the specified depth is greater than the current depth, an attempt will be
 * made to collect fields from the class at the specified depth in the inheritance hierarchy.
 */
public class ValidationContextInfo {
    private int currentDepth;
    private final Class<?> root;
    private Class<?> currentClass;
    private final Queue<ValidationContext> validationContexts;

    public ValidationContextInfo(Class<?> root) {
        this.root = root;
        this.currentDepth = -1;
        this.currentClass = root;
        this.validationContexts = new ConcurrentLinkedQueue<>();
    }

    /**
     * Load validation contexts for each annotated field from classes in the current class's inheritance hierarchy,
     * up to <code>newDepth</code>, which is the level at which to cease scanning for fields.
     *
     * @param newDepth .
     */
    void loadContexts(int newDepth) {
        synchronized (this) {
            while (currentDepth < newDepth && currentClass != Object.class) {
                for (Field field : currentClass.getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Filter.class)) {
                        continue;
                    }
                    Filter filter;
                    List<FilterContext> filterContexts;

                    filter = field.getAnnotation(Filter.class);
                    if (filter.filters().length == 0) {
                        throw new JBeavaException(
                                Jbeava._$.format("At least one filter must be specified for field %s.%s.",
                                        currentClass.getCanonicalName(), field.getName()));
                    }
                    filterContexts = Jbeava._$.createValidationFilterContexts(filter.filters());
                    validationContexts.add(new ValidationContext(field, filter, filterContexts));
                }
                currentDepth++;
                currentClass = currentClass.getSuperclass();
            }
        }
    }

    Iterable<ValidationContext> getValidationContexts() {
        return validationContexts;
    }
}
