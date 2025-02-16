package number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        /*System.out.println(Integer.toBinaryString(1));
        System.out.println(Integer.toBinaryString(3));
        System.out.println(Integer.toBinaryString(7));
        System.out.println(Integer.toBinaryString(15));
        System.out.println(Integer.toBinaryString(31));
        System.out.println(Integer.toBinaryString(63));
        System.out.println(Integer.toBinaryString(127));
        System.out.println("<==============================>");
        System.out.println(Integer.toBinaryString(1 & 100));
        System.out.println(Integer.toBinaryString(3 & 100));
        System.out.println(Integer.toBinaryString(7 & 100));
        System.out.println(Integer.toBinaryString(15 & 100));
        System.out.println(Integer.toBinaryString(31 & 100));
        System.out.println(Integer.toBinaryString(63 & 100));
        System.out.println(Integer.toBinaryString(127 & 100));*/

        System.out.println(new Solution().letterCombinations("222"));
    }

    String[] abcs = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};


    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.length() == 0) {
            return new ArrayList<>();
        }
        List<String> ret = new ArrayList<>();
        if (digits.length() == 1) {
            int i = Integer.valueOf(digits.substring(0, 1));
            if (i < 2 || i > 9) {
                return new ArrayList<>();
            }
            final char[] ac = abcs[i].toCharArray();
            for (char c1 : ac) {
                ret.add(new String(new char[]{c1}));
            }
        } else if (digits.length() == 2) {
            int i = Integer.valueOf(digits.substring(0, 1));
            int j = Integer.valueOf(digits.substring(1, 2));
            if (i < 2 || i > 9 || j < 2 || j > 9) {
                return new ArrayList<>();
            }
            final char[] ac = abcs[i].toCharArray();
            final char[] bc = abcs[j].toCharArray();
            for (char c : ac) {
                for (char c1 : bc) {
                    ret.add(new String(new char[]{c, c1}));
                }
            }
        } else {
            int len = digits.length();
            final List<String> leftList = letterCombinations(digits.substring(0, len / 2));
            final List<String> rightList = letterCombinations(digits.substring(len / 2, len));
            for (String s : leftList) {
                for (String s1 : rightList) {
                    ret.add(s + s1);
                }
            }
        }
        return ret;
    }


    int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    /**
     * https://leetcode-cn.com/problems/integer-to-roman/
     */
    public String intToRoman(int num) {
        StringBuilder ret = new StringBuilder();
        int current = num;
        int boundIndex = 0;
        while (current > 0) {
            while (current < values[boundIndex]) {
                boundIndex++;
            }
            ret.append(symbols[boundIndex]);
            current -= values[boundIndex];
        }
        return ret.toString();
    }

    /**
     * 有待优化
     */
    public int romanToInt(String s) {
        int sum = 0, pre = 0;
        for (int i = 0; i < s.length(); i++) {
            int num = getValue(s.charAt(i));
            if (i == 0) {
                sum = num;
                pre = num;
                continue;
            }
            sum += num;
            if (num > pre) {
                sum -= pre * 2;
            }
            pre = num;
        }
        return sum;
    }

    private int getValue(char ch) {
        switch (ch) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ret = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return ret;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int target = -nums[i];
            int k = nums.length - 1;
            for (int j = i + 1; j < nums.length; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                while (j < k && nums[k] + nums[j] > target) {
                    k--;
                }
                if (j == k) {
                    break;
                }
                if (nums[k] + nums[j] == target) {
                    ret.add(Arrays.asList(nums[i], nums[j], nums[k]));
                }
            }
        }
        return ret;
    }
}
