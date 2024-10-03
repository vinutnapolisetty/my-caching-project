package com.datastructures;
import java.util.*;

public class TwoSum {
    


    public int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> mp = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int k = target - nums[i];
            if (mp.containsKey(k)) {
                return new int[] { mp.get(k), i };
            }
            mp.put(nums[i], i);
        }
        return new int[] {};
    }

    


    public static void main(String[] args) {
        TwoSum ts=new TwoSum();
        int[] nums={2,7,11,15};
        int target=9;
        int[] result=ts.twoSum2(nums, target);
        System.out.println(result[0]+","+result[1]);
    }
    
}
