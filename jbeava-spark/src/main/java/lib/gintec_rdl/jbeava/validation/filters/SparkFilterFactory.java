package lib.gintec_rdl.jbeava.validation.filters;

import lib.gintec_rdl.jbeava.validation.ValidationFilter;
import lib.gintec_rdl.jbeava.validation.ValidatorFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SparkFilterFactory implements ValidatorFactory {

    private static final Map<String, ValidationFilter<?, ?>> table = new LinkedHashMap<>() {{
        put("fileUpload", new SparkFileUploadFilter());
    }};

    @Override
    public ValidationFilter<?, ?> get(String name) {
        return table.get(name);
    }
}
