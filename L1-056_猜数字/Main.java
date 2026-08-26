import java.util.Scanner;

/**
 * L1-056 猜数字
 * 实现原理：先保存所有猜测并求和，目标为 sum/N/2 的整数部分；再扫描所有猜测，
 * 选择与目标绝对差最小的唯一玩家。
 * 时间复杂度 O(N)，空间复杂度 O(N)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] names = new String[n];
        int[] guesses = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            names[i] = scanner.next();
            guesses[i] = scanner.nextInt();
            sum += guesses[i];
        }
        int target = sum / n / 2;
        int winner = 0;
        for (int i = 1; i < n; i++) {
            if (Math.abs(guesses[i] - target) < Math.abs(guesses[winner] - target)) winner = i;
        }
        System.out.println(target + " " + names[winner]);
    }
}
