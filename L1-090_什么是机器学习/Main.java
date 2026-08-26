import java.util.Scanner;

/**
 * L1-090 什么是机器学习
 * 实现原理：求正确和后，依次输出比其少 16、3、1 的数及正确结果。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int sum = scanner.nextInt() + scanner.nextInt();
        System.out.println(sum - 16);
        System.out.println(sum - 3);
        System.out.println(sum - 1);
        System.out.println(sum);
    }
}
