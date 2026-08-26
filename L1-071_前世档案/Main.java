import java.util.Scanner;

/**
 * L1-071 前世档案
 * 实现原理：判断树叶从左到右编号等价于把回答串看作二进制数：y 走左边记 0，
 * n 走右边记 1。二进制值加 1 即为叶子编号。
 * 时间复杂度 O(MN)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), m = scanner.nextInt();
        while (m-- > 0) {
            String answers = scanner.next();
            int index = 0;
            for (int i = 0; i < n; i++) index = index * 2 + (answers.charAt(i) == 'n' ? 1 : 0);
            System.out.println(index + 1);
        }
    }
}
