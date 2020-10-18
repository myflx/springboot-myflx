import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ArraySolution {
    public static void main(String[] args) {
        /*new ArraySolution().removeDuplicates(new int[]{1, 2, 2});*/
/*
        new ArraySolution().merge(new int[]{1, 2, 2, 2, 3, 5, 6, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 8,
                new int[]{0, 4, 4, 4, 5, 8, 8, 9}, 8);
*/
        /*new ArraySolution().subsets(new int[]{1, 2, 3, 4, 5});*/
        System.out.println(Arrays.toString(new ArraySolution().generateMatrix(2)));
    }


    public int[][] generateMatrix(int n) {
        int[][] ret = new int[n][n];
        //信息表:0-第一维索引 1-第二维索引 2-索引位的值 3-上中心拐點（1-是，其他否） 4-中心扩展长度
        int[] inf = new int[]{0, n - 1, n, 1, n - 1};
        while (inf[4] >= 0) {
            int l = 1;
            int preVal = inf[2] - 1;
            int postVal = inf[2] + 1;
            int firstIndex = inf[0];
            int secondIndex = inf[1];
            ret[inf[0]][inf[1]] = inf[2];
            //交換
            inf[0] = inf[0] ^ inf[1];
            inf[1] = inf[0] ^ inf[1];
            inf[0] = inf[0] ^ inf[1];
            //中心点前后赋值
            if (inf[3] == 1) {
                while (l++ <= inf[4]) {
                    ret[inf[1]][--secondIndex] = preVal--;
                    ret[++firstIndex][inf[0]] = postVal++;
                }
            } else {
                while (l++ <= inf[4]) {
                    ret[inf[1]][++secondIndex] = preVal--;
                    ret[--firstIndex][inf[0]] = postVal++;
                }
                //进位
                inf[0] ++;
                inf[1] = n - 1 - inf[0];
            }
            //设置下个中心位
            inf[2] = inf[2] + inf[4] * 2;
            inf[3] = inf[3] == 1 ? 0 : 1;
            inf[4]--;
        }
        return ret;
    }

    /**
     * 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
     * https://leetcode-cn.com/problems/subsets/
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ret = new ArrayList<>();
        //无元素
        ret.add(new ArrayList<>());
        //单个元素
        int nextStartIndex = 1;
        List<List<Integer>> preIndex = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            ret.add(Collections.singletonList(nums[i]));
            preIndex.add(Collections.singletonList(i));
        }
        //开始组合
        int i = 2;
        int len = nums.length;
        while (i++ <= len && !preIndex.isEmpty()) {
            List<List<Integer>> currentIndex = new ArrayList<>();
            for (List<Integer> indexs : preIndex) {
                //获取索引列表中的最后一个索引值
                int nextIndex = indexs.get(indexs.size() - 1) + 1;
                while (nextIndex < len) {
                    //维护当前的索引值列表
                    List<Integer> nextIndexList = new ArrayList<>(indexs);
                    nextIndexList.add(nextIndex);
                    currentIndex.add(nextIndexList);
                    //维护输出列表
                    List<Integer> numberList = new ArrayList<>();
                    for (Integer integer : nextIndexList) {
                        numberList.add(nums[integer]);
                    }
                    ret.add(numberList);
                    nextIndex++;
                }
            }
            preIndex = currentIndex;
        }
        return ret;
    }

    /**
     * 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
     * https://leetcode-cn.com/problems/subsets/
     */
    public List<List<Integer>> subsets2(int[] nums) {
        List<List<Integer>> ret = new ArrayList<>();
        //无元素
        ret.add(new ArrayList<>());
        //单个元素
        int nextStartIndex = 1;
        List<List<Integer>> preIndex = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            ret.add(Collections.singletonList(nums[i]));
            preIndex.add(Collections.singletonList(i));
        }
        //开始组合
        int i = 2;
        int len = nums.length;
        while (i++ <= len && !preIndex.isEmpty()) {
            List<List<Integer>> currentIndex = new ArrayList<>();
            for (List<Integer> indexs : preIndex) {
                //获取索引列表中的最后一个索引值
                int nextIndex = indexs.get(indexs.size() - 1) + 1;
                while (nextIndex < len) {
                    //维护当前的索引值列表
                    List<Integer> nextIndexList = new ArrayList<>(indexs);
                    nextIndexList.add(nextIndex);
                    currentIndex.add(nextIndexList);
                    //维护输出列表
                    List<Integer> numberList = new ArrayList<>();
                    for (Integer integer : nextIndexList) {
                        numberList.add(nums[integer]);
                    }
                    ret.add(numberList);
                    nextIndex++;
                }
            }
            preIndex = currentIndex;
        }
        return ret;
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
        while (!queue.isEmpty()) {
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
     * ↑     ↑
     * 0, 1, 2, 1, 1, 1, 2
     * ↑           ↑
     * <p>
     * <p>
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
