package core.arithmetic;

import java.util.ArrayList;

/**
 * Class for storing and operating on internal number representation coefficients.
 * Allows access by every index. Returns defined default value if tries to get
 * element outside the array bounds.
 *
 * @author vadym
 * @since 08.03.15 16:49
 */
public class ExtendedArrayList<T> extends ArrayList<T> {
    private T def;

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ExtendedArrayList(T def) {
        super();
        this.def = def;
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param  capacity  the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *         is negative
     */
    public ExtendedArrayList(T def, int capacity) {
        super(capacity);
        this.def = def;
    }


    /**
     *  Returns the element at the specified position in this list
     *  or default value if element's index out of range.
     *
     * @param index index of the element to return
     * @param def default value to return if index is out of range
     * @return the element at the specified position in this list or the default value
     */
    public T get(int index, T def) {
        return isInRange(index) ? super.get(index) : def;
    }

    @Override
    public T get(int index) {
        return get(index, def);
    }


    /**
     * Replaces the element at the specified position in this list with
     * the specified element or inserts new one if element's index out of range.
     *
     * @param index index of the element to replace
     * @param element element to be stored or inserted at the specified position
     * @param def default value to return if index is out of range
     * @return the element previously at the specified position
     */
    public T set(int index, T element, T def) {
        if (isInRange(index)) {
            return super.set(index, element);
        } else {
            while (size() < index) super.add(def);
            super.add(element);
            return def;
        }
    }


    @Override
    public T set(int index, T element) {
        return set(index, element, def);
    }


    /**
     * Trims tailing default elements in array.
     * Element with index 0 is never trimmed.
     *
     * @param def default element
     */
    public void trim(T def) {
        while (get(size()-1) == def && size() > 1) {
            remove(size()-1);
        }
    }

    public void trim() {
        trim(def);
    }


    /**
     * Helper method for determining if specified index is in array's range.
     *
     * @param index to determine bounds
     * @return <code>true</code> if index is in range, <code>false</code> otherwise.
     */
    private boolean isInRange(int index) {
        return index >= 0 && index < size();
    }
}
