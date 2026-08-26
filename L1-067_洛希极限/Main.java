import java.util.Scanner;

/**
 * L1-067 洛希极限
 * 实现原理：流体倍数为 2.455、刚体倍数为 1.26；计算洛希极限比例后，
 * 距离小于该比例则会被撕碎。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double densityRootRatio = scanner.nextDouble();
        int type = scanner.nextInt();
        double distanceRatio = scanner.nextDouble();
        double limit = densityRootRatio * (type == 0 ? 2.455 : 1.26);
        System.out.printf("%.2f %s", limit, distanceRatio < limit ? "T_T" : "^_^");
    }
}
