import java.util.Arrays;

public class CombineSum4 {

    public static void main(String[] args) {
        System.out.println(new CombineSum4().combinationSum4V2(new int[]{1, 2, 3, 4}, 4));
    }


    /**
     * 动态规划
     */
    public int combinationSum4V3(int[] nums, int target) {
        if (nums.length == 1) {
            return target % nums[0] == 0 ? 1 : 0;
        }
        int[] dp = new int[target + 1];
        //索引值为0，两值相等计数为一个可行解
        dp[0] = 1;
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (num <= i) {
                    dp[i] += dp[i - num];
                    System.out.println(Arrays.toString(dp));
                }
            }
        }
        return dp[target];
    }


    int[] cache;

    /**
     *
     * dp[i] = dp[i-1]
     * 记忆搜索
     * [-1, 1, 2, 4, 8]
     * [-1, 1, 2, 4, 7]
     */
    public int combinationSum4V2(int[] nums, int target) {
        cache = new int[target + 1];
        Arrays.fill(cache, -1);
        return memorySearch(nums, target);
    }

    private int memorySearch(int[] nums, int target) {
        if (cache[target] != -1) {
            return cache[target];
        }
        if (target == 0) {
            return 1;
        }
        int count = 0;
        for (int num : nums) {
            if (num > target) {
                break;
            }
            count += memorySearch(nums, target - num);
        }
        cache[target] = count;
        System.out.println(Arrays.toString(cache));
        return count;
    }

    /**
     * 简单递归
     * 给定一个由正整数组成且不存在重复数字的数组，找出和为给定目标正整数的组合的个数。
     *
     * @param nums   [1, 2, 3]
     * @param target 4
     * @return 所有可能的组合为：
     * (1, 1, 1, 1)
     * (2, 2)
     * <p>
     * (1, 1, 2)
     * (1, 2, 1)
     * (1, 3)
     * (2, 1, 1)
     * (3, 1)
     * <p>
     * <p>
     * 请注意，顺序不同的序列被视作不同的组合。
     * <p>
     * 因此输出为 7。
     */
    public int combinationSum4(int[] nums, int target) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }
        Arrays.sort(nums);
        return dfs(nums, 0, target);
    }

    private int dfs(int[] candidates, int i, int target) {
        if (i == candidates.length) {
            return 0;
        }
        if (target == 0) {
            return 1;
        }
        int count = 0;
        for (int j = i; j < candidates.length; j++) {
            if (candidates[j] > target) {
                break;
            }
            count += dfs(candidates, 0, target - candidates[j]);
        }

        return count;
    }
}
