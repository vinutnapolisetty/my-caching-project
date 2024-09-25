package com.datastructures.sorting;

public class Insertion {
    public void ins(int[] arr)
    {
        int key=arr[0];
        for(int i=1;i<arr.length;i++)
        {
            key=arr[i];
            int j=i-1;
            while(j>=0 && arr[j]>key)
            {
                arr[j+1]=arr[j];
                j--;
            }
            arr[j+1]=key;
        }
        for(int i=0;i<arr.length;i++)
        {
            System.out.println(arr[i]);
        }
    }
    public static void main(String[] args) {
        Insertion i=new Insertion();
        int[] arr={2,4,1,8,90,-1};
        long start=System.nanoTime();
        System.out.println("Start time: "+start);
        i.ins(arr);
        long end=System.nanoTime();
        System.out.println("End time: "+end);
        System.out.println("Time taken to sort the array is: "+(end-start));
    }
}
