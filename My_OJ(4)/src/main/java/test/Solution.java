package test;

public class Solution {
    public  int[] twoSum(int[] nums,int target){
        return null;
    }

    public static void main(String[] args) {
      Solution solution=new Solution();
      int[] arr={2,7,11,15};
      int target=9;
      int[] result=solution.twoSum(arr,target);
      if (result.length==2 &&result[0]==1 &&result[1]==2){
          System.out.println("TestCase OK!");
      }else {
          System.out.println("TestCase Failed! arr:{2，7，11，15}");
      }

        int[] arr2={2,7,11,15};
        int target2=9;
        int[] result2=solution.twoSum(arr2,target2);
        if (result2.length==2 &&result2[0]==1 &&result2[1]==2){
            System.out.println("TestCase OK!");
        }else {
            System.out.println("TestCase Failed! ");
        }

    }
}
