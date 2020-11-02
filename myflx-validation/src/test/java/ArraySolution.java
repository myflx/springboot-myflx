import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class ArraySolution {
    public static void main(String[] args) {
        /*new ArraySolution().removeDuplicates(new int[]{1, 2, 2});*/
/*
        new ArraySolution().merge(new int[]{1, 2, 2, 2, 3, 5, 6, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 8,
                new int[]{0, 4, 4, 4, 5, 8, 8, 9}, 8);
*/
        /*new ArraySolution().subsets(new int[]{1, 2, 3, 4, 5});*/
        /*System.out.println(Arrays.toString(new ArraySolution().generateMatrix(2)));*/

        /*System.out.println(new ArraySolution().permute(new int[]{1, 2, 3, 4, 5}));*/
        /*System.out.println(Arrays.toString(new ArraySolution().productExceptSelf(new int[]{1, 2, 3, 4, 5})));*/

        /*System.out.println(new ArraySolution().findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));*/
        /*System.out.println(new ArraySolution().spiralOrder(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}}));*/
        System.out.println(new ArraySolution().maxArea(new int[]{1, 8, 6, 20, 5, 4, 20, 3, 7}));
    }

    /**
     * 螺旋数组
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> ret = new ArrayList<>();
        if (matrix.length == 0 || matrix[0].length == 0) {
            return ret;
        }
        int m = matrix.length;//第一维索引
        int n = matrix[0].length;//第二维索引
        //信息表:0-第一维索引 1-第二维索引 2-上拐點（1-是 -1否） 3-第一维度长度 4-第二维度长度
        int[] info = new int[]{0, n - 1, 1, m, n};
        while (info[3] > 1 || info[4] > 1) {
            int revert = info[2];
            int i = info[0];
            int j = info[1];
            if (revert > 0) {
                int k = info[1] - info[4] + 1;
                while (k < info[1]) ret.add(matrix[i][k++]);
                ret.add(matrix[i][k]);
                k = info[1] - info[3] + 1;
                while (k < info[3]) ret.add(matrix[k++][j]);
            } else {
                int k = info[4] + info[1] - 1;
                while (k > 0) ret.add(matrix[i][k--]);
                ret.add(matrix[i][k]);
                k = info[0] - info[3] + 1;
                while (k > 0) ret.add(matrix[k--][j]);
            }

            info[0] = revert > 0 ? (info[3] - 1 + info[0]) : (info[0] - info[3] + 1);
            info[1] = revert > 0 ? (info[1] + 1 - info[4]) : (info[4] - 1 + info[1]);
            info[2] *= -1;
            info[3] -= 1;
            info[4] -= 1;
        }
        return ret;
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
                inf[0]++;
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


    /**
     * 输入: [1,2,3]
     * 输出:
     * [
     * [1,2,3],
     * [1,3,2],
     * [2,1,3],
     * [2,3,1],
     * [3,1,2],
     * [3,2,1]
     * ]
     * <p>
     * 给定一个 没有重复 数字的序列，返回其所有可能的全排列。
     * https://leetcode-cn.com/problems/permutations/
     */
    public List<List<Integer>> permute(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }
        final List<List<Integer>> lists = permuteHelp(Arrays.stream(nums).boxed().collect(Collectors.toList()));
        return lists;
    }

    public List<List<Integer>> permuteHelp(List<Integer> list) {
        List<List<Integer>> ret = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return ret;
        }
        if (list.size() == 1) {
            ret.add(list);
        } else if (list.size() == 2) {
            ret.add(list);
            final List<Integer> integers = new ArrayList<>(list);
            Collections.reverse(integers);
            ret.add(integers);
        } else {
            for (int i = 0; i < list.size(); i++) {
                final List<Integer> collect = new ArrayList<>(list);
                final Integer remove = collect.remove(i);
                final List<List<Integer>> lists = permuteHelp(collect);
                for (List<Integer> integerList : lists) {
                    List<Integer> item = new ArrayList<>();
                    item.add(remove);
                    item.addAll(integerList);
                    ret.add(item);
                }
            }
        }
        return ret;
    }

    /**
     * 除自身积
     * 动态规划-左右积数组
     * https://leetcode-cn.com/problems/product-of-array-except-self/
     */
    public int[] productExceptSelf(int[] nums) {
        int length = nums.length;
        int[] answer = new int[length];

        // answer[i] 表示索引 i 左侧所有元素的乘积
        // 因为索引为 '0' 的元素左侧没有元素， 所以 answer[0] = 1
        answer[0] = 1;
        for (int i = 1; i < length; i++) {
            answer[i] = nums[i - 1] * answer[i - 1];
        }

        // R 为右侧所有元素的乘积
        // 刚开始右边没有元素，所以 R = 1
        int R = 1;
        for (int i = length - 1; i >= 0; i--) {
            // 对于索引 i，左边的乘积为 answer[i]，右边的乘积为 R
            answer[i] = answer[i] * R;
            // R 需要包含右边所有的乘积，所以计算下一个结果时需要将当前值乘到 R 上
            R *= nums[i];
        }
        return answer;
    }

    /**
     * 除自身积
     * 二分法
     * https://leetcode-cn.com/problems/product-of-array-except-self/
     */
    public int[] productExceptSelf2(int[] nums) {
        int[] ret = new int[nums.length];
        productExceptSelfHelp(nums, 0, nums.length - 1, ret);
        return ret;
    }

    public void productExceptSelfHelp(int[] nums, int s, int e, int[] ret) {
        //二分乘积数组
        int[] products = new int[2];
        Arrays.fill(products, 1);
        //中间靠左的索引
        int mid = (s + e) / 2;
        for (int i = s; i <= e; i++) {
            if (i <= mid) {
                products[0] *= nums[i];
            } else {
                products[1] *= nums[i];
            }
        }
        if (mid - s == 1) {
            //两个元素交换相乘入组
            ret[mid] = nums[s];
            ret[s] = nums[mid];
        } else if (mid - s == 0) {
            //单元素直接取积
            ret[s] = 1;
        } else if (mid - s > 1) {
            productExceptSelfHelp(nums, s, mid, ret);
        }

        if (e - mid - 1 == 1) {
            //两个元素交换相乘入组
            ret[mid + 1] = nums[e];
            ret[e] = nums[mid + 1];
        } else if (e - mid - 1 == 0) {
            //单元素置为1
            ret[e] = 1;
        } else if (e - mid - 1 > 1) {
            productExceptSelfHelp(nums, mid + 1, e, ret);
        }

        //处理完毕填充乘积
        for (int i = s; i <= e; i++) {
            if (i <= mid) {
                ret[i] *= products[1];
            } else {
                ret[i] *= products[0];
            }
        }
    }


    /**
     * 数组中的top kth
     * https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
     */
    public int findKthLargest(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k > nums.length) {
            return -1;
        }
        if (nums.length == 1 && k == 1) {
            return nums[0];
        }
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    public int findKthLargest2(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k > nums.length) {
            return -1;
        }
        if (nums.length == 1 && k == 1) {
            return nums[0];
        }
        //优先队列长度为k
        final PriorityQueue<Integer> queue = new PriorityQueue<>(k, Comparator.comparingInt(o -> o));
        for (int num : nums) {
            queue.offer(num);
            if (queue.size() > k) {
                queue.poll();
            }
        }
        return queue.isEmpty() ? -1 : queue.poll();
    }


    public int maxArea(int[] height) {
        if (height == null || height.length < 2) {
            return 0;
        }
        int S = 0;
        int i = 0, j = height.length - 1;
        while (i < j) {
            S = Math.max(S(height, i, j), S);
            if (height[i] <= height[j]) {
                i++;
            } else if (height[i] > height[j]) {
                j--;
            }
        }
        return S;
    }

    /**
     * 获取两挡板之间最大乘水量
     */
    public int S(int[] height, int i, int j) {
        return (j - i) * Math.min(height[i], height[j]);
    }
}
