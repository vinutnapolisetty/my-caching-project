package com.datastructures.sorting;
public class Bubble {
    public void bub(int[] arr)
    {
        for(int i=0;i<arr.length;i++)
        {
            boolean flag=false;
            for(int j=0;j<arr.length-i-1;j++)
            {
                if(arr[j]>arr[j+1])
                {
                    int temp=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=temp;
                    flag=true;
                }
            }
            if(flag==false)
            break;
        }
        for(int i=0;i<arr.length;i++)
        {
            System.out.println(arr[i]);
        }
    }
    public static void main(String[] args) {
        Bubble b=new Bubble();
        int[] arr={2,4,1,8,90,-1};
        long start=System.nanoTime();
        System.out.println("Start time: "+start);
        b.bub(arr);
        long end=System.nanoTime();
        System.out.println("End time: "+end);
        System.out.println("Time taken to sort the array is: "+(end-start));
    }
}
