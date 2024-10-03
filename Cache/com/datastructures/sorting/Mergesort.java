package com.datastructures.sorting;
import java.util.*;

public class Mergesort{
    public static void main(String[] args) {
        int[] arr = {5, 4, 3, 2, 1};
        mergeSort(arr, 0, arr.length-1);
        for(int i=0;i<arr.length;i++)
        {
            System.out.println(arr[i]);
        }
    }
    public static void mergeSort(int[] arr, int l, int r){
        if(l<r)
        {
            int mid=(l+r)/2;
            mergeSort(arr, l, mid);
            mergeSort(arr, mid+1, r);
            merge(arr, l, mid, r);
        }
    }
    public static void merge(int arr[],int l,int mid,int r)
    {
        int i=l,j=mid+1,k=l;
        int b[]=new int[arr.length];
        while(i<=mid && j<=r)
        {
            if(arr[i]<arr[j])
            {
                b[k++]=arr[i++];
            }
            else{
                b[k++]=arr[j++];
            }
        }
        while(i<=mid)
        {
            b[k++]=arr[i++];
        }
        while(j<=r)
        {
            b[k++]=arr[j++];
        }
        for(k=l;k<=r;k++)
        {
            arr[k]=b[k];
        }
    }
}