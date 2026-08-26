import java.util.Scanner;

/**
 * L1-076 降价提醒机器人
 * 实现原理：逐条读取价格，严格小于设定价格时按一位小数输出提醒。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), target = scanner.nextInt();
        while (n-- > 0) {
            double price = scanner.nextDouble();
            if (price < target) System.out.printf("On Sale! %.1f%n", price);
        }
    }
}
