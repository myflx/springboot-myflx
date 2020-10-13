
public class ArraySolution {
    public static void main(String[] args) {
        new ArraySolution().removeDuplicates(new int[]{1, 2, 2});
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
