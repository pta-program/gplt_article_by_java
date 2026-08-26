import java.util.Scanner;

/**
 * L1-084 拯救外星人
 * 实现原理：计算 A+B 后，从 1 连乘到该数得到阶乘。
 * 时间复杂度 O(A+B)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt() + scanner.nextInt();
        int factorial = 1;
        for (int i = 2; i <= n; i++) factorial *= i;
        System.out.println(factorial);
    }
}
