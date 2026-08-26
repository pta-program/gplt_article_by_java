import java.util.Scanner;

/**
 * L1-031 到底是不是太胖了
 * 实现原理：标准体重(市斤) = (身高-100)*0.9*2 = (身高-100)*1.8。
 * 完美身材需满足 |W-标准| < 标准*10% ，即 标准*0.9 < W < 标准*1.1，
 * 边界相等时不算完美（严格小于）。因此：
 *   W > 1.1*标准 => 太胖
 *   W < 0.9*标准（含等于边界时按题目“误差在10%以内”不含等于，应判太瘦/太胖）
 * 为避免浮点误差，使用 1e-9 容差比较。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n-- > 0) {
            int h = scanner.nextInt();
            int w = scanner.nextInt();
            double standard = (h - 100) * 1.8;
            double low = standard * 0.9;
            double high = standard * 1.1;
            final double EPS = 1e-9;
            // 完美区间为 (low, high) 严格开区间
            if (w > low + EPS && w < high - EPS) {
                System.out.println("You are wan mei!");
            } else if (w <= low + EPS) {
                System.out.println("You are tai shou le!");
            } else {
                System.out.println("You are tai pang le!");
            }
        }
    }
}
