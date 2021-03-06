package core;

/**
 * Helper class for simple storing two objects.
 * Useful when need to return multiple results from functions.
 *
 * @author vadym
 * @since 09.03.15 23:53
 */
public class Zip<T,V> {
    /**
     * First object to store. Made public for easier access.
     */
    public final T one;
    /**
     * Second object to store. Made public for easier access.
     */
    public final V two;


    /**
     * Constructor.
     *
     * @param one first object to be stored
     * @param two second object to be stored
     */
    public Zip(T one, V two) {
        this.one = one;
        this.two = two;
    }
}
