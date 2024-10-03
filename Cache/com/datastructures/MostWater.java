package com.datastructures;

public class MostWater {
    public int maxArea(int[] height)
    {
        int i=0,j=height.length-1;
        int max=Integer.MIN_VALUE;
        while(i<j)
        {
            int h=Math.min(height[i],height[j]);
            int w=j-i;
            int area=h*w;
            max=Math.max(max,area);
            if(height[i]<height[j])
            i++;
            else
            j--;
        }
        return max;
    }
    public static void main(String[] args) {
        MostWater obj=new MostWater();
        int[] height={1,8,6,2,5,4,8,3,7};
        System.out.println(obj.maxArea(height));
    }
}
