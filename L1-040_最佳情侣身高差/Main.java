import java.util.Scanner;

/**
 * L1-040 最佳情侣身高差
 * 实现原理：男方身高除以 1.09 得女方理想身高；女方身高乘以 1.09 得男方理想身高。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n-- > 0) {
            String gender = scanner.next();
            double height = scanner.nextDouble();
            double partner = gender.equals("M") ? height / 1.09 : height * 1.09;
            System.out.printf("%.2f%n", partner);
        }
    }
}
