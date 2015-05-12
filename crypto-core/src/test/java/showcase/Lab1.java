package showcase;

import core.arithmetic.Large;

/**
 * @author vadym
 * @since 07.03.15.
 */
public class Lab1 {

    public static void main(String[] args) {
        Large X = new Large("9234013274012419836418634983459547689126439817263478157836453178654923401327401241983641863498345954768912643981726347815783645317865492340132740124198364186349834595476891264398172634781578364531786549234013274012419836418634983459547689126439817263478157836453178654");
        Large Y = new Large("2934097831972391728347612783641927841983569834695293409783197239172834761278364192784198356983469529340978319723917283476127836419278419835698346952934097831972391728347612783641927841983569834695");

        long time = System.currentTimeMillis();
        X.multiply(Y);
        time = System.currentTimeMillis() - time;

        System.out.println("School alg:       " + time + " ms");


        X = new Large("9234013274012419836418634983459547689126439817263478157836453178654923401327401241983641863498345954768912643981726347815783645317865492340132740124198364186349834595476891264398172634781578364531786549234013274012419836418634983459547689126439817263478157836453178654");
        Y = new Large("2934097831972391728347612783641927841983569834695293409783197239172834761278364192784198356983469529340978319723917283476127836419278419835698346952934097831972391728347612783641927841983569834695");

        time = System.currentTimeMillis();
        Large.karatsuba(X, Y);
        time = System.currentTimeMillis() - time;

        System.out.println("School Karatsuba: " + time + " ms");
    }
}
