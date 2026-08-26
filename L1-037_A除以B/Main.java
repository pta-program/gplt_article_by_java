import java.util.Scanner;

/**
 * L1-037 A除以B
 * 实现原理：先按分母符号构造题目要求的表达式左侧；分母为零输出 Error，
 * 否则以 double 相除并保留两位小数。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt(), b = scanner.nextInt();
        String divisor = b < 0 ? "(" + b + ")" : String.valueOf(b);
        System.out.print(a + "/" + divisor + "=");
        if (b == 0) System.out.println("Error");
        else System.out.printf("%.2f", a * 1.0 / b);
    }
}
