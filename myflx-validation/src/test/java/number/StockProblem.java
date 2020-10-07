package number;

public class StockProblem {
    public static void main(String[] args) {
        System.out.println(new StockProblem().maxProfit(new int[]{5, 4, 3, 2, 1}) == 0);
    }

    public int maxProfit(int[] prices) {
        int maxprofit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1])
                maxprofit += prices[i] - prices[i - 1];
        }
        return maxprofit;
    }

    /**
     * [7,1,5,3,6,4]
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
