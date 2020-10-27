package power;

import java.util.ArrayList;
import java.util.List;

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


    /**
     * https://leetcode-cn.com/problems/number-of-1-bits/
     * 编写一个函数，输入是一个无符号整数，返回其二进制表达式中数字位数为 ‘1’ 的个数（也被称为汉明重量）。
     */
    public int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1);
            count++;
        }
        return count;
    }

    /**
     * 格雷编码
     * https://leetcode-cn.com/problems/gray-code/
     */
    public List<Integer> grayCode(int n) {
        List<Integer> list = new ArrayList<>();
        if (n == 0) {
            list.add(0);
            return list;
        }
        final List<Integer> integers = grayCode(n - 1);
        list.addAll(integers);
        for (int i = 0; i < integers.size(); i++) {
            list.add(integers.get(integers.size() - 1 - i) + (1 << (n - 1)));
        }
        return list;
    }


    public static void main(String[] args) {
        /*BigDecimal bigDecimal = BigDecimal.valueOf(2);
        BigDecimal bigDecimal2 = BigDecimal.valueOf(1000);
        BigDecimal pow = bigDecimal.pow(1000);
        System.out.println(pow);*/
        /*System.out.println(Integer.toBinaryString(174));
        System.out.println(Integer.toBinaryString(173));

        System.out.println(174 & 173);
        System.out.println(new Solution().hammingWeight(174));
        System.out.println(Integer.toBinaryString(Integer.MIN_VALUE));
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
        int max = 1 << 2;
        System.out.println(Integer.MAX_VALUE);*/
        final List<Integer> integers = new Solution().grayCode(4);
        System.out.println(integers);
    }
}
