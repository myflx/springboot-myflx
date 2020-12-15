import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sum {

    public static void main(String[] args) {
        System.out.println(new Sum().threeSum(new int[]{1, 2, 3, 4, 5, 6}));
    }


    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        if (nums == null || nums.length < 4) {
            return ret;
        }
        //sort
        Arrays.sort(nums);
        //loop
        int len = nums.length;
        for (int a = 0; a < len - 3; a++) {
            //one
            if (a > 0 && nums[a] == nums[a - 1]) {
                continue;
            }
            for (int d = len - 1; d > a + 2; d--) {
                //two
                if (d < nums.length - 1 && nums[d] == nums[d + 1]) {
                    continue;
                }
                int b = a + 1;
                while (b < d) {
                    //three
                    if (b > a + 1 && nums[b] == nums[b - 1]) {
                        b++;
                        continue;
                    }
                    int c = d - 1;
                    //four
                    while (c > b) {
                        if (c < d - 1 && nums[c] == nums[c + 1]) {
                            c--;
                            continue;
                        }
                        //sum value overflow
                        if (nums[a] + nums[b] + nums[c] + nums[d] < target) {
                            break;
                        }
                        if (nums[a] + nums[b] + nums[c] + nums[d] == target) {
                            ret.add(Arrays.asList(nums[a], nums[b], nums[c], nums[d]));
                        }
                        c--;
                    }
                    b++;
                }
            }
        }
        return ret;
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
