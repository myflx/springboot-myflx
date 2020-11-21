import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {

    public static void main(String[] args) {
        System.out.println(new ThreeSum().threeSum(new int[]{1, 2, 3, 4, 5, 6}));
    }

    /**
     * https://leetcode-cn.com/problems/3sum/
     */
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ret = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return ret;
        }
        Arrays.sort(nums);
        //前后指针
        int i = 0, j = nums.length - 1;
        while (j - i > 1) {
            int twoSum = nums[j] + nums[i];
            if (twoSum + nums[i] > 0) {
                //要往当前数前面找
                do {
                    j--;
                    i = 0;
                } while (j > 0 && nums[j] == nums[j + 1]);
                continue;
            }
            int k = i + 1;
            while (k < j) {
                int sum = nums[k] + twoSum;
                if (sum == 0) {
                    ret.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    break;
                } else if (sum > 0) {
                    break;
                }
                k++;
            }
            do {
                i++;
            } while (i < nums.length - 1 && nums[i] == nums[i - 1]);
        }
        return ret;
    }
}
