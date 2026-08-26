import java.util.Scanner;

/**
 * L1-036 A乘以B
 * 实现原理：读取两个整数并直接输出乘积。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(scanner.nextInt() * scanner.nextInt());
    }
}
