package lib.gintec_rdl.jbeava.validation.filters.integral;

import lib.gintec_rdl.jbeava.validation.internals.PrepackagedFilter;

@PrepackagedFilter("byte")
public class ByteFilter extends NumberFilter<Byte> {
    public ByteFilter() {
        super("byte", TYPE_BYTE);
    }
}