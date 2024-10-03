import java.util.*;
public class MergeArrays {
    public void fun(int arr1[],int arr2[])
    {
        int m=arr1.length;
        int n=arr2.length;
        Set<Integer> set=new HashSet<>();
        for(int i=0;i<m;i++)
        {
            set.add(arr1[i]);
        }
        for(int i=0;i<n;i++)
        {
            set.add(arr2[i]);
        }
        int arr3[]=new int[set.size()];
        int k=0;
        for(int i:set)
        {
            arr3[k++]=i;
        }
        for(int i=0;i<arr3.length;i++)
        {
            System.out.println(arr3[i]+" ");
        }
    }
    public static void main(String[] args) {
        int arr1[]={1,2,3,4,5};
        int arr2[]={3,4,5,6,7};
        MergeArrays ma=new MergeArrays();
        ma.fun(arr1, arr2);
    }
}
