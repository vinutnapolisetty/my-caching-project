package com.datastructures;

public class TrappingWater1 {
    public int trap(int[] h)
    {
        int n=h.length;
        int[] l=new int[n];
        int r[]=new int[n];
        l[0]=h[0];
        for(int i=1;i<n;i++)
        l[i]=Math.max(l[i-1],h[i]);
        r[n-1]=h[n-1];
        for(int i=n-2;i>=0;i--)
        r[i]=Math.max(r[i+1],h[i]);
        int res=0;
        for(int i=0;i<n;i++)
        res=res+Math.min(l[i],r[i])-h[i];
        return res;
    }
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
        TrappingWater1 obj=new TrappingWater1();
        int arr[]={0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(obj.trap(arr));
        int[] height={1,8,6,2,5,4,8,3,7};
        System.out.println(obj.maxArea(arr));
    }
}
