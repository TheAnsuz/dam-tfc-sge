package dev.amrv.sge.auth;

import java.util.Arrays;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class Probetest {

    public static void main(String[] args) {

        long t0 = System.nanoTime();

        System.out.println("792:   " + proceed(792));
        System.out.println("693:   " + proceed(693));
        System.out.println("594:   " + proceed(594));
        System.out.println("495:   " + proceed(495));
        System.out.println(" 64:   " + proceed(64, 3, 3));

        System.out.println("792 S: " + steps(792));
        System.out.println("693 S: " + steps(693));
        System.out.println("594 S: " + steps(594));
        System.out.println("495 S: " + steps(495));

        System.out.println(System.nanoTime() - t0);
    }

    public static int steps(int n) {
        int changed;
        int loops = 1;

        for (;;) {
            changed = proceed(n);

//            System.out.println(">>>>>>>>>> " + "OLD" + " == " + "NEW");
//            System.out.println(">>>>>>>>>> " + n + " == " + changed);
            if (changed == n)
                return --loops;

            n = changed;
            loops++;
        }
    }

    public static int proceed(int number) {
        return proceed(number, getDigits(number), getDigits(number));
    }

    public static int proceed(int number, int minDigits, int maxDigits) {
        int digits = getDigits(number);

//        System.out.println(number);
        digits = digits > maxDigits ? maxDigits : digits < minDigits ? minDigits : digits;

        int numl[] = new int[digits];
        int numh[] = new int[digits];

        for (int i = 0; i < digits; i++) {
            numl[i] = getDigit(number, digits - i - 1);
            numh[i] = getDigit(number, digits - i - 1);
        }

//        System.out.println("> " + Arrays.toString(numl));
        int low = recomposite(sortHigher(numl));
        int high = recomposite(sortLower(numh));

//        System.out.println("L " + low);
//        System.out.println("H " + high);
        return (low - high);
    }

    public static int recomposite(int... digits) {
        int result = 0;

        for (int i = 0; i < digits.length; i++) {
            result = (result * 10) + digits[i];
        }
        return result;
    }

    public static int[] sortHigher(int... digits) {

        int cache;
        boolean changed;

        do {
//            System.out.println(Arrays.toString(digits));
            changed = false;
            for (int i = 0; i < digits.length - 1; i++) {
                if (digits[i] < digits[i + 1]) {
                    changed = true;
                    cache = digits[i];
                    digits[i] = digits[i + 1];
                    digits[i + 1] = cache;
                    break;
                }
            }
        } while (changed);

        return digits;
    }

    public static int[] sortLower(int... digits) {

        int cache;
        boolean changed;

        do {
//            System.out.println(Arrays.toString(digits));
            changed = false;
            for (int i = 0; i < digits.length - 1; i++) {
                if (digits[i] > digits[i + 1]) {
                    changed = true;
                    cache = digits[i];
                    digits[i] = digits[i + 1];
                    digits[i + 1] = cache;
                    break;
                }
            }
        } while (changed);

        return digits;
    }

    public static int getDigits(int number) {
        int digits = 0;

        for (;;) {
            int n = getDigit(number, digits);

            if (n == 0)
                return digits;
            digits++;
        }
    }

    public static int getDigit(int number, int digit) {
        int mult = 10;

        for (int i = 1; i < digit; i++)
            mult *= 10;

        int divider = digit == 0 ? 1 : mult;

        return (number / divider) % 10;
    }

}
