import java.util.Arrays;
import java.util.Scanner;

/**
 * L1-112 现代战争
 * 原理：每次轰炸当前全局最大值所在的行列。先将所有格子按价值降序排序，
 *   维护 rowAlive、colAlive 布尔数组，k 次循环每次找到首个行列均存活的格子并标记删除。
 *   最后按原矩阵顺序输出剩余行列交叉的元素。
 * 时间复杂度 O(n*m log(n*m) + k*n*m) 最坏1e6 log 1e6，空间 O(n*m)
 */
public class Main {
    static class Cell implements Comparable<Cell> {
        int val, r, c;
        Cell(int v, int r, int c) { this.val = v; this.r = r; this.c = c; }
        public int compareTo(Cell o) { return Integer.compare(o.val, this.val); } // 降序
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int k = sc.nextInt();
        int[][] a = new int[n][m];
        Cell[] cells = new Cell[n * m];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                a[i][j] = sc.nextInt();
                cells[idx++] = new Cell(a[i][j], i, j);
            }
        }
        sc.close();
        Arrays.sort(cells);
        boolean[] rowAlive = new boolean[n];
        boolean[] colAlive = new boolean[m];
        Arrays.fill(rowAlive, true);
        Arrays.fill(colAlive, true);
        for (int t = 0; t < k; t++) {
            for (Cell cell : cells) {
                if (rowAlive[cell.r] && colAlive[cell.c]) {
                    rowAlive[cell.r] = false;
                    colAlive[cell.c] = false;
                    break;
                }
            }
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < n; i++) if (rowAlive[i]) {
            boolean first = true;
            for (int j = 0; j < m; j++) if (colAlive[j]) {
                if (!first) out.append(' ');
                out.append(a[i][j]);
                first = false;
            }
            out.append('\n');
        }
        System.out.print(out.toString());
    }
}
