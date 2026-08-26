import java.util.Scanner;

/**
 * L1-013 计算阶乘和
 * 实现原理：第 i 项阶乘可由前一项乘 i 得到，遍历时同时维护当前阶乘与总和，
 * 无需重复计算每一项阶乘。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        int factorial = 1;
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
            sum += factorial;
        }
        System.out.println(sum);
    }
}
