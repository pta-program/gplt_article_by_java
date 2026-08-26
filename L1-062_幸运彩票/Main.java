import java.util.Scanner;

/**
 * L1-062 幸运彩票
 * 实现原理：将 6 位号码作为字符串，分别累加前三位与后三位数值并比较。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n-- > 0) {
            String ticket = scanner.next();
            int left = 0, right = 0;
            for (int i = 0; i < 3; i++) left += ticket.charAt(i) - '0';
            for (int i = 3; i < 6; i++) right += ticket.charAt(i) - '0';
            System.out.println(left == right ? "You are lucky!" : "Wish you good luck.");
        }
    }
}
