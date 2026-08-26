import java.util.Scanner;

/**
 * L1-051 打折
 * 实现原理：折扣价=原价*折扣/10，按两位小数格式化输出。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("%.2f", scanner.nextInt() * scanner.nextInt() / 10.0);
    }
}
