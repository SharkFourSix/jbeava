package lib.gintec_rdl.jbeava.validation.filters.text;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.util.List;

public abstract class CaseTransformationFilter extends ValidationFilterImpl<String, String> {

    enum Transformation {
        Lower,
        Upper,
        UpperFirstLetter,
        UCString
    }

    private final Transformation transformation;

    public CaseTransformationFilter(String filterName, Transformation transformation) {
        super(filterName);
        this.transformation = transformation;
    }

    @Override
    public String filter(String name, String message, String value, List<String> args) throws JBeavaException {
        if (LocaleUtils.isNullOrEmpty(value)) {
            return null;
        }
        switch (transformation) {
            case Lower:
                return value.toLowerCase();
            case Upper:
                return value.toUpperCase();
            case UpperFirstLetter:
                return LocaleUtils.ucFirst(value);
            case UCString:
                StringBuilder builder;
                String[] tokens;

                tokens = value.split("\\s+");
                builder = new StringBuilder();
                for (int i = 0; i < tokens.length; i++) {
                    if (i > 0) {
                        builder.append(' ');
                    }
                    builder.append(LocaleUtils.ucFirst(tokens[i]));
                }
                return builder.toString();
            default:
                throw new JBeavaException("Unsupported case transformation method");
        }
    }
}
