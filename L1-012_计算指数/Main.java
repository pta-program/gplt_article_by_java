import java.util.Scanner;

/**
 * L1-012 计算指数
 * 实现原理：整数 n 很小，利用左移一位等价于乘 2，1<<n 即为 2 的 n 次方。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        System.out.println("2^" + n + " = " + (1 << n));
    }
}
