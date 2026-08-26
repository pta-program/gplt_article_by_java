import java.util.Scanner;

/**
 * L1-047 装睡
 * 实现原理：呼吸次数应在 [15,20]、脉搏应在 [50,70]；任一指标越界即输出姓名。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n-- > 0) {
            String name = scanner.next();
            int breath = scanner.nextInt(), pulse = scanner.nextInt();
            if (breath < 15 || breath > 20 || pulse < 50 || pulse > 70) System.out.println(name);
        }
    }
}
