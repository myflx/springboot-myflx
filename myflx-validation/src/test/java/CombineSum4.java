import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CombineSum4 {

    public static void main(String[] args) {
        System.out.println(new CombineSum4().combinationSum4(new int[]{1, 2, 3}, 4));
    }

    /**
     * 2+
     * 给定一个由正整数组成且不存在重复数字的数组，找出和为给定目标正整数的组合的个数。
     *
     * @param nums   [1, 2, 3]
     * @param target 4
     * @return 所有可能的组合为：
     * (1, 1, 1, 1)
     * (2, 2)
     *
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
        // 排序
        Arrays.sort(nums);

        //记录不能整除的余数
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        // 找出无组合解个数和数据池的公约数
        for (int num : nums) {
            if (num > target) {
                break;
            }
            if (target % num == 0) {
                list.add(num);
            } else {
                map.put(num, target / num);
            }
        }
        System.out.println("当前数据池中的目标值的公约数：" + list);
        System.out.println("当前数据池中的目标值的余数：" + map);
        AtomicInteger count = new AtomicInteger(list.size());


        /*dfs(nums, 0, target, count);*/
        return count.get();
    }

    private void dfs(int[] candidates, int i, int target, AtomicInteger count) {
        if (i == candidates.length) {
            return;
        }
        if (target == 0) {
            count.incrementAndGet();
            return;
        }
        dfs(candidates, i + 1, target, count);
        if (target >= candidates[i]) {
            dfs(candidates, 0, target - candidates[i], count);
        }
    }
}
