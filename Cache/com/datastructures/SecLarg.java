package com.datastructures;

import java.util.*;
public class SecLarg {
    static int secLarg(int[] arr, int n){
        int max=Integer.MIN_VALUE;
        int temp=Integer.MIN_VALUE;
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]>max)
            {
                temp=max;
                max=arr[i];
            }
        }
        if(temp==Integer.MIN_VALUE)
        return -1;
        return temp;
    }
    public static void main(String[] args) {
        int[] arr={1,2,3,-1,0,5};
        int n=arr.length;
        System.out.println(secLarg(arr,n));
    }
}
