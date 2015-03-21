package core.arithmetic;

import java.util.ArrayList;

/**
 * Class for storing and operating on internal number representation coefficients.
 * Allows access by every index. Returns 0 as a default value if tries to get
 * element outside the array bounds.
 *
 * @author vadym
 * @since 08.03.15 16:49
 */
public class Digits extends ArrayList<Integer> {
    /**
     *  Returns the element at the specified position in this list
     *  or default value if element's index out of range.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list or the default value
     */
    @Override
    public Integer get(int index) {
        return isInRange(index) ? super.get(index) : 0;
    }


    /**
     * Returns least significant element
     *
     * @return least significant element
     */
    public Integer getLSB() {
        return get(0);
    }

    /**
     * Returns most significant element
     *
     * @return most significant element
     */
    public Integer getMSB() {
        return get(size()-1);
    }


    /**
     * Replaces the element at the specified position in this list with
     * the specified element or inserts new one if element's index out of range.
     *
     * @param index index of the element to replace
     * @param element element to be stored or inserted at the specified position
     * @return the element previously at the specified position
     */
    @Override
    public Integer set(int index, Integer element) {
        if (isInRange(index)) {
            return super.set(index, element);
        } else {
            while (size() < index) add(0);
            add(element);
            return 0;
        }
    }

    /**
     * Trims tailing default elements in array.
     * Element with index 0 is never trimmed.
     */
    public void trim() {
        while (get(size()-1) == 0 && size() > 1) {
            remove(size()-1);
        }
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
