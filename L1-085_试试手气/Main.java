import java.util.Scanner;

/**
 * L1-085 试试手气
 * 实现原理：每颗骰子的后续结果按 6 到 1 递减，跳过初始点数（已出现不能再用）。
 * 在剩余候选中取得第 n 个即为第 n 次摇出的最大可行结果。
 * 时间复杂度 O(6*6)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] initial = new int[6];
        for (int i = 0; i < 6; i++) initial[i] = scanner.nextInt();
        int n = scanner.nextInt();
        for (int i = 0; i < 6; i++) {
            int remaining = n;
            int value = 6;
            while (true) {
                if (value != initial[i] && --remaining == 0) break;
                value--;
            }
            if (i > 0) System.out.print(' ');
            System.out.print(value);
        }
    }
}
