package kpi.pti.crypto.core.arithmetic;

import java.util.ArrayList;

/**
 * Created by yevhen.tsyba on 08.03.2015.
 */
public class ComputationUtils {

    /**
     * Util method to compute subtract from bigger number (first param)
     * smaller number (second param)
     *
     * @param minuend   - bigger value to subtract
     * @param deduction - smaller value to subtract
     * @return difference - {@link kpi.pti.crypto.core.arithmetic.Large}
     */
    static Large subtractNumbers(Large minuend, Large deduction) {

        // TODO yevhen.tsyba: fix method
        ArrayList<Integer> minuendReversed = minuend.getInternalItems();
        ArrayList<Integer> deductionReversed = deduction.getInternalItems();
        int minuendSize = minuendReversed.size();
        int deductionSize = deductionReversed.size();
        int delta = minuendSize - deductionSize;
        int carry = 0;

        StringBuilder diffReversed = new StringBuilder();
        for (int i = deductionSize -1; i >= 0; --i) {
            Integer res = minuendReversed.get(i + delta) - carry - deductionReversed.get(i);
            if (res < 0) {
                carry = 1;
                res += 10;
                diffReversed.append(res);
            } else {
                carry = 0;
            }
        }

        if(carry == 1){
            minuendReversed.set(delta - 1, minuendReversed.get(delta - 1) - carry);
        }

        for (int i = delta - 1; i >= 0; --i){
            diffReversed.append(minuendReversed.get(i));
        }


        return new Large(diffReversed.reverse().toString());
    }
}
