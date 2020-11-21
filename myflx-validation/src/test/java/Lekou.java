import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 算法语言无关性
 * 忘掉语言，使用最基础的，没有则自己构建
 */
public class Lekou {
    public static void main(String[] args) {
        /*int[] ints = new Lekou().topKFrequent(new int[]{0, 1, 0, 0, 0, 3, 3, 3, 3, 1, 1, 1, 1}, 2);
        System.out.println(Arrays.toString(ints));*/

        /*System.out.println(new Lekou().longestPalindrome("abacabbacaba"));*/

        /*System.out.println(new Lekou().countSubstrings("abacabbacaba"));*/

        /*System.out.println(new Lekou().combinationSum(new int[]{2, 3, 6, 7}, 7));*/

        /*System.out.println(new Lekou().combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8));*/
        /*System.out.println(new Lekou().combinationSum2(new int[]{4, 4, 2, 1, 4, 2, 2, 1, 3}, 6));*/
        System.out.println(new Lekou().combinationSum2(new int[]{2, 5, 1, 1, 2, 3, 3, 3, 1, 2, 2}, 5));
    }


    /**
     * 给定一个数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
     * <p>
     * candidates 中的每个数字在每个组合中只能使用一次。
     * <p>
     * 说明：
     * 所有数字（包括目标数）都是正整数。
     * 解集不能包含重复的组合。
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        Map<String, List<Integer>> listMap = new HashMap<>();
        List<Integer> container = new ArrayList<>();
        List<Integer> userIndexed = new ArrayList<>();
        getCombineFromIndex2(candidates, 0, target, container, listMap, userIndexed);
        List<List<Integer>> outList = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> stringListEntry : listMap.entrySet()) {
            outList.add(stringListEntry.getValue());
        }
        return outList;
    }

    private void getCombineFromIndex2(int[] candidates,
                                      int i, int target, List<Integer> container, Map<String, List<Integer>> listMap, List<Integer> userIndexed) {
        if (i >= candidates.length) {
            return;
        }
        if (!userIndexed.isEmpty() && userIndexed.subList(0, userIndexed.size() - 1).contains(i)) {
            return;
        }
        if (target == 0) {
            ArrayList<Integer> integers = new ArrayList<>(container);
            Collections.sort(integers);
            listMap.put(integers.toString(), integers);
            return;
        }
        if (target >= candidates[i]) {
            container.add(candidates[i]);
            userIndexed.add(i);
            getCombineFromIndex2(candidates, i, target - candidates[i], container, listMap, userIndexed);
            container.remove(container.size() - 1);
            userIndexed.remove(userIndexed.size() - 1);
        } else {
            return;
        }
        getCombineFromIndex2(candidates, i + 1, target, container, listMap, userIndexed);
    }

    /**
     * https://leetcode-cn.com/problems/combination-sum/
     * 给定一个无重复元素的数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
     * <p>
     * candidates 中的数字可以无限制重复被选取。
     * 说明：
     * 所有数字（包括 target）都是正整数。
     * 解集不能包含重复的组合。
     * <p>
     * candidates = [2,3,5], target = 8
     * a*x^n + b*y^m + c*z^l
     * [
     * [2,2,2,2],2^4
     * [2,3,3],1*2^1 +2*3^1
     * [3,5] 1*3^1 +1*5^1
     * ]
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> resultSet = new ArrayList<>();
        List<Integer> combine = new ArrayList<>();
        getCombineFromIndex(candidates, 0, target, resultSet, combine);
        return resultSet;
    }

    private void getCombineFromIndex(int[] candidates, int i, int target, List<List<Integer>> resultSet, List<Integer> combine) {
        if (i >= candidates.length) {
            return;
        }
        int x = candidates[i];
        if (target == 0) {
            resultSet.add(new ArrayList<>(combine));
            return;
        }
        getCombineFromIndex(candidates, i + 1, target, resultSet, combine);

        if (target >= x) {
            combine.add(x);
            getCombineFromIndex(candidates, i, target - x, resultSet, combine);
            combine.remove(combine.size() - 1);
        }

    }

    private Integer getNOfCandidate(int x, int target) {
        if (x == target) {
            return 1;
        }
        if (x > target) {
            return null;
        }
        int n = 0;
        if (target % x == 0) {
            n = target / x;
        }
        return n;
    }

    /**
     * 给定一个字符串，你的任务是计算这个字符串中有多少个回文子串。
     * <p>
     * 具有不同开始位置或结束位置的子串，即使是由相同的字符组成，也会被视作不同的子串
     *
     * @param s s
     * @return len
     */
    public int countSubstrings(String s) {
        if (s == null || s.length() < 1) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            count += expandAroundCenterAndRtCount(s, i, i);
            count += expandAroundCenterAndRtCount(s, i, i + 1);
        }
        return count;
    }

    private int expandAroundCenterAndRtCount(String s, int left, int right) {
        int count = 0;
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
            count++;
        }
        return count;
    }

    /**
     * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
     * babad "aba" 也是一个有效答案。
     * 使用中心扩展算法解答
     *
     * @param s s
     * @return s
     */
    public String longestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int maxLen = Math.max(len1, len2);
            if (maxLen > (end - start + 1)) {
                start = i - (maxLen - 1) / 2;
                end = i + maxLen / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    /**
     * 你可以假设给定的 k 总是合理的，且 1 ≤ k ≤ 数组中不相同的元素的个数。
     * 你的算法的时间复杂度必须优于 O(n log n) , n 是数组的大小。
     * 题目数据保证答案唯一，换句话说，数组中前 k 个高频元素的集合是唯一的。
     * 你可以按任意顺序返回答案。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/top-k-frequent-elements
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * <p>
     * hash表计数，顶堆取值/快速排序取值
     *
     * @param nums nums
     * @param k    k
     * @return arr
     */
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> occurrences = new HashMap<Integer, Integer>();
        for (int num : nums) {
            occurrences.put(num, occurrences.getOrDefault(num, 0) + 1);
        }

        // int[] 的第一个元素代表数组的值，第二个元素代表了该值出现的次数
        PriorityQueue<int[]> queue = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] m, int[] n) {
                return m[1] - n[1];
            }
        });
        for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
            int num = entry.getKey(), count = entry.getValue();
            if (queue.size() == k) {
                if (queue.peek()[1] < count) {
                    queue.poll();
                    queue.offer(new int[]{num, count});
                }
            } else {
                queue.offer(new int[]{num, count});
            }
        }
        int[] ret = new int[k];
        for (int i = 0; i < k; ++i) {
            ret[i] = queue.poll()[0];
        }
        return ret;
    }
}
