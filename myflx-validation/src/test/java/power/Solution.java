package power;

import java.math.BigDecimal;

public class Solution {
    public boolean isPowerOfTwo2(int n) {
        if (n <= 0) {
            return false;
        }
        if (n == 1) {
            return true;
        }
        if (n % 2 != 0) {
            return false;
        }
        return isPowerOfTwo(n / 2);
    }

    public boolean isPowerOfTwo3(int n) {
        if (n == 0) return false;
        long x = (long) n;
        return (x & (-x)) == x;
    }

    public boolean isPowerOfTwo(int n) {
        if (n == 0) return false;
        long x = (long) n;
        return (x & (x - 1)) == 0;
    }
    public boolean isPowerOfThree2(int num) {
        if (num <= 0) {
            return false;
        }
        if (num == 1) {
            return true;
        }
        if (num % 3 != 0) {
            return false;
        }
        return isPowerOfThree(num / 3);
    }

    public boolean isPowerOfThree(int n) {
        return Integer.toString(n, 3).matches("^10*$");
    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = BigDecimal.valueOf(2);
        BigDecimal bigDecimal2 = BigDecimal.valueOf(1000);
        BigDecimal pow = bigDecimal.pow(1000);
        System.out.println(pow);
    }
}
