import java.util.*;
public class LeftShift {
    public int[] left(int arr[],int k)
    {
        int n=arr.length;
        int[] temp=new int[n];
        int d=k%n;
        int j=0;
        for(int i=d;i<n;i++)
        {
            temp[j++]=arr[i];
        }
        for(int i=0;i<d;i++)
        {
            temp[j++]=arr[i];
        }
        return temp;
    }
    public static void main(String[] args) {
        int arr[]={1,2,3,4,5};
        int k=6;
        LeftShift ls=new LeftShift();
        int[] result=ls.left(arr, k);
        for(int i=0;i<result.length;i++)
        {
            System.out.println(result[i]+" ");
        }
    }
}
