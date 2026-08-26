import java.util.Scanner;

/**
 * L1-087 机工士姆斯塔迪奥
 * 实现原理：用集合（此处布尔数组）去重被攻击的行和列。安全格=未攻击行数×未攻击列数。
 * 时间复杂度 O(N+M+Q)，空间复杂度 O(N+M)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), m = scanner.nextInt(), q = scanner.nextInt();
        boolean[] rows = new boolean[n + 1], cols = new boolean[m + 1];
        int attackedRows = 0, attackedCols = 0;
        while (q-- > 0) {
            int type = scanner.nextInt(), index = scanner.nextInt();
            if (type == 0 && !rows[index]) { rows[index] = true; attackedRows++; }
            if (type == 1 && !cols[index]) { cols[index] = true; attackedCols++; }
        }
        System.out.println((n - attackedRows) * (m - attackedCols));
    }
}
