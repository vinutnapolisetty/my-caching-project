import java.util.*;
public class ZerosToEnd {
    public void fun(int arr[])
    {
        int temp[]=new int[arr.length];
        Arrays.fill(temp, 0);
        int j=0;
        for(int i=0;i<arr.length;i++)
        {
            if(arr[i]!=0)
            {
                temp[j++]=arr[i];
            }
        }
        for(int i=0;i<arr.length;i++)
        {
            System.out.println(temp[i]+" ");
        }
    }
public void fun1(int arr[])
{
    int j=-1;
    for(int i=0;i<arr.length;i++)
    {
        if(arr[i]==0)
        {
            j=i;
            break;
        }
    }
    for(int i=j+1;i<arr.length;i++)
    {
        if(arr[i]!=0)
        {
            int temp=arr[j];
            arr[j]=arr[i];
            arr[i]=temp;
            j++;
        }
    }
    for(int i=0;i<arr.length;i++)
    {
        System.out.println(arr[i]+" ");
    }
}

    public static void main(String[] args) {
        int arr[]={1,0,2,0,3,0,0,0,4,0,5};
        ZerosToEnd ze=new ZerosToEnd();
       // ze.fun(arr);
        ze.fun1(arr);
    }
}
