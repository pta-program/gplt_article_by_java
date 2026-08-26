import java.util.Scanner;

/**
 * L1-053 电子汪
 * 实现原理：两堆小球总数即输出 Wang! 的次数，循环拼接即可。
 * 时间复杂度 O(A+B)，空间复杂度 O(A+B)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt() + scanner.nextInt();
        System.out.println("Wang!".repeat(count));
    }
}
