import java.util.Scanner;

/**
 * L1-068 调和平均
 * 实现原理：调和平均= N / sum(1/x_i)，遍历时累计倒数和。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        double reciprocalSum = 0;
        for (int i = 0; i < n; i++) reciprocalSum += 1.0 / scanner.nextDouble();
        System.out.printf("%.2f", n / reciprocalSum);
    }
}
