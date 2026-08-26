import java.util.Scanner;

/**
 * L1-008 求整数段和
 * 实现原理：从 A 顺序遍历到 B，使用 %5 控制每行五个、%5d 保证字段宽度，
 * 同时累加得到总和。
 * 时间复杂度 O(B-A+1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int sum = 0;
        int count = 0;

        for (int value = a; value <= b; value++) {
            System.out.printf("%5d", value);
            sum += value;
            count++;
            if (count % 5 == 0) System.out.println();
        }
        if (count % 5 != 0) System.out.println();
        System.out.println("Sum = " + sum);
    }
}
