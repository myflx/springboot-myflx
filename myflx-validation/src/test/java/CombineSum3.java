import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CombineSum3 {

    public static void main(String[] args) {
        System.out.println(new CombineSum3().combinationSum3(3, 7));
    }

    /**
     * 组合求和
     *
     * @param k 数字个数
     * @param n 数字之和
     * @return 可行解
     */
    public List<List<Integer>> combinationSum3(int k, int n) {
        int[] candidates = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Deque<Integer> path = new ArrayDeque<>();
        List<List<Integer>> res = new ArrayList<>();
        dfs(candidates, 0, n, n, k, path, res);
        return res;
    }

    private void dfs(int[] candidates, int begin, int target, int sourceTarget, int targetLength, Deque<Integer> path, List<List<Integer>> res) {
        if (target == 0 && path.size() == targetLength) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = begin; i < candidates.length; i++) {
            //如果位置上的数大于当前target,那么他后边的值一定不满足要求（已经排序了） 如果
            if (candidates[i] > target || candidates[i] == sourceTarget) {
                break;
            }

            //如果存在相同的的数值那么第二个数值开始一定是重复的解
            if (i > begin && candidates[i] - candidates[i - 1] == 0) {
                continue;
            }
            if (path.size() > targetLength) {
                break;
            }
            //差值回溯
            path.add(candidates[i]);
            dfs(candidates, i + 1, target - candidates[i], sourceTarget, targetLength, path, res);
            path.removeLast();
        }
    }
}
