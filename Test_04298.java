import java.util.HashMap;
import java.util.Scanner;

public class Test_0429 {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            int n = scanner.nextInt();
//            int ret = count(n);
//            System.out.println(ret);
//        }
//    }
//    public static  int count(int n) {
//        int count = 0;
//        if (n >= 0 && n <= 500000) {
//            for (int i = 0; i <= n; i++) {
//                if (isperfectnum(i)) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//    public static boolean isperfectnum(int n){
//        if (n == 0 || n == 1)
//            return false;
//        int sum=0;
//        for (int i=1;i<n;i++){
//            if (n%i==0){
//                sum+=i;
//            }
//        }
//        if (sum==n) {
//            return true;
//        }else {
//            return false;
//        }
//    }

    public static void main(String[] args) {

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("3", 0);
        map.put("4", 1);
        map.put("5", 2);
        map.put("6", 3);
        map.put("7", 4);
        map.put("8", 5);
        map.put("9", 6);
        map.put("10", 7);
        map.put("J", 8);
        map.put("Q", 9);
        map.put("K", 10);
        map.put("A", 11);
        map.put("2", 12);
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.nextLine();
            if (str.contains("joker JOKER")) {
                System.out.println("joker JOKER");
            }
            String[] arr = str.split("-");
            String[] str1 = arr[0].split(" ");
            String[] str2 = arr[1].split(" ");
            if (str1.length == str2.length) {
                if (map.get(str1[0]) > map.get(str2[0])) {
                    System.out.println(str1);
                } else {
                    System.out.println(str2);
                }
            }
            else if (str1.length == 4 && str2.length != 4) {
                    System.out.println(str1);
                }
               else if (str1.length != 4 && str2.length == 4) {
                    System.out.println(str2);
                }else{
                    System.out.println("ERROR");
                }
            }
        }
    }
//public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("3", 0);
        map.put("4", 1);
        map.put("5", 2);
        map.put("6", 3);
        map.put("7", 4);
        map.put("8", 5);
        map.put("9", 6);
        map.put("10", 7);
        map.put("J", 8);
        map.put("Q", 9);
        map.put("K", 10);
        map.put("A", 11);
        map.put("2", 12);
        while (sc.hasNext()){
            String str = sc.nextLine();
            String left = str.split("-")[0];
            String[] lefts=left.split(" ");
            String right = str.split("-")[1];
            String[] rights=right.split(" ");
            if(left.equals("joker JOKER") || right.equals("joker JOKER")){
                System.out.println("joker JOKER");
            } else if(lefts.length == 4 && rights.length != 4){
                System.out.println(left);
            } else if(lefts.length != 4 && rights.length == 4){
                System.out.println(right);
            } else if(lefts.length!=rights.length){
                System.out.println("ERROR");
            }else if(map.get(lefts[0]) > map.get(rights[0])){
                System.out.println(left);
            } else {
                System.out.println(right);
            }
        }
    }
//}
