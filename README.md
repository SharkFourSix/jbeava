# jbeava

jbeava (Java Bean Validator)

## Background

Data input validation is one the most overlooked issues in Software Development by junior developers and seniors alike.
A lot of code that I have come across does not really do a good job of validating input data. This is usually indicative
 of the fact that a lot of people do not design their systems, rather opt to go straight to coding. One very important
 benefit of designing a system before developing it is that you get to discover all your edge cases much earlier, which
 in turn aids you in formulating your data validation rules. Once all edge cases have been identified, it then
 becomes easier to organize your logic: From capturing input, validation, transformation and normalization,
 processing, and storage and retrieval.

_There are already plenty of validation libraries, so why another one?_ Well, why not? On a serious note, I am very
 much aware of this fact as well as the existence of JSR 303. I found most of the libraries to be not as technically 
 intuitive as I would have liked them to be and hence the birth of **jbeava**.

## Technical Details

Validation takes place inside [filters](jbeava-commons/src/main/java/lib/gintec_rdl/jbeava/validation/ValidationFilter.java).
 The primary function of the filters is to validate input data passed to the filters.

Filters must return a value, which could be the very same input value or its transformed derivative. Thus, we have two
 types of filters: Inspection filters and transformation filters. Inspection filters simply validate input data and
 return the same value whereas transformation filters not only validate but also transform the input data into
 some other readily consumable data type. 

One main advantage of this design is that filters can be chained together to complete a whole logic block without
 writing too much boilerplate code. It is also possible to have filters that both inspect and transform,
 which in fact, is what most of the prepackaged filters do.

## Usage

Include the `jbeava-commons` artifact to get started.

Minimum supported language level is Java 9.

```xml
<project>
    ...
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    <dependency>
        <groupId>lib.gintec_rdl.jbeava</groupId>
        <artifactId>jbeava-commons</artifactId>
        <version>1.0.0</version>
    </dependency>
</project>
```

There's also a resolver for [Spark-Java](https://github.com/perwendel/spark)

```xml
<dependency>
    <groupId>lib.gintec_rdl.jbeava</groupId>
    <artifactId>jbeava-spark</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Interoperability

**jbeava** can be plugged into any environment through 
    the use of [FieldResolver](jbeava-commons/src/main/java/lib/gintec_rdl/jbeava/validation/FieldResolver.java) which 
    provides a conduit for passing data from various sources. Data input sources could be files, 
    HTTP forms, REST services, etc.

### Extensibility

Extending the functionality of jbeava is easy. 
    Simply implement [ValidationFilter](jbeava-commons/src/main/java/lib/gintec_rdl/jbeava/validation/ValidationFilter.java) 
    then register your filter [factory](jbeava-commons/src/main/java/lib/gintec_rdl/jbeava/validation/ValidatorFactory.java) 
    class and jbeava will handle the rest.

```java
Jbeava.addFactory(new MyFactory());
```

## Prepackaged Filters

1. [Temporal](#temporal)
2. [Integral](#integral)
3. [Textual](#textual)
4. [Countable](#countable)
5. [Comparable](#comparable)
6. [Other](#other)

### Temporal

- `date` - Validates and produces `LocalDate`
    - in: String
    - out: LocalDate
    - args:
        - (_Optional_) Pattern
- `datetime` - Converts string to `LocalDateTime` using the given pattern
    - in: String
    - out: LocalDateTime
    - args:
        - (_Optional_) Pattern. Defaults to `yyyy-MM-dd HH:mm:ss`.
- `time` - Converts string to `LocalTime`.
    - in: String
    - out: LocalTime
    - args:
        - (_Optional_) pattern
- `future` - Translates to `value > now()`.
    - in: Temporal object
    - out: Temporal object
- `past` - Translates to `value < now()`.
    - in: Temporal
    - out: Temporal 
- `present` - Translates to `value == now()`.
    - in: Temporal
    - out: Temporal
- `pastOrPresent` - Translates to `value <= now()`.
    - in: Temporal
    - out: Temporal
- `futureOrPresent` - Translates to `value >= now()`.
    - in: Temporal
    - out: Temporal

### Integral

- `byte`
    - in: String
    - out: Byte

- `short`
    - in: String
    - out: Short
    
- `int`
    - in: String
    - out: Integer
    
- `float`
    - in: String
    - out: Float
    
- `double`
    - in: String
    - out: Double
    
- `long`
    - in: String
    - out: Long
    
- `positive` - Enforces non-negative numbers.
    - in: Number
    - out: Number
    
- `range` - Verifies that the input number is inclusively within the given range.
    - in: Number
    - out: Number
    - args:
        - Number min: Lower bound.
        - Number max: Upper bound.

### Textual

- `lower` - Transforms string to lower case.
    - in: String
    - out: String

- `upper` - Transforms string to upper case.
    - in: String
    - out: String
    
- `trim` - Removes leading and trailing spaces.
    - in: String
    - out: String
    
- `ucfirst` - Transforms first letter of a sentence or word to upper case.
    - in: String
    - out: String
    
- `ucstring` - Transforms the first letter of every word in a sentence to upper case.
    - in: String
    - out: String

- `pattern` - Regular expression filter for extended custom validation.
    - in: String
    - out: String
    - args:
        - regex

### Countable

- `length` - Much like the `range` filter, this filter verifies the number of items in a given collection or container.
    - in: Any of the following types: Collection, CharSequence, Array.
    - out: Same as input.
    - args:
        - Minimum size of the container to enforce. (Inclusive).
        - Desired maximum size to enforce.

### Comparable

- `bool`
    - in: String
    - out: Boolean

### Other

- `required` - Verifies that the given input is not null.
    - in: Object
    - out: Object
    
- `enum` - This filter returns the enum constant of the given FQCN.
    - in: String
    - out: Enum
    - args:
        - FQCN of the target enumeration type.
        - Name of constant member.

Below is one use among many. Consult the tests for more information.

```java
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
        Options options;
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

        options = new Options();
        options.failFast(false);
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
}
```

Validation starts when you call `Jbeava.validate(...)`, which returns 
    a [ValidationResults](jbeava-commons/src/main/java/lib/gintec_rdl/jbeava/validation/ValidationResults.java) object 
    containing all the validation result details.

## Custom error messages.

The library comes with a simple expression parsing engine that provides the easiness of specifying custom error 
    messages.

You can reference the name and value of each target field by using variable placeholders like so. Below is one example:

```java
@Filter(
        label = "First name",
        message = "${name} must not exceed 100 characters. You have specified ${value} characters which exceeds this limit.",
        filters = {"required","length(2,100)"}
)
String firstName;
```

For filters that have parameters, you can reference arguments using `${arg1}` or `${arg2}` and so on, depending on how
    many parameters the filter takes.
    
The following references are guaranteed to exist:

    - ${name}
    - ${value}
    - ${arg} - Only if suppled.
    
Some filters also provide their own variables that can be referenced in error messages.

### Caveats

When specifying parameters to filters, i.e `length(1,200)`, the parameters must not have any leading or training spaces
    like `length(1, 2)`, unless intended because they will be passed back to the filters as is without any inspection.
    
### TODO

 - [ ] Implement caching and heuristics.