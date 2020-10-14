import java.util.Arrays;

public class ArraySolution {
    public static void main(String[] args) {
        /*new ArraySolution().removeDuplicates(new int[]{1, 2, 2});*/
        new ArraySolution().merge(new int[]{1, 2, 2, 2, 3, 5, 6, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 8,
                new int[]{4, 4, 4, 5, 8, 8, 9}, 7);
    }


    /**
     * 有序数组合并
     * https://leetcode-cn.com/problems/merge-sorted-array/
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = 0;
        int j = 0;
        while (i < m + n && j < n) {
            //找到了数组2第一个数据（重复元素中最右边的）
            while (j + 1 < n && nums2[j] == nums2[j + 1]) {
                j++;
            }
            if (i >= m) {
                nums1[i] = nums2[j++];
            } else {
                if (nums2[j] == nums1[i]) {
                    j++;
                } else if (nums2[j] < nums1[i]) {
                    //数据交换
                    nums2[j] = nums2[j] ^ nums1[i];
                    nums1[i] = nums1[i] ^ nums2[j];
                    nums2[j] = nums2[j] ^ nums1[i];
                }
            }
            i++;
        }
    }

    /**
     * 0, 0, 0, 1, 1, 1, 2
     * ↑        ↑
     * 0, 1, 0, 1, 1, 1, 2
     *    ↑     ↑
     * 0, 1, 2, 1, 1, 1, 2
     *       ↑           ↑
     *
     *
     * 0, 1, 1, 1, 2
     * ↑  ↑
     */
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int i = 0, j = 0;
        while (j < nums.length) {
            while (j < nums.length && nums[i] == nums[j]) {
                j++;
            }
            if (j < nums.length && ++i < j) {
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }
}
