import java.util.*;
public class BuyandSell {
    public static void main(String[] args) {
        int[] prices = {7,1,5,3,6,4};
        System.out.println(maxProfit(prices));
    }

    public static int maxProfit(int[] prices) {
        int minPrice=Integer.MAX_VALUE;
        int maxProfit=Integer.MIN_VALUE;
        for(int p: prices)
        {
            minPrice=Math.min(minPrice, p);
            maxProfit=Math.max(maxProfit, p-minPrice);
        }
        return maxProfit;
    }
}
