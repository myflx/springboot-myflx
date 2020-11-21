package number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(1));
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
        System.out.println(Integer.toBinaryString(127 & 100));
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
