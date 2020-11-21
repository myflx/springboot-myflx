import java.util.Arrays;

/**
 * 复杂度：<a href='https://upload-images.jianshu.io/upload_images/2463290-8cc59ad6c917aa9e.png?imageMogr2/auto-orient/strip|imageView2/2/w/639/format/webp'/>
 */
public class SortTest {
    /**
     * 插入排序
     * 1、时间复杂度：O(n2)  2、空间复杂度：O(1)  3、稳定排序  4、原地排序
     * 从第二位开始向
     *
     * @param nums nums
     * @return sorted nums
     */
    public int[] insertSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            int j = i;
            while (j >= 1 && nums[j - 1] > nums[j]) {
                int t = nums[j - 1];
                nums[j - 1] = nums[j];
                nums[j] = t;
                j--;
                System.out.println(Arrays.toString(nums));
            }
        }
        return nums;
    }

    /**
     * 选择排序
     * 1、时间复杂度：O(n2)  2、空间复杂度：O(1)  3、非稳定排序  4、原地排序
     * 循环找到最小的放到指定位置
     *
     * @param nums nums
     * @return sorted nums
     */
    public int[] selectSort(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int min = i;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[min]) {
                    min = j;
                }
            }
            if (min > i) {
                int t = nums[i];
                nums[i] = nums[min];
                nums[min] = t;
            }
            System.out.println(Arrays.toString(nums));
        }
        return nums;
    }

    /**
     * 冒泡排序
     * 1、时间复杂度：O(n2)  2、空间复杂度：O(1)  3、稳定排序  4、原地排序
     *
     * @param nums nums
     * @return sorted nums
     */
    public int[] bubbleSort(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int t = nums[j + 1];
                    nums[j + 1] = nums[j];
                    nums[j] = t;
                    System.out.println(Arrays.toString(nums));
                }
            }
        }
        return nums;
    }

    public static void main(String[] args) {
        int[] numbers = new int[]{1, 2, 5, 3, 4, 1, 9, 7, 8, 10};
        System.out.println(Arrays.toString(new SortTest().insertSort(numbers)));
        //System.out.println(Arrays.toString(new SortTest().selectSort(numbers)));
        //System.out.println(Arrays.toString(new SortTest().bubbleSort(numbers)));

    }
}
