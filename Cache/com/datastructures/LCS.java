public class LCS {
    public int lcs(int m,int n,String s1, String s2)
    {
        if(m==0 || n==0)
        return 0;
        if(s1.charAt(m-1)==s2.charAt(n-1))
        return 1+lcs(m-1, n-1, s1, s2);
        else
        return Math.max(lcs(m-1, n, s1, s2), lcs(m, n-1, s1, s2));
    }
    public static void main(String[] args) {
        String s1="abcde";
        String s2="ace";
        System.out.println(new LCS().lcs(s1.length(), s2.length(), s1, s2));
    }
}
