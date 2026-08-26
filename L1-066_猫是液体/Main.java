import java.util.Scanner;

/**
 * L1-066 猫是液体
 * 实现原理：长方体容积等于长、宽、高三者乘积。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(scanner.nextInt() * scanner.nextInt() * scanner.nextInt());
    }
}
