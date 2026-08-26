import java.util.Scanner;

/**
 * L1-002 打印沙漏
 * 实现原理：半边共有 k 行时，沙漏使用的符号数为 2*k*k-1。
 * 先递增寻找满足该公式的最大 k，再依次输出上半部分和下半部分。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        char symbol = scanner.next().charAt(0);

        int k = 1;
        while (2 * (k + 1) * (k + 1) - 1 <= n) {
            k++;
        }
        int used = 2 * k * k - 1;

        // 上半部分（含中间仅有一个符号的行）。
        for (int row = k; row >= 1; row--) {
            printLine(k - row, 2 * row - 1, symbol);
        }
        // 下半部分不重复输出中间行。
        for (int row = 2; row <= k; row++) {
            printLine(k - row, 2 * row - 1, symbol);
        }
        System.out.println(n - used);
    }

    private static void printLine(int spaces, int count, char symbol) {
        for (int i = 0; i < spaces; i++) System.out.print(' ');
        for (int i = 0; i < count; i++) System.out.print(symbol);
        System.out.println();
    }
}
