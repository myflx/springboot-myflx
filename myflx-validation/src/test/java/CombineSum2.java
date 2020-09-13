import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class CombineSum2 {
    public static void main(String[] args) {
        /*System.out.println(new Lekou().combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8));*/
        /*System.out.println(new Lekou().combinationSum2(new int[]{4, 4, 2, 1, 4, 2, 2, 1, 3}, 6));*/
        System.out.println(new CombineSum2().combinationSum2(new int[]{2, 5, 1, 1, 2, 3, 3, 3, 1, 2, 2}, 5));
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        int len = candidates.length;
        List<List<Integer>> res = new ArrayList<>();
        if (len == 0) {
            return res;
        }
        // 排序
        Arrays.sort(candidates);
        Deque<Integer> path = new ArrayDeque<>(len);
        dfs(candidates, 0, target, path, res);
        return res;
    }

    private void dfs(int[] candidates, int begin, int target, Deque<Integer> path, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = begin; i < candidates.length; i++) {
            //如果位置上的数大于当前target,那么他后边的值一定不满足要求（已经排序了）
            if (candidates[i] > target) {
                break;
            }

            //如果存在相同的的数值那么第二个数值开始一定是重复的解
            if (i > begin && candidates[i] - candidates[i - 1] == 0) {
                continue;
            }

            //差值回溯
            path.add(candidates[i]);
            dfs(candidates, i + 1, target - candidates[i], path, res);
            path.removeLast();
        }
    }
}
