package lib.gintec_rdl.jbeava.validation;

import lib.gintec_rdl.jbeava.validation.annotations.Filter;

public class Options {
    /**
     * Process only fields declared by the child class.
     */
    public static final int DEPTH_DEFAULT = 0;
    /**
     * Climb all the way until the root class ({@link Object}) is reached.
     */
    public static final int DEPTH_ALL = -1;

    boolean failFast;
    int depth;
    int context;
    boolean map;
    boolean sticky;
    boolean create;
    Object instance;

    public Options() {
        failFast = false;
        depth = 0;
        context = 0;
        sticky = false;
        map = create = true;
    }

    public static Options defaults() {
        return new Options();
    }

    /**
     * @param sticky Whether or not to keep track of raw unprocessed data. This is handy in cases where the
     *               data must be returned back to the source, i.e stick forms.
     *               Please note that this option may not necessarily work as intended if {@link #failFast(boolean)} is enabled.
     * @return .
     * @see #failFast(boolean)
     */
    public Options sticky(boolean sticky) {
        this.sticky = sticky;
        return this;
    }

    public Options create(boolean create) {
        this.create = create;
        return this;
    }

    /**
     * <p>Sets the object instance whose fields should be mapped from the validated fields.</p>
     * <p>If the instance is null it will be created unless {@link #create(boolean)} is set to false.</p>
     *
     * @param instance The instance to be mapped.
     * @return .
     */
    public Options instance(Object instance) {
        this.instance = instance;
        return this;
    }

    /**
     * <p>Sets whether or not to map the filtered fields to the instance provided through {@link #instance(Object)}.</p>
     *
     * @param map .
     * @return .
     */
    public Options map(boolean map) {
        this.map = map;
        return this;
    }

    /**
     * <p>Sets whether or not to stop processing filters upon the encountering the first violation.</p>
     * <p>If {@link #sticky(boolean)} is set, only the first valid captured fields will be returned.</p>
     *
     * @param failFast .
     * @return .
     * @see #sticky(boolean)
     */
    public Options failFast(boolean failFast) {
        this.failFast = failFast;
        return this;
    }

    /**
     * Set the search depth of fields. This tells jbeava how far up the inheritance tree to climb during field discovery.
     *
     * @param depth Depth value relative to the class being validated. 0 means the class itself. < 0 Traverse until
     *              object class is found and > 0 traverse that many levels up or until object class is found.
     * @return .
     */
    public Options depth(int depth) {
        this.depth = depth;
        return this;
    }

    /**
     * @param context .
     * @return .
     * @see Filter#contexts()
     */
    public Options context(int context) {
        this.context = context;
        return this;
    }
}
