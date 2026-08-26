import java.util.Scanner;

/**
 * L1-019 谁先倒
 * 实现原理：每轮比较双方划拳数是否等于双方喊数之和。恰有一方命中时，
 * 命中者输一杯；其喝酒数超过酒量时立即输出另一方已喝酒数。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int limitA = scanner.nextInt();
        int limitB = scanner.nextInt();
        int n = scanner.nextInt();
        int drinkA = 0, drinkB = 0;

        for (int i = 0; i < n; i++) {
            int callA = scanner.nextInt();
            int gestureA = scanner.nextInt();
            int callB = scanner.nextInt();
            int gestureB = scanner.nextInt();
            int sum = callA + callB;
            boolean loseA = gestureA == sum;
            boolean loseB = gestureB == sum;
            if (loseA != loseB) {
                if (loseA) drinkA++;
                else drinkB++;
            }
            if (drinkA > limitA) {
                System.out.println("A\n" + drinkB);
                return;
            }
            if (drinkB > limitB) {
                System.out.println("B\n" + drinkA);
                return;
            }
        }
    }
}
