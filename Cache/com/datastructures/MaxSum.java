package com.datastructures;

public class MaxSum {
    public int maxSubArray(int[] nums) {
        int max=Integer.MIN_VALUE;
        
        int res[]=new int[2];
        for(int i=0;i<nums.length-1;i++){
           
            for(int j=i+1;j<nums.length;j++)
            {
                int sum=nums[i]+nums[j];
                if(max<sum)
                {
                    res[0]=i;
                    res[1]=j;
                    max=sum;
                }
            }
            
        }
        System.out.println("Numbers are "+nums[res[0]]+" "+nums[res[1]]);
            return max;
    }
    public static void main(String[] args) {
        MaxSum obj=new MaxSum();
        int arr[]={1,2,4,-1, -2, -3, -4};
        System.out.println("Max sum is "+obj.maxSubArray(arr));
    }
}
