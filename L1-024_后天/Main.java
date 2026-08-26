import java.util.Scanner;

/**
 * L1-024 后天
 * 实现原理：后天为当前日期加2天，星期1..7循环。
 * 公式为 ((D-1+2)%7)+1 等价于 (D+1)%7+1，将7进制环绕映射回1..7。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int d = scanner.nextInt();
        int ans = (d + 1) % 7 + 1; // D+2 在 1..7 循环
        System.out.println(ans);
    }
}
