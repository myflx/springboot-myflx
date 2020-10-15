import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class ArraySolution {
    public static void main(String[] args) {
        /*new ArraySolution().removeDuplicates(new int[]{1, 2, 2});*/
        new ArraySolution().merge(new int[]{1, 2, 2, 2, 3, 5, 6, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 8,
                new int[]{0, 4, 4, 4, 5, 8, 8, 9}, 8);
    }

    /**
     * 官方题解：O(m+n)原地不动其实是我第一种思路的逆向，相差的不远!!!!，确实秒！！！
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        if (nums2 == null || nums2.length == 0 || nums1 == null || nums1.length == 0) {
            return;
        }
        //双指针从后遍历
        int index = m + n - 1;
        while (m >= 1 && n >= 1) {
            if (nums1[m - 1] >= nums2[n - 1]) {
                nums1[index--] = nums1[m - 1];
                m--;
            } else {
                nums1[index--] = nums2[n - 1];
                n--;
            }
        }
        while (n-- >= 1) {
            nums1[index--] = nums2[n];
        }
    }
    /**
     * 该解法借助外部数据结构
     * 个人问题:
     * 1,审题有误，误以为还要去重
     * 2,思路局限
     * 有序数组合并
     * https://leetcode-cn.com/problems/merge-sorted-array/
     */
    public void merge3(int[] nums1, int m, int[] nums2, int n) {
        if (nums2 == null || nums2.length == 0 || nums1 == null || nums1.length == 0) {
            return;
        }
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o));
        for (int i = 0; i < m; i++) {
            queue.add(nums1[i]);
        }
        for (int j = 0; j < n; j++) {
            queue.add(nums2[j]);
        }
        int i = 0;
        while (!queue.isEmpty()){
            nums1[i++] = queue.poll();
        }
    }

    /**
     * 该解法原地不动，耗时节省内存。
     * 个人问题:
     * 1,审题有误，误以为还要去重
     * 2,思路局限
     * 有序数组合并
     * https://leetcode-cn.com/problems/merge-sorted-array/
     */
    public void merge2(int[] nums1, int m, int[] nums2, int n) {
        if (nums2 == null || nums2.length == 0 || nums1 == null || nums1.length == 0) {
            return;
        }
        int i = 0;
        int j = 0;
        while (i < m || j < n) {
            if (i >= m) {
                nums1[i++] = nums2[j++];
                continue;
            }
            if (nums1[i] > nums2[j]) {
                nums1[i] = nums1[i] ^ nums2[j];
                nums2[j] = nums1[i] ^ nums2[j];
                nums1[i] = nums1[i] ^ nums2[j];
                //维护数组2顺序
                int k = j;
                while (k + 1 < n && nums2[k] > nums2[k + 1]) {
                    nums2[k] = nums2[k] ^ nums2[k + 1];
                    nums2[k + 1] = nums2[k] ^ nums2[k + 1];
                    nums2[k] = nums2[k] ^ nums2[k + 1];
                    k++;
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
