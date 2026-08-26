import java.util.Scanner;

/**
 * L1-077 大笨钟的心情
 * 实现原理：用长度为 24 的数组按小时保存心情值，读到非法小时停止；
 * 指数严格大于 50 时回答 Yes。
 * 时间复杂度 O(询问次数)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] mood = new int[24];
        for (int i = 0; i < 24; i++) mood[i] = scanner.nextInt();
        while (scanner.hasNextInt()) {
            int hour = scanner.nextInt();
            if (hour < 0 || hour >= 24) break;
            System.out.println(mood[hour] + (mood[hour] > 50 ? " Yes" : " No"));
        }
    }
}
