package com.datastructures;
import java.util.Scanner;
public class NameRec {
    public void rec(int n)
    {
        if(n==0)
        return;
        System.out.println("C401 mthree SD Cohort");
        rec(n-1);
    }
    public static void main(String[] args) {
        NameRec obj=new NameRec();
        Scanner s=new Scanner(System.in);
        int n=s.nextInt();
        obj.rec(n);
        s.close();
    }
}
