import java.util.Scanner;

/**
 * L1-016 查验身份证
 * 实现原理：对前 17 位逐位检查是否为数字并按给定权重累加，
 * 再以 sum%11 在校验码映射表中取出应有的末位进行比较。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] check = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int n = scanner.nextInt();
        boolean allValid = true;

        for (int i = 0; i < n; i++) {
            String id = scanner.next();
            int sum = 0;
            boolean valid = true;
            for (int j = 0; j < 17; j++) {
                char c = id.charAt(j);
                if (!Character.isDigit(c)) {
                    valid = false;
                    break;
                }
                sum += (c - '0') * weight[j];
            }
            if (!valid || id.charAt(17) != check[sum % 11]) {
                System.out.println(id);
                allValid = false;
            }
        }
        if (allValid) System.out.println("All passed");
    }
}
