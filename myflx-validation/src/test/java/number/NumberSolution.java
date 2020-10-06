package number;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class NumberSolution {
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

    public static void main(String[] args) {
        /*System.out.println(1 ^ 2);
        int i = new NumberSolution().singleNumber(new int[]{1, 4, 2, 2, 1});
        System.out.println(i == 4);*/
        int i = new NumberSolution().majorityElement(new int[]{1, 4, 2, 2, 1, 1});
        System.out.println(i == 1);
    }
}
