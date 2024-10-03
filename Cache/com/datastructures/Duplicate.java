package com.datastructures;
import java.util.*;
public class Duplicate {

    static void duplicate(int[] arr, int n)
    {
        Set<Integer> set=new LinkedHashSet<>();
        for(int i=0;i<n;i++)
        {
            set.add(arr[i]);
        }
        int j = 0;
        for (int value : set) {
            arr[j++] = value;
        }
        for(int i=0;i<arr.length;i++)
        {
            System.out.println(arr[i]);
        }
    }
//Optimal Solution
    static int remove(int[] arr,int n)
    {
        int i=0;
        for(int j=1;j<n;j++)
        {
            if(arr[i]!=arr[j])
            {
                i++;
                arr[i]=arr[j];
            }
        }
        return i+1;
    }

    public static void main(String[] args) {
        int[] arr={1,2,3,4,5,1,2,1,3};
        int n=arr.length;
        //duplicate(arr,n);
        int k=remove(arr,n);
        for(int i=0;i<k;i++)
        {
            System.out.println(arr[i]);
        }
    }
}
