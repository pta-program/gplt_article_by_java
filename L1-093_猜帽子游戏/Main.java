import java.util.Scanner;

/**
 * L1-093 猜帽子游戏
 * 实现原理：每组猜测必须至少一人猜且所有非零猜测都与真实帽色相同，才能获奖。
 * 时间复杂度 O(KN)，空间复杂度 O(N)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] hats = new int[n];
        for (int i = 0; i < n; i++) hats[i] = scanner.nextInt();
        int groups = scanner.nextInt();
        while (groups-- > 0) {
            boolean guessed = false, correct = true;
            for (int i = 0; i < n; i++) {
                int guess = scanner.nextInt();
                if (guess != 0) {
                    guessed = true;
                    if (guess != hats[i]) correct = false;
                }
            }
            System.out.println(guessed && correct ? "Da Jiang!!!" : "Ai Ya");
        }
    }
}
