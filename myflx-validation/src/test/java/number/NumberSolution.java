package number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

public class NumberSolution {

    /**
     * 斐波那契数列n超过45就溢出了
     * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
     *
     * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
     *
     * 注意：给定 n 是一个正整数。
     *
     * https://leetcode-cn.com/problems/climbing-stairs/
     */
    public int climbStairs(int n) {
        if (n <= 0) {
            return 0;
        }
        int[] methods = new int[2];
        methods[0] = 2;
        methods[1] = 1;
        for (int i = 3; i <= n; i++) {
            methods[i & 1] = methods[(i - 1) & 1] + methods[(i - 2) & 1];
        }
        return methods[n & 1];
    }


    /**
     * 凡是int都要注意边界问题
     * https://leetcode-cn.com/problems/contains-duplicate-iii/
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        //TODO 考虑是否有好的方式解决边界值的溢出
        //只能通过一个不可变的数据存储变量
        final boolean[] ma = {false};

        //维护一个长度为k的BST,维护过程中树内值若满足t则返回true。
        final TreeSet<Integer> set = new TreeSet<>((o1, o2) -> {
            if (Math.abs((long) o1 - (long) o2) <= t) {
                ma[0] = true;
            }
            return o1 - o2;
        });
        int i = 0;
        while (i < nums.length) {
            if (!set.isEmpty()) {
                if (ma[0]) {
                    return true;
                }
                set.add(nums[i]);
                if (ma[0]) {
                    return true;
                }
                set.clear();
            }
            int next = i + k;
            int j = i + 1;
            while (j <= next && j < nums.length) {
                if (Math.abs((long) nums[i] - (long) nums[j]) <= t) {
                    return true;
                }
                //避免中间的数据被淹没了增加变量增加中间比较结果
                if (!set.add(nums[j++])) {
                    return true;
                }
                //当添加第一个元素的时候个元素时候自身同自身做比较
                if (set.size() == 1) {
                    ma[0] = false;
                }
                if (ma[0]) {
                    return true;
                }
            }
            i = j;
        }
        return false;
    }

    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; ++i) {
            if (set.contains(nums[i])) return true;
            set.add(nums[i]);
            if (set.size() > k) {
                set.remove(nums[i - k]);
            }
        }
        return false;
    }

    /**
     * 错误数字
     * <a href='https://leetcode-cn.com/problems/set-mismatch/'/>
     */
    public int[] findErrorNums(int[] nums) {
        if (nums == null || nums.length < 2) {
            return new int[0];
        }
        int[] ret = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        for (int i = 1; i <= nums.length; i++) {
            if (map.containsKey(i)) {
                if (map.get(i) == 2) {
                    ret[0] = i;
                }
            } else {
                ret[1] = i;
            }
        }
        return ret;
    }

    /**
     * 错误数字
     * <a href='https://leetcode-cn.com/problems/set-mismatch/'/>
     */
    public int[] findErrorNums2(int[] nums) {
        if (nums == null || nums.length < 2) {
            return new int[0];
        }
        int[] ret = new int[2];
        Arrays.sort(nums);
        int firstMissPosition = 0;
        for (int i = 0; i < nums.length; i++) {
            //错误数
            if (nums[i] != i + 1) {
                ret[1] = i + 1;
                if (firstMissPosition == 0) {
                    firstMissPosition = i + 1;
                }
            }
            if (i == nums.length - 1) {
                continue;
            }
            //出现重复数
            if (nums[i] == nums[i + 1]) {
                ret[0] = nums[i];
                //重复前出现错位数则为第一个错误数
                if (ret[1] > 0) {
                    ret[1] = firstMissPosition;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * 有2个数字只出现一次，其他数字出现2次
     */
    public int[] singleNumberIII(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            //①目标数x^y的或运算。xy不相等xor位中肯定有某一位为1假设为第n位
            //②因为是xor运算所以在第n位上xy的码必然不相同
            xor ^= num;
        }
        //③找到最后一个1
        int[] single = new int[2];
        int diff = xor & (-xor);
        //④将数组元素同diff与运算是否为0将数组一分为二同时x,y必然在不同数组中（理解①②）
        //⑤两组数各自异或运算只剩下目标x/y
        for (int num : nums) {
            if ((diff & num) == 0) {
                single[0] ^= num;
            } else {
                single[1] ^= num;
            }
        }
        return single;
    }

    /**
     * 其他数字出现3次
     */
    public int singleNumberII(int[] nums) {
        int Y = 0, X = 0;
        for (int num : nums) {
            Y = ~X & (Y ^ num);
            X = ~Y & (X ^ num);
        }
        return Y;
    }




    /**
     * 给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，使得 nums [i] = nums [j]，并且 i 和 j 的差的 绝对值 至多为 k。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/contains-duplicate-ii
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */
    public boolean containsNearbyDuplicate2(int[] nums, int k) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        boolean rep = false;
        final HashMap<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            final List<Integer> integers = map.getOrDefault(nums[i], new ArrayList<>());
            if (integers.size() > 0) {
                rep = true;
                for (Integer idx : integers) {
                    if (i - idx <= k) {
                        return true;
                    }
                }
            }
            integers.add(i);
            map.put(nums[i], integers);
        }
        return rep;
    }

    public boolean containsDuplicate(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        final HashSet<Integer> hashSet = new HashSet<>();
        for (int num : nums) {
            if (!hashSet.add(num)) {
                return true;
            }
        }
        return false;
    }



    public int singleNumber3(int[] nums) {
        int single = 0;
        for (int num : nums) {
            single ^= num;
        }
        return single;
    }

    public int singleNumber2(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException();
        }
        if (nums.length == 0) {
            return -1;
        }
        int i = 0;
        int out = nums[0];
        while (i < nums.length) {
            out = nums[i];
            int j = i + 1;
            while (j < nums.length) {
                //发现相等的-换位置，换比较对象
                if (out == nums[j]) {
                    if (j != i + 1) {
                        int tmp = nums[i + 1];
                        nums[i + 1] = nums[j];
                        nums[j] = tmp;
                    }
                    i++;
                    break;
                }
                j++;
            }
            //循环到最后都未发现相同的值，说明他就是目标
            if (j == nums.length) {
                break;
            }
            i++;
        }
        return out;
    }

    public int singleNumber4(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException();
        }
        if (nums.length == 0) {
            return -1;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            if (map.containsKey(num)) {
                map.remove(num);
            } else {
                map.put(num, 1);
            }
        }
        for (Map.Entry<Integer, Integer> integerIntegerEntry : map.entrySet()) {
            return integerIntegerEntry.getKey();
        }
        return 0;
    }

    public int singleNumber(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException();
        }
        if (nums.length == 0) {
            return -1;
        }
        HashSet<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (!set.add(num)) {
                set.remove(num);
            }
        }
        return set.iterator().next();
    }


    /**
     * 多数元素
     */
    public int majorityElement2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(1, (o1, o2) -> o1[1] - o2[1]);
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        for (Map.Entry<Integer, Integer> entry : entries) {
            int[] obj = new int[2];
            obj[0] = entry.getKey();
            obj[1] = entry.getValue();
            priorityQueue.offer(obj);
            if (priorityQueue.size() > 1) {
                priorityQueue.poll();
            }
        }
        return priorityQueue.remove()[0];
    }

    public int majorityElement3(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = 0;
        int count = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            int c = map.getOrDefault(num, 0) + 1;
            if (c > count) {
                n = num;
                count = c;
            }
            map.put(num, c);
        }
        return n;
    }

    public int majorityElement4(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    public int majorityElement(int[] nums) {
        int count = 0;
        int candidate = 0;
        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }
            count += candidate == num ? 1 : -1;
        }
        return candidate;
    }

    public List<Integer> majorityElementII(int[] nums) {
        return null;
    }


    /**
     * 回文字符串-反转一半（完全反转的优化）
     */
    public boolean isPalindrome(int x) {
        if (x <= 0 || (x >= 10 && x % 10 == 0)) {
            return x == 0;
        }
        int rev = 0;
        while (rev < x) {
            rev = rev * 10 + x % 10;
            x /= 10;
        }
        return x == rev || x == rev / 10;
    }

    public boolean isPalindrome4(int x) {
        if (x <= 0) {
            return x == 0;
        }
        if (x >= 10 && x % 10 == 0) {
            return false;
        }
        String xStr = String.valueOf(x);
        return xStr.equals(new StringBuffer(xStr).reverse().toString());
    }

    public boolean isPalindrome3(int x) {
        int n = x;
        if (x <= 0) {
            return x == 0;
        }
        if (x >= 10 && x % 10 == 0) {
            return false;
        }
        long m = 0;
        while (n / 10 != 0) {
            m = m * 10 + n % 10;
            n /= 10;
        }
        m = m * 10 + n % 10;
        return m - x == 0;
    }

    public boolean isPalindrome2(int x) {
        int n = x;
        if (x <= 0) {
            return x == 0;
        }
        if (x >= 10 && x % 10 == 0) {
            return false;
        }
        long m = 0;
        while (n / 10 != 0) {
            m = (m + n % 10) * 10;
            n /= 10;
        }
        m += n;
        return m - x == 0;
    }

    public static void main(String[] args) {
        /*System.out.println(1 ^ 2);
        int i = new NumberSolution().singleNumber(new int[]{1, 4, 2, 2, 1});
        System.out.println(i == 4);
        int i = new NumberSolution().majorityElement(new int[]{1, 4, 2, 2, 1, 1});
        System.out.println(i == 1);
        System.out.println(new NumberSolution().isPalindrome(19091));
        System.out.println(new NumberSolution().singleNumberII(new int[]{2, 2, 2, 1}));

        //10101110
        String x = "10101110";
        int integer = Integer.valueOf(x, 2);
        int i = 0;
        while (integer != 0) {
            System.out.println((integer));
            i++;
            integer = integer & (integer - 1);
        }
        System.out.println("==============================>" + i);
        System.out.println("==============================>" + integer);
        System.out.println(3 & (-3));
        System.out.println(Integer.toBinaryString(3));
        System.out.println(Integer.toBinaryString(-3));
        NumberSolution.swap(2, 3);
        System.out.println(Integer.toBinaryString(-26));
        System.out.println(Integer.toBinaryString(-26 >> 1));
        System.out.println(-~6);
        System.out.println(~-6);
        System.out.println(7 & (4 - 1));
        System.out.println(7 & 1);
        System.out.println(~7);
        System.out.println(new NumberSolution().containsNearbyAlmostDuplicate(new int[]{1, 2, 2, 3, 4, 5}, 1, 0));*/
        System.out.println(new NumberSolution().climbStairs(4));

    }

    public static void swap(int a, int b) {
        System.out.println("交换前：a=" + a + ",b=" + b);
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println("交换后：a=" + a + ",b=" + b);
    }
}

