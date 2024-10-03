package com.datastructures.sorting;

public class Selection {
    public void selectionSort(int[] arr)
    {
        for(int i=0;i<arr.length-1;i++)
        {
            int min=i;
            for(int j=i+1;j<arr.length;j++)
            {
                if(arr[j]<arr[min])
                    min=j;
            }
            int temp=arr[min];
            arr[min]=arr[i];
            arr[i]=temp;
        }
        for(int i=0;i<arr.length;i++)
        System.out.println(arr[i]);
    }
    public static void main(String[] args) {
        Selection obj=new Selection();
        int[] arr={5,4,3,2,1,10};
        obj.selectionSort(arr);
    }
}
