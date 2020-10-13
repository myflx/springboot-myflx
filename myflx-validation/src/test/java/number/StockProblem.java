package number;

public class StockProblem {
    public static void main(String[] args) {
        /*System.out.println(new StockProblem().maxProfit(new int[]{5, 4, 3, 2, 1}) == 0);*/
        System.out.println(new StockProblem().maxSubArray(new int[]{1}));
    }


    /**
     * <p>
     * <p>
     * 输入: [-2,1,-3,4,-1,2,1,-5,4]
     * 输出: 6
     * 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
     */
    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return Integer.MIN_VALUE;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        //确认要使用几个变量存值
        //确认数据废弃条件
        int max = nums[0];
        int preMax = max;
        for (int i = 1; i < nums.length; i++) {
            max += nums[i];
            if (max < nums[i]) {
                max = nums[i];
            }
            if (max > preMax) {
                preMax = max;
            }
        }
        return preMax;
    }

    /**
     * 只交易一次
     */
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int x = 0;
        x = ~x;
        int minPrice = prices[0];
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            int profit = prices[i] - minPrice;
            if (profit < 0) {
                minPrice = prices[i];
            } else if (profit > maxProfit) {
                maxProfit = profit;
            }
        }
        return maxProfit;
    }


    /**
     * 允许多次交易
     */
    public int maxProfit2(int[] prices) {
        int maxprofit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1])
                maxprofit += prices[i] - prices[i - 1];
        }
        return maxprofit;
    }

    /**
     * 允许多次交易
     */
    public int maxProfit1(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int profit = 0;
        //假设购买价格
        int buyPrice = prices[0];
        //前一天的价格
        int preBuyPrice = prices[0];//6
        int day = 1;
        while (day < prices.length) {
            if (prices[day] <= preBuyPrice) {
                //发现今天降价了。。
                if (preBuyPrice > buyPrice) {
                    //前一天预购买获利
                    profit += preBuyPrice - buyPrice;
                }
                //假设当天购买
                buyPrice = prices[day];
            }
            preBuyPrice = prices[day];
            day++;
        }
        if (preBuyPrice > buyPrice) {
            //一直涨价到最后一天
            profit += preBuyPrice - buyPrice;
        }
        return profit;
    }
}
