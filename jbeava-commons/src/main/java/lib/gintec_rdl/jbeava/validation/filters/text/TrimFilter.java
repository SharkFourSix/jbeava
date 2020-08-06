package lib.gintec_rdl.jbeava.validation.filters.text;

import lib.gintec_rdl.jbeava.validation.exceptions.JBeavaException;
import lib.gintec_rdl.jbeava.validation.filters.ValidationFilterImpl;
import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;
import lib.gintec_rdl.jbeava.validation.utils.LocaleUtils;

import java.util.List;

@PrepackagedFilter("trim")
public class TrimFilter extends ValidationFilterImpl<String, String> {
    public TrimFilter() {
        super("trim");
    }

    @Override
    public String filter(String name, String message, String value, List<String> args) throws JBeavaException {
        if (LocaleUtils.isNullOrEmpty(value)) {
            return null;
        }
        return value.trim();
    }
}
