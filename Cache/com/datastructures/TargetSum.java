import java.util.*;
public class TargetSum {
    public static void main(String[] args) {
        int[] nums = {1, 3,2,4,1,5};
        int target = 3;
        Arrays.sort(nums);
        int[] result = findTargetSumWays(nums, target);
        if(result[0]==-1)
        System.out.println("No such pair");
        else
        System.out.println(nums[result[0]] + " " + nums[result[1]]);
    }

    public static int[] findTargetSumWays(int[] nums, int target) {
        int left=0,right=nums.length-1;
        while(left<right)
        {
            int sum=nums[left]+nums[right];
            if(sum==target)
            {
                return new int[]{left,right};
            }
            else if(sum<target)
            left++;
            else
            right--;
        }
        return new int[]{-1,-1};
    }
    
}
