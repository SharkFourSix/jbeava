package lib.gintec_rdl.jbeava.validation;

import junit.framework.TestCase;
import lib.gintec_rdl.jbeava.validation.annotations.Filter;
import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;
import org.apache.log4j.BasicConfigurator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JbeavaTest extends TestCase {

    public static class Person {

        @Filter(filters = {"required", "long", "positive"}, contexts = {1})
        private long id;

        @Filter(
                filters = {"required", "int", "range(18,65)"},
                message = "You must be at least 18 years old and no older than 65 to sign up. Unfortunately your age" +
                        " (${value}) falls outside of this range."
        )
        private int age;

        @Filter(
                label = "Full name",
                name = "fullName",
                filters = {"trim", "required", "length(1,9)"}
        )
        private String name;

        @Filter(
                filters = {"required", "double", "positive", "range(1.5,2)"},
                label = "Height",
                message = "Sorry! You are not tall enough to join our secret organization."
        )
        private double height;

        @Filter(
                label = "Email address",
                filters = {"trim", "required", "length(3,30)", "pattern(^([\\w.-]+@[\\w.-]+)$)"}
        )
        private String email;

        @Filter(label = "Favorite animal", filters = {"trim", "required", "reverse"})
        private String favoriteAnimal;

        private String favColor;

        public String getFavoriteAnimal() {
            return favoriteAnimal;
        }

        public String getFavColor() {
            return favColor;
        }

        public void setFavColor(String favColor) {
            this.favColor = favColor;
        }

        public long getId() {
            return id;
        }

        public int getAge() {
            return age;
        }
    }

    /**
     * Simulates an HTTP form
     */
    private static class HttpFormDataSource extends FieldResolver {
        private final Map<String, String> form;

        public HttpFormDataSource(Map<String, String> form) {
            this.form = form;
        }

        @Override
        public Object getField(String fieldName) {
            return form.get(fieldName);
        }
    }

    private static class StringReverser implements ValidatorFactory {
        private static final ValidationFilter<String, String> filter = new ValidationFilterImpl<>("reverse") {

            @Override
            public String filter(String name, String message, String value, List<String> args) throws JBeavaException {
                if (LocaleUtils.isNullOrEmpty(value)) {
                    return null;
                }
                return new StringBuilder(value).reverse().toString();
            }
        };

        @Override
        public ValidationFilter<?, ?> get(String name) {
            if ("reverse".equalsIgnoreCase(name)) {
                return filter;
            }
            return null;
        }
    }

    @Override
    protected void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    public void testValidate() {
        Person john;
        Map<String, String> map;
        FieldResolver fieldResolver;
        ValidationResults validationResults;

        map = new LinkedHashMap<>() {{
            put("age", "19");
            put("height", "1.5");
            put("fullName", "John Doe");
            put("email", "john.doe@gmail.com");
            put("id", "5");
            put("favoriteAnimal", "duck");
        }};

        fieldResolver = new HttpFormDataSource(map);

        // add custom factory
        Jbeava.addFactory(new StringReverser());

        validationResults = Jbeava.validate(Person.class, fieldResolver, Options.defaults());

        john = validationResults.getBean();
        assertEquals(john.getAge(), 19);
        assertEquals(john.getId(), 0);

        // Passing existing instance
        john.setFavColor("Red");
        validationResults = Jbeava.validate(Person.class, fieldResolver, Options.defaults().instance(john).context(1));
        john = validationResults.getBean();

        assertEquals(john.getId(), 5);
        assertEquals(john.getFavColor(), "Red");
        assertEquals(john.getFavoriteAnimal(), "kcud");
    }

    static class GranPa {
        @Filter(filters = {"required", "length(2,50)"}, label = "Last name", contexts = {1})
        private String lastName;

        @Filter(filters = {"required", "length(2,50)"}, label = "First name", contexts = {1})
        private String firstName;

        public String getLastName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }
    }

    public static class Pappy extends GranPa {
        @Filter(filters = {"required", "int", "positive"})
        private int age;

        public int getAge() {
            return age;
        }
    }

    static public class Kid extends Pappy {
        @Filter(filters = {"required", "long"})
        private long id;

        public long getId() {
            return id;
        }
    }

    public void testPostValidationMappingInheritance() {
        Kid kid;
        Map<String, String> map;
        FieldResolver fieldResolver;
        ValidationResults validationResults;

        map = new LinkedHashMap<>() {{
            put("firstName", "John");
            put("lastName", "Doe, III");
            put("age", "31");
            put("id", "78");
        }};

        fieldResolver = new HttpFormDataSource(map);

        // Initial validation and mapping, depth = 0
        validationResults = Jbeava.validate(Kid.class, fieldResolver, Options.defaults());

        assertTrue(validationResults.success);
        kid = validationResults.getBean();
        assertNull(kid.getFirstName());
        assertNull(kid.getLastName());

        validationResults = Jbeava.validate(Kid.class, fieldResolver, Options.defaults().map(true).instance(kid).depth(1));

        assertTrue(validationResults.success);
        assertEquals(kid.getAge(), 31);
        assertNull(kid.getFirstName());
        assertNull(kid.getLastName());

        // Go the GranPa level at depth 2. Here we will only validate first name and last name.
        // We will then update kid with the validated information without affected the previously validated data.
        // We will manually map the results to an already existing bean.
        validationResults = Jbeava.validate(Kid.class, fieldResolver, Options.defaults().map(false).depth(2).context(1));

        assertTrue(validationResults.success);
        // The results object already knows the validation context and the class of the bean to map to.
        validationResults.updateBean(kid);

        assertEquals(kid.getAge(), 31);
        assertEquals("John", kid.getFirstName());
        assertEquals("Doe, III", kid.getLastName());
    }
}